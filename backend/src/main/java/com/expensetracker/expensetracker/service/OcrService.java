package com.expensetracker.expensetracker.service;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OcrService {

    private static final String SERIALIZED_RESPONSES_DIR = "backend/serialized-responses/";

    /**
     * Extracts and formats text from an image file using the Google Cloud Vision API.
     *
     * This method employs a sophisticated hybrid strategy to reconstruct the text from a receipt image accurately.
     * It combines content-based analysis with geometric layout analysis to handle complex, multi-column receipts.
     *
     * The logic is as follows:
     * 1. It uses the `DOCUMENT_TEXT_DETECTION` feature to get a structured representation of the text.
     * 2. It iterates through each paragraph detected by the API.
     * 3. **Content-Based Rule**: It first checks if a paragraph contains a price pattern (e.g., "F $ 1.23").
     *    If a price is found, it assumes this is the end of a line item and forces a newline character, ensuring
     *    distinct items are separated.
     * 4. **Geometric Fallback**: If no price is found in the paragraph, it falls back to a geometric check.
     *    It compares the vertical position of the current paragraph with the next one. If they are on the same
     *    line, it joins them with spaces; otherwise, it adds a newline.
     *
     * This hybrid approach correctly separates multi-column items while still merging parts of a single item
     * that may have been split by the OCR.
     *
     * @param imageBytes The byte array of the image file to process.
     * @return A formatted string representing the receipt's text.
     * @throws IOException If an error occurs during the API communication.
     */
    public String extractTextFromImage(byte[] imageBytes) throws IOException {
        try {
            String imageHash = toSHA256(imageBytes);
            String serializedFilePath = SERIALIZED_RESPONSES_DIR + imageHash + ".ser";

            BatchAnnotateImagesResponse response;
            if (Files.exists(Paths.get(serializedFilePath))) {
                response = deserializeResponse(serializedFilePath);
            } else {
                try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
                    ByteString imgBytes = ByteString.copyFrom(imageBytes);

                    List<AnnotateImageRequest> requests = new ArrayList<>();
                    Image img = Image.newBuilder().setContent(imgBytes).build();
                    Feature feat = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
                    AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
                    requests.add(request);

                    response = vision.batchAnnotateImages(requests);
                    serializeResponse(response, serializedFilePath);
                }
            }

            List<AnnotateImageResponse> responses = response.getResponsesList();

            if (responses.isEmpty() || responses.get(0).hasError()) {
                System.err.println("Error during OCR: " + (responses.isEmpty() ? "No response" : responses.get(0).getError().getMessage()));
                return "";
            }

            TextAnnotation annotation = responses.get(0).getFullTextAnnotation();
            StringBuilder formattedText = new StringBuilder();
            List<Paragraph> paragraphs = new ArrayList<>();
            for (Page page : annotation.getPagesList()) {
                for (Block block : page.getBlocksList()) {
                    paragraphs.addAll(block.getParagraphsList());
                }
            }

            for (int i = 0; i < paragraphs.size(); i++) {
                Paragraph currentParagraph = paragraphs.get(i);
                String currentParagraphText = getParagraphText(currentParagraph);
                formattedText.append(currentParagraphText);

                // **Hybrid Logic**: Check for content (price) first, then fall back to geometry.
                // If the current paragraph contains a price, it's the end of a line item. Force a newline.
                /*if (currentParagraphText.matches(".*F\s+\\$[\\d\\.]+")) {
                    formattedText.append(System.lineSeparator());
                    continue; // Skip the geometric check and move to the next paragraph.
                }*/

                // If no price was found, use the geometric check to see if the next paragraph is on the same line.
                if (i + 1 < paragraphs.size()) {
                    Paragraph nextParagraph = paragraphs.get(i + 1);
                    if (areOnSameLine(currentParagraph, nextParagraph)) {
                        formattedText.append("   "); // Add spaces to merge same-line paragraphs.
                    } else {
                        formattedText.append(System.lineSeparator()); // Add newline for paragraphs on different lines.
                    }
                }
            }
            String string = formattedText.toString();
            return string;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not generate hash for image", e);
        }
    }

    /**
     * Reconstructs the text of a single paragraph from its constituent words and symbols.
     */
    private String getParagraphText(Paragraph paragraph) {
        StringBuilder paragraphText = new StringBuilder();
        for (Word word : paragraph.getWordsList()) {
            for (Symbol symbol : word.getSymbolsList()) {
                paragraphText.append(symbol.getText());
            }
            paragraphText.append(" ");
        }
        return paragraphText.toString().trim();
    }

    /**
     * Checks if two paragraphs are vertically aligned on the same line based on their bounding boxes.
     * @return True if the vertical centers of the two paragraphs are close enough to be considered on the same line.
     */
    private boolean areOnSameLine(Paragraph p1, Paragraph p2) {
        BoundingPoly p1Box = p1.getBoundingBox();
        BoundingPoly p2Box = p2.getBoundingBox();

        int p1CenterY = (p1Box.getVertices(0).getY() + p1Box.getVertices(2).getY()) / 2;
        int p2CenterY = (p2Box.getVertices(0).getY() + p2Box.getVertices(2).getY()) / 2;

        int p1Height = p1Box.getVertices(2).getY() - p1Box.getVertices(0).getY();

        // Heuristic: Check if the vertical centers are within half the height of the first paragraph.
        // This allows for some vertical tolerance.
        return Math.abs(p1CenterY - p2CenterY) < p1Height / 2;
    }

    private void serializeResponse(BatchAnnotateImagesResponse response, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(response);
        }
    }

    private BatchAnnotateImagesResponse deserializeResponse(String filePath) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (BatchAnnotateImagesResponse) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Could not deserialize response", e);
        }
    }

    private String toSHA256(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data);
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

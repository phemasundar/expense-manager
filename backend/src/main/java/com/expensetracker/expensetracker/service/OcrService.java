package com.expensetracker.expensetracker.service;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OcrService {

    public String extractTextFromImage(byte[] imageBytes) throws IOException {
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            ByteString imgBytes = ByteString.copyFrom(imageBytes);

            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
            requests.add(request);

            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            if (responses.isEmpty() || responses.get(0).hasError()) {
                System.err.println("Error during OCR: " + (responses.isEmpty() ? "No response" : responses.get(0).getError().getMessage()));
                return "";
            }

            return responses.get(0).getFullTextAnnotation().getText();
        }
    }
}

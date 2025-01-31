package com.bsanju.stressmate.service;

import com.google.cloud.language.v1.*;
import org.springframework.stereotype.Service;

@Service
public class SentimentAnalysisService {

    public String analyzeSentiment(String text) throws Exception {
        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            Document doc = Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();
            AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
            Sentiment sentiment = response.getDocumentSentiment();

            if (sentiment.getScore() < -0.25) return "negative";
            if (sentiment.getScore() > 0.25) return "positive";
            return "neutral";
        }
    }
}

package org.doug.core;


import org.doug.core.EmbeddingCalculator;
import org.doug.core.VectorUtils;
import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TestEmbeddingCalculator {

    private EmbeddingCalculator converter;

    @BeforeEach
    void setUp() throws ModelException, IOException {
//        TODO: Use this model. It ranks well: https://huggingface.co/spaces/mteb/leaderboard
//        Unfortunately, it is not available in the Hugging Face model zoo? Or the DJL url at least does not resolve
//        The move may be to download the model and to run it locally.
//        String modelUrl = "djl://ai.djl.huggingface.pytorch/dunzhang/stella_en_400M_v5";
        // That being said the below model is even smaller and still isn't awful.
        String modelUrl = "djl://ai.djl.huggingface.pytorch/sentence-transformers/all-MiniLM-L6-v2";
        converter = new EmbeddingCalculator(modelUrl);
    }

    @Test
    void testTextToVector() throws TranslateException {
        String text = "Your text here";
        float[] embedding = converter.textToVector(text);

        assertNotNull(embedding, "The embedding should not be null");
        assertTrue(embedding.length > 0, "The embedding should have a positive length");
        String hypertrophy_is = "hypertrophy is a process characterized by an increase in the size of an organ or tissue due to the enlargement of its component cells.";
        String what_is_hypertrophy = "what is hypertrophy?";
        double[] hypertrophyIs = VectorUtils.floatToDouble(converter.textToVector(hypertrophy_is));
        double[] whatIsHypertrophy = VectorUtils.floatToDouble(converter.textToVector(what_is_hypertrophy));
//      // Do cosine similarity to verify that the embeddings are different.
//      This is done with a dot product between the vectors
        double cosineSimilarity = VectorUtils.dotProduct(hypertrophyIs, whatIsHypertrophy);
        assertTrue(cosineSimilarity < 0.9, "The embeddings should be different");
        assertTrue(cosineSimilarity > 0.7, "The embeddings should be similar");
        System.out.println("Cosine similarity: " + cosineSimilarity);
    }
}
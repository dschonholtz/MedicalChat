package org.doug.core;

import ai.djl.Application;
import ai.djl.ModelException;
import ai.djl.huggingface.translator.TextEmbeddingTranslatorFactory;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;

import java.io.IOException;

public class EmbeddingCalculator {

    private Predictor<String, float[]> predictor;

    public EmbeddingCalculator(String modelUrl) throws ModelException, IOException {
        Criteria<String, float[]> criteria = Criteria.builder()
                .optApplication(Application.NLP.TEXT_EMBEDDING)
                .setTypes(String.class, float[].class)
                .optModelUrls(modelUrl)
                .optEngine("PyTorch")
                .optTranslatorFactory(new TextEmbeddingTranslatorFactory())
                .build();
//        TODO: Do some error handling.
//        For now we just want to see if this works...
        ZooModel<String, float[]> model = criteria.loadModel();
        this.predictor = model.newPredictor();
    }

    public float[] textToVector(String text) throws TranslateException {
        return predictor.predict(text);
    }
}
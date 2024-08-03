package org.doug.resources;

import ai.djl.translate.TranslateException;
import org.doug.core.Paper;
import org.doug.core.EmbeddingCalculator;
import org.doug.db.PaperDAO;
import org.jdbi.v3.sqlobject.customizer.Bind;

import java.util.List;

public class PaperService {

    private final PaperDAO paperDAO;
    private final EmbeddingCalculator embeddingCalculator;

    public PaperService(PaperDAO paperDAO, EmbeddingCalculator embeddingCalculator) {
        this.paperDAO = paperDAO;
        this.embeddingCalculator = embeddingCalculator;
    }

    public void addPaper(Paper paper) throws TranslateException {
        long paperId = paperDAO.insertPaper(paper);

        float[] titleVector = embeddingCalculator.textToVector(paper.getTitle());
        float[] abstractVector = embeddingCalculator.textToVector(paper.getAbstractText());

        paperDAO.insertPaperVector(paperId, titleVector, abstractVector);
        paperDAO.updateTSVectors(paperId);
    }

    public List<Paper> hybridSearchPapers(
            String textQuery,
            double titleVectorWeight,
            double abstractVectorWeight,
            double titleRankWeight,
            double abstractRankWeight,
            double keywordCountWeight,
            int topK
    ) {
        try {
            float[] queryVector = embeddingCalculator.textToVector(textQuery);
            return paperDAO.hybridSearch(
                    queryVector,
                    textQuery,
                    titleVectorWeight,
                    abstractVectorWeight,
                    titleRankWeight,
                    abstractRankWeight,
                    keywordCountWeight,
                    topK
            );
        } catch (TranslateException e) {
            throw new RuntimeException("Failed to convert query to vector", e);
        }
    }
}
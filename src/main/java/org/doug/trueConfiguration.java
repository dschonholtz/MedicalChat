package org.doug;

import io.dropwizard.core.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import jakarta.validation.Valid;
import org.doug.resources.MedRxivDataService;
import jakarta.validation.constraints.NotNull;

public class trueConfiguration extends Configuration {
    @Valid
    @NotNull
    private final MedRxivDataService medRxivDataService = new MedRxivDataService();

    public MedRxivDataService getMedRxivDataService() {
        return medRxivDataService;
    }

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory database) {
        this.database = database;
    }

    @NotNull
    private String embeddingModelUrl = "djl://ai.djl.huggingface.pytorch/sentence-transformers/all-MiniLM-L6-v2";

    @JsonProperty("embeddingModelUrl")
    public String getEmbeddingModelUrl() {
        return embeddingModelUrl;
    }

    @JsonProperty("embeddingModelUrl")
    public void setEmbeddingModelUrl(String embeddingModelUrl) {
        this.embeddingModelUrl = embeddingModelUrl;
    }
}
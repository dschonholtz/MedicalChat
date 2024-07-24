package org.doug;

import io.dropwizard.core.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import org.doug.resources.MedRxivDataService;
import org.hibernate.validator.constraints.*;
import jakarta.validation.constraints.*;

public class trueConfiguration extends Configuration {
    @Valid
    @NotNull
    private final MedRxivDataService medRxivDataService = new MedRxivDataService();

    public MedRxivDataService getMedRxivDataService() {
        return medRxivDataService;
    }
}

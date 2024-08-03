package org.doug;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.doug.resources.MedRxivDataService;

public class trueApplication extends Application<trueConfiguration> {

    public static void main(final String[] args) throws Exception {
        new trueApplication().run(args);
    }

    @Override
    public String getName() {
        return "true";
    }

    @Override
    public void initialize(final Bootstrap<trueConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(trueConfiguration configuration, Environment environment) {
        final MedRxivDataService medRxivDataService = configuration.getMedRxivDataService();
        environment.lifecycle().manage(medRxivDataService);

    }

}

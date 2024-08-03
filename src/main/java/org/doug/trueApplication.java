package org.doug;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.doug.core.EmbeddingCalculator;
import org.doug.db.PaperDAO;
import org.doug.resources.MedRxivDataService;
import org.doug.resources.PaperResource;
import org.doug.resources.PaperService;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

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

        // Set up database
        final Jdbi jdbi = Jdbi.create(configuration.getDataSourceFactory().getUrl(),
                configuration.getDataSourceFactory().getUser(),
                configuration.getDataSourceFactory().getPassword());
        jdbi.installPlugin(new SqlObjectPlugin());

        // Create DAOs
        final PaperDAO paperDAO = jdbi.onDemand(PaperDAO.class);

        // Create Services
        try {
            final PaperService paperService = new PaperService(
                    paperDAO,
                    new EmbeddingCalculator(configuration.getEmbeddingModelUrl())
            );
            // Register resources
            environment.jersey().register(new PaperResource(paperService));
        }
        catch (Exception e) {
            e.printStackTrace();
            // TODO yeah this is not where this should live
        }
    }

}

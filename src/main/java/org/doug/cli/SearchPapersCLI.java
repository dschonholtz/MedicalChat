package org.doug.cli;

import org.apache.commons.cli.*;
import org.doug.core.EmbeddingCalculator;
import org.doug.core.Paper;
import org.doug.db.PaperDAO;
import org.doug.db.PaperMapper;
import org.doug.resources.PaperService;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.util.List;

public class SearchPapersCLI {

    public static void main(String[] args) {
        Options options = new Options();

        Option queryOption = new Option("q", "query", true, "Search query. Default is 'What is the impact of caffeine on muscle growth?'");
        queryOption.setRequired(false);
        options.addOption(queryOption);

        Option abstractWeightOption = new Option("aw", "abstractWeight", true, "Weight for abstract relevance. Default is 1.0.");
        abstractWeightOption.setRequired(false);
        options.addOption(abstractWeightOption);

        Option titleWeightOption = new Option("tw", "titleWeight", true, "Weight for title relevance. Default is 1.0.");
        titleWeightOption.setRequired(false);
        options.addOption(titleWeightOption);

        Option titleRankWeightOption = new Option("trw", "titleRankWeight", true, "Weight for title rank. Default is 1.0.");
        titleRankWeightOption.setRequired(false);
        options.addOption(titleRankWeightOption);

        Option abstractRankWeightOption = new Option("arw", "abstractRankWeight", true, "Weight for abstract rank. Default is 1.0.");
        abstractRankWeightOption.setRequired(false);
        options.addOption(abstractRankWeightOption);

        Option keywordCountWeightOption = new Option("kcw", "keywordCountWeight", true, "Weight for keyword count. Default is 1.0.");
        keywordCountWeightOption.setRequired(false);
        options.addOption(keywordCountWeightOption);

        Option topKOption = new Option("k", "topK", true, "Number of top results to return. Default is 10.");
        topKOption.setRequired(false);
        options.addOption(topKOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            String query = cmd.hasOption("query") ? cmd.getOptionValue("query") : "What is the impact of caffeine on muscle growth?";
            double abstractWeight = cmd.hasOption("abstractWeight") ? Double.parseDouble(cmd.getOptionValue("abstractWeight")) : 1.0;
            double titleWeight = cmd.hasOption("titleWeight") ? Double.parseDouble(cmd.getOptionValue("titleWeight")) : 1.0;
            double titleRankWeight = cmd.hasOption("titleRankWeight") ? Double.parseDouble(cmd.getOptionValue("titleRankWeight")) : 1.0;
            double abstractRankWeight = cmd.hasOption("abstractRankWeight") ? Double.parseDouble(cmd.getOptionValue("abstractRankWeight")) : 1.0;
            double keywordCountWeight = cmd.hasOption("keywordCountWeight") ? Double.parseDouble(cmd.getOptionValue("keywordCountWeight")) : 1.0;
            int topK = cmd.hasOption("topK") ? Integer.parseInt(cmd.getOptionValue("topK")) : 10;

            String jdbcUrl = "jdbc:postgresql://localhost:5432/vectordb";
            String username = "postgres";
            String password = "mysecretpassword";

            // Create Jdbi instance
            Jdbi jdbi = Jdbi.create(jdbcUrl, username, password);
            jdbi.installPlugin(new SqlObjectPlugin());

            // Register PaperMapper
            jdbi.registerRowMapper(new PaperMapper());

            // Create PaperDAO instance
            PaperDAO paperDAO = jdbi.onDemand(PaperDAO.class);

            PaperService paperService = new PaperService(
                    paperDAO,
                    new EmbeddingCalculator(
                            "djl://ai.djl.huggingface.pytorch/sentence-transformers/all-MiniLM-L6-v2"
                    ));

            List<Paper> results = paperService.hybridSearchPapers(
                    query,
                    titleWeight,
                    abstractWeight,
                    titleRankWeight,
                    abstractRankWeight,
                    keywordCountWeight,
                    topK
            );

            for (Paper paper : results) {
                System.out.println(paper);
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("SearchPapersCLI", options);
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Error during search: " + e.getMessage());
            System.exit(1);
        }
    }
}
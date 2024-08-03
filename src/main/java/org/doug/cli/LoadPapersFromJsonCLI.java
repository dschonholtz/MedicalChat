package org.doug.cli;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.*;
import org.doug.core.EmbeddingCalculator;
import org.doug.core.Paper;
import org.doug.db.PaperDAO;
import org.doug.resources.PaperService;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LoadPapersFromJsonCLI {

    public static void main(String[] args) {
        Options options = new Options();

        Option filePathOption = new Option("f", "file", true, "File path to load JSON from. Default is medrxiv.json.");
        filePathOption.setRequired(false);
        options.addOption(filePathOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            String filePath = cmd.hasOption("file") ? cmd.getOptionValue("file") : "medrxiv.json";

            ObjectMapper objectMapper = new ObjectMapper();
            List<Paper> papers = objectMapper.readValue(new File(filePath), new TypeReference<List<Paper>>() {});

            String jdbcUrl = "jdbc:postgresql://localhost:5432/vectordb";
            String username = "postgres";
            String password = "mysecretpassword";

            // Create Jdbi instance
            Jdbi jdbi = Jdbi.create(jdbcUrl, username, password);
            jdbi.installPlugin(new SqlObjectPlugin());

            // Create PaperDAO instance
            PaperDAO paperDAO = jdbi.onDemand(PaperDAO.class);

//            Clear the tables before loading the papers
            paperDAO.clearTables();

            PaperService paperService = new PaperService(
                    paperDAO,
                    new EmbeddingCalculator(
                            "djl://ai.djl.huggingface.pytorch/sentence-transformers/all-MiniLM-L6-v2"
                    )); // Assume you have a way to get an instance of PaperService

            for (Paper paper : papers) {
                paperService.addPaper(paper);
            }

            System.out.println("Papers loaded from " + filePath);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("LoadPapersCLI", options);
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Error loading papers: " + e.getMessage());
            System.exit(1);
        }
    }
}
package org.doug.cli;

import org.apache.commons.cli.*;
import org.doug.resources.MedRxivDataService;

import java.time.LocalDate;

public class FetchPapersCLI {

    public static void main(String[] args) {
        Options options = new Options();

        Option startDateOption = new Option("s", "start", true, "Start date (YYYY-MM-DD). Default is 2018-01-01.");
        startDateOption.setRequired(false); // Not required anymore
        options.addOption(startDateOption);

        Option endDateOption = new Option("e", "end", true, "End date (YYYY-MM-DD). Default is today.");
        endDateOption.setRequired(false); // Not required anymore
        options.addOption(endDateOption);

        Option filePathOption = new Option("f", "file", true, "File path to save JSON");
        filePathOption.setRequired(false);
        options.addOption(filePathOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            LocalDate startDate = cmd.hasOption("start") ? LocalDate.parse(cmd.getOptionValue("start")) : LocalDate.of(2018, 1, 1);
            LocalDate endDate = cmd.hasOption("end") ? LocalDate.parse(cmd.getOptionValue("end")) : LocalDate.now();
//            If there is no file path provided, use the current directory and call the file medrxiv.json
            String filePath = cmd.hasOption("file") ? cmd.getOptionValue("file") : "medrxiv.json";
//            String filePath = cmd.getOptionValue("file");

            MedRxivDataService service = new MedRxivDataService();
            service.fetchAndSaveSportsSciencePapers(startDate, endDate, filePath);

            System.out.println("Papers saved to " + filePath);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
    }
}
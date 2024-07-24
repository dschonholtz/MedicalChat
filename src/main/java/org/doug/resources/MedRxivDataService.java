package org.doug.resources;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.lifecycle.Managed;
import org.doug.core.Paper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class MedRxivDataService implements Managed {
    private static final String API_BASE_URL = "https://api.medrxiv.org/details/medrxiv/";
    private final ObjectMapper objectMapper = new ObjectMapper(); // Define ObjectMapper here
    private static final Logger LOGGER = Logger.getLogger(MedRxivDataService.class.getName());

    @Override
    public void start() throws Exception {
        // Initialization logic if needed
    }

    @Override
    public void stop() throws Exception {
        // Cleanup logic if needed
    }

    private void saveJsonToFile(String json, String filePath) {
        try {
            Files.write(Paths.get(filePath), json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Modify the fetchSportsSciencePapers method to include saving the JSON to a file
    public void fetchAndSaveSportsSciencePapers(LocalDate startDate, LocalDate endDate, String filePath) {
        List<Paper> sportsSciencePapers = fetchSportsSciencePapers(startDate, endDate);
        try {
            String json = objectMapper.writeValueAsString(sportsSciencePapers);
            saveJsonToFile(json, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Paper> fetchSportsSciencePapers(LocalDate startDate, LocalDate endDate) {
        List<Paper> sportsSciencePapers = new ArrayList<>();
        int cursor = 0;

        while (true) {
            String apiUrl = buildApiUrl(startDate, endDate, cursor);
            String jsonResponse = fetchDataFromApi(apiUrl);

            if (jsonResponse == null) {
                break;
            }

            try {
                JsonNode responseObj = objectMapper.readTree(jsonResponse);
                JsonNode collection = responseObj.get("collection");

                for (JsonNode paperJson : collection) {
                    if ("sports medicine".equals(paperJson.get("category").asText())) {
                        sportsSciencePapers.add(objectMapper.treeToValue(paperJson, Paper.class));
                    }
                }

                cursor += 100;
                if (cursor >= responseObj.get("messages").get(0).get("total").asInt()) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }

        return sportsSciencePapers;
    }

    private String buildApiUrl(LocalDate startDate, LocalDate endDate, int cursor) {
        String apiUrl = API_BASE_URL + startDate + "/" + endDate + "/" + cursor;
        LOGGER.info("Constructed API URL: " + apiUrl);
        return apiUrl;
    }

    private String fetchDataFromApi(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                System.out.println("Failed to fetch data: HTTP error code " + conn.getResponseCode());
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

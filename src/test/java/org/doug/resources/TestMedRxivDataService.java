package org.doug.resources;

import org.doug.resources.MedRxivDataService;
import org.doug.core.Paper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TestMedRxivDataService {

    private MedRxivDataService service;
    private HttpURLConnection mockConnection;

    @BeforeEach
    void setUp() throws Exception {
        service = new MedRxivDataService();
        mockConnection = mock(HttpURLConnection.class);
        URL url = Mockito.mock(URL.class);
        Mockito.when(url.openConnection()).thenReturn(mockConnection);
        // Mock more behavior as needed
    }

    @Test
    void testFetchSportsSciencePapers() throws Exception {
        // Mock the response from the API
        String jsonResponse = "{\"collection\": [{\"doi\": \"10.1000/j.jmir.2020.1234\", \"title\": \"A Study on Sports Medicine\", \"authors\": \"Doe, John; Doe, Jane\", \"category\": \"sports medicine\", \"abstract_text\": \"This is a study on sports medicine.\"}]}";
        Mockito.when(mockConnection.getInputStream()).thenReturn(new java.io.ByteArrayInputStream(jsonResponse.getBytes()));

        // Call the method under test
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 12, 31);
        List<Paper> papers = service.fetchSportsSciencePapers(startDate, endDate);

        // Assert the results
        assertNotNull(papers);
        assertEquals(1, papers.size());
        Paper paper = papers.get(0);
        assertEquals("A Study on Sports Medicine", paper.getTitle());
        // Add more assertions as needed
    }

    // Add more test methods as needed
}

class TestMedRxivDataServiceIntegration {

    @Test
    @Tag("integration")
    void testFetchSportsSciencePapersRealRequest() throws Exception {
        MedRxivDataService service = new MedRxivDataService();
        LocalDate startDate = LocalDate.of(2024, 6, 1);
        LocalDate endDate = LocalDate.of(2024, 6, 10);
        List<Paper> papers = service.fetchSportsSciencePapers(startDate, endDate);

        //assert the number of papers is 3
        assertEquals(3, papers.size(), "The number of papers should be 3");

        assertFalse(papers.isEmpty(), "The list of papers should not be empty");
        for (Paper paper : papers) {
            assertEquals("sports medicine", paper.getCategory(), "The paper category should be 'sports medicine'");
        }

        // Example structure assertion
        Paper firstPaper = papers.get(0);
        assertNotNull(firstPaper.getTitle(), "The first paper should have a title");
        // Add more structure assertions as needed
//        print the whole paper object for the first paper
        System.out.println(firstPaper);
    }
}
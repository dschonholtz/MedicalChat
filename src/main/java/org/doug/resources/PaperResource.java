package org.doug.resources;

import org.doug.core.Paper;
import org.doug.resources.PaperService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/papers")
@Produces(MediaType.APPLICATION_JSON)
public class PaperResource {

    private final PaperService paperService;

    public PaperResource(PaperService paperService) {
        this.paperService = paperService;
    }

    @GET
    @Path("/search")
    public List<Paper> searchPapers(
            @QueryParam("query") String query,
            @QueryParam("titleWeight") @DefaultValue("1.0") double titleWeight,
            @QueryParam("abstractWeight") @DefaultValue("1.0") double abstractWeight,
            @QueryParam("titleRankWeight") @DefaultValue("1.0") double titleRankWeight,
            @QueryParam("abstractRankWeight") @DefaultValue("1.0") double abstractRankWeight,
            @QueryParam("keywordCountWeight") @DefaultValue("1.0") double keywordCountWeight,
            @QueryParam("topK") @DefaultValue("10") int topK
    ) {
        return paperService.hybridSearchPapers(
                query,
                titleWeight,
                abstractWeight,
                titleRankWeight,
                abstractRankWeight,
                keywordCountWeight,
                topK
        );
    }
}
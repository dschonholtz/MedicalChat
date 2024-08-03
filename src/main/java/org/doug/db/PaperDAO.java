package org.doug.db;

import org.doug.core.Paper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface PaperDAO {
    @SqlUpdate("INSERT INTO papers (doi, title, authors, category, abstract, " +
            "author_corresponding, author_corresponding_institution, date, " +
            "version, type, license, jatsxml, published, server) " +
            "VALUES (:doi, :title, :authors, :category, :abstractText, " +
            ":authorCorresponding, :authorCorrespondingInstitution, :date, " +
            ":version, :type, :license, :jatsxml, :published, :server)")
    @GetGeneratedKeys
    long insertPaper(@BindBean Paper paper);

    @SqlUpdate("INSERT INTO paper_vectors (paper_id, title_vector, abstract_segment_vector) " +
            "VALUES (:paperId, :titleVector, :abstractSegmentVector)")
    void insertPaperVector(
            @Bind("paperId") long paperId,
            @Bind("titleVector") float[] titleVector,
            @Bind("abstractSegmentVector") float[] abstractSegmentVector
    );

    @SqlUpdate("UPDATE papers SET abstract_tsv = to_tsvector('english', abstract), " +
            "title_tsv = to_tsvector('english', title) WHERE id = :paperId")
    void updateTSVectors(@Bind("paperId") long paperId);

    @SqlQuery("WITH vector_search AS (" +
            "    SELECT " +
            "        p.*," +
            "        1 - (pv.title_vector <=> CAST(:queryVector AS vector)) AS title_similarity, " +
            "        1 - (pv.abstract_segment_vector <=> CAST(:queryVector AS vector)) AS abstract_similarity " +
            "    FROM" +
            "        papers p" +
            "    JOIN" +
            "        paper_vectors pv ON p.id = pv.paper_id" +
            "    WHERE" +
            "        1 - (pv.title_vector <=> CAST(:queryVector AS vector)) * 0.5 + " +
            "        1 - (pv.abstract_segment_vector <=> CAST(:queryVector AS vector)) > 0.6 " +
            ")," +
            "keyword_matches AS (" +
            "    SELECT" +
            "        vs.*," +
            "        ts_rank_cd(vs.title_tsv, query) AS title_rank," +
            "        ts_rank_cd(vs.abstract_tsv, query) AS abstract_rank," +
            "        ts_rank_cd(vs.title_tsv || vs.abstract_tsv, query) AS combined_rank" +
            "    FROM" +
            "        vector_search vs," +
            "        plainto_tsquery(:textQuery) query" +
            ")" +
            "SELECT" +
            "    *, " +
            "    (title_similarity * :titleVectorWeight + " +
            "     abstract_similarity * :abstractVectorWeight + " +
            "     title_rank * :titleRankWeight + " +
            "     abstract_rank * :abstractRankWeight + " +
            "     combined_rank * :keywordCountWeight) AS combined_score " +
            "FROM " +
            "    keyword_matches " +
            "ORDER BY " +
            "    combined_score DESC " +
            "LIMIT :limit")
    List<Paper> hybridSearch(
            @Bind("queryVector") float[] queryVector,
            @Bind("textQuery") String textQuery,
            @Bind("titleVectorWeight") double titleVectorWeight,
            @Bind("abstractVectorWeight") double abstractVectorWeight,
            @Bind("titleRankWeight") double titleRankWeight,
            @Bind("abstractRankWeight") double abstractRankWeight,
            @Bind("keywordCountWeight") double keywordCountWeight,
            @Bind("limit") int limit
    );

    @SqlUpdate("TRUNCATE TABLE papers CASCADE")
    void clearPapersTable();

    @SqlUpdate("TRUNCATE TABLE paper_vectors")
    void clearPaperVectorsTable();

    default void clearTables() {
        clearPapersTable();
        clearPaperVectorsTable();
    }
}
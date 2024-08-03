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


    @SqlQuery("SELECT * FROM (" +
            "    SELECT " +
            "        p.id, " +
            "        p.doi, " +
            "        p.title, " +
            "        p.authors, " +
            "        p.category, " +
            "        p.abstract, " +
            "        p.author_corresponding, " +
            "        p.author_corresponding_institution, " +
            "        p.date, " +
            "        p.version, " +
            "        p.type, " +
            "        p.license, " +
            "        p.jatsxml, " +
            "        p.published, " +
            "        p.server, " +
            "        p.title_tsv, " +
            "        p.abstract_tsv, " +
            "        1 - (pv.title_vector <=> CAST(:queryVector AS vector)) AS title_similarity, " +
            "        1 - (pv.abstract_segment_vector <=> CAST(:queryVector AS vector)) AS abstract_similarity " +
            "    FROM " +
            "        papers p " +
            "    JOIN " +
            "        paper_vectors pv ON p.id = pv.paper_id " +
            ") subquery " +
            "WHERE " +
            "   title_similarity * .5 + abstract_similarity > .6 " +
            "ORDER BY " +
            "    title_similarity * .5 + abstract_similarity DESC " +
            "LIMIT 10"
//                            +
//                    "), " +
//                    "keyword_matches AS ( " +
//                    "    SELECT " +
//                    "        id, " +
//                    "        title, " +
//                    "        abstract, " +
//                    "        title_tsv, " +
//                    "        abstract_tsv, " +
//                    "        title_similarity, " +
//                    "        abstract_similarity, " +
//                    "        ts_rank_cd(title_tsv, query) AS title_rank, " +
//                    "        ts_rank_cd(abstract_tsv, query) AS abstract_rank, " +
//                    "        count(*) FILTER (WHERE word = ANY(tsvector_to_array(title_tsv || abstract_tsv))) AS keyword_count " +
//                    "    FROM " +
//                    "        vector_search, " +
//                    "        plainto_tsquery(:textQuery) query, " +
//                    "        unnest(tsvector_to_array(query::tsvector)) word " +
//                    "    GROUP BY " +
//                    "        id, title, abstract, title_tsv, abstract_tsv, title_similarity, abstract_similarity, query " +
//                    ") " +
//                    "SELECT " +
//                    "    id, " +
//                    "    title, " +
//                    "    abstract, " +
//                    "    title_similarity, " +
//                    "    abstract_similarity, " +
//                    "    title_rank, " +
//                    "    abstract_rank, " +
//                    "    keyword_count, " +
//                    "    (title_similarity * :titleVectorWeight + " +
//                    "     abstract_similarity * :abstractVectorWeight + " +
//                    "     title_rank * :titleRankWeight + " +
//                    "     abstract_rank * :abstractRankWeight + " +
//                    "     (CASE WHEN keyword_count > 0 THEN ln(keyword_count + 1) * :keywordCountWeight ELSE 0 END)) AS combined_score " +
//                    "FROM " +
//                    "    keyword_matches " +
//                    "ORDER BY " +
//                    "    combined_score DESC " +
//                    "LIMIT :limit"
    )
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
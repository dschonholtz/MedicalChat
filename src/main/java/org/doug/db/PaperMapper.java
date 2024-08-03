package org.doug.db;

import org.doug.core.Paper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaperMapper implements RowMapper<Paper> {
    @Override
    public Paper map(ResultSet rs, StatementContext ctx) throws SQLException {
//        Print the title and abstract similarity scores
        System.out.println("Title similarity: " + rs.getFloat("title_similarity"));
        System.out.println("Abstract similarity: " + rs.getFloat("abstract_similarity"));
        return new Paper(
                rs.getString("doi"),
                rs.getString("title"),
                rs.getString("authors"),
                rs.getString("category"),
                rs.getString("abstract"),
                rs.getString("author_corresponding"),
                rs.getString("author_corresponding_institution"),
                rs.getDate("date"),
                rs.getString("version"),
                rs.getString("type"),
                rs.getString("license"),
                rs.getString("jatsxml"),
                rs.getString("published"),
                rs.getString("server")
        );
    }
}
package com.debrief;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManage {
    public static final String DB_URL = "hdbc:sqlite:debrief.db";
    public void initDatabase(){
        String createTagTable = """
                CREATE TABLE IF NOT EXISTS tags(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    index_number INTEGER UNIQUE NOT NULL,
                    question TEXT NOT NULL
                )
                """;
        String createURLTable ="""
                CREATE TABLE IF NOT EXISTS urls(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    index_number INTEGER UNIQUE NOT NULL
                    url TEXT NOT NULL
                )
                """;
        try(
            Connection con = DriverManager.getConnection(DB_URL);
            Statement statement = con.createStatement();
        ){
            statement.execute(createTagTable);
            statement.execute(createURLTable);

            statement.execute("CREATE INDEX IF NOT EXISTS idx_tags_index on tags(index_number)");
            statement.execute("CREATE INDEX IF NOT EXISTS idx_urls_index on urls(index_number)");

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    private int getNextIndex(Connection conn, String tableName) throws SQLException{
        String sql = String.format("SELECT COALESCE(MAX(index_number), 0) + 1 FROM %s", tableName);
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.getInt(1);
        }
    }
    public int insertTag(String questionText){
        String sql = "INSERT INTO tags (index_number, question) VALUES (?, ?)";
        try(Connection conn = DriverManager.getConnection(DB_URL)){
            int nextIndex = getNextIndex(conn, "tags");
            try(PreparedStatement statement = conn.prepareStatement(sql)){
                statement.setInt(1, nextIndex);
                statement.setString(2, questionText);
                statement.executeUpdate();
                return nextIndex;
            }
        }catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }
    public int insertURL(String urlText){
        String sql = "INSERT INTO urls(index_number, url) VALUES(?, ?)";
        try(Connection conn = DriverManager.getConnection(DB_URL)){
            int nextIndex = getNextIndex(conn, "urls");
            try(PreparedStatement statement = conn.prepareStatement(sql)){
                statement.setInt(1, nextIndex);
                statement.setString(2, urlText);
                statement.executeUpdate();
                return nextIndex;
            }
        }catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }
    public String getTagsByIndex(int indexNum){
        String sql = "SELECT question FROM tags WHERE index_number = ? ";
        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = conn.prepareStatement(sql)){
                statement.setInt(1, indexNum);
                var rs = statement.executeQuery();
                if(rs.next()) return rs.getString("tags");    
        }catch(SQLException e){
            e.printStackTrace();;

        }
        return null;
    }
    public String getURLByIndex(int indexNum){
        String sql = "SELECT url FROM urls WHERE index_number = ?";
        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = conn.prepareStatement(sql)
        ){
            statement.setInt(1, indexNum);
            var rs = statement.executeQuery();
            if(rs.next()) return rs.getString("url");

        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}

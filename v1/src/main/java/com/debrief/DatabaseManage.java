package com.debrief;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseManage {
    public static final String DB_URL = "jdbc:sqlite:debrief.db";
    public DatabaseManage(){

    }
    public void initDatabase(){
        System.out.println("========Database Init=============");
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
                    index_number INTEGER UNIQUE NOT NULL,
                    url TEXT NOT NULL
                )
                """;
        String createToDoTable = """
                CREATE TABLE IF NOT EXISTS ToDos(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    index_number INTEGER UNIQUE NOT NULL,
                    text TEXT NOT NULL
                )
                """;
        try(
            Connection con = DriverManager.getConnection(DB_URL);
            Statement statement = con.createStatement();
        ){
            statement.execute(createTagTable);
            statement.execute(createURLTable);
            statement.execute(createToDoTable);
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
                System.out.println("========Insert=========");
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
                System.out.println("========Insert=========");
                return nextIndex;
            }
        }catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }
    public int insertToDo(String todo){
        String sql = "INSERT INTO ToDos(index_number, text) VALUES (?, ?)";
        try(Connection conn = DriverManager.getConnection(DB_URL)){
            int nextIndex = getNextIndex(conn, "todos");
            try(PreparedStatement statement = conn.prepareStatement(sql)){
                statement.setInt(1, nextIndex);
                statement.setString(2, todo);
                statement.executeUpdate();
                System.out.println("========Insert=========");
                return nextIndex;
            }
        }catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }
    public String getTagsByIndex(int indexNum){
        String sql = "SELECT question FROM tags WHERE index_number=?";
        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = conn.prepareStatement(sql)){
                statement.setInt(1, indexNum);
                var rs = statement.executeQuery();
                if(rs.next()) return rs.getString("question");    
        }catch(SQLException e){
            e.printStackTrace();

        }
        return null;
    }
    public String getToDosByIndex(int indexNum){
        String sql = "SELECT text FROM ToDos WHERE index_number=?";
        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = conn.prepareStatement(sql)){
                statement.setInt(1, indexNum);
                var rs = statement.executeQuery();
                if(rs.next()) return rs.getString("text");
        }catch(SQLException e){
            e.printStackTrace();
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
    public void verifyTable(){
        String sql = """
                SELECT name FROM sqlite_master
                WHERE type='table' AND (name='tags' OR name='urls')
                """;
        try(Connection conn = DriverManager.getConnection(DB_URL);
            Statement statement = conn.createStatement()){
                try(ResultSet rs = statement.executeQuery(sql)){
                    while(rs.next()){
                        System.out.println("table: " + rs.getString("name"));
                    }
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
    }
    public void clearDatabase(){
        clearTagsTable();
        clearUrlsTable();
        clearToDosTable();
    }
    public void printTagTable(){
        String sql = "SELECT * FROM tags";
        System.out.println("printTagTable() called");
        try(Connection conn = DriverManager.getConnection(DB_URL);
            Statement statement = conn.createStatement();){
            System.out.println("\n=== Tags Table ===");
                try(ResultSet rs = statement.executeQuery(sql)){
                    while(rs.next()){
                        int num = rs.getInt("index_number");
                        String temp = rs.getString("question");
                        System.out.printf("Index: %d, Question: %s%n", num, temp);

                    }
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
    }
    public void printUrlTable(){
        String sql = "SELECT * FROM urls";
        try(Connection conn = DriverManager.getConnection(DB_URL);
            Statement statement = conn.createStatement()){
                try(ResultSet rs = statement.executeQuery(sql)){
                    while(rs.next()){
                        int num = rs.getInt("index_number");
                        String temp = rs.getString("url");
                        System.out.printf("Index: %d, Url: %s%n",num, temp );
                        
                        
                    }
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
    }
    public void printToDosTable(){
        String sql = "SELECT * FROM ToDos";
        try(Connection conn = DriverManager.getConnection(DB_URL);
            Statement statement = conn.createStatement()){
                try(ResultSet rs = statement.executeQuery(sql)){
                    while(rs.next()){
                        int num = rs.getInt("index_number");
                        String temp = rs.getString("text");
                        System.out.printf("Index: %d, text: %s%n",num, temp );
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void clearTagsTable(){
        String sql = "DELETE FROM tags";
        try(Connection conn = DriverManager.getConnection(DB_URL);
            Statement statement=conn.createStatement()){
                statement.executeUpdate(sql);
            }catch(SQLException e){
                e.printStackTrace();
            }
    }
    public void clearToDosTable(){
        String sql = "DELETE FROM ToDos";
        try(Connection conn = DriverManager.getConnection(DB_URL);
        Statement statement=conn.createStatement()){
            statement.executeUpdate(sql);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void clearUrlsTable(){
        String sql = "DELETE FROM urls";
        try(Connection conn = DriverManager.getConnection(DB_URL);
            Statement statement=conn.createStatement()){
                statement.executeUpdate(sql);
            }catch(SQLException e){
                e.printStackTrace();
            }
    }
    public void deleteTagsColumn(int column){
        String sql = "DELETE FROM tags WHERE index_number=?";
        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = conn.prepareStatement(sql)){
                statement.setInt(1, column);
                statement.executeUpdate(sql);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void deleteUrlsColumn(int column){
        String sql="DELETE FROM urls WHERE index_number=?";
        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = conn.prepareStatement(sql)){
                statement.setInt(1, column);
                statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void deleteToDosColumn(int column){
        String sql = "DELETE FROM ToDos WHERE index_number=?";
        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = conn.prepareStatement(sql)){
                statement.setInt(1, column);
                statement.executeUpdate();
            }catch(SQLException e){
                e.printStackTrace();
            }
    }
    public int getTagsTableSize(){
        String sql = "SELECT COUNT(*) AS count FROM tags";
        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();){
                if(rs.next()) return rs.getInt("count");
        }catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }
    public int getUrlsTableSize(){
        String sql = "SELECT COUNT(*) as count FROM urls";
        try(Connection conn=DriverManager.getConnection(DB_URL);
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()){
                if(rs.next()) return rs.getInt("count");
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        return -1;
    }
    public int getToDosTableSize(){
        String sql = "SELECT COUNT(*) as count FROM ToDos";
        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = conn.prepareStatement(sql)){
                ResultSet rs = statement.executeQuery();
                if(rs.next()) return rs.getInt("count");
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
}

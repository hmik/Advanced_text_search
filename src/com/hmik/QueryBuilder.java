package com.hmik;

import org.postgresql.util.PGobject;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryBuilder {

    static String SQLtext;

    public static String getSQLText(){
        return SQLtext;
    }

    public static List<String> getSuggestion(String query){
        List<String> suggestions = new ArrayList<>();

//        String sql = "select * from summary\n" +
//        "where summary % 'A Beautiful Docum' \n" +
//        "order by word_similarity( summary, 'A Beautiful Docum') desc\n" +
//        "Limit 5;";

        String sql = String.format("select * from summary\n" +
                "where summary %% '%s' \n" +
                "order by word_similarity( summary, '%s') desc\n" +
                "Limit 5;", query, query);

        try (Connection con = new ConnectionManager().connect()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while(rs.next()){

                suggestions.add( rs.getString("summary"));
            }

        } catch (
                SQLException ex) {
            Logger lgr = Logger.getLogger(Main.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return suggestions;
    }

    private static String getCondition(String query, boolean and){
        String connector = " | ";
        if( and ){
            connector = " & ";
        }
        String must = "";
        String optional = "";
        List<String> allMatches = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"(.*?)\"");
        Matcher m = pattern.matcher(query);
        while (m.find()) {
            allMatches.add(m.group());
        }

        for( String s : allMatches){
            must+= s.replaceAll("\"", "").replaceAll(" ", " <-> ");
            must += connector;

        }
        optional = query.replaceAll("\"(.*?)\"", "").trim().replaceAll("\\s+", " ").replaceAll(" ", connector);
        String result;
        if(!optional.equals("")){
            result = must + optional;
        }
        else{
            result = must.substring(0, must.trim().length()-2);
        }
        //System.out.println(result);
        return result;
    }

    public static List<Movie> query(String query, boolean and) {
        List<Movie> result = new ArrayList<>();

        String condition = getCondition(query, and);
        String sql = String.format("SELECT ts_headline(title, to_tsquery('english', '%s')) as title, \n" +
                "ts_headline(categories, to_tsquery('english', '%s')) as categories,\n" +
                "ts_headline(summary, to_tsquery('english', '%s')) as summary,\n" +
                "ts_headline(description, to_tsquery('english', '%s')) as description,\n" +
                "rank\n" +
                "from(select title, categories, summary, description, ts_rank_cd(document_vectors,\n" +
                "to_tsquery('%s')) as rank from movie\n" +
                "WHERE document_vectors @@ to_tsquery('english', '%s')\n" +
                "order by rank desc) data", condition, condition, condition, condition, condition, condition);
        try (Connection con = new ConnectionManager().connect()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            SQLtext = sql;

//            System.out.println("-----------------------------------------------------------------------------------------------------------------");
//            System.out.println(sql);
//            System.out.println("-----------------------------------------------------------------------------------------------------------------");

            while(rs.next()){

                result.add( new Movie( rs.getString("title"), rs.getString("categories"), rs.getString("summary"), rs.getString("description"), rs.getString("rank")));
            }

//            if( result.isEmpty()){
//                System.out.println("No reults found");
//            }
//
//            int counter = 1;
//            for( Movie item : result){
//                System.out.print(counter++ + ". ");
//                System.out.println(item.toString().replaceAll("<b>", "\033[1m").replaceAll("</b>", "\033[0m"));
//                System.out.println("-----------------------------------------");
//
//            }

        } catch (
                SQLException ex) {
            Logger lgr = Logger.getLogger(Main.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return result;
    }

    static int count(String query) {


        try (Connection con = new ConnectionManager().connect()) {
            Statement st = con.createStatement();
            String SQL = "SELECT COUNT(title) from movie";

            if( !query.equals("")){
                //add
                String condition = "WHERE ...";
                SQL = SQL + condition;
            }
            ResultSet rs = st.executeQuery(SQL);

            if(rs.next()) {
                return rs.getInt(1);
            }

        } catch (
                SQLException ex) {
            Logger lgr = Logger.getLogger(Main.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return -1;
    }

    public static String add(Movie newMovie) {

//        Movie newMovie = new Movie();
//        System.out.println("Insert movie title: ");
//        Scanner sc = new Scanner(System.in);  // Create a Scanner object
//        newMovie.setName(sc.nextLine());
//
//        System.out.println("Insert movie category: ");
//        newMovie.setCategory(sc.nextLine());
//
//        System.out.println("Insert movie summary: ");
//        newMovie.setSummary(sc.nextLine());
//
//        System.out.println("Insert movie description: ");
//        newMovie.setDescription(sc.nextLine());
//        sc.close();

        int id = count("");

        String SQL  = "INSERT into movie ( movieId, title, categories, summary, description) VALUES(?, ?,?,?,?)";
        try (Connection con = new ConnectionManager().connect(); PreparedStatement pstmt = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setInt(1, id);
            pstmt.setString(2,newMovie.getName());
            pstmt.setString(3,newMovie.getCategory());
            pstmt.setString(4,newMovie.getSummary());
            pstmt.setString(5,newMovie.getDescription());

            int affectedRows = pstmt.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                Statement st = con.createStatement();
                String update = "UPDATE movie \n" +
                        "SET \n" +
                        "    document_vectors = (to_tsvector('english', title) || to_tsvector('english', description) || to_tsvector('english', summary) || to_tsvector('english', categories)); \n";
                try{
                    st.executeQuery(update);
                }catch(PSQLException exc){
                    //Logger lgr = Logger.getLogger(Main.class.getName());
                    //lgr.log(Level.SEVERE, exc.getMessage(), exc);
                }
                return "Successfully added data";
            }
            else {
                return "Data not added, try again";
            }

        } catch (
                SQLException ex) {
            Logger lgr = Logger.getLogger(Main.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return "Data not added, try again";
    }
}

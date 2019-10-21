package com.hmik;

import javax.swing.*;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import com.hmik.gui.Movies;


public class Main {

    public static void main(String[] args) {


        JFrame frame = new JFrame("Movies");
        frame.setContentPane(new Movies().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

//        List<Movie> movie = new ArrayList<>();
//
//        Scanner input = new Scanner(System.in);
//        String query="";
//        while( !query.equals("/q")) {
//            System.out.print("For exit enter '/q'. Enter your query: ");
//            query = input.nextLine();
//            System.out.print("Enter condition: and/or ");
//            String and = input.nextLine();
//            if( query.equals("/q")){
//                System.out.println("Exiting...");
//                break;
//            }
//            movie = QueryBuilder.query(query, and.equalsIgnoreCase("and"));
//
//            if( movie.isEmpty()){
//                System.out.println("No reults found");
//                continue;
//            }
//
//            int counter = 1;
//            for( Movie item : movie){
//                System.out.print(counter++ + ". ");
//                System.out.println(item.toString().replaceAll("<b>", "\033[1m").replaceAll("</b>", "\033[0m"));
//                System.out.println("-----------------------------------------");
//
//            }
//        }
//        System.out.println(QueryBuilder.add());
//
//
//        input.close();

    }
}

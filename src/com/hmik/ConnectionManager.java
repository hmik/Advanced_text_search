package com.hmik;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class ConnectionManager {
    private String url = "jdbc:postgresql://localhost:5432/Movies";
    private String user = "postgres";
    private String password = "thufra35";

    Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}

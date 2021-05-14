package database;

import javafx.util.Pair;
import ui.CustomQuery;
import ui.PSQuery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles all database related transactions. Taken from tutorial 6 JavaDemo.
 */
public class DatabaseConnectionHandler {
    // Use this version of the ORACLE_URL if you are running the code off of the server
//	private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
    // Use this version of the ORACLE_URL if you are tunneling into the undergrad servers
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public DatabaseConnectionHandler() {
        try {
            // Load the Oracle JDBC driver
            // Note that the path could change for new drivers
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public String dmlQuery(CustomQuery customQuery) {
        String logMessage = "Query OK";
        try {
            PSQuery psQuery = customQuery.createPreparedStatement(connection);
            String errorMsg = psQuery.getErrorMsg();

            if(!errorMsg.isEmpty()) {
                logMessage = errorMsg;
            } else {
                PreparedStatement ps = psQuery.getPreparedStatement();
                int rowCount;
                if (ps != null) {
                    rowCount = ps.executeUpdate();
                    if (rowCount == 0) {
                        logMessage = WARNING_TAG + " this tuple does not exist!";
                    }
                    connection.commit();
                    ps.close();
                } else {
                    logMessage = "Query improperly formatted";
                }
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());

            switch (e.getErrorCode()){
                case 1: //PRIMARY KEY CONFLICT
                    logMessage = "An entry for this entity already exists!";
                    break;
                case 2291: //PARENT KEY NOT FOUND
                    logMessage = "Please ensure all information not directly related to this specific request are registered, i.e. Division, Nationality, etc.";
                    break;
                default:
                    logMessage = e.getMessage();
                    break;
            }
            rollbackConnection();
        }
        return logMessage;
    }

    public Pair<String, List<List<String>>> query(CustomQuery customQuery) {
        String logMessage = "Query OK";
        List<List<String>> result = null;
        try {
            PSQuery psQuery = customQuery.createPreparedStatement(connection);
            String errorMsg = psQuery.getErrorMsg();

            if(!errorMsg.isEmpty()) {
                logMessage = errorMsg;
            } else {
                PreparedStatement ps = psQuery.getPreparedStatement();
                if (ps != null) {
                    ResultSet rs = ps.executeQuery();
                    connection.commit();

                    ResultSetMetaData metadata = rs.getMetaData();
                    int numCols = metadata.getColumnCount();

                    // todo: improve this handling
                    result = new ArrayList<>();
                    while (rs.next()) {
                        List<String> row = new ArrayList<>();
                        for (int i = 1; i <= numCols; i++) {
                            row.add(rs.getString(i));
                        }
                        result.add(row);
                    }
                    ps.close();
                } else {
                    logMessage = "Query improperly formatted";
                }
            }
        } catch (SQLException e) {
            logMessage = EXCEPTION_TAG + " " + e.getMessage() + "\n Code: " + e.getErrorCode() + "\n State: " + e.getSQLState();
            result = null;
            rollbackConnection();
        }
        return new Pair<>(logMessage, result);
    }

    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    private void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void databaseSetup() {
        dropTablesIfExist();
        // todo read queries from init.sql
        try {
            Statement stmt = connection.createStatement();
//            stmt.executeUpdate("CREATE TABLE \"Nationality_Federation\" (\n" +
//                    "    \"nationality\" varchar(20) PRIMARY KEY,\n" +
//                    "    \"FifaFederation\" varchar(20) NOT NULL\n" +
//                    ")");
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void dropTablesIfExist() {
        // todo implement this
    }
}

package controller;

import database.DatabaseConnectionHandler;
import delegates.LoginWindowDelegate;
import delegates.OperationTabDelegate;
import javafx.util.Pair;
import ui.CustomQuery;
import ui.LoginWindow;
import ui.SoccerGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.List;

/**
 * This is the main controller class that will orchestrate everything. taken from tutorial 6 JavaDemo.
 */
public class Controller implements LoginWindowDelegate, OperationTabDelegate {
    private DatabaseConnectionHandler dbHandler = null;
    private LoginWindow loginWindow = null;

    public Controller() {
        dbHandler = new DatabaseConnectionHandler();
    }

    private void start() {
        loginWindow = new LoginWindow();

        URL logoUrl = this.getClass().getClassLoader().getResource("mls_logo.png");
        if (logoUrl != null) {
            loginWindow.setIconImage((new ImageIcon(logoUrl)).getImage());
        }

        loginWindow.showFrame(this);
    }

    /**
     * LoginWindowDelegate Implementation taken from tutorial 6 JavaDemo
     * <p>
     * connects to Oracle database with supplied username and password
     */
    @Override
    public void login(String username, String password) {
        boolean didConnect = dbHandler.login(username, password);

        if (didConnect) {
            // Once connected, remove login window and start text transaction flow
            loginWindow.dispose();
            JFrame frame = new SoccerGUI("Soccer Database", this);

            URL logoUrl = this.getClass().getClassLoader().getResource("mls_logo.png");
            if (logoUrl != null) {
                frame.setIconImage((new ImageIcon(logoUrl)).getImage());
            }

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.out.println("Disconnecting from DB");
                    dbHandler.close();
                }
            });
            frame.setVisible(true);
            dbHandler.databaseSetup();
        } else {
            loginWindow.handleLoginFailed();

            if (loginWindow.hasReachedMaxLoginAttempts()) {
                loginWindow.dispose();
                System.out.println("You have exceeded your number of allowed attempts");
                System.exit(-1);
            }
        }
    }

    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.start();
    }

    @Override
    public String dmlQuery(CustomQuery customQuery) {
        return dbHandler.dmlQuery(customQuery);
    }

    @Override
    public Pair<String, List<List<String>>> query(CustomQuery customQuery) {
        return dbHandler.query(customQuery);
    }
}

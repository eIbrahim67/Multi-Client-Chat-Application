package db;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.logging.Logger;

public class DBHelper {
    private static final String URL = "jdbc:sqlserver://localhost;databaseName=multi_client_chat_server;integratedSecurity=true;trustServerCertificate=true";
    private static final Logger LOGGER = Logger.getLogger(DBHelper.class.getName());

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static String registerUser(String username, String password) {
        if (username == null || username.length() < 3 || username.length() > 20 || !username.matches("^[a-zA-Z0-9]+$")) {
            LOGGER.warning("Invalid username attempted: " + username);
            return "INVALID_USERNAME";
        }
        if (password == null || password.length() < 8 || !password.matches("^(?=.*[a-zA-Z])(?=.*[0-9]).+$")) {
            LOGGER.warning("Invalid password for user: " + username);
            return "INVALID_PASSWORD";
        }
        try (Connection conn = getConnection()) {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Users (username, password) VALUES (?, ?)");
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.executeUpdate();
            LOGGER.info("User registered successfully: " + username);
            return "SUCCESS";
        } catch (SQLException e) {
            LOGGER.warning("Registration error for user " + username + ": " + e.getMessage());
            return "USERNAME_EXISTS";
        }
    }

    public static boolean authenticateUser(String username, String password) {
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT password FROM Users WHERE username=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                boolean authenticated = BCrypt.checkpw(password, hashedPassword);
                LOGGER.info("Authentication " + (authenticated ? "successful" : "failed") + " for user: " + username);
                return authenticated;
            }
            LOGGER.warning("User not found: " + username);
            return false;
        } catch (SQLException e) {
            LOGGER.severe("Authentication error for user " + username + ": " + e.getMessage());
            return false;
        }
    }
}
package handler;

import db.DBHelper;
import server.Server_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientHandler extends Thread {
    private final Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private String username;
    private boolean authenticated = false;
    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            LOGGER.severe("Error initializing client handler: " + e.getMessage());
            System.err.println("Error initializing client handler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // Authentication loop
            while (!authenticated) {
                String command = input.readLine();
                if (command == null) {
                    LOGGER.warning("Client disconnected during authentication");
                    return;
                }
                LOGGER.info("Received command: " + command);

                switch (command) {
                    case "SIGNUP":
                        String signupUsername = input.readLine();
                        String signupPassword = input.readLine();
                        LOGGER.info("Signup attempt for user: " + signupUsername);
                        String result = DBHelper.registerUser(signupUsername, signupPassword);
                        if (result.equals("SUCCESS")) {
                            output.println("SIGNUP_SUCCESS");
                            authenticated = true;
                            username = signupUsername;
                            Server_1.broadcast("User " + username + " joined the chat.", this);
                        } else {
                            output.println("SIGNUP_FAILED_" + result);
                        }
                        break;

                    case "LOGIN":
                        String loginUsername = input.readLine();
                        String loginPassword = input.readLine();
                        LOGGER.info("Login attempt for user: " + loginUsername);
                        if (DBHelper.authenticateUser(loginUsername, loginPassword)) {
                            username = loginUsername;
                            authenticated = true;
                            output.println("LOGIN_SUCCESS");
                            Server_1.broadcast("User " + username + " joined the chat.", this);
                        } else {
                            output.println("LOGIN_FAILED");
                        }
                        break;

                    default:
                        LOGGER.warning("Invalid command received: " + command);
                        output.println("INVALID_COMMAND");
                        break;
                }
            }

            // Chat loop
            while (authenticated) {
                String message = input.readLine();
                if (message == null) {
                    LOGGER.warning("Client " + username + " disconnected");
                    break;
                }
                LOGGER.info("Received message from " + username + ": " + message);

                if (message.equalsIgnoreCase("/exit")) {
                    break;
                } else if (message.equalsIgnoreCase("/help")) {
                    sendMessage("Available commands:\n" +
                            "/online - List online users\n" +
                            "/msg <username> <message> - Send a private message\n" +
                            "/exit - Leave the chat");
                    continue;
                } else if (message.equalsIgnoreCase("/online")) {
                    StringBuilder onlineUsers = new StringBuilder("Online users: ");
                    synchronized (Server_1.getClientHandlers()) {
                        for (ClientHandler ch : Server_1.getClientHandlers()) {
                            if (ch.isAuthenticated()) {
                                onlineUsers.append(ch.username).append(", ");
                            }
                        }
                    }
                    sendMessage(onlineUsers.length() > 14 ? onlineUsers.substring(0, onlineUsers.length() - 2) : "No other users online");
                    continue;
                } else if (message.startsWith("/msg ")) {
                    String[] parts = message.split(" ", 3);
                    if (parts.length < 3) {
                        sendMessage("Usage: /msg <username> <message>");
                        continue;
                    }
                    String targetUsername = parts[1];
                    String privateMessage = parts[2];
                    boolean sent = false;
                    synchronized (Server_1.getClientHandlers()) {
                        for (ClientHandler ch : Server_1.getClientHandlers()) {
                            if (ch.isAuthenticated() && ch.username.equalsIgnoreCase(targetUsername)) {
                                ch.sendMessage("Private from " + username + ": " + privateMessage);
                                sendMessage("Sent to " + targetUsername + ": " + privateMessage);
                                LOGGER.info("Private message from " + username + " to " + targetUsername + ": " + privateMessage);
                                sent = true;
                                break;
                            }
                        }
                    }
                    if (!sent) {
                        sendMessage("User " + targetUsername + " not found or offline.");
                    }
                    continue;
                }
                Server_1.broadcast(username + ": " + message, this);
            }
        } catch (IOException e) {
            LOGGER.warning("Client disconnected: " + (username != null ? username : "unknown"));
        } finally {
            try {
                Server_1.broadcast("User " + (username != null ? username : "unknown") + " left the chat.", this);
                Server_1.removeHandler(this);
                if (input != null) input.close();
                if (output != null) output.close();
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException e) {
                LOGGER.severe("Error closing resources: " + e.getMessage());
                System.err.println("Error closing resources: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        if (output != null) {
            LOGGER.info("Sending message to " + (username != null ? username : "unknown") + ": " + message);
            output.println(message);
        }
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getUsername() {
        return username;
    }
}
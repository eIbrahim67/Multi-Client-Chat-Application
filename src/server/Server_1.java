package server;

import handler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Server_1 {
    private static final List<ClientHandler> clientHandlers = Collections.synchronizedList(new ArrayList<>());
    private static final Logger LOGGER = Logger.getLogger(Server_1.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("server.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.INFO);
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LOGGER.info("Starting server on port 5555...");
        try (ServerSocket server = new ServerSocket(5555)) {
            System.out.println("Server is running on port 5555...");
            while (true) {
                Socket clientSocket = server.accept();
                LOGGER.info("New client connected: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandlers.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            LOGGER.severe("Server error: " + e.getMessage());
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void broadcast(String message, ClientHandler sender) {
        String timestampedMessage = String.format("[%s] %s",
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                message);
        synchronized (clientHandlers) {
            for (ClientHandler ch : clientHandlers) {
                if (ch != sender && ch.isAuthenticated()) {
                    ch.sendMessage(timestampedMessage);
                }
            }
        }
        LOGGER.info("Broadcast: " + timestampedMessage);
    }

    public static void removeHandler(ClientHandler handler) {
        clientHandlers.remove(handler);
        LOGGER.info("Removed client handler: " + (handler.getUsername() != null ? handler.getUsername() : "unknown"));
    }

    public static List<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }
}
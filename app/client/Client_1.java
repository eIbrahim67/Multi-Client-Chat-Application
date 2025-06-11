package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client_1 {
    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            socket = new Socket("localhost", 5555);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                System.out.println("1. Sign Up");
                System.out.println("2. Login");
                System.out.print("Enter choice: ");
                try {
                    int choice = Integer.parseInt(scanner.nextLine());
                    if (choice == 1) {
                        if (signUp()) {
                            System.out.println("Sign Up Successful!");
                            startChat();
                            break;
                        }
                    } else if (choice == 2) {
                        if (login()) {
                            System.out.println("Login Successful!");
                            startChat();
                            break;
                        }
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    private static boolean signUp() throws IOException {
        System.out.print("Enter new username (3-20 characters, alphanumeric only): ");
        String username = scanner.nextLine();
        System.out.print("Enter new password (8+ characters, must include letters and numbers): ");
        String password = scanner.nextLine();

        out.println("SIGNUP");
        out.println(username);
        out.println(password);

        String response = in.readLine();
        if (response == null) return false;
        if (response.equals("SIGNUP_SUCCESS")) return true;
        if (response.equals("SIGNUP_FAILED_INVALID_USERNAME")) {
            System.out.println("Invalid username. Use 3-20 alphanumeric characters.");
        } else if (response.equals("SIGNUP_FAILED_INVALID_PASSWORD")) {
            System.out.println("Invalid password. Use 8+ characters with letters and numbers.");
        } else if (response.equals("SIGNUP_FAILED_USERNAME_EXISTS")) {
            System.out.println("Username already exists.");
        } else {
            System.out.println("Sign Up Failed. Try again.");
        }
        return false;
    }

    private static boolean login() throws IOException {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        out.println("LOGIN");
        out.println(username);
        out.println(password);

        String response = in.readLine();
        if (response == null) return false;
        if (response.equals("LOGIN_SUCCESS")) return true;
        System.out.println("Login Failed. Invalid username or password.");
        return false;
    }

    private static void startChat() throws IOException {
        System.out.println("You can start chatting now! Type '/help' for commands or 'exit' to quit.");

        new Thread(() -> {
            try {
                String serverMsg;
                while ((serverMsg = in.readLine()) != null) {
                    System.out.println(serverMsg);
                }
            } catch (IOException e) {
                System.err.println("Disconnected from server.");
            }
        }).start();

        String userMsg;
        while (!(userMsg = scanner.nextLine()).equalsIgnoreCase("exit")) {
            out.println(userMsg);
        }

        out.println("/exit");
    }

    private static void closeResources() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            if (scanner != null) scanner.close();
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
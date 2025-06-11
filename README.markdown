# Multi-Client Chat Application

## Overview

The Multi-Client Chat Application is a robust, terminal-based real-time messaging system built in Java using TCP/IP socket programming. It enables multiple users to connect to a central server, authenticate securely, and exchange messages in a group chat or through private messaging. Integrated with a Microsoft SQL Server database for user management, the application ensures secure authentication with password hashing. Designed for scalability and usability, it includes features like online user listing, command help, and detailed server logging, making it suitable for educational purposes, small-scale deployments, or as a foundation for more complex chat systems.

This project was developed to demonstrate client-server architecture, multi-threaded programming, and secure database integration while providing a user-friendly terminal interface. It is actively maintained and tested as of June 2025.

## Features

### Core Functionality
- **Multi-Client Support**: Handles multiple concurrent clients, each running in a separate thread for seamless communication.
- **Real-Time Messaging**: Broadcasts messages instantly to all authenticated users, with support for private messaging.
- **Secure Authentication**: Integrates with SQL Server to manage user registration and login, using BCrypt for password hashing.
- **Terminal-Based Interface**: Provides a simple, command-line interface for all interactions, ideal for console enthusiasts.

### Enhanced Features
- **List Online Users**: Users can type `/online` to view a list of currently connected users.
- **Username Validation**: Ensures usernames are 3–20 characters, alphanumeric, and unique, preventing invalid or duplicate entries.
- **Private Messaging**: Supports private messages via `/msg <username> <message>` for one-to-one communication.
- **Message Timestamps**: Prepends messages with timestamps (e.g., `[2025-06-11 23:45]`) for better context.
- **Password Strength Validation**: Requires passwords to be 8+ characters with a mix of letters and numbers.
- **Command Help Menu**: The `/help` command displays available commands (`/online`, `/msg`, `/exit`).
- **Enhanced Server Logging**: Logs all significant events (connections, messages, authentication) to `server.log` for debugging and monitoring.

## Prerequisites

To run the application, ensure the following are installed and configured:

- **Java Development Kit (JDK)**: Version 11 or higher (tested with OpenJDK 21.0.1).
- **Microsoft SQL Server**: A running instance (local or remote) with a database named `multi_client_chat_server`.
- **SQL Server JDBC Driver**: Version 12.10.0 or compatible (e.g., `mssql-jdbc-12.10.0.jre11.jar`).
- **BCrypt Library**: Version 0.4 for password hashing (e.g., `jbcrypt-0.4.jar`).
- **Operating System**: Tested on Windows; compatible with Linux/macOS with minor path adjustments.

## Setup Instructions

### 1. Database Configuration
1. **Install SQL Server**:
   - Ensure SQL Server is running on `localhost` (or update the JDBC URL in `DBHelper.java` if using a different host).
   - Configure Windows Authentication or SQL Server Authentication as needed.

2. **Create Database**:
   - Open SQL Server Management Studio (SSMS) and create a database:
     ```sql
     CREATE DATABASE multi_client_chat_server;
     ```

3. **Create Users Table**:
   - Execute the following SQL to create the `Users` table:
     ```sql
     CREATE TABLE Users (
         username VARCHAR(50) PRIMARY KEY,
         password VARCHAR(60)
     );
     ```

### 2. Project Setup
1. **Clone or Download**:
   - Obtain the project source code from the repository or copy the provided files (`Server_1.java`, `Client_1.java`, `ClientHandler.java`, `DBHelper.java`).

2. **Directory Structure**:
   - Place all Java files in a `socket` package (e.g., `src/socket/`).
   - Ensure dependencies (JDBC driver and BCrypt) are accessible.

3. **Dependencies**:
   - Download the SQL Server JDBC driver and BCrypt JARs, or use Maven:
     ```xml
     <dependency>
         <groupId>com.microsoft.sqlserver</groupId>
         <artifactId>mssql-jdbc</artifactId>
         <version>12.10.0.jre11</version>
     </dependency>
     <dependency>
         <groupId>org.mindrot</groupId>
         <artifactId>jbcrypt</artifactId>
         <version>0.4</version>
     </dependency>
     ```
   - Place JARs in a known location (e.g., `C:\Users\<YourUser>\Desktop\sqljdbc_12.10\enu\jars\` and `C:\Users\<YourUser>\.m2\repository\org\mindrot\jbcrypt\0.4\`).

### 3. Compilation
- Compile the Java files with the dependencies in the classpath:
  ```
  javac -cp ".;C:\Users\<YourUser>\Desktop\sqljdbc_12.10\enu\jars\mssql-jdbc-12.10.0.jre11.jar;C:\Users\<YourUser>\.m2\repository\org\mindrot\jbcrypt\0.4\jbcrypt-0.4.jar" socket/*.java
  ```

### 4. Running the Application
1. **Start the Server**:
   - Run the server in a terminal:
     ```
     java -cp ".;C:\Users\<YourUser>\Desktop\sqljdbc_12.10\enu\jars\mssql-jdbc-12.10.0.jre11.jar;C:\Users\<YourUser>\.m2\repository\org\mindrot\jbcrypt\0.4\jbcrypt-0.4.jar" socket.Server_1
     ```
   - The server listens on port 5555 and logs events to `server.log`.

2. **Run Clients**:
   - Open additional terminals and start clients:
     ```
     java -cp ".;C:\Users\<YourUser>\Desktop\sqljdbc_12.10\enu\jars\*.jar;C:\Users\<YourUser>\.m2\repository\org\mindrot\jbcrypt\0.4\*.jar" socket.Client_1
     ```
   - Each client connects to `localhost:5555`.

## Usage

### Client Interface
1. **Authentication**:
   - Choose `1. Sign Up` or `2. Login`.
   - **Sign Up**:
     - Enter a username (3–20 characters, alphanumeric only).
     - Enter a password (8+ characters, must include letters and numbers).
     - Example:
       ```
       Enter choice: 1
       Enter new username (3-20 characters, alphanumeric only): mohamed
       Enter new password (8+ characters, must include letters and numbers): password123
       Sign Up Successful!
       ```
   - **Login**:
     - Enter existing credentials.
     - Example:
       ```
       Enter choice: 2
       Enter username: ibrahim
       Enter password: password123
       Login Successful!
       ```

2. **Chatting**:
   - Upon successful authentication, enter the chat mode:
     ```
     You can start chatting now! Type '/help' for commands or 'exit' to quit.
     ```
   - **Send Messages**: Enter text to broadcast (e.g., `hi`).
   - **Commands**:
     - `/help`: Displays available commands.
     - `/online`: Lists online users (e.g., `Online users: mohamed, ibrahim`).
     - `/msg <username> <message>`: Sends a private message (e.g., `/msg ibrahim Hello`).
     - `/exit`: Disconnects from the server.
   - **Message Format**: Messages include timestamps (e.g., `[2025-06-11 23:45] mohamed: hi`).
   - **Private Messages**: Appear as `Private from ibrahim: Hello`.

3. **Disconnecting**:
   - Type `/exit` or `exit` to leave; other clients are notified (e.g., `[2025-06-11 23:46] User mohamed left the chat.`).

### Example Interaction
**Server Log (`server.log`)**:
```
Jun 11, 2025 11:45:23 PM socket.Server_1 main
INFO: Starting server on port 5555...
Jun 11, 2025 11:45:30 PM socket.Server_1 main
INFO: New client connected: /127.0.0.1
Jun 11, 2025 11:45:35 PM socket.ClientHandler run
INFO: Signup attempt for user: mohamed
Jun 11, 2025 11:45:40 PM socket.ClientHandler run
INFO: Received message from mohamed: hi
Jun 11, 2025 11:45:40 PM socket.Server_1 broadcast
INFO: Broadcast: [2025-06-11 23:45] mohamed: hi
```

**Client 1 (`ibrahim`)**:
```
1. Sign Up
2. Login
Enter choice: 2
Enter username: ibrahim
Enter password: password123
Login Successful!
You can start chatting now! Type '/help' for commands or 'exit' to quit.
[2025-06-11 23:45] mohamed: hi
hello
Private from mohamed
```

**Client 2 (`mohamed`)**:
```
1. Sign Up
2. Login
Enter choice: 1
Enter new username (3-20 characters, alphanumeric only): mohamed
Enter new password (8+ characters, must include letters and numbers): password123
Sign Up Successful!
You can start chatting now! Type '/help' for commands or 'exit' to quit.
[2025-06-11 23:45] User ibrahim joined the chat.
hi
[2025-06-11 23:46] ibrahim: hello
/online
Online users: mohamed, ibrahim
/msg ibrahim Hello
Sent to ibrahim: Hello
Private from ibrahim: Hi back!
/exit
```
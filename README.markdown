# Multi-Client Chat Application

## Overview

The Multi-Client Chat Application is a robust, terminal-based real-time messaging system built in Java using TCP/IP socket programming. It enables multiple users to connect to a central server, authenticate securely, and exchange messages in a group chat or through private messaging. Integrated with a Microsoft SQL Server database for user management, the application ensures secure authentication with password hashing. Designed for scalability and usability, it includes features like online user listing, command help, and detailed server logging, making it suitable for educational purposes, small-scale deployments, or as a foundation for more complex chat systems.

This project was developed to demonstrate client-server architecture, multi-threaded programming, and secure database integration while providing a user-friendly terminal interface. It is actively maintained and tested as of June 2025.

---

## Features

### Core Functionality
- **Multi-Client Support**: Handles multiple concurrent clients, each running in a separate thread for seamless communication.
- **Real-Time Messaging**: Broadcasts messages instantly to all authenticated users, with support for private messaging.
- **Secure Authentication**: Integrates with SQL Server to manage user registration and login, using BCrypt for password hashing.
- **Terminal-Based Interface**: Provides a simple, command-line interface for all interactions, ideal for console enthusiasts.

---

### Enhanced Features
- **List Online Users**: Users can type `/online` to view a list of currently connected users.
- **Username Validation**: Ensures usernames are 3–20 characters, alphanumeric, and unique, preventing invalid or duplicate entries.
- **Private Messaging**: Supports private messages via `/msg <username> <message>` for one-to-one communication.
- **Message Timestamps**: Prepends messages with timestamps (e.g., `[2025-06-11 23:45]`) for better context.
- **Password Strength Validation**: Requires passwords to be 8+ characters with a mix of letters and numbers.
- **Command Help Menu**: The `/help` command displays available commands (`/online`, `/msg`, `/exit`).
- **Enhanced Server Logging**: Logs all significant events (connections, messages, authentication) to `server.log` for debugging and monitoring.

---

## Architecture Diagram

```
+---------+       +--------+       +---------+
| Client1 | <---> | Server | <---> | Client2 |
+---------+       +--------+       +---------+
         \                          /
          \                        /
           +----------------------+
                     |
            Database (SQL Server)
```

---

---

## Prerequisites

* **Java Development Kit (JDK)**: Version 11 or higher (tested with OpenJDK 21.0.1).
* **Microsoft SQL Server**: Running instance with `multi_client_chat_server` database.
* **SQL Server JDBC Driver**: Version 12.10.0.
* **BCrypt Library**: Version 0.4.
* **Operating System**: Tested on Windows; Linux/macOS compatible.

---

## Setup Instructions

### 1. Database Configuration

1. **Install SQL Server** and ensure it is running.
2. **Create Database**:

```sql
CREATE DATABASE multi_client_chat_server;
```

3. **Create Users Table**:

```sql
CREATE TABLE Users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(60)
);
```

### 2. Project Setup

1. **Clone/Download** project.
2. Place Java files under `src/socket/`.
3. Add dependencies (JDBC and BCrypt) via Maven or manually.

### 3. Compilation

```bash
javac -cp ".;path_to_jars/*" socket/*.java
```

### 4. Running

**Start Server:**

```bash
java -cp ".;path_to_jars/*" socket.Server_1
```

**Start Clients:**

```bash
java -cp ".;path_to_jars/*" socket.Client_1
```

---

## Usage

### Client Interface

1. **Authentication:** Sign Up or Login.
2. **Commands:**

   * `/help`: Show commands.
   * `/online`: List online users.
   * `/msg <username> <message>`: Private message.
   * `/exit`: Disconnect.

### Example Interaction

```
1. Sign Up
2. Login
Enter choice: 2
Enter username: ibrahim
Enter password: password123
Login Successful!
```

**Server Log:**

```
Jun 11, 2025 11:50:12 PM socket.ClientHandler run
INFO: Signup attempt for user: mohamed
```

---

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

---

### Test Screenshot

![Test Screenshot](https://raw.githubusercontent.com/eIbrahim67/Multi-Client-Chat-Application/refs/heads/master/app/src/test.png)

---

## Troubleshooting

| Problem                 | Possible Cause                    | Solution                                 |
| ----------------------- | --------------------------------- | ---------------------------------------- |
| Port 5555 in use        | Another service using the port    | Change server port in `Server_1.java`    |
| JDBC Driver Not Found   | Incorrect classpath or dependency | Verify JAR paths or Maven config         |
| Login Fails             | Incorrect username/password       | Ensure credentials match DB records      |
| Password Strength Error | Weak password during signup       | Use 8+ chars, letters & numbers required |

---

## To Do / Future Improvements

* JavaFX/Swing GUI.
* End-to-End encryption.
* File transfer capability.
* Dockerized server deployment.

---

## License

```
MIT License © 2025 Ibrahim Mohamed Ibrahim
```

---

## Author & Contact

```
Developed by: Ibrahim Mohamed Ibrahim
GitHub: https://github.com/eIbrahim67
LinkedIn: https://www.linkedin.com/in/eibrahim67
Email: ibrahim.mohamed.ibrahim.t@gmail.com
```

---

package util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;

public class Service {
    static UserIdGenerator usernameGenerator;

    static {
        try {
            usernameGenerator = new UserIdGenerator(
                    Paths.get("misc/28K adjectives.txt"),
                    Paths.get("misc/91K nouns.txt"),
                    20, 120);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static Map<Socket, String> clients = new HashMap<>();
    public static Scanner getReader(Socket socket) throws IOException {
        return new Scanner(new InputStreamReader(socket.getInputStream()));
    }

    public static PrintWriter getWriter(Socket socket) throws IOException {
        try {
            return new PrintWriter(socket.getOutputStream());
        } catch (SocketException e) {
            System.out.println("Something went wrong");
        }
        return new PrintWriter(socket.getOutputStream());
    }

    public static void execute(int port, ExecutorService pool) {
        try (ServerSocket server = new ServerSocket(port)) {
            while (!server.isClosed()) {
                Socket clientSocket = server.accept();
                clients.put(clientSocket, usernameGenerator.generate());
                pool.submit(() -> {
                    try {
                        handle(clientSocket);
                    } catch (IOException e) {
                        throw new RuntimeException("Something went wrong\n");
                    }
                });
            }
        } catch (IOException e) {
            System.out.printf("The port %s is probably busy.\n", port);
            e.printStackTrace();
        }
    }

    public static void handle(Socket socket) throws IOException {
        notifyAboutConnection(socket);
        Scanner reader = getReader(socket);
        PrintWriter writer = getWriter(socket);
        try (socket; reader; writer) {
            handleMessageFromClient(socket);
        } catch (NoSuchElementException e) {
            System.out.println("Client has dropped the connection");
        } catch (IOException e) {
            e.printStackTrace();
        }
        notifyAboutDisconnect(socket);
        clients.remove(socket);
    }

    public static void notifyAboutDisconnect(Socket socket) {
        System.out.printf("Client %s decided to leave the chat\n", clients.get(socket));
    }

    public static void notifyAboutConnection(Socket socket) {
        System.out.printf("Client %s has connected\n", clients.get(socket));
    }

    public static String sayHi(Socket socket) {
        return "Hello, " + clients.get(socket);
    }

    public static boolean isEmpty(String message) {
        return message == null || message.isBlank();
    }

    public static void sendResponse(String response, Writer writer) throws IOException {
        writer.write(response);
        writer.write(System.lineSeparator());
        writer.flush();
    }

    public static String failedUsernameChange(String username) {
        return String.format("Sorry, username %s is already taken", username);
    }

    public static String usernameChangeMessageForUsers(Socket socket, String username) {
        return String.format("User %s is now known as %s", clients.get(socket), username);
    }

    public static String successfulUsernameChangeMessageFor(String username) {
        return String.format("You are now known as %s", username);
    }

    public static String failedPrivateMessageSend() {
        return "There are no users with such usernames";
    }

    public static void sendMessageToEveryone(Socket socket, String message) throws IOException {
        for (var client : clients.entrySet()) {
            if (socket.getPort() != client.getKey().getPort()) {
                sendResponse(clients.get(socket) + ": " + message, getWriter(client.getKey()));
            }
        }
    }

    public static void getListOfUsers(Socket socket) throws IOException {
        for (var client : clients.entrySet()) {
            sendResponse(client.getValue() + "\n", getWriter(socket));
        }
    }

    public static String getUsernameFrom(String message) {
        String[] array = message.split(" ");
        return array[1].trim();
    }

    public static String getMessage(String message) {
        String[] array = message.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < array.length; i++) {
            sb.append(array[i]).append(" ");
        }
        return sb.toString();
    }

    public static void notifyUsersAboutNicknameChange(Socket socket, String username) throws IOException {
        for (var client : clients.entrySet()) {
            if (socket.getPort() != client.getKey().getPort()) {
                sendResponse(usernameChangeMessageForUsers(socket, username), getWriter(client.getKey()));
            }
        }
    }

    public static void notifySelfAboutNicknameChange(Socket socket, String username) throws IOException {
        for (var client : clients.entrySet()) {
            if (socket.getPort() == client.getKey().getPort()) {
                clients.replace(socket, username);
                sendResponse(successfulUsernameChangeMessageFor(username), getWriter(socket));
            }
        }
    }

    public static void executeUsernameChange(Socket socket, String currentNickname) throws IOException {
        if (clients.containsValue(currentNickname)) {
            sendResponse(failedUsernameChange(currentNickname), getWriter(socket));
        } else {
            notifyUsersAboutNicknameChange(socket, currentNickname);
            notifySelfAboutNicknameChange(socket, currentNickname);
        }
    }

    public static void executePrivateMessageSend(String message, Socket socket) throws IOException {
        String username = getUsernameFrom(message);
        String sentMessage = getMessage(message);
        if (!clients.containsValue(username)) {
            sendResponse(failedPrivateMessageSend(), getWriter(socket));
        } else {
            for (var client : clients.entrySet()) {
                if (socket.getPort() != client.getKey().getPort() && client.getValue().equals(username)) {
                    sendResponse(clients.get(socket) + ": " + sentMessage, getWriter(client.getKey()));
                }
            }
        }
    }

    public static void handleWrongCommand(Socket socket) throws IOException {
        for (var client : clients.entrySet()) {
            if (socket.getPort() == client.getKey().getPort()) {
                sendResponse("Wrong command", getWriter(socket));
            }
        }
    }

    public static void handleMessageFromClient(Socket socket) throws IOException {
        sendResponse(sayHi(socket), getWriter(socket));
        while (true) {
            String message = getReader(socket).nextLine();
            if (isEmpty(message)) {
                break;
            }
            if (message.equals("/list")) {
                getListOfUsers(socket);
            } else if (message.startsWith("/name ")) {
                String currentNickname = getUsernameFrom(message);
                executeUsernameChange(socket, currentNickname);
            } else if (message.startsWith("/whisper ")) {
                executePrivateMessageSend(message, socket);
            } else if (message.startsWith("/")) {
                handleWrongCommand(socket);
            } else {
                sendMessageToEveryone(socket, message);
            }
        }
    }
}

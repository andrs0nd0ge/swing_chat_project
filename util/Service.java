package util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ExecutorService;

public class Service {
    static Map<Socket, String> clients = new HashMap<>();
    static Map<Integer, String> usernames = new HashMap<>();
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
                clients.put(clientSocket, null);
                usernames.put(clientSocket.getPort(), UUID.randomUUID().toString());
                pool.submit(() -> {
                    try {
                        handle(clientSocket);
                    } catch (IOException e) {
                        throw new RuntimeException("Something went wrong\n");
                    }
                });
            }
            usernames.clear();
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
            sendMessageToClient(socket);
        } catch (NoSuchElementException e) {
            System.out.println("Client has dropped the connection");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("Client %s decided to leave the chat\n", usernames.get(socket.getPort()));
    }

    public static void notifyAboutConnection(Socket socket) {
        System.out.printf("Client %s has connected\n", usernames.get(socket.getPort()));
    }

    public static String sayHi(Socket socket) {
        return "Hello, " + usernames.get(socket.getPort());
    }

    public static boolean isQuit(String message) {
        return "bye".equalsIgnoreCase(message);
    }

    public static boolean isEmpty(String message) {
        return message == null || message.isBlank();
    }

    public static void sendResponse(String response, Writer writer) throws IOException {
        writer.write(response);
        writer.write(System.lineSeparator());
        writer.flush();
    }

    public static void sendMessageToClient(Socket socket) throws IOException {
        sendResponse(sayHi(socket), getWriter(socket));
        while (true) {
            String message = getReader(socket).nextLine();
            if (isQuit(message) || isEmpty(message)) {
                break;
            }
            for (var client : clients.entrySet()) {
                if (socket.getPort() != client.getKey().getPort()) {
                    sendResponse(usernames.get(socket.getPort()) + ": " + message, getWriter(client.getKey()));
                }
            }
        }
    }
}

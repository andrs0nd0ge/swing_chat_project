package server;

import util.Service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer{
    private final int port;

    private final ExecutorService pool = Executors.newCachedThreadPool();

    private EchoServer(int port) {
        this.port = port;
    }

    static EchoServer bindToPort(int port) {
        return new EchoServer(port);
    }

    public void run() {
        Service.execute(port, pool);
//        try (ServerSocket server = new ServerSocket(port)) {
//            while (!server.isClosed()) {
//                Socket clientSocket = server.accept();
//                pool.submit(() -> {
//                    try {
//                        handle(clientSocket);
//                    } catch (IOException e) {
//                        throw new RuntimeException("Something went wrong");
//                    }
//                });
//            }
//        } catch (IOException e) {
//            System.out.printf("The port %s is probably busy.\n", port);
//            e.printStackTrace();
//        }
    }

//    private void handle(Socket socket) throws IOException {
//        Service.notifyAboutConnection(socket);
//        Scanner reader = Service.getReader(socket);
//        PrintWriter writer = Service.getWriter(socket);
//        try (socket; reader; writer) {
//            Service.sendMessageToClient(socket);
//        } catch (NoSuchElementException e) {
//            System.out.println("Client has dropped the connection");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.printf("Client %s leaved the chat\n", socket.getPort());
//        System.out.println("Write \"bye\" to exit");
//        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
//        InputStreamReader isr = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
//        OutputStream output = socket.getOutputStream();
//        PrintWriter writer = new PrintWriter(output);
//        Scanner sc = new Scanner(isr);
//        try (sc; scanner; writer) {
//            while (true) {
//                String messageFromTheClient = sc.nextLine();
//                while ("bye".equalsIgnoreCase(messageFromTheClient)) {
//                    System.out.println("Client has decided to leave the chat :(");
//                    return;
//                }
//                while ("date".trim().equalsIgnoreCase(messageFromTheClient)) {
//                    System.out.println("Client decided to know what day it is");
//                    break;
//                }
//                while ("time".trim().equalsIgnoreCase(messageFromTheClient)) {
//                    System.out.println("Client decided to know what time it is");
//                    break;
//                }
//                while (messageFromTheClient.startsWith("reverse".toLowerCase().trim())) {
//                    System.out.println("Client decided to reverse their message");
//                    break;
//                }
//                while (messageFromTheClient.startsWith("upper".toLowerCase().trim())) {
//                    System.out.println("Client decided to make their message in upper case");
//                    break;
//                }
//                System.out.printf("Client: %s\n", messageFromTheClient);
//                String message = scanner.nextLine().trim();
//                writer.write(message);
//                writer.write(System.lineSeparator());
//                writer.flush();
//            }
//        } catch (NoSuchElementException e) {
//            System.out.println("Client has dropped the connection");
//        }
//    }
}

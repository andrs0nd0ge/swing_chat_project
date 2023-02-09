package client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.UUID;

public class EchoClient {
    private final int port;
    private final String host;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static EchoClient connectTo(int port) {
        var localhost = "127.0.0.1";
        return new EchoClient(localhost, port);
    }

    public void run() {
//        try (Socket socket = new Socket(host, port)) {
//            System.out.println("Write \"bye\" to exit");
//            InputStreamReader isr = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
//            Scanner serverScanner = new Scanner(isr);
//            Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
//            OutputStream output = socket.getOutputStream();
//            PrintWriter writer = new PrintWriter(output);
//            try (serverScanner; scanner; writer) {
//                while (true) {
//                    String message = scanner.nextLine();
//                    writer.write(message);
//                    writer.write(System.lineSeparator());
//                    writer.flush();
//                    while ("bye".trim().equalsIgnoreCase(message)) {
//                        System.out.println("Bye!");
//                        return;
//                    }
//                    while ("date".trim().equalsIgnoreCase(message)) {
//                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
//                        LocalDate localDate = LocalDate.now();
//                        System.out.println(dtf.format(localDate));
//                        break;
//                    }
//                    while ("time".trim().equalsIgnoreCase(message)) {
//                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
//                        LocalTime localTime = LocalTime.now();
//                        System.out.println(dtf.format(localTime));
//                        break;
//                    }
//                    while (message.startsWith("reverse".toLowerCase().trim())) {
//                        System.out.println(appendStringBuilder(message).reverse());
//                        break;
//                    }
//                    while (message.startsWith("upper".toLowerCase().trim())) {
//                        System.out.println(appendStringBuilder(message).toString().toUpperCase());
//                        break;
//                    }
//                    String messageFromTheServer = serverScanner.nextLine();
//                    System.out.printf("Server: %s\n", messageFromTheServer);
//                }
//            }
//        } catch (NoSuchElementException e) {
//            System.out.println("Connection has been dropped");
//        } catch (IOException e) {
//            System.out.printf("Can't connect to %s:%s\n", host, port);
//            e.printStackTrace();
//        }
    }

//    private StringBuilder appendStringBuilder(String input) {
//        String[] message = input.split(" ");
//        StringBuilder sb = new StringBuilder();
//        for (int i = 1; i < message.length; i++) {
//            sb.append(message[i]).append(" ");
//        }
//        return sb;
//    }
}


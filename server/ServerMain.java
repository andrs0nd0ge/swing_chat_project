package server;

public class ServerMain {
    public static void main(String[] args) {
        EchoServer.bindToPort(8780).run();
    }
}

package client;

public class ClientMain {
    public static void main(String[] args) {
        EchoClient.connectTo(8780).run();
    }
}

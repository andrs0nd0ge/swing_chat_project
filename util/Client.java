package util;

import java.net.Socket;

public class Client extends Socket {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void generateUsername() {

    }
}

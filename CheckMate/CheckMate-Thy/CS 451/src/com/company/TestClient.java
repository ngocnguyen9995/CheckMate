package com.company;
public class TestClient {

    public static void main(String[] args) {
        System.out.println("Init Client");
        Client client = new Client();
        System.out.println("Join game");
        client.playGame();
    }
}

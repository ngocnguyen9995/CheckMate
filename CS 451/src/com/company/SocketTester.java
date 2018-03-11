/*
 * SocketTester.java -
 *
 * 3/12/18
 *
 * Java SE 8
 * Kyle Eng
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class SocketTester {

    public static void main(String[] args) {
        startHost();
        startClient();
    }

    public static void startClient() {
        (new Thread() {
            @Override
            public void run() {
                try {
                    SocketManager manager1 = new SocketManager();
                    Socket client = manager1.initClient();
                    Thread.sleep(4000);
                    System.out.println("Type in host port number");
                    Scanner scan = new Scanner(System.in);
                    int hostPort = scan.nextInt();
                    System.out.println("Client connecting to port " + hostPort);
                    if (manager1.connect("localhost", hostPort, client)) {
                        String sArray[][] = {{"Green","Blue","Red"}, {"Jack","Queen","King"}};
                        System.out.println("Client Message being sent:" + System.lineSeparator()
                                + Arrays.deepToString(sArray) + System.lineSeparator());
                        manager1.sendMessage(client, sArray);

                        String messageArray[][] = (String[][]) manager1.waitForMessage(client);
                        if (messageArray != null) {
                            System.out.println("Client got an array:");
                            System.out.println(Arrays.deepToString(messageArray) + System.lineSeparator());
                        }

                    } else {
                        System.out.println("Connection to host failed");
                    }

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();
    }

    public static void startHost() {
        (new Thread() {
            @Override
            public void run() {
                ServerSocket serverSock;
                try {
                    SocketManager manager = new SocketManager();
                    serverSock = manager.initServer();

                    Socket server = SocketManager.listen(serverSock);

                    String resultArray[][] = (String[][]) manager.waitForMessage(server);
                    if (resultArray != null) {
                        System.out.println("Host got an array:");
                        System.out.println(Arrays.deepToString(resultArray) + System.lineSeparator());
                    }

                    String sArray[][] = {{"Car","Camera","Cat"}, {"Flying","Ace","Plane"}};
                    System.out.println("Host Message being sent:" + System.lineSeparator()
                            + Arrays.deepToString(sArray) + System.lineSeparator());
                    manager.sendMessage(server, sArray);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

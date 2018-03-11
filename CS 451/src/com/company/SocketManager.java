/*
 * SocketManager.java - Class responsible for managing initialization, listening/connecting between servers,
 * and sending/receiving messages between sockets.
 *
 * 3/12/18
 *
 * Java SE 8
 * Kyle Eng
 */

package com.company;
import java.io.*;
import java.net.*;
import java.util.*;

public class SocketManager {

    protected static boolean connectStatus = false;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;

    public static boolean isInt(String str) {
        Scanner scan = new Scanner(str.trim());
        if(!scan.hasNextInt()) { return false; }
        scan.nextInt();
        return !scan.hasNext();
    }

    public ServerSocket initServer() throws IOException{
        ServerSocket serverSocket = new ServerSocket(0);
        serverSocket.setSoTimeout(50000);
        System.out.println("Host is listening on port " + serverSocket.getLocalPort());
        return serverSocket;
    }

    public Socket initClient() {
        return new Socket();
    }

    public static Socket listen(ServerSocket serverSocket) throws IOException {
        Socket server = null;
        try {
            server = serverSocket.accept();
            connectStatus = true;
        } catch(SocketTimeoutException e) {
            System.out.println("Connection to server timed out");
            // prompt user if they want to wait or return to main menu
            // while loop for waiting
            // break to return control to main controller
            Scanner scan = new Scanner(System.in);
            while(true) {
                System.out.println("Do you wish to continue waiting or go back to main menu?");
                System.out.println("Enter '1' to continue waiting or '2' to go back to main menu");
                String userIn = scan.nextLine();
                while (!isInt(userIn) || (userIn.length() != 1)) {
                    System.out.println("Please type an integer, 1 or 2");
                    scan = new Scanner(System.in);
                    userIn = scan.nextLine();
                }
                int userInput = Integer.parseInt(userIn);

                while ((userInput != 1) && (userInput != 2)) {
                    System.out.println("Please enter '1' or '2' ");
                    scan = new Scanner(System.in);
                    userInput = scan.nextInt();
                }

                if (userInput == 1) {
                    SocketManager.listen(serverSocket);
                }
                break;
            }
        }
        return server;
    }

    public boolean connect(String ipAddress, int port, Socket socket) throws IOException{
        InetAddress ipAddr = InetAddress.getByName(ipAddress);
        SocketAddress socketAddr = new InetSocketAddress(ipAddr, port);
        socket.connect(socketAddr);
        return (socket.isConnected());
    }

    public void sendMessage(Socket fromSock, String[][] message) {
        try {
            if (outputStream == null) {
                outputStream = new ObjectOutputStream(fromSock.getOutputStream());
                outputStream.writeObject(message);
            } else {
                outputStream.writeObject(message);
            }
            //out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Object waitForMessage(Socket inSock) {
        Object message = null;
        try {
            if (inputStream == null) {
                inputStream = new ObjectInputStream(inSock.getInputStream());
                message = inputStream.readObject();
            } else {
                message = inputStream.readObject();
            }
            //inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return message;
    }
}

import java.util.*;
import java.io.*;
import java.net.*;

package com.company;

public class Client extends Host {

    private SocketManager socketManager;
    private GameManager gameManager;
    private Socket client;
    private GameBoard board;

    @Override
    public void initSession() {
        socketManager = new SocketManager();
        client = socketManager.initClient();

        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Type in the host IP Address: ");
            String ip = sc.nextLine();
            System.out.print("Type in the host port: ");
            int port = sc.nextInt();

            if (!socketManager.connect(ip, port, client))
                System.out.println("Connection failed");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void quit() {
        try {
            client.close();
        } catch(IOException e) {
            System.out.println(e.getStackTrace());
        }
    }

    @Override
    public void doTurn() {
        int[] pieces;
        String[] canJumpPieces;
        int row, col, newRow, newCol;
        Scanner sc = new Scanner(System.in);
        String input, position;
        gameManager.isHost = false;


        while(true) {
            System.out.println("");
            // jump
            if(gameManager.searchJump()) { //searchJump return true if there is jump

                System.out.print("Choose a piece to jump: ");
                input = sc.nextLine();

                pieces = gameManager.checkInput(input);

                if (pieces.length == 2) {
                    row = pieces[0];
                    col = pieces[1];

                    if(gameManager.canJump(row,col)) {
                        while(gameManager.canJump(row,col)) {
                            System.out.print("Choose a position to jump to: ");
                            position = sc.nextLine();

                            pieces = gameManager.checkInput(position);

                            if (pieces.length == 2) {
                                newRow = pieces[0];
                                newCol = pieces[1];

                                if (gameManager.jump(row, col, newRow, newCol)) {

                                    board = gameManager.getBoard();
                                    gameManager.printPiecesLeft();
                                    row = newRow;
                                    col = newCol;

                                } else {
                                    System.out.println("Invalid jump position. Please choose again");
                                }
                            } else {
                                System.out.println("Invalid format of input. Please choose again");
                            }
                        }
                        break;
                    }
                    else {
                        System.out.println("Invalid piece. Please choose again");
                    }
                }
                else {
                    System.out.println("Invalid format of input. Please choose again");
                }
            }
            // normal move
            else {
                System.out.print("Choose a piece to move: ");
                input = sc.nextLine();

                pieces = gameManager.checkInput(input);

                if (pieces.length == 2) {
                    row = pieces[0];
                    col = pieces[1];

                    if(gameManager.validatePiece(row, col)) {

                        System.out.print("Choose a position to move into: ");
                        position = sc.nextLine();

                        pieces = gameManager.checkInput(position);

                        if (pieces.length == 2) {
                            newRow = pieces[0];
                            newCol = pieces[1];

                            if (gameManager.validateMove(row, col, newRow, newCol)) {

                                gameManager.updateBoard(row, col, newRow, newCol);
                                board = gameManager.getBoard();
                                System.out.println("");
                                gameManager.printPiecesLeft();
                                break;
                            }
                            else {
                                System.out.println("Invalid move. Please choose again");
                            }
                        }
                        else {
                            System.out.println("Invalid format of input. Please choose again");
                        }
                    }
                    else {
                        System.out.println("Invalid piece. Please choose again");
                    }
                }
                else {
                    System.out.println("Invalid format of input. Please choose again");
                }
            }
        }
    }

    @Override
    public void playGame() {

        gameManager = new GameManager();
        board = gameManager.getBoard();
        initSession();

        System.out.println("\n***************** Game start *****************\n");
        System.out.println("You are player 2. Choose \'o\' to move");

        System.out.println("\n**********************************************\n");
        board.displayBoard();
        System.out.println("\nPlayer 1 turn. Wait for Player 1.\n");

        // client must wait for server to move first
        board = (GameBoard) socketManager.waitForMessage(client);

        if (board == null) {
            System.out.println("Can't read message");
        }

        // update board
        gameManager.readBoard(board);
        board = gameManager.getBoard();

        // print board
        System.out.println("\n**********************************************\n");
        board.displayBoard();

        System.out.println("\nPlayer 2 turn.");
        System.out.println("Choose an option: 1. Make move  2. Quit game");
        Scanner sc = new Scanner(System.in);
        int option;

        try {
            option = Integer.parseInt(sc.nextLine());
            while(true) {
                if (option == 1) {

                    if(gameManager.gameOver() != true) {

                        doTurn();
                        socketManager.sendMessage(client, board);

                        System.out.println("\n******** You moved. Wait for Player 1 ********\n");

                        // read board
                        board = (GameBoard) socketManager.waitForMessage(client);

                        if(board == null) {
                            System.out.println("Player 1 exit.\nQuit to main menu");
                            quit();
                            break;
                        }

                        gameManager.readBoard(board);
                        board = gameManager.getBoard();
                        board.displayBoard();
                        gameManager.printPiecesLeft();

                    }
                    else {
                        System.out.println("Game over");
                        break;
                    }
                    System.out.println("\nPlayer 2 turn.");
                    System.out.println("Choose an option: 1. Make move  2. Quit game");
                    option = Integer.parseInt(sc.nextLine());

                } else if (option == 2) {
                    System.out.println("You choose to quit the game. Back to main menu.");
                    quit();
                    break;
                }
                else {
                    System.out.println("Invalid option.");
                    System.out.println("Choose an option: 1. Make move  2. Quit game");
                    option = Integer.parseInt(sc.nextLine());
                }
            }
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

}

package com.company;
import java.util.*;
import java.io.*;
import java.net.*;

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
            System.out.println("You choose to quit the game. Back to main menu.");
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

        board.displayBoard();

        while(true) {
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

        board.displayBoard();

        while(gameManager.gameOver() != true) {

            // read board
            board = (GameBoard) socketManager.waitForMessage(client);

            if(board == null) {
                System.out.println("Can't read message");
                break;
            }

            System.out.println("\nHost moved.\n");

            gameManager.readBoard(board);
            board = gameManager.getBoard();
            gameManager.printPiecesLeft();

            if(gameManager.gameOver() == true) {
                System.out.println("Game over");
                break;
            }
            else {
                doTurn();
                socketManager.sendMessage(client, board);
            }
        }
    }
}

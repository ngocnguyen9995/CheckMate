package com.company;

import java.util.Scanner;

public class MainController {

    private static final  String[] MAINMENU = {"Play With A Friend", "About The Game", "Quit"};
    private static final  String[] CONNECTIONMENU = {"Host A Game", "Connect To A Host", "Back To Main Menu"};


    public static void main() {
        // Start with main menu
        mainMenuScreen();
    }

    public static void displayMenu(String[] options){
        System.out.println("Please choose one of the options below by entering the option number\n");
        for (int i = 0; i < options.length; i++){
            System.out.println(i + 1 + ". " + options[i] + "\n");
        }
    }

    public static void mainMenuScreen(){
        displayMenu(MAINMENU);
        int option = new InputHandler().getSafeUserInput(new InputCheck(), 3);
        switch(option) {
            case 1: connectionScreen(); break;
            case 2: aboutScreen(); break;
            case 3: Quit(); break;
        }
    }

    public static void connectionScreen(){
        displayMenu(CONNECTIONMENU);
        int option = new InputHandler().getSafeUserInput(new InputCheck(), 3);
        switch (option){
            case 1: hostScreen(); break;
            case 2: clientScreen(); break;
            case 3: mainMenuScreen(); break;
        }
    }

    public static void aboutScreen(){
        System.out.println("Checkers is a 2 player, strategy, turn based game. CheckMate lets you play Checkers with your friend over the Internet");
        System.out.println("Type anything to go back to main menu");
        Scanner reader = new Scanner(System.in);
        if (!reader.nextLine().isEmpty()){
            mainMenuScreen();
        }
    }

    public static void hostScreen(){
        System.out.println("---Initializing server---");
        Host host = new Host();
        System.out.println("Starting game");
        host.playGame();
    }

    public static void clientScreen(){
        System.out.println("---Connecting to host---");
        Client client = new Client();
        System.out.println("Joined host's game");
        client.playGame();
    }

    public static void Quit(){
        //System.exit(0);
    }
}

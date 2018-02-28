package com.company;

import java.util.Scanner;

public class MainController {

    private static final  String[] MAINMENU = {"Play With A Friend", "About The Game", "Quit"};
    private static final  String[] CONNECTIONMENU = {"Host A Game", "Connect To A Host", "Back To Main Menu"};


    public static void main(String[] args) {
        // Start with main menu
        mainMenuScreen();
    }

    private static void displayMenu(String[] options){
        System.out.println("Please choose one of the options below by entering the option number\n");
        for (int i = 0; i < options.length; i++){
            System.out.println(i + 1 + ". " + options[i] + "\n");
        }
    }

    private static void mainMenuScreen(){
        displayMenu(MAINMENU);
        int option = new InputHandler().getSafeUserInput(new InputCheck(), 3);
        switch(option) {
            case 1: connectionScreen(); break;
            case 2: aboutScreen(); break;
            case 3: Quit(); break;
        }
    }

    private static void connectionScreen(){
        displayMenu(CONNECTIONMENU);
        int option = new InputHandler().getSafeUserInput(new InputCheck(), 3);
        switch (option){
            case 1: hostScreen(); break;
            case 2: clientScreen(); break;
            case 3: mainMenuScreen(); break;
        }
    }

    private static void aboutScreen(){
        System.out.println("Checkers is a 2 player, strategy, turn based game. CheckMate lets you play Checkers with your friend over the Internet");
        System.out.println("Type anything to go back to main menu");
        Scanner reader = new Scanner(System.in);
        if (!reader.nextLine().isEmpty()){
            mainMenuScreen();
        }
    }

    private static void hostScreen(){
        System.out.println("---Initializing server---");
        System.out.println("Waiting for connection...");
        System.out.println("Type in \"0\" to quit back to main menu");
        String input;
        do {
            System.out.println("Waiting for connection...");
            input = new Scanner(System.in).nextLine();
        } while (!input.equals("0"));
        if (input.equals("0")) mainMenuScreen();
}

    private static void clientScreen(){
        System.out.println("Please type in the IP address of your desired host");
        String addr = new Scanner(System.in).nextLine();
        System.out.println("Connecting to host " + addr);
    }

    private static void Quit(){
        //System.exit(0);
    }
}

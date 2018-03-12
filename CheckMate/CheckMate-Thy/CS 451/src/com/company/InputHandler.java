package com.company;


import java.util.Scanner;

public class InputHandler implements CheckInput{
    private boolean check = false;
    private int trueInput;

    protected int getSafeUserInput(InputCheck inputCheck, int validInput){
        Scanner reader = new Scanner(System.in);
        while (!inputCheck.checkMenuInput(reader, validInput)){
            inputCheck.checkMenuInput(reader, validInput);
        }

        return inputCheck.getTrueInput();
    }

    public boolean checkMenuInput(Scanner reader, int validInput){
        try {
            int input = Integer.parseInt(reader.nextLine());
            if (input >= 1 && input <= validInput){
                check = true;
                trueInput = input;
            } else {
                System.out.println("Out of range input, please enter a number between 1 and " + validInput);
            }
        }

        catch(NumberFormatException e) {
            System.out.println("Invalid input, please enter ONLY A NUMBER between 1 and " + validInput);
        }
        return check;
    }

    public int getTrueInput(){
        return trueInput;
    }
}

/*
 * InputCheck class
 * implements the CheckInput interface
 * Used to ensure that user input is valid (Within bound, only integer);
 */
class InputCheck implements CheckInput{

    private boolean check = false;
    private int trueInput;

    public boolean checkMenuInput(Scanner reader, int validInput){
        try {
            int input = Integer.parseInt(reader.nextLine());
            if (input >= 1 && input <= validInput){
                check = true;
                trueInput = input;
            } else {
                System.out.println("Out of range input, please enter a number between 1 and " + validInput);
            }
        }

        catch(NumberFormatException e) {
            System.out.println("Invalid input, please enter ONLY A NUMBER between 1 and " + validInput);
        }
        return check;
    }

    public int getTrueInput(){
        return trueInput;
    }
}
/**
 * Ngoc Nguyen
 * CS350 Assignment 2
 * CheckInput interface
 */
package com.company;

import java.util.Scanner;

public interface CheckInput {

    /**
     * check if user input is valid
     * @param reader -> Scanner object to read user input
     * @param validInput -> input range (1 - validInput)
     * @return boolean value
     */
    boolean checkMenuInput(Scanner reader, int validInput);
}

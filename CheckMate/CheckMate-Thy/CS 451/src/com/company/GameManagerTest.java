package com.company;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class GameManagerTest
{

    @Test
    public void testCheckInput() {
        GameManager GM = new GameManager();
        int[] expected = {0,0};

        assert(GM.checkInput("1a")[0] == 0);
        assert(GM.checkInput("1a")[1] == 0);
    }


    @Test
    public void testIntToLetter() {
        GameManager GM = new GameManager();

        assert(GM.intToLetter(0,0).equals( "1a" ));
    }

    @Test
    public void testIsOccupied() {
        GameManager GM = new GameManager();

        assert(GM.isOccupied(0,0) == ( false ));
        assert(GM.isOccupied(5,2) == ( true ));
    }

    @Test
    public void testIsX() {
        GameManager GM = new GameManager();

        assert(GM.isX(0,0) == ( false ));
        assert(GM.isX(1,0) == ( false ));
        assert(GM.isX(5,2) == ( true ));
    }

    @Test
    public void testIsOnBoard() {
        GameManager GM = new GameManager();

        assert(GM.isOnBoard(-1,0) == ( false ));
        assert(GM.isOnBoard(8,0) == ( false ));
        assert(GM.isOnBoard(0,-1) == ( false ));
        assert(GM.isOnBoard(0,8) == ( false ));
        assert(GM.isOnBoard(9,8) == ( false ));
        assert(GM.isOnBoard(-1,-2) == ( false ));

        assert(GM.isX(5,2) == ( true ));
        assert(GM.isX(6,3) == ( true ));
    }

    @Test
    public void testIsKing() {
        GameManager GM = new GameManager();
        GM.getBoard().getPiece(0,1).setKing("O");

        assert(GM.isKing(0,1));
        assert( !GM.isKing(0,3));
    }

    @Test
    public void testSearchKing() {

    }

    @Test
    public void testValidatePiece() {
        GameManager GM = new GameManager();

        assert(GM.validatePiece(0,1));

        GM.isHost = true;
        assert(GM.validatePiece(5,0));

        assert( !GM.validatePiece(0,1) );
        assert( !GM.validatePiece(0,0) );
    }

    @Test
    public void testMoveLeftUp() {
        GameManager GM = new GameManager();

        assert( GM.moveLeftUp(5,4, 4,3) );
        assert( !GM.moveLeftUp(5,4, 5,7) );
        assert( !GM.moveLeftUp(5,4, 6,7) );

    }

    @Test
    public void testMoveRightUp() {
        GameManager GM = new GameManager();

        assert( GM.moveRightUp(5,0, 4,1) );
        assert( !GM.moveRightUp(5,0, 5,2) );
        assert( !GM.moveRightUp(5,0, 6,2) );
    }

    @Test
    public void testMoveLeftDown() {
        GameManager GM = new GameManager();

        assert( GM.moveLeftDown(2,3, 3,2) );
        assert( !GM.moveLeftDown(2,3, 3,1) );
        assert( !GM.moveLeftDown(2,3, 4,2) );
    }

    @Test
    public void testMoveRightDown() {
        GameManager GM = new GameManager();

        assert( GM.moveRightDown(2,3, 3,4) );
        assert( !GM.moveRightDown(2,3, 3,3) );
        assert( !GM.moveRightDown(2,3, 4,4) );
    }

    @Test
    public void testValidateMove() {
        GameManager GM = new GameManager();

        assert (GM.validateMove(5,4, 4,3));
        assert (GM.validateMove(5,0, 4,1));
        assert (GM.validateMove(2,3, 3,2));
        assert (GM.validateMove(2,3, 3,4));


        GM.getBoard().getPiece(5,4).setKing("X");
        GM.getBoard().movePiece(5,4, 4,3);

        assert (GM.validateMove(4,3, 3,2));
        assert (GM.validateMove(4,3, 3,4));
        assert (GM.validateMove(4,3, 5,4));

    }

    public void testCanJumpUpLeft() {
    }

    public void testCanJumpUpRight() {
    }

    public void testCanJumpDownLeft() {
    }

    public void testCanJumpDownRight() {
    }

    public void testCanJump() {
    }

    public void testJump() {
    }

    public void testUpdateBoard() {
    }

    public void testPrintPiecesLeft() {
    }

    public void testGameOver() {
    }

    public void testSearchJump() {
    }
}

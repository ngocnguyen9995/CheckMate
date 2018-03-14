package com.company;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PiecxeTest {

    @Test
    void setPieceName() {
        Piece p1 = new Piece("");
        p1.setPieceName("x");
        assertEquals(p1.getPieceName(), "x");
        assertNotEquals(p1.getPieceName(), "X");
        assertNotEquals(p1.getPieceName(), "o");
    }

    @Test
    void getPieceName() {
        Piece p1 = new Piece("O");
        assertEquals(p1.getPieceName(), "O");
        assertEquals(p1.getPieceName(), "O");
        assertNotEquals(p1.getPieceName(), "X");
        assertNotEquals(p1.getPieceName(), "o");
    }

    @Test
    void setKing() {
        Piece p1 = new Piece("x");
        assertFalse(p1.isKing());
        p1.setKing("X");
        assertTrue(p1.isKing());
        assertEquals(p1.getPieceName(), "X");
    }
}
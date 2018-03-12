package com.company;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {

    @Test
    void setPieceName() {
        Piece p1 = new Piece("");
        p1.setPieceName("x");
        assertEquals(p1.getPieceName());
    }

    @Test
    void getPieceName() {
    }

    @Test
    void setKing() {
    }

    @Test
    void isKing() {
    }
}
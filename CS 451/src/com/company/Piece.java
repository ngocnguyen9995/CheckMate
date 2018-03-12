import java.io.Serializable;

package com.company;

public class Piece implements Serializable
{
    private String name;
    private boolean isKing;

    public Piece(String name)
    {
        this.name = name;
        isKing = false;
    }

    public void setPieceName(String name)
    {
        this.name = name;
    }

    public String getPieceName()
    {
        return name;
    }

    public void setKing(String name_)
    {
        isKing = true;
        setPieceName(name_);
    }

    public boolean isKing()
    {
        return isKing;
    }
}

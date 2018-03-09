public class Piece
{
    private String name;
    private boolean isKing;

    public Piece(String name)
    {
        this.name = name;
        isKing = false;
    }

    public String getPieceName()
    {
        return name;
    }

    public void setKing()
    {
        isKing = true;
    }

    public boolean isKing()
    {
        return isKing;
    }
}

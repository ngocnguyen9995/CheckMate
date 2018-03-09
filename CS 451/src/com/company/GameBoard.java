
public class GameBoard
{
    public final static int ROW = 8;
    public final static int COL = 8;
    private Piece[][] board;

    public int hostPieceCount = 12;
    public int clientPieceCount = 12;


    public boolean noHostPieceLeft = false;
    public boolean noClientPieceLeft = false;

    public GameBoard()
    {
        board = new Piece[ROW][COL];

        for (int i = 0; i < ROW; i++)
        {
            if (i < 3)
            {
                for (int j = 0; j < COL; j++)
                {
                    if (i % 2 != 0 && j % 2 != 0)
                        board[i][j] = new Piece(" ");
                    else if (i % 2 == 0 && j % 2 == 0)
                        board[i][j] = new Piece(" ");
                    else
                        board[i][j] = new Piece("O");
                }
            }
            else if (i > 4)
            {
                for (int j = 0; j < COL; j++)
                {
                    if (i % 2 != 0 && j % 2 != 0)
                        board[i][j] = new Piece(" ");
                    else if (i % 2 == 0 && j % 2 == 0)
                        board[i][j] = new Piece(" ");
                    else
                        board[i][j] = new Piece("X");
                }
            }

        }

        for (int i = 3; i < 5; i++)
        {
            for (int j = 0; j < COL; j++)
            {
                board[i][j] = new Piece(" ");
            }
        }
    }

    public void movePiece(int prevRow, int prevCol, int newRow, int newCol)
    {
        board[newRow][newCol] = board[prevRow][prevCol];
        board[prevRow][prevCol] = new Piece(" ");
    }

    public Piece getPiece(int row, int col)
    {
        return board[row][col];
    }

    public void resetPiece(int row, int col)
    {
        board[row][col] = new Piece(" ");
    }

    // change to return String
    public String displayBoard()
    {
        String out = "  ---------------------------------\n";
        System.out.println("  ---------------------------------");
        for (int i = 0; i < ROW; i++)
        {
            out += i+1 + " | ";
            System.out.print(i+1 + " | ");
            for (int j = 0; j < COL; j++)
            {
                //board[i][j] = "A";
                out += board[i][j].getPieceName() + " | ";
                System.out.print(board[i][j].getPieceName() + " | ");
            }
            out += "\n";
            System.out.println();
        }
        out += "  ---------------------------------\n";
        System.out.println("  ---------------------------------");

        out += "    h   g   f   e   d   c   b   a ";
        System.out.println("    h   g   f   e   d   c   b   a ");
        return out;
    }

    public void reduceCount(String piece) {
        if (piece.compareTo("X") == 0) {
            if (clientPieceCount > 0)
                clientPieceCount--;
            else
                noClientPieceLeft = true;
        } else if (piece.compareTo("O") == 0) {
            if (hostPieceCount > 0)
                hostPieceCount--;
            else
                noHostPieceLeft = true;
        }
    }
}

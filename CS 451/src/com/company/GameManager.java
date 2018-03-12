
public class GameManager
{
    private GameBoard gameBoard;
    public boolean isHost; // check if it is Host's turn
    private int[] posUpLeft = new int[4];
    private int[] posUpRight = new int[4];
    private int[] posDownLeft = new int[4];
    private int[] posDownRight = new int[4];

    public GameManager()
    {
        gameBoard = new GameBoard();
    }

    public GameBoard getBoard()
    {
        return gameBoard;
    }

    // read board fetched from socket
    public void readBoard(GameBoard board_)
    {
        gameBoard = board_;
    }

    // check format of the input
    // convert from letter to int
    public int[] checkInput(String input)
    {
        int[] temp = new int[2];

        input = input.toLowerCase();

        if (input.length() == 2)
        {
            char row = input.charAt(0); // check from 1 - 8
            char col = input.charAt(1); // check from a - h (97 - 104)

            if ( col < 97 || col > 104 )
                return temp;

            if ( Integer.parseInt(String.valueOf(row)) < 1 || Integer.parseInt(String.valueOf(row)) > 8 )
                return temp;

            temp[0] = Integer.parseInt(String.valueOf(row)) - 1; // - 1 since the actual array index is off by 1
            temp[1] = col - 97;
        }
        return temp;
    }

    // convert from int to letter
    public String intToLetter(int row, int col)
    {
        String result = "";

        result += String.valueOf(row+1); // indices from 0->7 but row from 1->8
        char b = (char)('a' + col); // indices from 0->7 but col from a->h = 0+97 -> 7+97
        result += b;

        return result;
    }

/***********************************************************************************************************************/
    public boolean isOccupied(int row, int col)
    {
        if (gameBoard.getPiece(row, col).getPieceName().equals(" "))
            return false;
        else
            return true;
    }

    public boolean isX(int row, int col)
    {
        if (gameBoard.getPiece(row, col).getPieceName().equalsIgnoreCase("X"))
            return true;
        else
            return false;
    }

    public boolean isOnBoard(int row, int col)
    {
        if ( row >= 0 && row < GameBoard.ROW && col >= 0 && col < GameBoard.COL) // row and col range from 0-7 bc they are indices
            return true;
        else
            return false;
    }


    // return true if the piece passed down is a king
    public boolean isKing(int row, int col)
    {
        if ( gameBoard.getPiece(row, col).isKing())
            return true;
        return false;
    }

    // scan and search for a king
    public void searchKing()
    {
        for (int i = 0; i < GameBoard.COL; i ++)
        {
            if (gameBoard.getPiece(0,i).getPieceName().equals("x"))
            {
                if ( ! gameBoard.getPiece(0,i).isKing() )
                {
                    gameBoard.getPiece(0,i).setKing("X");
                    System.out.println( "x reaches the top row. x becomes King" );

                }
            }
            if (gameBoard.getPiece(7,i).getPieceName().equals("o"))
            {
                if ( ! gameBoard.getPiece(7,i).isKing() )
                {
                    gameBoard.getPiece(7,i).setKing("O");
                    System.out.println( "o reaches the bottom row. o becomes King" );
                }
            }
        }
    }

    public boolean validatePiece(int row, int col)
    {
        if (isHost && isX(row, col))
            return true;
        else if ( ! isHost && ! isX(row, col))
            return true;
        else if ( ! isOccupied(row, col) )
        {
            return false;
        }
        else
        {
            return false;
        }
    }
/***********************************************************************************************************************/
    // move for X
    public boolean moveLeftUp(int prevRow, int prevCol, int newRow, int newCol)
    {
        if (newRow == prevRow - 1)
        {
            if (newCol == prevCol - 1)
                return true;
        }
        return false;
    }

    public boolean moveRightUp(int prevRow, int prevCol, int newRow, int newCol)
    {
        if (newRow == prevRow - 1)
        {
            if (newCol == prevCol + 1)
                return true;
        }
        return false;
    }

    // move for O
    public boolean moveLeftDown(int prevRow, int prevCol, int newRow, int newCol)
    {
        if (newRow == prevRow + 1)
        {
            if (newCol == prevCol - 1)
                return true;
        }
        return false;
    }

    public boolean moveRightDown(int prevRow, int prevCol, int newRow, int newCol)
    {
        if (newRow == prevRow + 1)
        {
            if (newCol == prevCol + 1)
                return true;
        }
        return false;
    }

    // check the actual game rules
    // move diagonally
    public boolean validateMove(int prevRow, int prevCol, int newRow, int newCol)
    {
        if (isOnBoard(prevRow, prevCol) && isOnBoard(newRow, newCol)
                && !isOccupied(newRow, newCol) && isOccupied(prevRow, prevCol))
        {
            searchKing();
            if (isKing(prevRow, prevCol))
            {
                if ( moveLeftUp(prevRow, prevCol, newRow, newCol) || moveRightUp(prevRow, prevCol, newRow, newCol) ||
                        moveLeftDown(prevRow, prevCol, newRow, newCol) || moveRightDown(prevRow, prevCol, newRow, newCol)  )
                    return true;
            }
            else
            {
                if ( isX(prevRow, prevCol) )
                {
                    if ( moveLeftUp(prevRow, prevCol, newRow, newCol) || moveRightUp(prevRow, prevCol, newRow, newCol) )
                        return true;
                }
                if ( ! isX(prevRow, prevCol) )
                {
                    if ( moveLeftDown(prevRow, prevCol, newRow, newCol) || moveRightDown(prevRow, prevCol, newRow, newCol) )
                        return true;
                }
            }
        }
        return false;
    }
/***********************************************************************************************************************/

    public boolean canJumpUpLeft(int row, int col)
    {
        String current = gameBoard.getPiece(row, col).getPieceName();

        if ( ! isOnBoard( row-1, col-1 ) && ! isOnBoard( row-2, col-2 ) )
            return false;

        String upLeft = gameBoard.getPiece(row-1, col-1).getPieceName();

        if ( isOccupied(row-1, col-1) && current.compareToIgnoreCase(upLeft) != 0 && isOnBoard(row-2, col-2) && !isOccupied(row-2, col-2)) {
            posUpLeft[0] = row-1;
            posUpLeft[1] = col-1;
            posUpLeft[2] = row-2;
            posUpLeft[3] = col-2;
            return true;
        }
        return false;
    }

    public boolean canJumpUpRight(int row, int col)
    {
        String current = gameBoard.getPiece(row, col).getPieceName();

        if ( ! isOnBoard( row-1, col+1 ) && ! isOnBoard( row-2, col+2 ) )
            return false;

        String upRight = gameBoard.getPiece(row-1, col+1).getPieceName();

        if ( isOccupied(row-1, col+1) && current.compareToIgnoreCase(upRight) != 0 && isOnBoard(row-2, col+2) && !isOccupied(row-2, col+2)) {
            posUpRight[0] = row-1;
            posUpRight[1] = col+1;
            posUpRight[2] = row-2;
            posUpRight[3] = col+2;
            return true;
        }
        return false;
    }

    public boolean canJumpDownLeft(int row, int col)
    {
        String current = gameBoard.getPiece(row, col).getPieceName();

        if ( ! isOnBoard( row+1, col-1 ) && ! isOnBoard( row+2, col-2 ) )
            return false;

        String DownLeft = gameBoard.getPiece(row+1, col-1).getPieceName();

        if ( isOccupied(row+1, col-1) && current.compareToIgnoreCase(DownLeft) != 0 && isOnBoard(row+2, col-2) && !isOccupied(row+2, col-2)) {
            posDownLeft[0] = row+1;
            posDownLeft[1] = col-1;
            posDownLeft[2] = row+2;
            posDownLeft[3] = col-2;
            //System.out.println("Can jump down left!");
            return true;
        }
        return false;
    }

    public boolean canJumpDownRight(int row, int col)
    {
        String current = gameBoard.getPiece(row, col).getPieceName();

        if ( ! isOnBoard( row+1, col+1 ) && ! isOnBoard( row+2, col+2 ) )
            return false;

        String downRight = gameBoard.getPiece(row+1, col+1).getPieceName();

        if ( isOccupied(row+1, col+1) && current.compareToIgnoreCase(downRight) != 0 && isOnBoard(row+2, col+2) && !isOccupied(row+2, col+2)) {
            posDownRight[0] = row+1;
            posDownRight[1] = col+1;
            posDownRight[2] = row+2;
            posDownRight[3] = col+2;
            //System.out.println("Can jump down right!");
            return true;
        }
        return false;
    }

    public boolean canJump(int row, int col)
    {
        boolean jumpUpLeft = canJumpUpLeft(row, col);
        boolean jumpUpRight = canJumpUpRight(row, col);
        boolean jumpDownLeft = canJumpDownLeft(row, col);
        boolean jumpDownRight = canJumpDownRight(row, col);

        if ( isKing(row, col) ) {
            if ( validatePiece(row, col) && isOccupied(row, col) && (jumpUpLeft || jumpUpRight || jumpDownLeft || jumpDownRight))
                return true;
        }
        else
        {
            if ( isX(row, col) )
            {
                if (  validatePiece(row, col) && isOccupied(row, col) && ( jumpUpLeft || jumpUpRight ))
                    return true;
            }
            else {
                if (  validatePiece(row, col) && isOccupied(row, col) && ( jumpDownLeft || jumpDownRight ))
                    return true;
            }
        }
        return false;
    }

    public boolean jump(int row, int col, int newRow, int newCol)
    {
        boolean isUpdated = false;

        if ( newRow == posUpLeft[2] && newCol == posUpLeft[3] )
        {
            gameBoard.resetPiece(posUpLeft[0], posUpLeft[1]);
            updateBoard(row, col, newRow, newCol);
            isUpdated = true;
        }
        else if ( newRow == posUpRight[2] && newCol == posUpRight[3] )
        {
            gameBoard.resetPiece(posUpRight[0], posUpRight[1]);
            updateBoard(row, col, newRow, newCol);
            isUpdated = true;
        }
        else if ( newRow == posDownLeft[2] && newCol == posDownLeft[3] )
        {
            gameBoard.resetPiece(posDownLeft[0], posDownLeft[1]);
            updateBoard(row, col, newRow, newCol);
            isUpdated = true;
        }
        else if ( newRow == posDownRight[2] && newCol == posDownRight[3] )
        {
            gameBoard.resetPiece(posDownRight[0], posDownRight[1]);
            updateBoard(row, col, newRow, newCol);
            isUpdated = true;
        }

        posUpLeft = new int[4];
        posUpRight = new int[4];
        posDownLeft = new int[4];
        posDownRight = new int[4];

        if ( isUpdated )
        {
            if (isX(newRow, newCol))
                gameBoard.reduceCount("O");
            else
                gameBoard.reduceCount("X");
            return true;
        }
        return false;

    }

/***********************************************************************************************************************/

    public void updateBoard(int prevRow, int prevCol, int newRow, int newCol)
    {
        gameBoard.movePiece(prevRow, prevCol, newRow, newCol);
        searchKing();
        gameBoard.displayBoard();
    }

    public void printPiecesLeft()
    {
        System.out.println("Number of pieces left for Host: " + gameBoard.hostPieceCount);
        System.out.println("Number of pieces left for Client: " + gameBoard.clientPieceCount);
    }

    public boolean gameOver()
    {
        if ( gameBoard.hostPieceCount == 0 )
        {
            System.out.println("x loses");
            System.out.println("o wins");
            return true;
        }
        else if ( gameBoard.clientPieceCount == 0 )
        {
            System.out.println("o loses");
            System.out.println("x wins");
            return true;
        }
        else
            return false;
    }

    public boolean searchJump()
    {
        String pos;
        boolean canJump = false;
        for (int r = 0; r < gameBoard.ROW; r++)
        {
            for (int c = 0; c < gameBoard.COL; c++)
            {
                if ( canJump(r,c) )
                {
                    pos = intToLetter(r,c);
                    System.out.println( gameBoard.getPiece(r,c).getPieceName() + " at position " + pos + " can jump.");
                    canJump = true;
                }
            }
        }
        return canJump;
    }
}

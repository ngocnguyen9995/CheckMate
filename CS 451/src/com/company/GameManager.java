
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
        if (gameBoard.getPiece(row, col).getPieceName().equals("X"))
            return true;
        else
            return false;
    }

    public boolean isOnBoard(int row, int col)
    {
        if (row <= GameBoard.ROW && col <= GameBoard.COL)
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
            if (gameBoard.getPiece(0,i).getPieceName().equals("X"))
            {
                gameBoard.getPiece(0,i).setKing();
            }
            if (gameBoard.getPiece(7,i).getPieceName().equals("O"))
            {
                gameBoard.getPiece(7,i).setKing();
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
            System.out.println("No piece at the choosen position. Please try again");
            return false;
        }
        else
        {
            System.out.println("You can't move a piece that is not yours. Please try again");
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
        System.out.println("Invalid move!");
        return false;
    }
/***********************************************************************************************************************/

    public boolean canJumpUpLeft(int row, int col)
    {
        String current = gameBoard.getPiece(row, col).getPieceName();

        if ( ! isOnBoard( row-1, col-1 ))
            return false;

        String upLeft = gameBoard.getPiece(row-1, col-1).getPieceName();

        if ( current.compareTo(upLeft) != 0 && isOnBoard(row-2, col-2) && !isOccupied(row-2, col-2)) {
            posUpLeft[0] = row-1;
            posUpLeft[1] = col-1;
            posUpLeft[2] = row-2;
            posUpLeft[3] = col-2;
            System.out.println("Can jump up left!");
            return true;
        }
        return false;
    }

    public boolean canJumpUpRight(int row, int col)
    {
        String current = gameBoard.getPiece(row, col).getPieceName();

        if ( ! isOnBoard( row-1, col+1 ))
            return false;

        String upLeft = gameBoard.getPiece(row-1, col+1).getPieceName();

        if ( current.compareTo(upLeft) != 0 && isOnBoard(row-2, col+2) && !isOccupied(row-2, col+2)) {
            posUpRight[0] = row-1;
            posUpRight[1] = col+1;
            posUpRight[2] = row-2;
            posUpRight[3] = col+2;
            System.out.println("Can jump up right!");
            return true;
        }
        return false;
    }

    public boolean canJumpDownLeft(int row, int col)
    {
        String current = gameBoard.getPiece(row, col).getPieceName();

        if ( ! isOnBoard( row+1, col-1 ))
            return false;

        String upLeft = gameBoard.getPiece(row+1, col-1).getPieceName();

        if ( current.compareTo(upLeft) != 0 && isOnBoard(row+2, col-2) && !isOccupied(row+2, col-2)) {
            posDownLeft[0] = row+1;
            posDownLeft[1] = col-1;
            posDownLeft[2] = row+2;
            posDownLeft[3] = col-2;
            System.out.println("Can jump down left!");
            return true;
        }
        return false;
    }

    public boolean canJumpDownRight(int row, int col)
    {
        String current = gameBoard.getPiece(row, col).getPieceName();

        if ( ! isOnBoard( row+1, col+1 ))
            return false;

        String upLeft = gameBoard.getPiece(row+1, col+1).getPieceName();

        if ( current.compareTo(upLeft) != 0 && isOnBoard(row+2, col+2) && !isOccupied(row+2, col+2)) {
            posDownRight[0] = row+1;
            posDownRight[1] = col+1;
            posDownRight[2] = row+2;
            posDownRight[3] = col+2;
            System.out.println("Can jump down right!");
            return true;
        }
        return false;
    }

    public boolean canJump(int row, int col)
    {
        //int[] pos = new int[4];
        if ( isKing(row, col) ) {
            if (canJumpUpLeft(row, col)) {
                return true;
            } if (canJumpUpRight(row, col)) {
                return true;
            } if (canJumpDownLeft(row, col)) {
                return true;
            } if (canJumpDownRight(row, col)) {
                return true;
            }
        }
        else
        {
            if ( isX(row, col) )
            {
                if (canJumpUpLeft(row, col)) {
                    return true;
                } if (canJumpUpRight(row, col)) {
                    return true;
                }
            }
            else {
                if (canJumpDownLeft(row, col)) {
                    return true;
                } if (canJumpDownRight(row, col)) {
                    return true;
                }
            }
        }
        return false;
    }

    public GameBoard jump(int row, int col, int newRow, int newCol)
    {
        boolean isUpdated = false;

        if ( ! posUpLeft.equals(null) )
        {
            if ( newRow == posUpLeft[2] && newCol == posUpLeft[3] )
            {
                gameBoard.resetPiece(posUpLeft[0], posUpLeft[1]);
                updateBoard(row, col, newRow, newCol);
                isUpdated = true;
            }
        }
        else if ( ! posUpRight.equals(null) )
        {
            if ( newRow == posUpRight[2] && newCol == posUpRight[3] )
            {
                gameBoard.resetPiece(posUpRight[0], posUpRight[1]);
                updateBoard(row, col, newRow, newCol);
                isUpdated = true;
            }
        }
        else if ( ! posDownLeft.equals(null) )
        {
            if ( newRow == posDownLeft[2] && newCol == posDownLeft[3] )
            {
                gameBoard.resetPiece(posDownLeft[0], posDownLeft[1]);
                updateBoard(row, col, newRow, newCol);
                isUpdated = true;
            }
        }
        else if ( ! posDownRight.equals(null) )
        {
            if ( newRow == posDownRight[2] && newCol == posDownRight[3] )
            {
                gameBoard.resetPiece(posDownRight[0], posDownRight[1]);
                updateBoard(row, col, newRow, newCol);
                isUpdated = true;
            }
        }

        posUpLeft = new int[4];
        posUpRight = new int[4];
        posDownLeft = new int[4];
        posDownRight = new int[4];

        if ( isUpdated )
        {
            if (isX(row, col))
                gameBoard.reduceCount("X");
            else
                gameBoard.reduceCount("O");
            return gameBoard;
        }
        return gameBoard;

    }

/***********************************************************************************************************************/

    public GameBoard updateBoard(int prevRow, int prevCol, int newRow, int newCol)
    {
        gameBoard.movePiece(prevRow, prevCol, newRow, newCol);
        gameBoard.displayBoard();
        return gameBoard;
    }

    public boolean gameOver()
    {
        if ( gameBoard.noHostPieceLeft )
        {
            System.out.println("X loses");
            System.out.println("O wins");
            return true;
        }
        else if ( gameBoard.noClientPieceLeft )
        {
            System.out.println("O loses");
            System.out.println("X wins");
            return true;
        }
        return false;
    }
}

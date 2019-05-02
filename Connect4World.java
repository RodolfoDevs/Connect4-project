import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
/**
 * Write a description of class Connect4World here.
 * Creates the game columns where the balls will be dropped and checks turns, creates appropiate ball to the player,
 * checks if the column is full or not and then places the ball, checks if a player wins, and stops the game if it happens.
 * 
 * @author (Rodolfo Madriz Masis) 
 * @version (12/8/2013)
 */
public class Connect4World extends World
{
    // you will likely want to represent each of the potential resting places
    // for a ball here. Please do not declare 64 (8x8) different variable names
    // to do so ... such will make the rest of the project quite difficult
    //
    //  You are also welcome to put other instance variables that you think
    //    will help you out *and* truly represent something about the world

    private PlayerBall humanBall; // holds reference to the gold ball of the player
    private ComputerBall steelBall; // holds reference to the steel ball of the computer
    // 2-dim array that represents the table game that will be filled with the numbers: 0 (empty cell), 1(player ball), 2(computer ball)
    private int[][] gameTable;
    
    private boolean playerTurn; // boolean instance variable that tells if it is the player turns or not (i.e. computer turn)
    private boolean gameOver; // holds boolean value if the game is over because a player won (connected 4 balls in any direction).
    
    // these variables will hold the row and column number where the last player (also a number) placed the last ball (in the matrix)
    // this will be useful to check who wins, and to change turns.
    private int rowToCheck; // holds the row number of the ball that was last placed/dropped so it can be found in the gameTable.
    private int columnToCheck; // holds the column number of the ball that was last placed/dropped so it can be found in the gameTable.
    private int playerToCheck; // holds a number that represents the player who "put" the last ball. 1 is the player; 2 is the computer.
    
    
    /**
     *  given a specific column number in the game, returns the middle of
     *        the column's x-value in the world
     * @param col - The column number (0..7) of the column in question
     */
    public int getX(int col)
    {
        int totalWidth=getWidth()-20; // the width of the playable gameboard
        int colWidth = totalWidth/8;  // the width of a single column
        int offset = col*colWidth;    // how far from the gameboard's left side?
        
        return 5+40+offset;//40 is the middle of a "chute", 5 is border width
        // there are really too many "magic numbers" here; I should have done
        // some code refactoring to base this on a more "general" calculation.
    }
    
    /**
     * given a specific row number in the game, returns the middle of
     *        the row's world y-value 
     * @param row - The row number (0..7) of the row in question
     */
    public int getY(int row)
    {
        int totalHeight=getHeight()-200; // how high the gameboard is
        int rowHeight = totalHeight/8;   // how tall a single row is
        int offset = row*rowHeight;      // how far from the bottom?
        
        return totalHeight+100-40-offset;// 100+40 gives message space
    }
    
    /**
     * given a specific world x-Value, returns the corresponding column
     *        number.
     * @param xVal - The world x-value in question
     */
    public int getCol(int xVal)
    {
        int totalWidth=getWidth()-20; // the width of the playable gameboard
        int colWidth = totalWidth/8;  // the width of a single column
        
        return (xVal-10)/colWidth;
    }
    
    
    
    /**
     * draw a string indicating someone won the game.
     * @param winnerName - the name of the player taht won the game
     */    
    public void displayWinner(String winnerName)
    {
        getBackground().drawString("The winner is:" + winnerName, 50, getHeight()-50);
    }
    
    
    //This function will give the row where the ball is placed and it will be use to find it in the matrix.
    /**
     * Given a specific column, finds the row where the ball will be placed
     * @param  column - the selected column where the ball was placed
     */
    public int getRow(int column)
    {
        int wantedRow = -1; // row number that will be return
        // checks where there is an empty space in the matrix. That empty space is then the one where the ball is/will be placed.
        for (int row=gameTable.length-1; row>-1; row--)
        {
            if (gameTable[row][column] == 0)
            {
                wantedRow = row;
            }
        }
        return wantedRow;
    }
    
    
    /**
     * Constructor for objects of class Connect4World.
     * 
     */
    public Connect4World()
    {    
        // Create a new world with 600x700 cells with a cell size of 1x1 pixels.
        super(600, 700, 1); 
        
        // draw the game board. 
        GreenfootImage bg=getBackground();
                
        bg.setColor(Color.black);
        int colWidth=(getWidth()-10-10)/8;
        bg.drawLine(10,getHeight()-100, 10+colWidth*8,getHeight()-100);
        bg.drawLine(10,getHeight()-99, 12+colWidth*8,getHeight()-99);        

        for(int colLine=10; colLine<=10+8*colWidth; colLine+=colWidth)
        {
            bg.drawLine(colLine,getHeight()-100, colLine,getHeight()-640);
            bg.drawLine(colLine+1,getHeight()-100, colLine+1,getHeight()-640);
            bg.drawLine(colLine+2,getHeight()-100, colLine+2,getHeight()-640);
        }
        
        // don't forget to initialize any instance variable that you likley 
        //   created!!!!
        
        
        gameTable = new int[8][8]; // initiates the table game to a size of 8 rows and 8 columns
        playerTurn = true; // initiates the playerTurn instance variable to true because the human player will start the game
        gameOver = false; // initiates gameOver to false because the game is just starting
        
        // since the game is starting, these variables are initiated to zero because there is no ball and/or player playing
        rowToCheck = 0;
        columnToCheck = 0; 
        playerToCheck = 0;
        
        Greenfoot.start(); // starts running the game automatically
    }
    
    /**
     * YOU WILL NEED TO CODE THIS, OR BALLS WILL "OVERFLOW" THE COLUMNS!!!
     * This function should return true if the column already has 8 balls
     *   in it; otherwise it should return false.
     * @param col - the column number to check
     */
    public boolean columnFull(int col)
    {
        // **YOU** will need to put some code here ....
        boolean isFull = false; //holds boolean value that will be returned
        int filledPlaces = 0; // number of places where there is a ball
        // checks if there is a ball on the column number in every row. if there is a ball, then adds one to filledPlaces variable
        for (int row=0; row<gameTable.length; row++)
        {
            if (gameTable[row][col] == 1 || gameTable[row][col] == 2)
            {
                filledPlaces++;
            }
        }
        // if the quatity of filled spaces in the matrix is 8 or greater, then sets isFull to true.
        if (filledPlaces >= 8)
        {
            isFull = true;
        }
        return isFull;
    }
    /**
     * yes, a world can act in the same way as an Actor. In this case, 
     *    the code just looks for mouse clicks and ... well, you have to 
     *    figure out what else to do ...
     *    ... you may add code anywhere you want to in this method. 
     */
    public void act()
    { 
        
        // if game is over, returns nothing (like "stopping" the game)          
        if (gameOver)
        {return;}
        
        //checks if there is a winner by calling winTheGame method
        if (winTheGame())
        {
            // statements that display the player who won according to the number that represent it (1-human player, 2-computer)
            if (playerToCheck == 1)
            {displayWinner("  You!!!");}
            if (playerToCheck == 2)
            {displayWinner("  The Computer!!!");}
            gameOver = true; // sets the gameOver variable to true
        }
        else
        {
            // checks if it's the turn of the human player and if there is no computer ball falling
            if (playerTurn && !ballFalling(2))
            {
                    // checks if the mouse was clicked
                    if (Greenfoot.mouseClicked(null))
                    {
                        // where was the mouse clicked?
                        MouseInfo mi = Greenfoot.getMouseInfo();
                        int xLoc=mi.getX();
                        int yLoc=mi.getY();
            
                        // if the attempt is to add below the bottom of the "board" 
                        //   .... ignore it and move on
                        if (yLoc>getHeight()-99)
                            return;
            
                        // otherwise ...
                        //  convert xLoc to a column number ...
                        int col=getCol(xLoc);
            
                        // if the click is too far left or too far right or there are 
                        // already 8 balls here ... then you should not add a ball
                        if (xLoc<10 || col>=8 || columnFull(col))
                            return;
                
                        else
                        {
                                // ***YOU*** need to add code to place a new gold ball here.
                                //  It is also likley that you will want to update some of
                                //  your instance variables to reflect that this new ball
                                //  is being dropped and where it will end up. 
                                //
                                // NOTE: you will want to add a gold (human) ball at
                                //  the x-location corresponding to col  (see getX() above). 
                                //  and y-location 30.
                    
                                int rowToBePlaced = getRow(col); //holds the row number in the array where the ball will be located
                                int yLocFinal = getY(rowToBePlaced); //holds final y Location where the ball will stop falling
                                gameTable[rowToBePlaced][col] = 1; //updates table game placing a 1 for a human ball in the appropiate cell
                         
                                // statements that update the instance variables with the number that represents the player who just played
                                // and the row and column where that ball could be found in the matrix
                                rowToCheck = rowToBePlaced;
                                columnToCheck = col;
                                playerToCheck = 1;
                         
                                // initializes the humanBall variable creating the gold ball for the human player 
                                humanBall = new PlayerBall(yLocFinal);
                                addObject(humanBall, getX(col), 30); // adds gold ball at the clicked column in the world
                    
                                playerTurn = false; // switches the turn,so the computer is next.
                        }
                    }
            }   
        
            // checks if it is the computer's turn to play and if there is no human player ball falling
            if (!playerTurn && !ballFalling(1))
            {
                    int randomCol = Greenfoot.getRandomNumber(8); //random column (0...7) where the computer ball will be dropped       
                    //checks if there are 8 balls in the columnd by calling columnFull method that takes as argument the ramdon column
                    if (columnFull(randomCol))
                    {return;}
            
                    else
                    {
                        int rowToBePlaced = getRow(randomCol); //holds the row number in the array where the ball will be located
                        int yLocFinal = getY(rowToBePlaced); //holds final y Location where the ball will stop falling
                        gameTable[rowToBePlaced][randomCol] = 2; //updates table game placing a 2 for a computer ball in the appropiate cell
                    
                        // statements that update the instance variables with the number that represents the player who just played
                        // and the row and column where that ball could be found in the matrix
                        rowToCheck = rowToBePlaced;
                        columnToCheck = randomCol;
                        playerToCheck = 2;
                    
                        // initializes the humanBall variable creating the steel ball for the computer player
                        steelBall = new ComputerBall(yLocFinal);
                        addObject(steelBall, getX(randomCol), 30); // adds steel ball at the clicked column in the world
                
                        playerTurn = true; // switches the turn,so the human player is next.
                    }
            }
        }
    }
    
    /**
     * returns boolean value if a ball of a player is falling (true) or not (false)
     * @param num - number that represents the ball of the player that has to be checked if it's falling
     */
    public boolean ballFalling(int num)
    {
        boolean falling = false; //holds boolean, value that will be returned, whether a ball is falling or not
        // checks if the player is the computer
        if (num == 2)
        {
            // checks if theres a computer ball and if it is falling by calling the ComputerBall class method called inPlay()
            if (steelBall != null && steelBall.inPlay())
            {falling = true;} // sets value of falling to true
        }
        // checks if the player is the human player
        if (num == 1)
        {
            // checks if theres a human player ball and if it is falling by calling the PlayerBall class method called inPlay()
            if (humanBall != null && humanBall.inPlay())
            {falling = true;} // sets value of falling to true
        }
        return falling;
    }
    
    
    /**
     * method that checks if one the players won by connecting 4 of their balls horizontally, vertically or diagonally and returns
     *  a boolean value. This method makes use of 3 other methods that checks the count of balls in each direction, starting
     *  horizontally,then vertically, and then diagonally
     *  It doesn't need any arguments because it uses the instances variables rowToCheck, columnToCheck, and playerToCheck
     */
    public boolean winTheGame()
    {
        // checks if there is not a player playing yet
        if (playerToCheck == 0)
        {return false;}
        
        // first, checks if the horizontal count is greater or equal to 4
        int horizontalCount = getHorizontalCount(rowToCheck, columnToCheck-1, 1); //holds count of balls conected horizontally
        if (horizontalCount >= 4)
        {return true;}
        
        // second, checks if the horizontal count is greater or equal to 4
        int verticalCount = getVerticalCount(rowToCheck-1, columnToCheck, 1); //holds count of balls conected vertically
        if (verticalCount >= 4)
        {return true;}
        
        // then, checks if the horizontal count is greater or equal to 4
        int diagonalCount = getDiagonalCount(rowToCheck-1, columnToCheck+1, 1, 1, "up-RightToLeft", 1, 1); //holds count of balls conected diagonally
        if (diagonalCount >= 4)
        {return true;}
        // finaly, if no count was greater or equal to zero, returns false.
        else
        {return false;}
    }
    
    
    /**
     * given the row and column number of the space (cell) below the one where the last ball was placed, it starts counting
     * how many balls of the same type are there, and return that count at the end.
     * @param checkingRow - the row below the row where the last ball is located
     * @param colChecked - the column where the last ball is located
     * @param count - number of balls connected vertically. It starts at 1 because method starts checking one place below.
     */
    public int getVerticalCount(int checkingRow, int colChecked, int count)
    {
        // if it already checked all rows down, returns count
        if (checkingRow < 0)
        {return count;}
        else
        {
            // if there is a ball of the same type in the cell below, calls the method again but adding one to count and checking
            // the next row below.
            if (gameTable[checkingRow][colChecked] == playerToCheck)
            {
                return getVerticalCount(checkingRow-1, colChecked, count+1);
            }
            // if the ball that's below is not of the same player, it returns the count
            else
            {
                return count;
            }
        }
    }
    
    
    /**
     * Return number of balls connected horizontally, given the row and column number of the cell to the left. It starts checking
     * to the left of where the last ball was placed, and then checks to the right.
     * @param row - row where the last ball was placed.
     * @param chekingCol - column to the left of the one where the last ball was placed
     * @param count - number of ball connected horizontally. It starts at 1 because method starts checking one cell to the left.
     */
    public int getHorizontalCount(int row, int checkingCol, int count)
    {
        // checks if it already checked all columns to the right
        if (checkingCol >= gameTable.length)
        {return count;}
        
        else
        {
            // checks if it already checked all columns to the left and calls the method again to start checking to the right
            if (checkingCol < 0)
            {return getHorizontalCount(row, checkingCol+count+1, count); }
            
            else
            {
                // checks if the column of the cell that's being checked is to the left of where the last ball was placed
                if (checkingCol < columnToCheck)
                {
                    // if there is a ball of the same type in that cell, adds 1 to count, and keeps checking to the left
                    if (gameTable[row][checkingCol] == playerToCheck)
                    {return getHorizontalCount(row, checkingCol-1, count+1); }
                    // if not, keeps the count, and start checking to the right
                    else
                    {return getHorizontalCount(row, checkingCol+count+1, count); }
                }
                // if the column of the cell that's being checked is to the right of where the last ball was placed
                else 
                {
                    // if there is a ball of the same type in that cell, adds 1 to count, and keeps checking to the right
                    if (gameTable[row][checkingCol] == playerToCheck)
                    {return getHorizontalCount(row, checkingCol+1, count+1); }
                    // if not, returns count because it already checked both sides.
                    else
                    {return count; }
                }
            }
        }
    }
    
    
    /**
     * Returns the number of balls connnected diagonally. It starts checking the cell to the right down of where the last ball was placed
     * It keeps checks down right; then checks up left (diagonal right-left). If it finds 4 balls connected in that direction, then it 
     * returns the count, and STOPS checking. Otherwise, it keeps checking down left and then up right (diagonal left-right).
     * 
     * @param checkRow - row of the cell that will be checked
     * @param checkCol - column of the cell that will be checked
     * @param rowsMoved - number of rows moved from the one where the last ball was placed
     * @param colsMoved - number of columns moved from the one where the last ball was placed
     * @param direction - diagonal direction that is being checked
     * @param directionAux - number that tells if it checking down-right(1) or up-left(-1),  or down-left(1) or up-right(-1),
     *                       according to the specified diagonal direction
     * @param count - number of balls connected diagonally. It starts at 1 because it counts the last ball placed
     */
    public int getDiagonalCount(int checkRow, int checkCol, int rowsMoved, int colsMoved, String direction, int directionAux, int count)
    {
        // checks if the count is greater or equal to 4 in any direction or if direction says "finishedChecking" and then returns count
        if ((count >= 4 && (direction.equals("up-RightToLeft") || direction.equals("up-leftToright"))) || direction.equals("finishedChecking"))
        {return count;}
        else
        {
            //checks if it's checking diagonal right-left
            if (direction.equals("up-RightToLeft"))
            {
                // if there's no cell down-right, then it starts checking up-left
                if (checkRow < 0)
                { return getDiagonalCount(checkRow+rowsMoved+1, checkCol-colsMoved-1, 1, 1, direction, -1, count); }
                // if there's no cell up-left, it starts checking diagonal left-right, and sets count to 1 again.
                if (checkRow >= gameTable.length)
                { return getDiagonalCount(checkRow-rowsMoved-1, checkCol+colsMoved-1, 1, 1, "up-leftToright", 1, 1); }
                else
                {
                    // checks if it's cheking down-right for same player balls
                    if(directionAux == 1)
                    {
                        // if the column exists and there is a ball of the same player in the cell that's being checked, then
                        // keeps checking next cell down-right, and adds one to count
                        if (checkCol >= 0 && checkCol < gameTable[0].length && gameTable[checkRow][checkCol] == playerToCheck)
                        { return getDiagonalCount(checkRow-1, checkCol+1, rowsMoved+1, colsMoved+1, direction, directionAux, count+1); }
                        // otherwise, keeps the count, sets directionAux to -1, and starts checking up-left
                        else
                        { return getDiagonalCount(checkRow+rowsMoved+1, checkCol-colsMoved-1, 1, 1, direction, -1, count); }
                        
                    }
                    else // it does this if it's checking up-left for same player balls
                    {
                        // if the column exists and there is a ball of the same player in the cell that's being checked, then
                        // keeps checking next cell up-left, and adds one to count
                        if (checkCol >=0 && checkCol < gameTable[0].length && gameTable[checkRow][checkCol] == playerToCheck)
                        { return getDiagonalCount(checkRow+1, checkCol-1, rowsMoved+1, colsMoved+1, direction, directionAux, count+1); }
                        // otherwise, it now starts checking diagonal left-right
                        else
                        { return getDiagonalCount(checkRow-rowsMoved-1, checkCol+colsMoved-1, 1, 1, "up-leftToright", 1, 1); }
                        
                    }
                }
            }
            
            //it does this if it's now checking diagonal right-left
            else
            {
               // if there's no cell down-left, then it starts checking up-right
               if (checkRow < 0)
               { return getDiagonalCount(checkRow+rowsMoved+1, checkCol+colsMoved+1, 1, 1, direction, -1, count); }
               // if there's no cell up-right, it sets direction to "finishedChecking" so the method returns count.
               if (checkRow >= gameTable.length)
               { return getDiagonalCount(checkRow-rowsMoved-1, checkCol+colsMoved-1, 1, 1, "finishedChecking", 1, count); }
               else
               {
                   // checks if it's cheking down-left for same player balls
                   if (directionAux == 1)
                   {
                       // if the column exists and there is a ball of the same player in the cell that's being checked, then
                       // keeps checking next cell down-left, and adds one to count
                       if (checkCol >= 0 && checkCol < gameTable[0].length && gameTable[checkRow][checkCol] == playerToCheck)
                       { return getDiagonalCount(checkRow-1, checkCol-1, rowsMoved+1, colsMoved+1, direction, directionAux, count+1); }
                       // otherwise, keeps the count, sets directionAux to -1, and starts checking up-right
                       else
                       { return getDiagonalCount(checkRow+rowsMoved+1, checkCol+colsMoved+1, 1, 1, "up-leftToright", -1, count); }
                   }
                   else // it does this if it's checking up-right for same player balls
                   {
                       // if the column exists and there is a ball of the same player in the cell that's being checked, then
                       // keeps checking next cell up-right, and adds one to count
                       if (checkCol >= 0 && checkCol < gameTable[0].length && gameTable[checkRow][checkCol] == playerToCheck)
                       { return getDiagonalCount(checkRow+1, checkCol+1, rowsMoved+1, colsMoved+1, direction, directionAux, count+1); }
                       // if there's no cell up-right, it sets direction to "finishedChecking" so the method returns count.
                       else
                       {return getDiagonalCount(checkRow-rowsMoved-1, checkCol+colsMoved-1, 1, 1, "finishedChecking", 1, count);}
                   }
               } 
            }
        
        }
        
    }
    
}

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class PlayerBall here.
 * Creates the gold ball for the human player and makes it fall until its final y position
 * 
 * @author (Rodolfo Madriz Masis) 
 * @version (12/2/2013)
 */
public class PlayerBall extends Actor
{
    // instance variables
    private double ySpeed; // holds speed at which the  ball is falling
    private double xLocation; // holds the current x location of the ball
    private double yLocation; // holds the current y location of the ball
    private double finalYLocation; // holds the y location where the ball will stop falling
    
    private boolean isInPlay; // if the ball is falling, it holds a boolean value of true
    
    
    /**
     * Constructor of the PlayerBall class
     * @param positionToBePlaced - y location where the ball will stop falling (i.e. will be placed)
     */
    public PlayerBall(int positionToBePlaced)
    {
        // initialization of the instance variables when the object (ball) is created
        ySpeed = 2;
        isInPlay = true; 
        finalYLocation = positionToBePlaced;
    }
    
    // method that will initialize xLocation and yLocation to their positions after the ball
    // was added to the world
    protected void addedToWorld(World connectWorld)
    {
        xLocation = getX();
        yLocation = getY();
    }
    
    /**
     * Act - do whatever the PlayerBall wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        
        // checks if the ball is in play.
        if (isInPlay)
        {
            //checks if the yLocation is less than the final y location. if it is, then
            //it adds the speed to the y location and updates its position so the ball keeps falling
            if ((int) yLocation < (int) finalYLocation)
            {
                yLocation = yLocation + ySpeed;
                setLocation((int) xLocation, (int) yLocation);
            }
            // if not, then it means the ball got to its final y position and sets isInPlay to false
            else
            { isInPlay = false; }
        }
    }  
    
    //method the returns the boolean value that corresponds to whether the ball is falling or not
    public boolean inPlay()
    {return isInPlay;}
}

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class Springboard
{
    //Variables
    int xPos, yPos;
    String status, direction;

    long springStartTime;
    long springTimeElapsed = 0;
    long springInterval = (long) 5;

    //Constructor sets variables
    public Springboard(int xPos, int yPos, String status, String direction)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.status = status;
        this.direction = direction;
    }

    //Starts the timer for the springboard
    public void startTimer()
    {
        springStartTime = System.currentTimeMillis();
    }

    //Checks elapsed time and changes springboard state if greater than interval
    public void isPressed()
    {
        springTimeElapsed = (System.currentTimeMillis() / 1000) - (springStartTime / 1000);

        //Timer setup for animation
        if(springTimeElapsed >= springInterval)
        {
            status = "down";
        }
    }
    
    public void switchState()
    {
        if(status.equals("up"))
        {
            status = "down";
        }
        else if(status.equals("down"))
        {
            status = "up";
        }
    }

}
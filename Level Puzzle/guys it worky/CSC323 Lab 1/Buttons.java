import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class Buttons
{
    //Variables
    long buttonStartTime;
    long buttonTimeElapsed = 0;
    long buttonInterval = (long) 10;
    long prevTimeElapsed;

    String type, state;
    int rDown, gDown, bDown, rUp, gUp, bUp, xPos, yPos, buttonID;

    //Constructor sets variables
    public Buttons(String type, int rDown, int gDown, int bDown, int rUp, int gUp, int bUp, String state, int xPos, int yPos, int buttonID, int timeElapsed)
    {
        this.type = type;
        this.rDown = rDown;
        this.gDown = gDown;
        this.bDown = bDown;
        this.rUp = rUp;
        this.gUp = gUp;
        this.bUp = bUp;
        this.state = state;
        this.xPos = xPos;
        this.yPos = yPos;
        this.buttonID = buttonID;
        this.prevTimeElapsed = (long) timeElapsed;
        buttonStartTime = System.currentTimeMillis();

    }

    //Starts the timer for the button
    public void startTimer()
    {
        buttonStartTime = System.currentTimeMillis();
    }

    //Checks elapsed time and changes button state if greater than interval
    public void isPressed()
    {
        buttonTimeElapsed = (System.currentTimeMillis() / 1000) - (buttonStartTime / 1000) + prevTimeElapsed;

        //Timer setup for animation
        if(buttonTimeElapsed >= buttonInterval)
        {
            prevTimeElapsed = 0;
            buttonStartTime = System.currentTimeMillis();
            state = "up";    
        }
    }

    //Functions to get and set variables
    public void setState(String state) { this.state = state; }

}


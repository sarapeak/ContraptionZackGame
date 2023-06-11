import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class Spikes
{
    //Variables
    String direction, status;
    int r, g, b, xPos, yPos, spikeID;

    //Constructor sets variables
    public Spikes(String direction, int r, int g, int b, String status, int xPos, int yPos, int spikeID)
    {
        this.direction = direction;
        this.r = r;
        this.g = g;
        this.b = b;
        this.status = status;
        this.xPos = xPos;
        this.yPos = yPos;
        this.spikeID = spikeID;

    }

    //Functions to get and set variables

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


import java.util.List;

class Doors
{
    //Variables
    int xPos, yPos, xSize, ySize, openClose;
    long doorStartTime;
    long doorTimeElapsed = 0;
    long doorIntervalClosed;
    long doorIntervalOpen;
    long prevTimeElapsed;
    boolean firstPass = false;
    List<Barriers> barrierList;

    //Constructor sets variables
    public Doors(int xPos, int yPos, int xSize, int ySize, int openClose, List <Barriers> barrierList, int timeElapsed)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.xSize = xSize;
        this.ySize = ySize;
        this.openClose = openClose;
        this.barrierList = barrierList;
        doorStartTime = System.currentTimeMillis();
        this.prevTimeElapsed = (long) timeElapsed;
    }

    //Switches State
    public void switchState()
    {
        if(openClose == 0)
        {
            openClose = 1;
        }
        else if(openClose == 1)
        {
            openClose = 0;
        }
    }

    public void changeDoor()
    {
        doorIntervalClosed = 12;
        doorIntervalOpen = 4;

        doorTimeElapsed = (System.currentTimeMillis() / 1000) - (doorStartTime / 1000) + prevTimeElapsed;
        
        if (doorTimeElapsed >= doorIntervalClosed && openClose == 1)
        {
            prevTimeElapsed = 0;
            switchState();
            doorStartTime = System.currentTimeMillis();

            for (Barriers currentBarrier : barrierList)
            {
                if (xPos == currentBarrier.xPos && yPos == currentBarrier.yPos)
                {
                    currentBarrier.switchState();
                }
            }
        }
        else if(doorTimeElapsed >= doorIntervalOpen && openClose == 0)
        {
            prevTimeElapsed = 0;
            switchState();
            doorStartTime = System.currentTimeMillis();

            for (Barriers currentBarrier : barrierList)
            {
                if (xPos == currentBarrier.xPos && yPos == currentBarrier.yPos)
                {
                    currentBarrier.switchState();
                }
            }
        }
    }
}


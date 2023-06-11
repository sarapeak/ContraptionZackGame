class Barriers
{
    //Variables
    int xPos, yPos, xSize, ySize, OnOff;

    //Constructor sets variables
    public Barriers(int xPos, int yPos, int xSize, int ySize, int OnOff)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.xSize = xSize;
        this.ySize = ySize;
        this.OnOff = OnOff;
    }

    public void switchState()
    {
        if(OnOff == 0)
        {
            OnOff = 1;
        }
        else if(OnOff == 1)
        {
            OnOff = 0;
        }
    }
}


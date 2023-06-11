import java.io.File;
import java.util.*;
import java.util.Scanner;


class MyFile
{
    //make char array for gameBoard and scanner variable
    char [][] gameBoard = new char[44][44];

    //List for all levels and buttons
    List<String> nextLevel = new ArrayList<String>();
    List<Buttons> buttonsList = new ArrayList<Buttons>();
    List<Barriers> barrierList = new ArrayList<Barriers>();
    List<Spikes> spikesList = new ArrayList<Spikes>();
    List<Arrow> arrowList = new ArrayList<Arrow>();
    List<Springboard> springList = new ArrayList<Springboard>();
    List<Doors> doorList = new ArrayList<Doors>();
    String currentLevel;

    Scanner scan;

    int xPos;
    int yPos;

    //getBoard method to make the board
    public MyFile(String filename)
    {
        //Getting file
        try
        {
            scan = new Scanner(new File(filename));
            //If we are trying to load a regular file
            if(filename.equals("Level_1.txt") || filename.equals("Level_2.txt") || filename.equals("Level_3.txt") || filename.equals("Level_4.txt") || filename.equals("Level3toLevel2.txt"))
            {
                currentLevel = filename;
            }
            //If we are loading a save file
            else
            {
                currentLevel = scan.next();   //Gets the level
                //Gets the x and y position
                xPos = scan.nextInt();
                yPos = scan.nextInt();

                //Sets the player's position
                setXPosition(xPos);
                setYPosition(yPos);
            }
        }
        catch(Exception e)
        {
            System.out.println("Could not get file!");
        }

        //Reading map
        for(int i=0;i<44;i++)
        {
            for(int j=0;j<44;j++)
            {
                gameBoard[j][i] = scan.next().charAt(0);
            }
        }

        //reading in next levels
        for(int i = 0; i < 4; i++)
        {
            nextLevel.add(scan.next());
        }

        //reading in game objects
        while(scan.hasNext())
        {
            if(scan.hasNext("square")||scan.hasNext("circle"))
            {
               buttonsList.add(new Buttons(scan.next(), scan.nextInt(), scan.nextInt(), scan.nextInt(), scan.nextInt(),
                                        scan.nextInt(), scan.nextInt(), scan.next(), scan.nextInt(), scan.nextInt(), scan.nextInt(), scan.nextInt()));
            }
            else if(scan.hasNext("horizontal")||scan.hasNext("vertical"))
            {
               spikesList.add(new Spikes(scan.next(), scan.nextInt(), scan.nextInt(), scan.nextInt(), scan.next(),scan.nextInt(), scan.nextInt(), scan.nextInt()));
            }
            else if(scan.hasNext("barrier"))
            {
               scan.next();
               barrierList.add(new Barriers(scan.nextInt(), scan.nextInt(), scan.nextInt(), scan.nextInt(), scan.nextInt()));
            }
            else if(scan.hasNext("arrow"))
            {
               scan.next();
               arrowList.add(new Arrow(scan.nextInt(), scan.nextInt(), scan.next()));
            }
            else if(scan.hasNext("springboard"))
            {
               scan.next();
               springList.add(new Springboard(scan.nextInt(), scan.nextInt(), scan.next(), scan.next()));
            }
            else if(scan.hasNext("door"))
            {
                scan.next();
                doorList.add(new Doors(scan.nextInt(), scan.nextInt(), scan.nextInt(), scan.nextInt(), scan.nextInt(), barrierList, scan.nextInt()));
            }
        }

    }

    //Function gets Coordinate on gameboard
    public char getCoord(int i, int j)
    {
        return gameBoard[j][i];
    }

    //Function returns next level wanted
    public String getLevel(int x)
    {
        return nextLevel.get(x);
    }

    //Function returns current level
    public String getCurrentLevel() { return currentLevel; }

    //Function returns button list
    public List<Buttons> getButtonsList()
    {
        return buttonsList;
    }
    
    //Function returns spike list
    public List<Spikes> getSpikesList()
    {
        return spikesList;
    }
    
    //Function returns barrier list
    public List<Barriers> getBarrierList()
    {
        return barrierList;
    }

    //Function returns arrow list
    public List<Arrow> getArrowList()
    {
        return arrowList;
    }

    //Function returns springboard list
    public List<Springboard> getSpringList()
    {
        return springList;
    }

    //Function returns door list
    public List<Doors> getDoorList()
    {
        return doorList;
    }

    //Sets the player's x position
    public void setXPosition(int xPosition) { xPos = xPosition; }

    //Gets the player's x position
    public int getXPosition() { return xPos; }

    //Sets the player's y position
    public void setYPosition(int yPosition) { yPos = yPosition; }

    //Gets the player's y position
    public int getYPosition() { return yPos; }
}
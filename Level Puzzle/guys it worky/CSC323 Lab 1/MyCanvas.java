import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.File;

class MyCanvas extends JComponent implements KeyListener, ActionListener
{
    MyFile board;
    List<Buttons> buttonsList;
    List<Spikes> spikesList;
    List<Barriers> barrierList;
    List<Arrow> arrowList;
    List<Springboard> springList;
    List<Doors> doorList;

    //Timer variables
    Timer animationTimer;
    int timerDelay = 15;

    //Player Animation timer variables
    long playerStartTime;
    long playerTimeElapsed = 0;
    long playerInterval = (long) 1.5;
    boolean playerAnimation = false;

    //Jukebox Animation timer variables
    long jukeboxStartTime;
    long jukeboxTimeElapsed = 0;
    long jukeboxInterval =  2;
    boolean jukeboxAnimation = false;

    //Door Animation Timer Variables
    long nextDoorStart;
    long doorStartTimeElapsed = 0;
    long doorStartInterval = 4;
    int doorsStarted = -1;

    //Button Variables
    String buttonType, buttonState;
    int buttonrDown, buttongDown, buttonbDown, buttonrUp, buttongUp, buttonbUp, buttonxPos, buttonyPos, buttonID;
    long buttonTime;

    //Spike Variables
    String spikeDirection, spikeStatus;
    int spikexPos, spikeyPos, spikeR, spikeG, spikeB, spikeID;

    //variables for arrow
    String arrowDirection;
    int arrowXPos, arrowYPos;

    //variables for springboard
    String springStatus, springDirection;
    int springXPos, springYPos;

    //variables for doors
    int doorXPos, doorYPos, doorXSize, doorYSize, doorStatus;
    long doorTime;

    //variables for barriers
    int barrierXPos, barrierYPos, barrierXSize, barrierYSize, barrierState;

    //screwdriver variables
    boolean screwPickUp = false;
    int screwXPos = 560, screwYPos = 320;

    //Variables for player position
    int playerSize = 40;
    int playerSpeed = 2; //only do 1 or 2 or player will clip into walls and stop moving
    int xPos = 420;
    int yPos = 620;
    boolean left = false;
    boolean right = false;
    boolean up = false;
    boolean down = false;
    boolean canMoveR, canMoveL, canMoveU, canMoveD;

    //Variables for the menu functionality
    boolean menuPause = false;
    JFrame game;
    JPanel p;
    BufferedWriter bw;
    JButton save;
    JButton load;
    JButton restartA;
    JButton restartL;
    JButton exit;
    JButton tempButton;
    JTextField text;
    JLabel saveAs;
    String fileName = "";
    JButton saveFile;
    JLabel exceedMax;
    JLabel inputZero;
    JTextField textLoad;
    JLabel whichFile;
    JButton loadFile;
    JLabel tryAgain;
    JTextArea loadList;

    public MyCanvas()
    {}

    //Constructor
    public MyCanvas(String firstLevel, JFrame window)
    {
        window.setSize(895,920);
        window.requestFocus();
        board = new MyFile(firstLevel);
        buttonsList = board.getButtonsList();
        barrierList = board.getBarrierList();
        spikesList = board.getSpikesList();
        arrowList = board.getArrowList();
        springList = board.getSpringList();
        doorList = board.getDoorList();
        if(firstLevel.equals("Save_File.txt"))
        {
            //Gets the player's position from the load file
            xPos = board.getXPosition();
            yPos = board.getYPosition();
        }
        game = window;
        game.addKeyListener(this);
        animationTimer = new Timer(timerDelay, this);
        animationTimer.start();
        
        game.setVisible(true);
    }

    //Action function repeated from timer.
    @Override
    public void actionPerformed(ActionEvent e)
    {
        update();
        //getPosition();
        repaint();
    }

    //Function updates player position and map
    public void update()
    {
        canMoveR = true;
        canMoveL = true;
        canMoveU = true;
        canMoveD = true;

        for (Barriers currentBarrier : barrierList)
        {

            barrierXPos = currentBarrier.xPos;
            barrierYPos = currentBarrier.yPos;
            barrierXSize = currentBarrier.xSize;
            barrierYSize = currentBarrier.ySize;
            barrierState = currentBarrier.OnOff;

            if ((xPos > barrierXPos && xPos < barrierXPos + barrierXSize + 1) && (yPos > barrierYPos - 40 && yPos < barrierYPos + barrierYSize) && barrierState == 1)
            {
                canMoveL = false;
            }
            if ((xPos + 40 > barrierXPos - 1 && xPos + 40 < barrierXPos + barrierXSize) && (yPos > barrierYPos - 40 && yPos < barrierYPos + barrierYSize) && barrierState == 1)
            {
                canMoveR = false;
            }
            if ((yPos > barrierYPos && yPos < barrierYPos + barrierYSize + 1) && (xPos > barrierXPos - 40 && xPos < barrierXPos + barrierXSize) && barrierState == 1)
            {
                canMoveU = false;
            }
            if ((yPos + 40 > barrierYPos - 1 && yPos + 40 < barrierYPos + barrierYSize + 1) && (xPos > barrierXPos - 40 && xPos < barrierXPos + barrierXSize) && barrierState == 1)
            {
                canMoveD = false;
            }
        }

        if (down && canMoveD)
        {
            yPos += playerSpeed;
            //Goes back to Level 2 from Level 3 if correct position
            if (yPos > 680 && xPos >= 400 && xPos <= 450 && board.currentLevel.equals("Level_3.txt"))
            {
                board = new MyFile("Level3toLevel2.txt");   //Changes the board
                buttonsList = board.getButtonsList();
                spikesList = board.getSpikesList();
                barrierList = board.getBarrierList();
                arrowList = board.getArrowList();
                springList = board.getSpringList();
                doorList = board.getDoorList();
                addSpikeBarriers();
                addSpringBarriers();

                //Sets the new x and y coordinates
                xPos = 420;
                yPos = 80;
            }
        }
        if (up && canMoveU)
        {
            yPos -= playerSpeed;
            //Goes to Level 2 from Level 1 is at correct position
            if (xPos >= 400 && xPos <= 450 && yPos < 200 && board.currentLevel.equals("Level_1.txt"))
            {
                board = new MyFile(board.getLevel(1));
                buttonsList = board.getButtonsList();
                spikesList = board.getSpikesList();
                barrierList = board.getBarrierList();
                arrowList = board.getArrowList();
                springList = board.getSpringList();
                doorList = board.getDoorList();
                addSpikeBarriers();
                addSpringBarriers();

                xPos = 420;
                yPos = 760;
            }
            //Goes to Level 3 from Level 2 is at correct position
            if (xPos >= 400 && xPos <= 450 && yPos < 80 && (board.currentLevel.equals("Level_2.txt")||board.currentLevel.equals("Level3toLevel2.txt")))
            {
                board = new MyFile(board.getLevel(2));
                buttonsList = board.getButtonsList();
                spikesList = board.getSpikesList();
                barrierList = board.getBarrierList();
                arrowList = board.getArrowList();
                springList = board.getSpringList();
                doorList = board.getDoorList();
                doorsStarted = -1;
                addSpikeBarriers();
                addSpringBarriers();

                xPos = 420;
                yPos = 680;
            }
        }
        if (left && canMoveL)
        {
            xPos -= playerSpeed;
            //Goes to Level 4 from Level 3 if at correct position
            if (yPos >= 280 && yPos <= 320 && xPos <= 200 && board.currentLevel.equals("Level_3.txt"))
            {
                board = new MyFile(board.getLevel(3));
                buttonsList = board.getButtonsList();
                spikesList = board.getSpikesList();
                barrierList = board.getBarrierList();
                arrowList = board.getArrowList();
                springList = board.getSpringList();
                doorList = board.getDoorList();
                addSpikeBarriers();
                addSpringBarriers();

                xPos = 640;
                yPos = 220;
            }
        }
        if (right && canMoveR)
        {
            xPos += playerSpeed;
            //Goes to Level 3 from Level 4 if at correct position
            if (xPos > 640 && yPos < 250 && yPos > 198 && board.currentLevel.equals("Level_4.txt"))
            {
                board = new MyFile(board.getLevel(2));
                buttonsList = board.getButtonsList();
                spikesList = board.getSpikesList();
                barrierList = board.getBarrierList();
                arrowList = board.getArrowList();
                springList = board.getSpringList();
                doorList = board.getDoorList();
                doorsStarted = -1;
                addSpikeBarriers();
                addSpringBarriers();

                xPos = 200;
                yPos = 300;
            }
        }

        switchStates();
    }

    //Switches the states of buttons and spikes
    public void switchStates()
    {
        //loop to check player position and change button state if walked on
        for (Buttons currentButton : buttonsList)
        {
            String prevState;
            String postState;
            buttonType = currentButton.type;
            buttonState = currentButton.state;
            buttonxPos = currentButton.xPos;
            buttonyPos = currentButton.yPos;
            buttonID = currentButton.buttonID;

            //check player X Y (center of box) pos
            if ((xPos + 20 >= buttonxPos && xPos + 20 <= buttonxPos + 80 && yPos + 20 >= buttonyPos && yPos + 20 <= buttonyPos + 80) && buttonState.equals("up"))
            {
                currentButton.setState("down");

                //Starts timer on button if circle
                if(buttonType.equals("circle"))
                {
                    currentButton.startTimer();
                }

                //get spikes associated with current button and change state.
                for (Spikes currentSpike : spikesList)
                {
                    if (currentSpike.spikeID == buttonID)
                    {
                        currentSpike.switchState();
                    }

                    //switch state of barrier at same x y position to corresponding spike when a specific button is pressed
                    for (Barriers currentBarrier : barrierList)
                    {
                        if (currentSpike.xPos == currentBarrier.xPos && currentSpike.yPos == currentBarrier.yPos && currentSpike.spikeID == buttonID)
                        {
                            currentBarrier.switchState();
                        }
                    }
                }
            }

            //Checks if button timer has elapsed and changes state of button if true
            prevState = currentButton.state;
            if(buttonType.equals("circle"))
            {
                currentButton.isPressed();
            }
            postState = currentButton.state;

            //Changes states if button pops up
            if(prevState.equals("down") && postState.equals("up"))
            {
                for (Spikes currentSpike : spikesList)
                {
                    if (currentSpike.spikeID == buttonID)
                    {
                        currentSpike.switchState();
                    }

                    //switch state of barrier at same x y position to corresponding spike when a specific button is pressed
                    for (Barriers currentBarrier : barrierList)
                    {
                        if (currentSpike.xPos == currentBarrier.xPos && currentSpike.yPos == currentBarrier.yPos && currentSpike.spikeID == buttonID)
                        {
                            currentBarrier.switchState();
                        }
                    }
                }
            }
        }
        
        //springboard detection and player moving
        for(Springboard currentSpring : springList)
        {
            String prevState;
            String postState;
            springXPos = currentSpring.xPos;
            springYPos = currentSpring.yPos;
            springStatus = currentSpring.status;
            springDirection = currentSpring.direction;
            
            if ((xPos + 20 >= springXPos && xPos + 20 <= springXPos + 80 && yPos + 20 >= springYPos && yPos + 20 <= springYPos + 80) && springStatus.equals("down"))
            {
                if (springDirection.equals("left"))
                {
                    xPos = springXPos - 120;
                    yPos = springYPos + 20;
                }
                if (springDirection.equals("right"))
                {
                    xPos = springXPos + 160;
                    yPos = springYPos + 20;
                }
                currentSpring.switchState();

                for (Barriers currentBarrier : barrierList)
                {
                    if (currentSpring.xPos == currentBarrier.xPos && currentSpring.yPos == currentBarrier.yPos)
                    {
                        currentBarrier.switchState();
                    }
                }

                if(board.currentLevel.equals("Level_4.txt") || board.currentLevel.equals("Save_File.txt"))
                {
                    currentSpring.startTimer();
                }
            }

           //Changes state of button on timer if level 4
           if(board.currentLevel.equals("Level_4.txt"))
           {
               //Checks if button timer has elapsed and changes state of button if true
               prevState = currentSpring.status;
               currentSpring.isPressed();
               postState = currentSpring.status;

               if (prevState.equals("up") && postState.equals("down"))
               {
                   //switch state of barrier at same x y position to corresponding spike when a specific button is pressed
                   for (Barriers currentBarrier : barrierList)
                   {
                       if (currentSpring.xPos == currentBarrier.xPos && currentSpring.yPos == currentBarrier.yPos)
                       {
                           currentBarrier.switchState();
                       }
                   }
               }
           }
        }

        //Pick up screwdriver
        if ((xPos + 20 >= screwXPos && xPos + 20 <= screwXPos + 80 && yPos + 20 >= screwYPos && yPos + 20 <= screwYPos + 80) && !screwPickUp && board.currentLevel.equals("Level_3.txt"))
        {
            screwPickUp = true;
        }

        //Changes player animation
        changePlayerAnimation();

        //Changes JukeBox Animation
        changeJukeboxAnimation();

        //Changes Door Animation and barriers
        if(board.currentLevel.equals("Level_3.txt"))
        {
            doorStartTimer();
            for(int i = 0; i < doorList.size(); i++)
            {
               doorList.get(i).changeDoor();
            }
        }
    }

    //Adds Barriers to BarrierList for spikes
    public void addSpikeBarriers()
    {
        for (Spikes currentSpike : spikesList)
        {
            if (currentSpike.direction.equals("vertical"))
            {
                if (currentSpike.status.equals("up"))
                {
                    barrierList.add(new Barriers(currentSpike.xPos, currentSpike.yPos, 20, 80, 1));
                }
                else
                {
                    barrierList.add(new Barriers(currentSpike.xPos, currentSpike.yPos, 20, 80, 0));
                }

            }
            else if (currentSpike.direction.equals("horizontal"))
            {
                if (currentSpike.status.equals("up"))
                {
                    barrierList.add(new Barriers(currentSpike.xPos, currentSpike.yPos, 80, 20, 1));
                }
                else
                {
                    barrierList.add(new Barriers(currentSpike.xPos, currentSpike.yPos, 80, 20, 0));
                }
            }
        }
    }
    
    //Adds Barriers to BarrierList for springboards
    public void addSpringBarriers()
    {
        for (Springboard currentSpring : springList)
        {
             if (currentSpring.direction.equals("left"))
             {
                 barrierList.add(new Barriers(currentSpring.xPos, currentSpring.yPos, 80, 80, 0));
             }
             else if (currentSpring.direction.equals("right"))
             {
                 barrierList.add(new Barriers(currentSpring.xPos, currentSpring.yPos, 80, 80, 0));
             }
        }
    }

    //Point function continually redrawn
    public void paint(Graphics g)
    {
        //Variables
        boolean waterColorSwitch = false;
        int[] triangleXCoords = new int[3];
        int[] triangleYCoords = new int[3];
        int waterCount = 0;

        //Drawing Map
        for(int i=0;i<44;i++)
        {
            for(int j=0;j<44;j++)
            {
                //floor
                if(board.getCoord(i,j) == '0')
                {
                    switch(board.currentLevel)
                    {
                        //part of checkerboard pattern
                        case "Level_1.txt":
                            g.setColor(Color.DARK_GRAY);
                            g.fillRect(j * 20, i * 20, 80, 80);
                            break;
                        //tile like floor
                        case "Level_2.txt":
                            g.setColor(Color.DARK_GRAY);
                            g.fillRect(j * 20, i * 20, 80, 80);
                            g.setColor(Color.LIGHT_GRAY);
                            g.fillRect(j * 20 + 1, i * 20 + 1, 78, 78);
                            break;
                        case "Level_4.txt":
                            g.setColor(Color.GRAY);
                            g.fillRect(j*20, i*20, 20, 20);
                            g.setColor(Color.DARK_GRAY);
                            g.fillRect((j*20)+1,(i*20)+1, 18, 18);
                            break;
                        case "Level3toLevel2.txt":
                            g.setColor(Color.DARK_GRAY);
                            g.fillRect(j * 20, i * 20, 80, 80);
                            g.setColor(Color.LIGHT_GRAY);
                            g.fillRect(j * 20 + 1, i * 20 + 1, 78, 78);
                            break;
                    }
                }
                //wall
                else if(board.getCoord(i,j) == '1')
                {
                    g.setColor(new Color(33,33,33));
                    g.fillRect(j*20,i*20,20,20);
                }
                //water
                else if(board.getCoord(i,j) == '2')
                {
                    if(!waterColorSwitch)
                    {
                        g.setColor(new Color(0,41,58));
                        g.fillRect(j*20,i*20,20,20);
                        waterCount++;
                        if(waterCount >= 4)
                        {
                            waterCount = 0;
                            waterColorSwitch = true;
                        }
                    }
                    if(waterColorSwitch)
                    {
                        g.setColor(new Color(0,71,101));
                        g.fillRect(j*20,i*20,20,20);
                        waterCount++;
                        if(waterCount >= 4)
                        {
                            waterCount = 0;
                            waterColorSwitch = false;
                        }
                    }
                }
                //other part of checkboard pattern
                else if(board.getCoord(i,j) == '3')
                {
                    g.setColor(Color.WHITE);
                    g.fillRect(j * 20, i * 20, 80, 80);
                }
                else if(board.getCoord(i,j) == '4')
                {
                    g.setColor(Color.GRAY);
                    g.fillRect(j*20, i*20, 20, 20);
                    g.setColor(new Color(57,57,57));
                    g.fillRect((j*20)+1,(i*20)+1, 18, 18);
                }
                else if(board.getCoord(i,j) == '5')
                {
                    g.setColor(Color.GRAY);
                    g.fillRect(j*20, i*20, 20, 20);
                    g.setColor(new Color(90,90,90));
                    g.fillRect((j*20)+1,(i*20)+1, 18, 18);
                }
                else if(board.getCoord(i,j) == '6')
                {
                    g.setColor(Color.GRAY);
                    g.fillRect(j*20, i*20, 20, 20);
                    g.setColor(new Color(137,137,137));
                    g.fillRect((j*20)+1,(i*20)+1, 18, 18);
                }
                //Jukebox
                else if(board.getCoord(i,j) == 'J')
                {
                    g.setColor(new Color(0,112,198));
                    g.fillRect(j*20,i*20,60,40);
                }
                //Screwdriver
                else if(board.getCoord(i,j) == 'K' && !screwPickUp)
                {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(j*20, i*20, 80, 80);
                    g.setColor(Color.BLUE);
                    g.fillRect((j*20)+40,(i*20)+25,40,35);
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(j*20,(i*20)+35,40, 15);
                }
                else if(board.getCoord(i,j) == 'K' && screwPickUp)
                {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(j*20, i*20, 80, 80);
                }

            }

        }

        //Drawing Animations
        if(jukeboxAnimation && board.currentLevel.equals("Level_1.txt"))
        {
            g.setColor(Color.CYAN);
            g.fillRect(495,405, 5,70);
        }

        //Drawing entrance arrows
        for (Arrow currentArrow : arrowList)
        {
            //get arrow x/y position and direciton of arrow
            arrowXPos = currentArrow.xPos;
            arrowYPos = currentArrow.yPos;
            arrowDirection = currentArrow.direction;

            //draw dark gray baseplate under arrow
            g.setColor(Color.DARK_GRAY);
            g.fillRect(arrowXPos, arrowYPos, 80, 80);

            //set color to red, draw a triangle and a rectangle based on direction
            g.setColor(Color.RED);
            if (arrowDirection.equals("up"))
            {
                triangleXCoords[0] = arrowXPos + 40;
                triangleXCoords[1] = arrowXPos + 60;
                triangleXCoords[2] = arrowXPos + 20;
                triangleYCoords[0] = arrowYPos;
                triangleYCoords[1] = arrowYPos + 40;
                triangleYCoords[2] = arrowYPos + 40;
                g.fillPolygon(triangleXCoords, triangleYCoords, 3);
                g.fillRect(arrowXPos + 31, arrowYPos + 40, 19, 40);
            }
            else if (arrowDirection.equals("down"))
            {
                triangleXCoords[0] = arrowXPos + 40;
                triangleXCoords[1] = arrowXPos + 60;
                triangleXCoords[2] = arrowXPos + 20;
                triangleYCoords[0] = arrowYPos + 80;
                triangleYCoords[1] = arrowYPos + 40;
                triangleYCoords[2] = arrowYPos + 40;
                g.fillPolygon(triangleXCoords, triangleYCoords, 3);
                g.fillRect(arrowXPos + 31, arrowYPos, 19, 40);
            }
            else if (arrowDirection.equals("left"))
            {
                triangleXCoords[0] = arrowXPos;
                triangleXCoords[1] = arrowXPos + 40;
                triangleXCoords[2] = arrowXPos + 40;
                triangleYCoords[0] = arrowYPos + 40;
                triangleYCoords[1] = arrowYPos + 20;
                triangleYCoords[2] = arrowYPos + 60;
                g.fillPolygon(triangleXCoords, triangleYCoords, 3);
                g.fillRect(arrowXPos + 40, arrowYPos + 30, 40, 19);
            }
            else if (arrowDirection.equals("right"))
            {
                triangleXCoords[0] = arrowXPos + 80;
                triangleXCoords[1] = arrowXPos + 40;
                triangleXCoords[2] = arrowXPos + 40;
                triangleYCoords[0] = arrowYPos + 40;
                triangleYCoords[1] = arrowYPos + 20;
                triangleYCoords[2] = arrowYPos + 60;
                g.fillPolygon(triangleXCoords, triangleYCoords, 3);
                g.fillRect(arrowXPos, arrowYPos + 30, 40, 19);
            }
        }

        //Drawing Buttons
        for (Buttons currentButton : buttonsList)
        {
            //variables
            buttonType = currentButton.type;
            buttonrDown = currentButton.rDown;
            buttongDown = currentButton.gDown;
            buttonbDown = currentButton.bDown;
            buttonrUp = currentButton.rUp;
            buttongUp = currentButton.gUp;
            buttonbUp = currentButton.bUp;
            buttonState = currentButton.state;
            buttonxPos = currentButton.xPos;
            buttonyPos = currentButton.yPos;

            //Drawing base plate
            g.setColor(new Color(110, 110, 110));
            g.fillRect(buttonxPos, buttonyPos, 80, 80);

            //Drawing unpressed square buttons
            if (buttonType.equals("square") && buttonState.equals("up"))
            {
                g.setColor(new Color(buttonrDown, buttongDown, buttonbDown));
                g.fillRect(buttonxPos + 5, buttonyPos + 5, 70, 70);

                g.setColor(new Color(buttonrUp, buttongUp, buttonbUp));
                g.fillRect(buttonxPos + 10, buttonyPos + 10, 60, 60);
            }
            //Drawing pressed square buttons
            else if (buttonType.equals("square") && buttonState.equals("down"))
            {
                g.setColor(new Color(buttonrDown, buttongDown, buttonbDown));
                g.fillRect(buttonxPos + 5, buttonyPos + 5, 70, 70);
            }
            //Drawing unpressed circle buttons
            else if (buttonType.equals("circle") && buttonState.equals("up"))
            {
                g.setColor(new Color(buttonrDown, buttongDown, buttonbDown));
                g.fillOval(buttonxPos + 5, buttonyPos + 5, 70, 70);

                g.setColor(new Color(buttonrUp, buttongUp, buttonbUp));
                g.fillOval(buttonxPos + 10, buttonyPos + 10, 60, 60);
            }
            //Drawing pressed circle buttons
            else if (buttonType.equals("circle") && buttonState.equals("down"))
            {
                g.setColor(new Color(buttonrDown, buttongDown, buttonbDown));
                g.fillOval(buttonxPos + 5, buttonyPos + 5, 70, 70);
            }

        }

        //Drawing Spikes
        for (Spikes currentSpike : spikesList)
        {
            //get all spike attributes and variables for drawing
            spikeDirection = currentSpike.direction;
            spikeR = currentSpike.r;
            spikeG = currentSpike.g;
            spikeB = currentSpike.b;
            spikeStatus = currentSpike.status;
            spikexPos = currentSpike.xPos;
            spikeyPos = currentSpike.yPos;
            spikeID = currentSpike.spikeID;

            //if horizontal row of spikes
            if (spikeDirection.equals("horizontal"))
            {
                //if spikes are up
                if (spikeStatus.equals("up"))
                {
                    for (int y = 0; y < 4; y++)
                    {
                        triangleXCoords[0] = spikexPos + 10;
                        triangleXCoords[1] = spikexPos + 20;
                        triangleXCoords[2] = spikexPos;
                        triangleYCoords[0] = spikeyPos;
                        triangleYCoords[1] = spikeyPos + 20;
                        triangleYCoords[2] = spikeyPos + 20;

                        g.setColor(new Color(spikeR, spikeG, spikeB));
                        g.fillPolygon(triangleXCoords, triangleYCoords, 3);
                        spikexPos += 20;
                    }
                }
                //if spikes are down
                else if (spikeStatus.equals("down"))
                {
                    for (int y = 0; y < 4; y++)
                    {
                        g.setColor(Color.DARK_GRAY);
                        g.fillOval(spikexPos, spikeyPos, 20, 20);
                        g.setColor(new Color(spikeR, spikeG, spikeB));
                        g.fillOval(spikexPos + 8, spikeyPos + 8, 4, 4);
                        spikexPos += 20;
                    }
                }
            }
            //if vertical column of spikes
            else if (spikeDirection.equals("vertical"))
            {
                //if spikes are up
                if (spikeStatus.equals("up"))
                {
                    for (int y = 0; y < 4; y++)
                    {
                        triangleXCoords[0] = spikexPos + 10;
                        triangleXCoords[1] = spikexPos + 20;
                        triangleXCoords[2] = spikexPos;
                        triangleYCoords[0] = spikeyPos;
                        triangleYCoords[1] = spikeyPos + 20;
                        triangleYCoords[2] = spikeyPos + 20;

                        g.setColor(new Color(spikeR, spikeG, spikeB));
                        g.fillPolygon(triangleXCoords, triangleYCoords, 3);
                        spikeyPos += 20;
                    }
                }
                //if spikes are down
                else if (spikeStatus.equals("down"))
                {
                    for (int y = 0; y < 4; y++)
                    {
                        g.setColor(Color.DARK_GRAY);
                        g.fillOval(spikexPos, spikeyPos, 20, 20);
                        g.setColor(new Color(spikeR, spikeG, spikeB));
                        g.fillOval(spikexPos + 8, spikeyPos + 8, 4, 4);
                        spikeyPos += 20;
                    }
                }
            }

        }
        
        //drawing for springboards
        for(Springboard currentSpring : springList)
        {
             springStatus = currentSpring.status;
             springXPos = currentSpring.xPos;
             springYPos = currentSpring.yPos;
             springDirection = currentSpring.direction;
             
             if(springStatus.equals("down"))
             {
                g.setColor(Color.BLACK);
                g.fillRect(springXPos, springYPos, 80, 80);
                g.setColor(Color.DARK_GRAY);
                g.fillRect(springXPos, springYPos, 79, 79);
                g.setColor(Color.GRAY);
                g.fillRect(springXPos+10, springYPos+12, 16, 56);
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(springXPos+26, springYPos+12, 16, 56);
                g.setColor(Color.GRAY);
                g.fillRect(springXPos+42, springYPos+12, 16, 56);
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(springXPos+58, springYPos+12, 16, 56);
             }
             else if(springStatus.equals("up"))
             {
                 g.setColor(Color.GRAY);
                 g.fillRect(springXPos, springYPos, 80, 80);
                 if(springDirection.equals("left"))
                 {
                     g.setColor(Color.DARK_GRAY);
                     g.fillRect(springXPos, springYPos, 25, 80);
                     g.setColor(Color.GRAY);
                     g.fillRect(springXPos, springYPos+5, 15, 70);
                 }
                 else if(springDirection.equals("right"))
                 {
                     g.setColor(Color.DARK_GRAY);
                     g.fillRect(springXPos+55, springYPos, 25, 80);
                     g.setColor(Color.GRAY);
                     g.fillRect(springXPos+65, springYPos+5, 15, 70);
                 }
             }
        }

        //drawing for doors
        for(Doors currentDoor : doorList)
        {
            doorStatus = currentDoor.openClose;
            doorXPos = currentDoor.xPos;
            doorYPos = currentDoor.yPos;
            doorXSize = currentDoor.xSize;
            doorYSize = currentDoor.ySize;

            if(doorStatus == 1)
            {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(doorXPos, doorYPos, doorXSize, doorYSize);
                g.setColor(Color.BLACK);
                g.fillRect(doorXPos, doorYPos + 29, doorXSize, 3);
                g.setColor(Color.DARK_GRAY);
                g.fillRect(doorXPos + 4, doorYPos + 3, 3, 24);
                g.fillRect(doorXPos + 14, doorYPos + 3, 3, 24);
                g.fillRect(doorXPos + 24, doorYPos + 3, 3, 24);
                g.fillRect(doorXPos + 34, doorYPos + 3, 3, 24);

                g.fillRect(doorXPos + 4, doorYPos + 34, 3, 24);
                g.fillRect(doorXPos + 14, doorYPos + 34, 3, 24);
                g.fillRect(doorXPos + 24, doorYPos + 34, 3, 24);
                g.fillRect(doorXPos + 34, doorYPos + 34, 3, 24);
            }
            else if(doorStatus == 0)
            {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(doorXPos, doorYPos, doorXSize, 3);
                g.fillRect(doorXPos, doorYPos + 57, doorXSize, 3);
                g.setColor(Color.BLACK);
                g.fillRect(doorXPos, doorYPos + 3, doorXSize, 2);
                g.fillRect(doorXPos, doorYPos + 55, doorXSize, 2);
            }
        }


        //Drawing player
        if(!playerAnimation)
        {
            g.setColor(Color.BLUE);
            g.fillRect(xPos, yPos, playerSize, playerSize);
        }
        else if(playerAnimation)
        {
            g.setColor(new Color(6, 0, 171));
            g.fillRect(xPos, yPos, playerSize, playerSize);
        }

    }

    //Function does nothing
    @Override
    public void keyTyped(KeyEvent event) { }

    //Function changes boolean variables on press
    @Override
    public void keyPressed(KeyEvent event)
    {
        if(event.getKeyCode() == KeyEvent.VK_DOWN)
        {
            down = true;
        }
        if(event.getKeyCode() == KeyEvent.VK_UP)
        {
            up = true;
        }
        if(event.getKeyCode() == KeyEvent.VK_LEFT)
        {
            left = true;
        }
        if(event.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            right = true;
        }
        if(event.getKeyCode() == KeyEvent.VK_ENTER)
        {
            //In case the robot does not work
            try
            {
                //Creates a robot to click the buttons
                Robot bot = new Robot();
                PointerInfo mouse = MouseInfo.getPointerInfo();
                Point location = mouse.getLocation();
                int oldX = (int)location.getX(), oldY = (int)location.getY();
                
                //If current button is saved, activates the save action event
                if(getMenuButton().getText().equals("Save"))
                {
                    saveAs.setVisible(true);
                    text.setVisible(true);
                    saveFile.setVisible(true);
                    save.setBorder(null);
                    saveFile.setBorder(BorderFactory.createBevelBorder(1, Color.MAGENTA, Color.BLUE));
                    setMenuButton(saveFile);   //Sets the current menu button
                    
                    location = game.getLocation();
                    bot.mouseMove((int)location.getX()+150, (int)location.getY()+350);          
                    bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    
                    //When the user hits enter again
                    text.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent action)
                        {
                           //Gets the file name from the user input
                           fileName = text.getText();
                           
                           int checker = 0;
                           try
                           {
                              //Checks to see how many save files how been created
                              Scanner scanSaves = new Scanner(new File("Save_Files.txt"));
                              while(scanSaves.hasNext())
                              {
                                 //Adds one to checker
                                 checker++;
                                 scanSaves.next();
                              }
                           }
                           catch(Exception f){}
                           
                           //Save files exceed amount allowed
                           if(checker == 10)
                           {
                              //Tells the user that the maximum number of saves files has been created
                              exceedMax.setVisible(true);
                           }
                           //Inform user that there was nothing entered and to try again
                           else if(fileName.equals(""))
                           {
                              //Asks the user to input something again
                              inputZero.setVisible(true);
                           }
                           else
                           {
                              //Activates the saveFile button
                              Point location = game.getLocation();
                              bot.mouseMove((int)location.getX()+30, (int)location.getY()+380);
                              bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                              bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                              
                              try
                              {
                                 boolean inSaveFile = false;
                                 
                                 //Inputs the file name to the save files doc
                                 BufferedWriter saveBW = new BufferedWriter(new FileWriter("Save_Files.txt",true));
                                 Scanner scanSaves = new Scanner(new File("Save_Files.txt"));
                                 
                                 //Loops through the save files
                                 while(scanSaves.hasNext())
                                 {
                                    //If the save files already has the name, changes the boolean to true
                                    if(scanSaves.next().equals(fileName))
                                    {
                                       inSaveFile = true;
                                       break;   //Exits the loop
                                    }
                                 }
                                 
                                 //If the user file is not in the save files, adds it
                                 if(inSaveFile == false)
                                 {
                                    saveBW.write(fileName);
                                    saveBW.newLine();
                                    saveBW.close();
                                 }
                              }
                              catch(Exception io){}
                              animationTimer.start();
                           }
                        }
                    });
                    
                    //If the user decides not to save a file, they can hit escape again to go back to the menu
                    text.addKeyListener(new KeyListener(){
                        @Override
                        public void keyPressed(KeyEvent e)
                        {
                           //When escape is pressed
                           if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                           {
                              //Sets the save files to false visibility
                              saveAs.setVisible(false);
                              text.setVisible(false);
                              saveFile.setVisible(false);
                              exceedMax.setVisible(false);
                              inputZero.setVisible(false);
                              
                              //Puts the focus back on the save button
                              save.setBorder(BorderFactory.createBevelBorder(1, Color.MAGENTA, Color.BLUE));
                              setMenuButton(save);   //Sets the current menu button
                              
                              game.requestFocus();
                           }
                        }
                        //Unused functions
                        @Override
                        public void keyTyped(KeyEvent e){}
                        @Override
                        public void keyReleased(KeyEvent e){}
                    });
                }
                //If current button is load, activates the load action event
                if(getMenuButton().getText().equals("Load"))
                {
                    whichFile.setVisible(true);
                    textLoad.setVisible(true);
                    loadFile.setVisible(true);
                    
                    //Adding file names from the save files list to the jtextarea
                    try
                    {
                       Scanner files = new Scanner(new File("Save_Files.txt"));
                       //Goes through the file
                       while(files.hasNext())
                       {
                           //Adds the file name to the jtextarea
                           loadList.append(files.next()+"\n");
                       }
                    }
                    catch(Exception x){}
                    
                    loadList.setVisible(true);
                    load.setBorder(null);
                    
                    //Focuses on the load file button
                    loadFile.setBorder(BorderFactory.createBevelBorder(1, Color.MAGENTA, Color.BLUE));
                    setMenuButton(loadFile);   //Sets the current menu button
                    
                    location = game.getLocation();
                    bot.mouseMove((int)location.getX()+150, (int)location.getY()+350);          
                    bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    
                    //When the user hits enter again
                    textLoad.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent action)
                        {
                           //Gets the file name from the user input
                           fileName = textLoad.getText();
                           
                           //Need to check if the file exists
                           boolean inLoadFile = false;
                           
                           try
                           {
                              //Checks to see if the file is in saves files
                              Scanner scanLoads = new Scanner(new File("Save_Files.txt"));
                              while(scanLoads.hasNext())
                              {
                                 //If the file is valid, moves on
                                 if(scanLoads.next().equals(fileName))
                                 {
                                    inLoadFile = true;
                                    break;
                                 }
                              }
                           }
                           catch(Exception f){}
                           
                           if(inLoadFile == true)
                           {
                              //Activates the loadFile button
                              Point location = game.getLocation();
                              bot.mouseMove((int)location.getX()+50, (int)location.getY()+380);
                              bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                              bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                           }
                           else
                           {
                              tryAgain.setVisible(true);
                           }
                        }
                    });
                    
                    //If the user decides not to load a file, they can hit escape again to go back to the menu
                    textLoad.addKeyListener(new KeyListener(){
                        @Override
                        public void keyPressed(KeyEvent e)
                        {
                           //When escape is pressed
                           if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                           {
                              //Sets the save files to false visibility
                              whichFile.setVisible(false);
                              textLoad.setVisible(false);
                              loadFile.setVisible(false);
                              loadList.setVisible(false);
                              tryAgain.setVisible(false);
                              
                              //Puts the focus back on the save button
                              load.setBorder(BorderFactory.createBevelBorder(1, Color.MAGENTA, Color.BLUE));
                              setMenuButton(load);   //Sets the current menu button
                              
                              game.requestFocus();
                           }
                        }
                        //Unused functions
                        @Override
                        public void keyTyped(KeyEvent e){}
                        @Override
                        public void keyReleased(KeyEvent e){}
                    });
                }
                //If current button is restart area, activates the restart area action event
                if(getMenuButton().getText().equals("Restart Area"))
                {
                    location = game.getLocation();
                    bot.mouseMove((int)location.getX()+30, (int)location.getY()+170);
                    bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                }
                //If current button is restart level, activates the restart level action event
                if(getMenuButton().getText().equals("Restart Level"))
                {
                    location = game.getLocation();
                    bot.mouseMove((int)location.getX()+30, (int)location.getY()+225);
                    bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                }
                //If current button is exit, activates the exit action event
                if(getMenuButton().getText().equals("Exit"))
                {
                    location = game.getLocation();
                    bot.mouseMove((int)location.getX()+30, (int)location.getY()+280);
                    bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                }
                bot.mouseMove(oldX,oldY);
            }
            catch(AWTException ignored)
            {}
        }
        //When the escape key is pressed, the game pauses and a menu is brought up
        if(event.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            menuPause = !menuPause;   //Switches the menuPause boolean
            p = new JPanel();
                        
            //If the game is paused
            if(menuPause)
            {
                p.setSize(160,700);   //Sets the size of the window
                p.setBackground(Color.BLACK);
      
                //Create the game buttons
                save = new JButton("Save");
                buttonDesign(save);   //Calls on a function to design the buttons
                p.add(save);   //Adds it to the JPanel
         
                load = new JButton("Load");
                buttonDesign(load);
                p.add(load);
         
                restartA = new JButton("Restart Area");
                buttonDesign(restartA);
                p.add(restartA);
         
                restartL = new JButton("Restart Level");
                buttonDesign(restartL);
                p.add(restartL);
         
                exit = new JButton("Exit");
                buttonDesign(exit);
                p.add(exit);
                
                saveAs = new JLabel("Save as...");
                saveAs.setForeground(Color.WHITE);
                saveAs.setFont(new Font("Serif",Font.BOLD,15));
                p.add(saveAs);
                
                text = new JTextField(14);
                p.add(text);
                
                saveFile = new JButton("Save File");
                buttonDesign(saveFile);
                p.add(saveFile);
                
                exceedMax = new JLabel("At max saves!");
                exceedMax.setForeground(Color.WHITE);
                exceedMax.setFont(new Font("Serif",Font.BOLD,15));
                p.add(exceedMax);
                
                inputZero = new JLabel("Try again...");
                inputZero.setForeground(Color.WHITE);
                inputZero.setFont(new Font("Serif",Font.BOLD,15));
                p.add(inputZero);
                
                whichFile = new JLabel("Type which file...");
                whichFile.setForeground(Color.WHITE);
                whichFile.setFont(new Font("Serif",Font.BOLD,15));
                p.add(whichFile);
                
                textLoad = new JTextField(14);
                p.add(textLoad);
                
                loadFile = new JButton("Load File");
                buttonDesign(loadFile);
                p.add(loadFile);
                
                tryAgain = new JLabel("Enter a valid file...");
                tryAgain.setForeground(Color.WHITE);
                tryAgain.setFont(new Font("Serif",Font.BOLD,15));
                p.add(tryAgain);
                
                loadList = new JTextArea(10,14);
                loadList.setEditable(false);
                p.add(loadList);
                
                animationTimer.stop();   //Stop the player
                p.setVisible(true);   //Makes the window visible
                
                //Make certain items not visible
                saveAs.setVisible(false);
                text.setVisible(false);
                saveFile.setVisible(false);
                exceedMax.setVisible(false);
                inputZero.setVisible(false);
                whichFile.setVisible(false);
                textLoad.setVisible(false);
                loadFile.setVisible(false);
                tryAgain.setVisible(false);
                loadList.setVisible(false);
                
                game.add(p);   //Adds the panel to the window

                //Sets the indicator around the save button
                save.setBorder(BorderFactory.createBevelBorder(1, Color.MAGENTA, Color.BLUE));
                setMenuButton(save);   //Sets the current menu button

                //Save button functionality
                saveFile.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        //Adds current level and assets to the save file
                        String level = board.getCurrentLevel();
                        try
                        {
                            //Allows to write to Save_File.txt
                            bw = new BufferedWriter(new FileWriter(fileName));
                            bw.write(""+level);   //Writes the level to the file
                            bw.newLine();
                            bw.write(""+xPos+" "+yPos);   //Writes the player's position to the file
                            bw.newLine();

                            //Writes the board to the file
                            for(int i=0;i<44;i++)
                            {
                                for(int j=0;j<44;j++)
                                {
                                    bw.write(""+board.getCoord(i,j)+" ");
                                }
                                bw.newLine();
                            }

                            //Writes the levels to the file
                            bw.write("Level_1.txt");
                            bw.newLine();
                            bw.write("Level_2.txt");
                            bw.newLine();
                            bw.write("Level_3.txt");
                            bw.newLine();
                            bw.write("Level_4.txt");
                            bw.newLine();

                            //Gets the buttons
                            for (Buttons currentButton : buttonsList)
                            {
                                buttonType = currentButton.type;
                                buttonrDown = currentButton.rDown;
                                buttongDown = currentButton.gDown;
                                buttonbDown = currentButton.bDown;
                                buttonrUp = currentButton.rUp;
                                buttongUp = currentButton.gUp;
                                buttonbUp = currentButton.bUp;
                                buttonState = currentButton.state;
                                buttonxPos = currentButton.xPos;
                                buttonyPos = currentButton.yPos;
                                buttonID = currentButton.buttonID;
                                buttonTime = currentButton.buttonTimeElapsed;
                                bw.write(""+buttonType+" "+buttonrDown+" "+buttongDown+" "+buttonbDown+" "+buttonrUp+" "
                                        +buttongUp+" "+buttonbUp+" "+buttonState+" "+buttonxPos+" "+buttonyPos+" "+buttonID+" "+(int)buttonTime);
                                bw.newLine();
                            }
                            //Gets the spikes
                            for (Spikes currentSpike : spikesList)
                            {
                                spikeDirection = currentSpike.direction;
                                spikeR = currentSpike.r;
                                spikeG = currentSpike.g;
                                spikeB = currentSpike.b;
                                spikeStatus = currentSpike.status;
                                spikexPos = currentSpike.xPos;
                                spikeyPos = currentSpike.yPos;
                                spikeID = currentSpike.spikeID;
                                bw.write(""+spikeDirection+" "+spikeR+" "+spikeG+" "+spikeB+" "+spikeStatus+" "
                                        +spikexPos+" "+spikeyPos+" "+spikeID);
                                bw.newLine();
                            }
                            //Gets the barriers
                            for (Barriers currentBarrier : barrierList)
                            {
                                int barrierXPos = currentBarrier.xPos;
                                int barrierYPos = currentBarrier.yPos;
                                int barrierXSize = currentBarrier.xSize;
                                int barrierYSize = currentBarrier.ySize;
                                int barrierState = currentBarrier.OnOff;

                                bw.write("barrier " + barrierXPos + " " + barrierYPos + " " + barrierXSize + " " + barrierYSize + " " + barrierState);
                                bw.newLine();
                            }
                            //Gets the arrows
                            for (Arrow currentArrow : arrowList)
                            {
                                arrowXPos = currentArrow.xPos;
                                arrowYPos = currentArrow.yPos;
                                arrowDirection = currentArrow.direction;

                                bw.write("arrow "+arrowXPos+" "+arrowYPos+" "+arrowDirection);
                                bw.newLine();
                            }
                            //Gets the springboards
                            for(Springboard spring : springList)
                            {
                                springStatus = spring.status;
                                springXPos = spring.xPos;
                                springYPos = spring.yPos;
                                springDirection = spring.direction;

                                bw.write("springboard "+springXPos+" "+springYPos+" "+springStatus+" "+springDirection);
                                bw.newLine();
                            }
                            //Gets the doors
                            for(Doors door : doorList)
                            {
                                doorXPos = door.xPos;
                                doorYPos = door.yPos;
                                doorXSize = door.xSize;
                                doorYSize = door.ySize;
                                doorStatus = door.openClose;
                                doorTime = door.doorTimeElapsed;

                                bw.write("door "+doorXPos+" "+doorYPos+" "+doorXSize+" "+doorYSize+ " "+ doorStatus + " "+ (int)doorTime);//may need to set door status to 1 always
                                bw.newLine();
                            }
                            bw.close();  //Closes the file
                        }
                        catch(Exception ignored)
                        {}

                        //Allows the player to play after a save
                        p.setVisible(false);
                        menuPause = !menuPause;
                        animationTimer.start();
                        game.requestFocus();
                    }
                });

                //Load button functionality
                loadFile.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        //Loads up the save file to the game board
                        board = new MyFile(fileName);

                        //Gets the player's position from the load file
                        xPos = board.getXPosition();
                        yPos = board.getYPosition();

                        //Makes the board
                        buttonsList = board.getButtonsList();
                        doorList = board.getDoorList();
                        barrierList = board.getBarrierList();
                        springList = board.getSpringList();
                        spikesList = board.getSpikesList();
                        arrowList = board.getArrowList();

                        //Allows player to play again
                        p.setVisible(false);
                        menuPause = !menuPause;
                        animationTimer.start();
                        game.requestFocus();
                    }
                });

                //Restarts the area the player is in
                restartA.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        //Checks to see which level the player is on
                        if(board.getCurrentLevel().equals("Level_1.txt"))
                        {
                            //Resets the player position
                            xPos = 420;
                            yPos = 620;
                        }
                        if(board.getCurrentLevel().equals("Level_2.txt"))
                        {
                            xPos = 420;
                            yPos = 750;
                            board = new MyFile("Level_2.txt");
                        }
                        if(board.getCurrentLevel().equals("Level_3.txt"))
                        {
                            xPos = 420;
                            yPos = 670;
                            screwPickUp = false;  //Puts the screwdriver back
                            doorsStarted = -1;
                            board = new MyFile("Level_3.txt");
                        }
                        if(board.getCurrentLevel().equals("Level_4.txt"))
                        {
                            xPos = 620;
                            yPos = 220;
                            board = new MyFile("Level_4.txt");
                        }
                        if(board.getCurrentLevel().equals("Level3toLevel2.txt"))
                        {
                            xPos = 420;
                            yPos = 750;
                            board = new MyFile("Level_2.txt");
                        }

                        //Makes the board
                        buttonsList = board.getButtonsList();
                        barrierList = board.getBarrierList();
                        spikesList = board.getSpikesList();
                        arrowList = board.getArrowList();
                        springList = board.getSpringList();
                        doorList = board.getDoorList();
                        addSpikeBarriers();
                        addSpringBarriers();

                        //Gets rid of the in game menu and lets the player move
                        p.setVisible(false);
                        menuPause = !menuPause;
                        animationTimer.start();
                    }
                });

                //Restarts the level
                restartL.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        //Goes to level
                        board = new MyFile("Level_1.txt");
                        buttonsList = board.getButtonsList();
                        barrierList = board.getBarrierList();
                        spikesList = board.getSpikesList();
                        arrowList = board.getArrowList();
                        springList = board.getSpringList();
                        doorList = board.getDoorList();

                        //Resets the player position
                        xPos = 420;
                        yPos = 620;

                        //Gets rid of the in game menu and lets the player move
                        p.setVisible(false);
                        menuPause = !menuPause;
                        animationTimer.start();
                    }
                });

                //Exit button functionality
                exit.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        //Exits and ends program
                        game.dispatchEvent(new WindowEvent(game, WindowEvent.WINDOW_CLOSING));
                    }
                });
            }
            //Allows program to run again
            else if(!menuPause)
            {
                p.setVisible(false);
                animationTimer.start();
            }
        }
    }

    //Function changes player boolean variables on release
    @Override
    public void keyReleased(KeyEvent event)
    {
        if(event.getKeyCode() == KeyEvent.VK_DOWN)
        {
            down = false;
            //If the pause menu is true, moves the indicator down
            if(menuPause)
            {
                moveDown();
            }
        }
        if(event.getKeyCode() == KeyEvent.VK_UP)
        {
            up = false;
            //If the pause menu is true, moves the indicator up
            if(menuPause)
            {
                moveUp();
            }
        }
        if(event.getKeyCode() == KeyEvent.VK_LEFT)
        {
            left = false;
        }
        if(event.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            right = false;
        }
    }

    //Function to get current player position
    public void getPosition()
    {
        System.out.println("You are currently at: " + xPos + ", " + yPos);
    }

    //Function changes player color animation
    public void changePlayerAnimation()
    {
        playerTimeElapsed = (System.currentTimeMillis() / 1000) - (playerStartTime / 1000);

        if(playerTimeElapsed >= playerInterval)
        {
            playerAnimation = !playerAnimation;
            playerStartTime = System.currentTimeMillis();
        }
    }

    //Function changes jukebox color animation
    public void changeJukeboxAnimation()
    {
        jukeboxTimeElapsed = (System.currentTimeMillis() / 1000) - (jukeboxStartTime / 1000);

        if(jukeboxTimeElapsed >= jukeboxInterval)
        {
            jukeboxAnimation = !jukeboxAnimation;
            jukeboxStartTime = System.currentTimeMillis();
        }
    }

    //Function changes jukebox color animation
    public void doorStartTimer()
    {
        doorStartTimeElapsed = (System.currentTimeMillis() / 1000) - (nextDoorStart / 1000);

        if(doorStartTimeElapsed >= doorStartInterval)
        {
            doorsStarted++;
            nextDoorStart = System.currentTimeMillis();
        }
    }

    //A function to keep the button design uniform
    public void buttonDesign(JButton buttonName)
    {
        if(buttonName.getText().equals("New Game") || buttonName.getText().equals("Load Game"))
         {
            buttonName.setBackground(Color.BLACK);
            buttonName.setForeground(Color.WHITE);
            buttonName.setFont(new Font("Serif",Font.BOLD,30));
            buttonName.setPreferredSize(new Dimension(200,100));
            buttonName.setFont(new Font("Serif",Font.BOLD,30));
            buttonName.setBorder(BorderFactory.createBevelBorder(1, Color.MAGENTA, Color.BLUE));
         }
         else
         {
            buttonName.setBackground(Color.DARK_GRAY);
            buttonName.setForeground(Color.WHITE); 
            buttonName.setFont(new Font("Serif",Font.BOLD,16));
            buttonName.setPreferredSize(new Dimension(150,50));
         }
         buttonName.setBorder(null);
         buttonName.setFocusPainted(false);   //Gets rid of the focus border around the button
    }

    //Sets the which menu button is in focus
    public void setMenuButton(JButton button)
    { tempButton = button; 
      //tempButton.setFont(new Font("Comic Sans MS",Font.BOLD,20)); 
    }

    //Gets the menu button in focus
    public JButton getMenuButton()
    { return tempButton; }

    //Moves the indicator of which button the player is on up
    public void moveUp()
    {
        if(getMenuButton().getText().equals("Load"))
        {
            changeButton(load, save);
        }
        else if(getMenuButton().getText().equals("Restart Area"))
        {
            changeButton(restartA, load);
        }
        else if(getMenuButton().getText().equals("Restart Level"))
        {
            changeButton(restartL, restartA);
        }
        else if(getMenuButton().getText().equals("Exit"))
        {
            changeButton(exit, restartL);
        }
        else if(getMenuButton().getText().equals("Save"))
        {
            changeButton(save, exit);
        }
    }

    //Moves the indicator for the buttons down
    public void moveDown()
    {
        if(getMenuButton().getText().equals("Save"))
        {
            changeButton(save, load);
        }
        else if(getMenuButton().getText().equals("Load"))
        {
            changeButton(load, restartA);
        }
        else if(getMenuButton().getText().equals("Restart Area"))
        {
            changeButton(restartA, restartL);
        }
        else if(getMenuButton().getText().equals("Restart Level"))
        {
            changeButton(restartL, exit);
        }
        else if(getMenuButton().getText().equals("Exit"))
        {
           changeButton(exit, save);
        }
    }
    
    public void moveLeft(JButton oldButton, JButton newButton)
    {
         if(getMenuButton().getText().equals("Load Game"))
         {
            changeButton(oldButton, newButton);
         }
    }
    
    public void moveRight(JButton oldButton, JButton newButton)
    {
         if(getMenuButton().getText().equals("New Game"))
         {
            changeButton(oldButton, newButton);
         }
    }

    //Changes the focus of the button
    public void changeButton(JButton oldButton, JButton newButton)
    {
        buttonDesign(oldButton);
        oldButton.setBorder(null);
        if(oldButton.getText().equals("New Game") || oldButton.getText().equals("Load Game"))
        {
           newButton.setFont(new Font("Serif",Font.BOLD,30));
        }
        else
        {
           newButton.setFont(new Font("Serif",Font.BOLD,16));
        }
        newButton.setBorder(BorderFactory.createBevelBorder(1, Color.MAGENTA, Color.BLUE));
        setMenuButton(newButton);
    }
}
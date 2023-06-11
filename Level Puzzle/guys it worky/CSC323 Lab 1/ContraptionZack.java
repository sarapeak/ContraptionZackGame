import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class ContraptionZack extends JPanel
{
   //set ints for game screen width and height
   public static final int WIDTH = 890;
   public static final int HEIGHT = 915;
   
   public static void main(String[] args)
   {
      MyCanvas help = new MyCanvas();
     
      //Sets the name and size
      JFrame menu = new JFrame("Contraption Zack");
      menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      /*Problem, can go to new level and play however you have to adjust the window to see the game*/
      menu.setBackground(Color.BLACK);
      menu.setSize(WIDTH,HEIGHT);
      menu.setContentPane(new ContraptionZack());
      menu.getContentPane().setBackground(Color.BLACK);
      
      //Checks to make sure we have an image
      try
      {
         //Reads in an image file
         BufferedImage titlePic = ImageIO.read(new File("contrapzack.jpg"));
         JLabel titleLabel = new JLabel(new ImageIcon(titlePic));
         menu.add(titleLabel);   //Adds the title picture
      
         //Creates a button to start a new game
         JButton newGame = new JButton("New Game");
         //Sets the background and foreground color and the size and font
         help.buttonDesign(newGame);
         newGame.setFont(new Font("Serif",Font.BOLD,30));
         newGame.setBorder(BorderFactory.createBevelBorder(1, Color.MAGENTA, Color.BLUE));

         menu.add(newGame);   //Adds the button to the window
         help.setMenuButton(newGame);
         
         //Creates a button to load a game
         JButton loadGame = new JButton("Load Game");
         help.buttonDesign(loadGame);
         menu.add(loadGame);   //Adds the button to the window
         
         //Adds a label for who created the game
         JLabel createdBy = new JLabel("\nCreated by: Brian Ballinger, Mackenzie Gee, and Sara Peak");
         createdBy.setFont(new Font("Serif",Font.BOLD,20));
         createdBy.setForeground(Color.WHITE);
         menu.add(createdBy);
         
         menu.setVisible(true);   //Sets window visibility to true
         
         //If the button is pressed, it opens a new game
         newGame.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
               //Sets the main menu visibility to false
               menu.setVisible(false);
               menu.getContentPane().removeAll();   //Removes all components from the window
               menu.setContentPane(new MyCanvas("Level_1.txt", menu));   //Refreshes the window
            }
         });
         
         menu.addKeyListener(new KeyListener(){
         @Override
         public void keyPressed(KeyEvent event)
         {
            if(event.getKeyCode() == KeyEvent.VK_LEFT)
            {
               help.moveLeft(loadGame, newGame);
            }
            if(event.getKeyCode() == KeyEvent.VK_RIGHT)
            {
               help.moveRight(newGame, loadGame);
            }
            if(event.getKeyCode() == KeyEvent.VK_ENTER)
            {
               try
               {
                  Robot clicker = new Robot();
                  Point location;
                  //If current button is new game, activates the new game action event
                  if(help.getMenuButton().getText().equals("New Game"))
                  {
                     location = menu.getLocation();
                     clicker.mouseMove((int)location.getX()+400, (int)location.getY()+550);     
                     clicker.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                     clicker.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                  }
               }
               catch(AWTException a)
               {}
            }
         }
         @Override
         public void keyTyped(KeyEvent event)
         {}
         @Override
         public void keyReleased(KeyEvent event)
         {}
         });
      }
      catch(Exception e)
      {}
      menu.requestFocus();
   }
}
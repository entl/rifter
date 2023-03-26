//locate file in the package
package Game;

//import local classes

import Database.Account;
import Database.Quests;
import Game.Entity.Player;
import Game.Object.SuperObject;
import Game.Tile.TileManager;

//import java classes
import javax.swing.JPanel;
import java.awt.*;

//GamePanel class combines all the game logic, gameplay
//Extends JPanel class to have access to its methods
//Runnable is necessary to create game thread
public class GamePanel extends JPanel implements Runnable
{
    public Quests quests;
    public Account user;
    public Player player;

    //16x16 size of the tile
    final int originalTileSize = 16;
    final int multiplier = 3;

    //resized tile size to look good on the FullHD screen
    public final int tileSize = originalTileSize * multiplier;

    //screen ratio 9x16 like a mobile phone
    public final int maxScreenColumn = 9;
    public final int maxScreenRow = 16;

    // 432x768 pixels
    public final int screenWidth = tileSize * maxScreenColumn;
    public final int screenHeight = tileSize * maxScreenRow;

    // world size
    public final int maxWorldColumn = 100;
    public final int maxWorldRow = 110;
    public final int worldWidth = tileSize * maxWorldColumn;
    public final int worldHeight = tileSize * maxWorldRow;

    //constructor
    public GamePanel(Quests quests, Account user)
    {
        this.user = user;
        this.quests = quests;
        //create a player
        this.player = new Player(this, keyHandler, quests, user);
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        //improves rendering performance
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        //with set focusable, game panel in focused on receiving key input
        this.setFocusable(true);
    }

    // FPS
    final int FPS = 240;
    //create instance of the tile to render map and objects
    TileManager tileManager = new TileManager(this);
    //used to get user pressed buttons
    KeyHandler keyHandler = new KeyHandler();
    //used to create game loop
    Thread gameThread;
    //'this' refers to game panel class
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    //assetSetter used to locate quests on the map and have interaction with them
    public AssetSetter assetSetter = new AssetSetter(this);

    //currently objects refers only to quests. Can be extended to other objects which should have interaction
    //10 means that on the screen can be up to 10 objects
    public SuperObject[] objects = new SuperObject[10];

    //load quests to the map
    public void setupGame(Quests quests)
    {
        assetSetter.setObject(quests);
    }

    //starts game thread
    public void startGameThread()
    {
        //instantiate thread
        gameThread = new Thread(this);
        gameThread.start();
    }

    //implementation of the game loop
    //to make possible run game with certain fps 'delta' method is used
    @Override
    public void run()
    {
        //https://stackoverflow.com/questions/26838286/delta-time-getting-60-updates-a-second-in-java

        //Get the system time
        long lastTime = System.nanoTime();
        //Set definition of how many ticks per 1000000000 drawInterval or 1 sec
        double drawInterval = 1000000000 / (double) FPS;
        double delta = 0;

        while (gameThread != null)
        {
            //Update the time
            long now = System.nanoTime();
            //calculate change in time since last known time
            delta += (now - lastTime) / drawInterval;
            //update last known time
            lastTime = now;
            //continue while delta is less than or equal to 1
            if (delta >= 1)
            {
                //Go through one tick
                update();
                repaint();
                //decrement delta
                delta--;
            }
        }
    }

    //used to updates player position and reload quests
    public void update()
    {
        player.update();
        assetSetter.setObject(quests);
    }


    //used to draw updated information
    //this method already implemented in JPanel
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        //set to Graphics2D as it has more functions
        Graphics2D graphics2D = (Graphics2D) graphics;

        //draw tiles
        tileManager.draw(graphics2D);

        //draw objects (quests)
        for (int i = 0; i < objects.length; i++)
        {
            if (objects[i] != null) objects[i].draw(graphics2D, this);
        }

        //draw player
        player.draw(graphics2D);

        //used to release graphics
        graphics2D.dispose();
    }
}

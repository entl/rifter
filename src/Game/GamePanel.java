package Game;

import Database.Account;
import Database.Quests;
import Game.Entity.Player;
import Game.Object.ObjectQuest;
import Game.Tile.TileManager;

import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable
{
    public Quests quests;
    public Account user;
    public Player player;
    //Screen settings
    //16x16 size
    final int originalTileSize = 16;
    final int multiplier = 3;
    //resized tile size to look good on the screen
    public final int tileSize = originalTileSize * multiplier;

    //screen ratio 9x16 like a mobile phone
    public final int maxScreenColumn = 9;
    public final int maxScreenRow = 16;

    // 432x768 pixels
    public final int screenWidth = tileSize*maxScreenColumn;
    public final int screenHeight = tileSize*maxScreenRow;

    // world size
    public final int maxWorldColumn = 100;
    public final int maxWorldRow = 110;
    public final int worldWidth = tileSize * maxWorldColumn;
    public final int worldHeight = tileSize * maxWorldRow;

    public GamePanel(Quests quests, Account user)
    {
        this.user = user;
        this.quests = quests;
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
    TileManager tileManager = new TileManager(this);
    //used to get user pressed buttons
    KeyHandler keyHandler = new KeyHandler();
    //used to create game loop
    Thread gameThread;
    //'this' refers to game panel class
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    //create instance of player
    public AssetSetter assetSetter = new AssetSetter(this);

    public ObjectQuest[] objects = new ObjectQuest[10];

    public void setupGame(Quests quests)
    {
        assetSetter.setObject(quests);
    }
    public void startGameThread()
    {
        //instantiate thread
        gameThread = new Thread(this);
        gameThread.start();
    }

    //game loop
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

    //used to updates player position
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

        tileManager.draw(graphics2D);
        for (int i = 0; i < objects.length; i++)
        {
            if(objects[i] != null)
                objects[i].draw(graphics2D, this);
        }
        player.draw(graphics2D);

        //used to release graphics
        graphics2D.dispose();
    }


}

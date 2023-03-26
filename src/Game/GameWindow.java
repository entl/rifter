//locate file in game package
package Game;

//import local classes

import Database.Account;
import Database.Quests;

//import java classes
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


//class is used to declare game window properties
public class GameWindow
{
    private JFrame window;

    //isOpen used to allow user open only one window
    private boolean isOpen;

    /**
     * Start method used to open window and start game
     *
     * @param quests instance of the Quest class used to locate quests on the map
     * @param user   instance of the Account class used to get sex of the character and add coins to the wallet
     */
    public void start(Quests quests, Account user)
    {
        this.isOpen = true;

        //create a window
        this.window = new JFrame();
        this.window.setResizable(false);
        this.window.setTitle("Rifter");

        //https://www.youtube.com/watch?v=po49sg9ckio
        this.window.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                isOpen = false;
            }
        });

        //add game panel to the window
        GamePanel gamePanel = new GamePanel(quests, user);
        this.window.add(gamePanel);

        //show window
        this.window.pack();
        this.window.setLocationRelativeTo(null);
        this.window.setAlwaysOnTop(true);
        this.window.toFront();
        this.window.setVisible(true);

        //start game
        gamePanel.setupGame(quests);
        gamePanel.startGameThread();

    }

    public boolean getIsOpen()
    {
        return isOpen;
    }


}

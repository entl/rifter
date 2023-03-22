package Game;

import Database.Account;
import Database.Quests;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameWindow
{
    private JFrame window;

    private boolean isOpen;

    public void start(Quests quests, Account user)
    {
        this.isOpen = true;

        //create a window
        this.window = new JFrame();
        this.window.setResizable(false);
        this.window.setTitle("Rifter");

        //https://www.youtube.com/watch?v=po49sg9ckio
        this.window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
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

        gamePanel.setupGame(quests);
        gamePanel.startGameThread();

    }

    public boolean getIsOpen()
    {
        return isOpen;
    }


}

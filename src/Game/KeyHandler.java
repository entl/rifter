//locate file in game package
package Game;

//import java classes

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//implements is needed by documentation

/**
 * Class that is listens to keyboard input inside game
 */
public class KeyHandler implements KeyListener
{
    //whn certain key is pressed change it to true
    public boolean wPressed, sPressed, aPressed, dPressed;

    //override is required because we implement methods
    //method is empty because we do not use type in game
    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    /**
     * Gets pressed key and sets according key to true
     *
     * @param e instance of the KeyEvent used compare keys
     */
    @Override
    public void keyPressed(KeyEvent e)
    {
        //return number of the key
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W)
        {
            wPressed = true;
        }
        if (code == KeyEvent.VK_S)
        {
            sPressed = true;
        }
        if (code == KeyEvent.VK_A)
        {
            aPressed = true;
        }
        if (code == KeyEvent.VK_D)
        {
            dPressed = true;
        }
    }

    /**
     * Gets released key and sets according key to false
     *
     * @param e instance of the KeyEvent used compare keys
     */
    @Override
    public void keyReleased(KeyEvent e)
    {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W)
        {
            wPressed = false;
        }
        if (code == KeyEvent.VK_S)
        {
            sPressed = false;
        }
        if (code == KeyEvent.VK_A)
        {
            aPressed = false;
        }
        if (code == KeyEvent.VK_D)
        {
            dPressed = false;
        }
    }
}

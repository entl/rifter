//locate in game\object
package Game.Object;

//import local class
import Game.GamePanel;

//java class used to work with graphics
import java.awt.*;

//class used to create objects which we will have interaction with
//currently it is only quests
public class SuperObject
{
    public String id;
    public Image image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;

    //create collision area, so when we touch object we will have interaction
    public Rectangle collisionArea = new Rectangle(0, 0, 48, 48);
    public int collisionAreaDefaultX = 0;
    public int collisionAreaDefaultY = 0;

    /**
     * Takes a number and returns its square root.
     * @param graphics2D used to draw object
     * @param gamePanel used to get relative screen position
     */
    public void draw(Graphics2D graphics2D, GamePanel gamePanel)
    {
        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

        graphics2D.drawImage(this.image, screenX,screenY,gamePanel.tileSize, gamePanel.tileSize, null);
    }
}

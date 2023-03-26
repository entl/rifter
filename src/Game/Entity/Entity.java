package Game.Entity;

import java.awt.*;

//class with describes all entities in the game
//currently only player itself
public class Entity
{
    public int worldX, worldY;
    public int speed;

    //used to draw right player image based on the direction
    public Image up1, up2, down1, down2, left1, left2, right1, right2, standdown, standup, standleft, standright;
    public String direction;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle collisionArea;
    public int collisionAreaDefaultX, collisionAreaDefaultY;
    public boolean collisionOn = false;
}

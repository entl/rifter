package Game.Entity;

import java.awt.Image;

public class Entity
{
    public int worldX, worldY;
    public int speed;

    public Image up1, up2, down1, down2, left1, left2, right1, right2, standdown, standup, standleft, standright;
    //set base direction, otherwise null exception
    public String direction;
    public int spriteCounter = 0;
    public int spriteNum = 1;
}
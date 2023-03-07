package Game.Entity;

import Game.GamePanel;
import Game.KeyHandler;

import javax.swing.*;
import java.awt.*;

public class Player extends Entity
{
    GamePanel gamePanel;
    KeyHandler keyHandler;
    public final int screenX;
    public final int screenY;


    public Player(GamePanel gamePanel, KeyHandler keyHandler)
    {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;

        screenX = gamePanel.screenWidth/2;
        screenY = gamePanel.screenHeight/2;

        //set base value otherwise null exception
        this.direction = "down";
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues()
    {
        this.worldX = 100;
        this.worldY = 100;
        this.speed = 4;
    }

    public void getPlayerImage()
    {
        this.up1 = new ImageIcon("res\\player\\up1.png").getImage();
        this.up2 = new ImageIcon("res\\player\\up2.png").getImage();
        this.down1 = new ImageIcon("res\\player\\down1.png").getImage();
        this.down2 = new ImageIcon("res\\player\\down2.png").getImage();
        this.left1 = new ImageIcon("res\\player\\left1.png").getImage();
        this.left2 = new ImageIcon("res\\player\\left2.png").getImage();
        this.right1 = new ImageIcon("res\\player\\right1.png").getImage();
        this.right2 = new ImageIcon("res\\player\\right2.png").getImage();
        this.standdown = new ImageIcon("res\\player\\standdown.png").getImage();
        this.standup = new ImageIcon("res\\player\\standup.png").getImage();
        this.standleft = new ImageIcon("res\\player\\standleft.png").getImage();
        this.standright = new ImageIcon("res\\player\\standright.png").getImage();
    }

    public void update()
    {
        //make animation only when key is pressed
        if (keyHandler.wPressed || keyHandler.sPressed || keyHandler.aPressed || keyHandler.dPressed)
        {
            //based on pressed key we set direction
            //later based on this direction we render right image
            if (keyHandler.wPressed)
            {
                this.direction = "up";
                this.worldY -= this.speed;
            }
            else if (keyHandler.sPressed)
            {
                this.direction = "down";
                this.worldY += this.speed;
            }
            else if (keyHandler.aPressed)
            {
                this.direction = "left";
                this.worldX -= this.speed;
            }
            else if (keyHandler.dPressed)
            {
                this.direction = "right";
                this.worldX += this.speed;
            }
            this.spriteCounter++;
            //every 15 fps change sprite num.
            //in order look like our character walks
            if (this.spriteCounter > 15)
            {
                if (this.spriteNum == 1)
                {
                    this.spriteNum = 2;
                }
                else if (this.spriteNum == 2)
                {
                    this.spriteNum = 1;
                }
                this.spriteCounter = 0;
            }
        }
        //if no key pressed set direction to stand + side to make possible to render image
        else
        {
            switch (this.direction)
            {
                case "up" -> this.direction = "standup";
                case "down" -> this.direction = "standdown";
                case "left" -> this.direction = "standleft";
                case "right" -> this.direction = "standright";

            }
        }
    }
    public void draw(Graphics2D graphics2D)
    {

        Image image = null;

        switch (this.direction)
        {
            case "up" ->
            {
                if (this.spriteNum == 1)
                {
                    image = this.up1;
                }
                if (this.spriteNum == 2)
                {
                    image = this.up2;
                }
            }
            case "down" ->
            {
                if (this.spriteNum == 1)
                {
                    image = this.down1;
                }
                if (this.spriteNum == 2)
                {
                    image = this.down2;
                }
            }
            case "left" ->
            {
                if (this.spriteNum == 1)
                {
                    image = this.left1;
                }
                if (this.spriteNum == 2)
                {
                    image = this.left2;
                }
            }
            case "right" ->
            {
                if (this.spriteNum == 1)
                {
                    image = this.right1;
                }
                if (this.spriteNum == 2)
                {
                    image = this.right2;
                }
            }
            case "standdown" -> image = this.standdown;
            case "standup" -> image = this.standup;
            case "standleft" -> image = this.standleft;
            case "standright" -> image = this.standright;
        }
        graphics2D.drawImage(image, this.screenX, this.screenY, gamePanel.tileSize, gamePanel.tileSize, null);
    }
}

package Game.Entity;

import Database.Account;
import Database.QuestColumns;
import Database.Quests;
import Game.GamePanel;
import Game.KeyHandler;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Player extends Entity
{
    GamePanel gamePanel;
    KeyHandler keyHandler;
    Quests quests;
    Account user;
    public final int screenX;
    public final int screenY;


    public Player(GamePanel gamePanel, KeyHandler keyHandler, Quests quests, Account user)
    {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.quests = quests;
        this.user = user;

        screenX = gamePanel.screenWidth/2;
        screenY = gamePanel.screenHeight/2;

        //create rectangle smaller than player tile
        collisionArea = new Rectangle(8, 16, 32, 32);
        collisionAreaDefaultX = 8;
        collisionAreaDefaultY = 16;

        //set base direction otherwise null exception
        this.direction = "down";
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues()
    {
        this.worldX = 3000;
        this.worldY = 800;
        this.speed = 1;
    }

    public void getPlayerImage()
    {
        if (user.getSex().equals("m"))
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
        else
        {
            this.up1 = new ImageIcon("res\\player\\up1_girl.png").getImage();
            this.up2 = new ImageIcon("res\\player\\up2_girl.png").getImage();
            this.down1 = new ImageIcon("res\\player\\down1_girl.png").getImage();
            this.down2 = new ImageIcon("res\\player\\down2_girl.png").getImage();
            this.left1 = new ImageIcon("res\\player\\left1_girl.png").getImage();
            this.left2 = new ImageIcon("res\\player\\left2_girl.png").getImage();
            this.right1 = new ImageIcon("res\\player\\right1_girl.png").getImage();
            this.right2 = new ImageIcon("res\\player\\right2_girl.png").getImage();
            this.standdown = new ImageIcon("res\\player\\standdown_girl.png").getImage();
            this.standup = new ImageIcon("res\\player\\standup_girl.png").getImage();
            this.standleft = new ImageIcon("res\\player\\standleft_girl.png").getImage();
            this.standright = new ImageIcon("res\\player\\standright_girl.png").getImage();
        }
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
            }
            if (keyHandler.sPressed)
            {
                this.direction = "down";
            }
            if (keyHandler.aPressed)
            {
                this.direction = "left";
            }
            if (keyHandler.dPressed)
            {
                this.direction = "right";
            }

            //check collision with tiles
            this.collisionOn = false;
            this.gamePanel.collisionChecker.checkTile(this);

            //check object collision
            String objectIndex = gamePanel.collisionChecker.checkObject(this, true);
            try
            {
                if (!objectIndex.equals(""))
                {
                    ArrayList<HashMap<String, Object>> userQuests = this.quests.getQuests();
                    for (HashMap<String, Object> quest:userQuests)
                    {
                        if (quest.get(QuestColumns.QUEST_ID.name()).equals(objectIndex))
                        {
                            System.out.printf("[+] %s\n",quest.get(QuestColumns.DESCRIPTION.name()));
                            System.out.printf("[+] You have earned %s and now your balance is %s\n", quest.get(QuestColumns.PRIZE.name()), this.user.getBalance()+ (Float) quest.get(QuestColumns.PRIZE.name()));
                        }
                    }
                    this.quests.addQuestCompleted(objectIndex, this.user);
                    gamePanel.objects[Integer.parseInt(objectIndex)-1].worldY = -500;
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

            //if no collision let player move
            if(!this.collisionOn)
            {
                switch (direction)
                {
                    case "up" -> this.worldY -= this.speed;
                    case "down" -> this.worldY += this.speed;
                    case "left" -> this.worldX -= this.speed;
                    case "right" -> this.worldX += this.speed;
                }
            }

            this.spriteCounter++;
            //every 30 fps change sprite num.
            //in order to look like our character walks
            if (this.spriteCounter > 30)
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
        //if no key pressed set standing image based on last direction
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
        //draw walking animation
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
        //draw image of player that was chosen in switch case block
        graphics2D.drawImage(image, this.screenX, this.screenY, gamePanel.tileSize, gamePanel.tileSize, null);
    }
}

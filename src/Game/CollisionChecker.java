//locate in game package
package Game;

//import local class

import Game.Entity.Entity;

public class CollisionChecker
{
    GamePanel gamePanel;

    //constructor
    public CollisionChecker(GamePanel gamePanel)
    {
        this.gamePanel = gamePanel;
    }

    /**
     * Check the collision with the tiles
     *
     * @param entity instance of the Entity class used to get player's position
     */
    public void checkTile(Entity entity)
    {
        //here we get pixels of the entity
        int entityLeftWorldX = entity.worldX + entity.collisionArea.x;
        int entityRightWorldX = entity.worldX + entity.collisionArea.x + entity.collisionArea.width;
        int entityTopWorldY = entity.worldY + entity.collisionArea.y;
        int entityBottomWorldY = entity.worldY + entity.collisionArea.y + entity.collisionArea.height;

        //transform pixels to columns
        int entityLeftColumn = entityLeftWorldX / gamePanel.tileSize;
        int entityRightColumn = entityRightWorldX / gamePanel.tileSize;
        int entityTopRow = entityTopWorldY / gamePanel.tileSize;
        int entityBottomRow = entityBottomWorldY / gamePanel.tileSize;

        //tilenum represent edges of the entity. for instance if we want to check the collision on top.
        //we need to check top left and top right corner
        String tileNum1, tileNum2;

        switch (entity.direction)
        {
            case "up" ->
            {
                //entityTopRow is a prediction of the next position, that's why we subtract speed.
                entityTopRow = (entityTopWorldY - entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapObjectsNum[entityTopRow][entityLeftColumn];
                tileNum2 = gamePanel.tileManager.mapObjectsNum[entityTopRow][entityRightColumn];
                //if any of entity corners collided
                if (gamePanel.tileManager.objects.get(tileNum1).collision || gamePanel.tileManager.objects.get(tileNum2).collision)
                {
                    //set collision to true
                    entity.collisionOn = true;
                }
            }
            case "down" ->
            {
                entityBottomRow = (entityBottomWorldY + entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapObjectsNum[entityBottomRow][entityLeftColumn];
                tileNum2 = gamePanel.tileManager.mapObjectsNum[entityBottomRow][entityRightColumn];
                if (gamePanel.tileManager.objects.get(tileNum1).collision || gamePanel.tileManager.objects.get(tileNum2).collision)
                {
                    entity.collisionOn = true;
                }
            }
            case "left" ->
            {
                entityLeftColumn = (entityLeftWorldX - entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapObjectsNum[entityTopRow][entityLeftColumn];
                tileNum2 = gamePanel.tileManager.mapObjectsNum[entityBottomRow][entityLeftColumn];
                if (gamePanel.tileManager.objects.get(tileNum1).collision || gamePanel.tileManager.objects.get(tileNum2).collision)
                {
                    entity.collisionOn = true;
                }
            }
            case "right" ->
            {
                entityRightColumn = (entityRightWorldX + entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapObjectsNum[entityTopRow][entityRightColumn];
                tileNum2 = gamePanel.tileManager.mapObjectsNum[entityBottomRow][entityRightColumn];
                if (gamePanel.tileManager.objects.get(tileNum1).collision || gamePanel.tileManager.objects.get(tileNum2).collision)
                {
                    entity.collisionOn = true;
                }
            }
        }
    }

    /**
     * Check the collision with the objects like quests
     *
     * @param entity instance of the Entity class used to get player's position
     * @return id of the quest later based on it, we will have interaction with the object
     */
    public String checkObject(Entity entity)
    {
        String id = "";

        for (int i = 0; i < gamePanel.objects.length; i++)
        {
            if (gamePanel.objects[i] != null)
            {
                //get entity collision area in world dimension (not screen one)
                entity.collisionArea.x = entity.worldX + entity.collisionArea.x;
                entity.collisionArea.y = entity.worldY + entity.collisionArea.y;
                //get entity object position in world dimension (not screen one)
                gamePanel.objects[i].collisionArea.x = gamePanel.objects[i].worldX + gamePanel.objects[i].collisionArea.x;
                gamePanel.objects[i].collisionArea.y = gamePanel.objects[i].worldY + gamePanel.objects[i].collisionArea.y;

                switch (entity.direction)
                {
                    case "up" ->
                    {
                        //predict next position
                        entity.collisionArea.y -= entity.speed;
                        //if player position overlaps object area it means that they have collided
                        if (entity.collisionArea.intersects(gamePanel.objects[i].collisionArea))
                        {
                            //check whether object has collision (can we go through object or not)
                            if (gamePanel.objects[i].collision)
                            {
                                entity.collisionOn = true;
                            }
                            //set id of the object
                            id = gamePanel.objects[i].id;
                        }
                    }
                    case "down" ->
                    {
                        entity.collisionArea.y += entity.speed;
                        if (entity.collisionArea.intersects(gamePanel.objects[i].collisionArea))
                        {
                            if (gamePanel.objects[i].collision)
                            {
                                entity.collisionOn = true;
                            }
                            id = gamePanel.objects[i].id;
                        }
                    }
                    case "left" ->
                    {
                        entity.collisionArea.x -= entity.speed;
                        if (entity.collisionArea.intersects(gamePanel.objects[i].collisionArea))
                        {
                            if (gamePanel.objects[i].collision)
                            {
                                entity.collisionOn = true;
                            }
                            id = gamePanel.objects[i].id;
                        }
                    }
                    case "right" ->
                    {
                        entity.collisionArea.x += entity.speed;
                        if (entity.collisionArea.intersects(gamePanel.objects[i].collisionArea))
                        {
                            if (gamePanel.objects[i].collision)
                            {
                                entity.collisionOn = true;
                            }
                            id = gamePanel.objects[i].id;
                        }
                    }
                }
                //reset values
                entity.collisionArea.x = entity.collisionAreaDefaultX;
                entity.collisionArea.y = entity.collisionAreaDefaultY;
                gamePanel.objects[i].collisionArea.x = gamePanel.objects[i].collisionAreaDefaultX;
                gamePanel.objects[i].collisionArea.y = gamePanel.objects[i].collisionAreaDefaultY;
            }
        }
        return id;
    }
}

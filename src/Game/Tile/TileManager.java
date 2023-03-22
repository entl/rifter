package Game.Tile;

import Game.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class TileManager
{
    GamePanel gamePanel;
//    Tile[] tile;
    HashMap<String, Tile> tile = new HashMap<>();
    public HashMap<String, Tile> objects = new HashMap<>();
    public HashMap<String, Tile> objectsNoCollision = new HashMap<>();
    String[][] mapTileNum;
    public String[][] mapObjectsNum;
    String[][] mapObjectsNoCollisionNum;


    public TileManager(GamePanel gamePanel)
    {
        this.gamePanel = gamePanel;

        mapTileNum = new String[gamePanel.maxWorldRow][gamePanel.maxWorldColumn];
        mapObjectsNum = new String[gamePanel.maxWorldRow][gamePanel.maxWorldColumn];
        mapObjectsNoCollisionNum = new String[gamePanel.maxWorldRow][gamePanel.maxWorldColumn];

        loadMap();
        loadTileImage();
        loadObjects();
        loadObjectsImage();
        loadObjectsNoCollisions();
        loadObjectsNoCollisionImage();
    }

    public void loadTileImage()
    {
        try
        {
            BufferedImage fullSet = ImageIO.read(new File("res\\tile\\CP_NEW2.png"));
            for (int i = 0; i < gamePanel.maxWorldRow; i++)
                for (int j = 0; j < gamePanel.maxWorldColumn; j++)
                {
                    Tile tempTile = new Tile();
//                    System.out.println(Integer.parseInt(mapTileNum[i][j]) + "-"+Integer.parseInt(mapTileNum[i][j])%64*16 + ":" + Integer.parseInt(mapTileNum[i][j])/64*16);
                    tempTile.image = fullSet.getSubimage(Integer.parseInt(mapTileNum[i][j])%64*16, Integer.parseInt(mapTileNum[i][j])/64*16, 16, 16);
                    this.tile.put(mapTileNum[i][j], tempTile);
                }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void loadObjectsNoCollisionImage()
    {
        try
        {
            BufferedImage fullSet = ImageIO.read(new File("res\\tile\\CP_NEW2.png"));
            for (int i = 0; i < gamePanel.maxWorldRow; i++)
                for (int j = 0; j < gamePanel.maxWorldColumn; j++)
                {
                    Tile tempTile = new Tile();
                    if (mapObjectsNoCollisionNum[i][j].equals("-1"))
                    {
                        tempTile.image = fullSet.getSubimage(16, 16, 16, 16);
                        this.objectsNoCollision.put(mapObjectsNoCollisionNum[i][j], tempTile);
                        continue;
                    }
                    tempTile.image = fullSet.getSubimage(Integer.parseInt(mapObjectsNoCollisionNum[i][j])%64*16, Integer.parseInt(mapObjectsNoCollisionNum[i][j])/64*16, 16, 16);
                    this.objectsNoCollision.put(mapObjectsNoCollisionNum[i][j], tempTile);
                }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void loadObjectsImage()
    {
        try
        {
            BufferedImage fullSet = ImageIO.read(new File("res\\tile\\CP_NEW2.png"));
            for (int i = 0; i < gamePanel.maxWorldRow; i++)
                for (int j = 0; j < gamePanel.maxWorldColumn; j++)
                {
                    Tile tempTile = new Tile();
                    if (mapObjectsNum[i][j].equals("-1"))
                    {
                        tempTile.image = fullSet.getSubimage(16, 16, 16, 16);
                        this.objects.put(mapObjectsNum[i][j], tempTile);
                        continue;
                    }
                    tempTile.image = fullSet.getSubimage(Integer.parseInt(mapObjectsNum[i][j])%64*16, Integer.parseInt(mapObjectsNum[i][j])/64*16, 16, 16);
                    tempTile.collision = true;
                    this.objects.put(mapObjectsNum[i][j], tempTile);
                }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void loadMap()
    {
        try
        {
            File fileMap = new File("res\\map\\map2_Tile.csv");
            Scanner scannerMap = new Scanner(fileMap);

            int i = 0;
            while (scannerMap.hasNext())
            {
                String[] line = scannerMap.nextLine().split(",");
                mapTileNum[i] = line;
                i++;
            }
            scannerMap.close();
        }
        catch (FileNotFoundException e)
        {

        }
    }

    public void loadObjectsNoCollisions()
    {
        try
        {
            File fileMap = new File("res\\map\\map2_Objects_No_Collision.csv");
            Scanner scannerMap = new Scanner(fileMap);

            int i = 0;
            while (scannerMap.hasNext())
            {
                String[] line = scannerMap.nextLine().split(",");
                mapObjectsNoCollisionNum[i] = line;
                i++;
            }
            scannerMap.close();
        }
        catch (FileNotFoundException e)
        {

        }
    }

    public void loadObjects()
    {
        try
        {
            File fileMap = new File("res\\map\\map2_Objects.csv");
            Scanner scannerMap = new Scanner(fileMap);

            int i = 0;
            while (scannerMap.hasNext())
            {
                String[] line = scannerMap.nextLine().split(",");
                mapObjectsNum[i] = line;
                i++;
            }
            scannerMap.close();
        }
        catch (FileNotFoundException e)
        {

        }
    }

    public void draw(Graphics2D graphics2D)
    {
//      world camera
//      https://www.youtube.com/watch?v=Ny_YHoTYcxo&list=PL_QPQmz5C6WUF-pOQDsbsKbaBZqXj4qSq&index=5
        int worldColumn = 0, worldRow = 0;
        while (worldColumn < gamePanel.maxWorldColumn && worldRow < gamePanel.maxWorldRow)
        {
            String tileId = mapTileNum[worldRow][worldColumn];
            String objectId = mapObjectsNum[worldRow][worldColumn];
            String objectNoCollisionId = mapObjectsNoCollisionNum[worldRow][worldColumn];

            int worldX = worldColumn * gamePanel.tileSize;
            int worldY = worldRow * gamePanel.tileSize;
            int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
            int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

            graphics2D.drawImage(tile.get(tileId).image, screenX,screenY,gamePanel.tileSize, gamePanel.tileSize, null);
            graphics2D.drawImage(objects.get(objectId).image, screenX,screenY,gamePanel.tileSize, gamePanel.tileSize, null);
            graphics2D.drawImage(objectsNoCollision.get(objectNoCollisionId).image, screenX,screenY,gamePanel.tileSize, gamePanel.tileSize, null);
            worldColumn++;

            if (worldColumn == gamePanel.maxWorldColumn)
            {
                worldColumn = 0;
                worldRow++;
            }
        }
    }

}

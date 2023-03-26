//locate in Game\Tile
package Game.Tile;

//import local class

import Game.GamePanel;

//import java classes
import javax.imageio.ImageIO; //used to read image
import java.awt.*; //used to store image
import java.awt.image.BufferedImage; //used to store image in another way
import java.io.*; //used to operate with files
import java.util.HashMap;
import java.util.Scanner;

public class TileManager
{
    GamePanel gamePanel;
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

    /**
     * Loads tile based on it location in the tileset
     * Overall used to load first layer of map
     */
    public void loadTileImage()
    {
        try
        {
            BufferedImage fullSet = ImageIO.read(new File(TileLocation.TILESET));
            for (int i = 0; i < gamePanel.maxWorldRow; i++)
                for (int j = 0; j < gamePanel.maxWorldColumn; j++)
                {
                    Tile tempTile = new Tile();
                    tempTile.image = fullSet.getSubimage(Integer.parseInt(mapTileNum[i][j]) % 64 * 16, Integer.parseInt(mapTileNum[i][j]) / 64 * 16, 16, 16);
                    this.tile.put(mapTileNum[i][j], tempTile);
                }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads objects based on it location in the tileset
     * without collision area such as flowers, signs
     */
    public void loadObjectsNoCollisionImage()
    {
        try
        {
            BufferedImage fullSet = ImageIO.read(new File(TileLocation.TILESET));
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
                    tempTile.image = fullSet.getSubimage(Integer.parseInt(mapObjectsNoCollisionNum[i][j]) % 64 * 16, Integer.parseInt(mapObjectsNoCollisionNum[i][j]) / 64 * 16, 16, 16);
                    this.objectsNoCollision.put(mapObjectsNoCollisionNum[i][j], tempTile);
                }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads object which player cannot go through
     * Such as buildings, cars, trees
     */
    public void loadObjectsImage()
    {
        try
        {
            BufferedImage fullSet = ImageIO.read(new File(TileLocation.TILESET));
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
                    tempTile.image = fullSet.getSubimage(Integer.parseInt(mapObjectsNum[i][j]) % 64 * 16, Integer.parseInt(mapObjectsNum[i][j]) / 64 * 16, 16, 16);
                    tempTile.collision = true;
                    this.objects.put(mapObjectsNum[i][j], tempTile);
                }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads map file, which stores id of the tile and saves it to the array
     */
    public void loadMap()
    {
        try
        {
            File fileMap = new File(TileLocation.MAP_TILES);
            Scanner scannerMap = new Scanner(fileMap);

            int i = 0;
            while (scannerMap.hasNext())
            {
                String[] line = scannerMap.nextLine().split(",");
                mapTileNum[i] = line;
                i++;
            }
            scannerMap.close();
        } catch (FileNotFoundException e)
        {
            System.out.println("[!] Error in map loading");
        }
    }

    /**
     * Reads map_objects_no_collision file, which stores id of the tile and saves it to the array
     */
    public void loadObjectsNoCollisions()
    {
        try
        {
            File fileMap = new File(TileLocation.MAP_OBJECTS_NO_COLLISION);
            Scanner scannerMap = new Scanner(fileMap);

            int i = 0;
            while (scannerMap.hasNext())
            {
                String[] line = scannerMap.nextLine().split(",");
                mapObjectsNoCollisionNum[i] = line;
                i++;
            }
            scannerMap.close();
        } catch (FileNotFoundException e)
        {
            System.out.println("[!] Error in map loading");
        }
    }

    /**
     * Reads map_objects file, which stores id of the tile and saves it to the array
     */
    public void loadObjects()
    {
        try
        {
            File fileMap = new File(TileLocation.MAP_OBJECTS);
            Scanner scannerMap = new Scanner(fileMap);

            int i = 0;
            while (scannerMap.hasNext())
            {
                String[] line = scannerMap.nextLine().split(",");
                mapObjectsNum[i] = line;
                i++;
            }
            scannerMap.close();
        } catch (FileNotFoundException e)
        {
            System.out.println("[!] Error in map loading");
        }
    }

    /**
     * Method is used to draw world around the player
     * Camera follows player
     *
     * @param graphics2D used to draw the map
     */
    public void draw(Graphics2D graphics2D)
    {
//      world camera
//      https://www.youtube.com/watch?v=Ny_YHoTYcxo&list=PL_QPQmz5C6WUF-pOQDsbsKbaBZqXj4qSq&index=5
        int worldColumn = 0, worldRow = 0;
        //iterate through all columns and rows of the map
        while (worldColumn < gamePanel.maxWorldColumn && worldRow < gamePanel.maxWorldRow)
        {
            String tileId = mapTileNum[worldRow][worldColumn];
            String objectId = mapObjectsNum[worldRow][worldColumn];
            String objectNoCollisionId = mapObjectsNoCollisionNum[worldRow][worldColumn];

            //get x,y of the world based on the columns and rows
            int worldX = worldColumn * gamePanel.tileSize;
            int worldY = worldRow * gamePanel.tileSize;
            //calculate the position of the tile based on the player position
            //for instance if user have position 500 and tile 0
            //to make camera follow player we will set tile position to -500
            //and when player will come closer to the tile player x = 300
            //tile position will become -300
            int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
            int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

            //draws tile
            graphics2D.drawImage(tile.get(tileId).image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
            //draw objects
            graphics2D.drawImage(objects.get(objectId).image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
            //draws objects without collision
            graphics2D.drawImage(objectsNoCollision.get(objectNoCollisionId).image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
            worldColumn++;

            if (worldColumn == gamePanel.maxWorldColumn)
            {
                worldColumn = 0;
                worldRow++;
            }
        }
    }

}

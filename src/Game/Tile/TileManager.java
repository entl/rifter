package Game.Tile;

import Game.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class TileManager
{
    GamePanel gamePanel;
//    Tile[] tile;
    HashMap<String, Tile> tile = new HashMap<>();
    String[][] mapTileNum;

    public TileManager(GamePanel gamePanel)
    {
        this.gamePanel = gamePanel;

        mapTileNum = new String[gamePanel.maxWorldRow][gamePanel.maxWorldColumn];

        loadMap();
        loadTileImage();
    }

    public void loadTileImage()
    {
        try
        {
            BufferedImage fullSet = ImageIO.read(new File("res\\tile\\CP_V1.0.4.png"));
            for (int i = 0; i < gamePanel.maxWorldRow; i++)
                for (int j = 0; j < gamePanel.maxWorldColumn; j++)
                {
                    Tile tempTile = new Tile();
                    tempTile.image = fullSet.getSubimage(Integer.parseInt(mapTileNum[i][j])%64*16, Integer.parseInt(mapTileNum[i][j])/64*16, 16, 16);
                    this.tile.put(mapTileNum[i][j], tempTile);
                }
//            this.tile[0] = new Tile();
//            this.tile[0].image = new ImageIcon("res\\tile\\pavement.png").getImage();
//            this.tile[1] = new Tile();
//            this.tile[1].image = fullSet.getSubimage(2581%64*16, 2581/64*16, 16, 16);
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
            File fileMap = new File("res\\map\\map.csv");
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

    public void draw(Graphics2D graphics2D)
    {
//      world camera
//      https://www.youtube.com/watch?v=Ny_YHoTYcxo&list=PL_QPQmz5C6WUF-pOQDsbsKbaBZqXj4qSq&index=5
        int worldColumn = 0, worldRow = 0;
        while (worldColumn < gamePanel.maxWorldColumn && worldRow < gamePanel.maxWorldRow)
        {
            String tileId = mapTileNum[worldRow][worldColumn];

            int worldX = worldColumn * gamePanel.tileSize;
            int worldY = worldRow * gamePanel.tileSize;
            int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
            int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

            graphics2D.drawImage(tile.get(tileId).image, screenX,screenY,gamePanel.tileSize, gamePanel.tileSize, null);
            worldColumn++;

            if (worldColumn == gamePanel.maxWorldColumn)
            {
                worldColumn = 0;
                worldRow++;
            }
        }
    }

}

//locate in game package
package Game;

//import local classes

import Database.QuestColumns;
import Database.Quests;
import Game.Object.ObjectQuest;

//import java classes
import java.util.ArrayList;
import java.util.HashMap;

public class AssetSetter
{
    GamePanel gamePanel;

    public AssetSetter(GamePanel gamePanel)
    {
        this.gamePanel = gamePanel;
    }

    /**
     * Load quests properties to object array of game panel class
     */
    public void setObject(Quests questsObject)
    {
        ArrayList<HashMap<String, Object>> quests = questsObject.getQuests();
        for (int i = 0; i < quests.size(); i++)
        {
            gamePanel.objects[i] = new ObjectQuest();
            gamePanel.objects[i].worldX = Integer.parseInt((String) quests.get(i).get(QuestColumns.LOCATIONX.name())) * gamePanel.tileSize;
            gamePanel.objects[i].worldY = Integer.parseInt((String) quests.get(i).get(QuestColumns.LOCATIONY.name())) * gamePanel.tileSize;
            gamePanel.objects[i].id = (String) quests.get(i).get(QuestColumns.QUEST_ID.name());
        }
    }
}

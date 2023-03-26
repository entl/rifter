package Game.Object;

import javax.swing.*;

public class ObjectQuest extends SuperObject
{
    public ObjectQuest()
    {
        this.name = "quest";
        this.image = new ImageIcon("res\\quests\\questionmark.png").getImage();
        collision = false;
    }
}

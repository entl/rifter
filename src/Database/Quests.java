package Database;

import java.io.*;
import java.util.*;

public class Quests
{
    private final File userQuestFile;
    private final File questFile;
    private String userId;

    private ArrayList<HashMap<String, Object>> quests = new ArrayList<>();

    public Quests(String questFilename, String userQuestFilename, String userId) throws FileNotFoundException
    {
        this.userQuestFile = new File(userQuestFilename);
        this.questFile = new File(questFilename);
        this.userId = userId;
        loadQuests();
    }

    private void loadQuests() throws FileNotFoundException
    {
        List<String> completedQuests = new ArrayList<>();

        Scanner scannerQuest = new Scanner(this.questFile);
        //explicitly set delimiter to avoid problems with spaces
        scannerQuest.useDelimiter("\n");

        Scanner scannerUserQuest = new Scanner(this.userQuestFile);
        //explicitly set delimiter to avoid problems with spaces
        scannerUserQuest.useDelimiter("\n");

        //skip first line
        scannerUserQuest.next();
        while(scannerUserQuest.hasNext())
        {
            String currentLine = scannerUserQuest.next();
            String[] userQuestRecord = currentLine.split(",");

            if (userQuestRecord[UserQuestsColumns.USER_ID.value].equals(this.userId))
            {
                //we need to replace \r because it is the last value in row, so it have return
                completedQuests.add(userQuestRecord[UserQuestsColumns.QUEST_ID.value].replace("\r", ""));
            }
        }
        scannerUserQuest.close();

        //skip first line
        scannerQuest.next();
        while (scannerQuest.hasNext())
        {
            String currentLine = scannerQuest.next();
            String[] questRecord = currentLine.split(",");

            if (!completedQuests.contains(questRecord[QuestColumns.QUEST_ID.value]))
            {
                HashMap <String, Object> tempHashmap = new HashMap<>();
                tempHashmap.put("questId", questRecord[QuestColumns.QUEST_ID.value]);
                tempHashmap.put("location", questRecord[QuestColumns.LOCATION.value]);
                tempHashmap.put("description", questRecord[QuestColumns.DESCRIPTION.value]);
                tempHashmap.put("prize", Float.parseFloat(questRecord[QuestColumns.PRIZE.value]));
                this.quests.add(tempHashmap);
            }
        }
        scannerQuest.close();

    }

    public void addQuestCompleted(String questId, Account user) throws IOException, InterruptedException
    {

        for (HashMap<String, Object> quest:this.quests)
        {
            if (quest.get("questId").equals(questId))
            {
                user.addBalance((Float) quest.get("prize"));
                user.addScore((Float) quest.get("prize"));
                break;
            }
        }
        user.updateCredentials();

        FileWriter writerUserQuest = new FileWriter(this.userQuestFile, true);

        writerUserQuest.write(String.format("%s,%s\n", this.userId, questId));
        writerUserQuest.close();
        //we need to clear array list in order to get rid of old values
        this.quests.clear();
        //call load quests to update quests arraylist
        loadQuests();
    }
    public ArrayList<HashMap<String, Object>> getQuests()
    {
        return quests;
    }
}

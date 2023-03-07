package Database;

import java.io.File; //used to open file
import java.io.FileNotFoundException; //exception for file opening
import java.io.FileWriter; //used to write into a file
import java.io.IOException; // exception for writing
import java.util.HashMap; //used to store quests as key:value
import java.util.ArrayList; //used to make flexible list of quests
import java.util.Scanner; //used to read from file

public class Quests
{
    private final File userQuestFile;
    private final File questFile;
    private Account user;

    private ArrayList<HashMap<String, Object>> quests = new ArrayList<>();

    //constructor for quests class. pass instance of user to access its attributes and methods
    public Quests(String questFilename, String userQuestFilename, Account user)
    {
        this.userQuestFile = new File(userQuestFilename);
        this.questFile = new File(questFilename);
        this.user = user;

        try
        {
            loadQuests();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("[!] Error in loading quests. " + e);
        }
    }

    private void loadQuests() throws FileNotFoundException
    {
        ArrayList<String> completedQuests = new ArrayList<>();

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

            //if id in user quests file coincides with current user id we add this quest to completed
            if (userQuestRecord[UserQuestsColumns.USER_ID.value].equals(this.user.getUserId()))
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

            //if array of completed quests does not contain quest_id from quests file
            //add this quest to complete
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

    //this method adds quest to the file user_quest as completed
    //and add prize to the balance
    public void addQuestCompleted(String questId, Account user) throws IOException
    {
        //iterate over each hashmap in array
        for (HashMap<String, Object> quest:this.quests)
        {
            //get exact quest and add its prize to balance and score
            if (quest.get("questId").equals(questId))
            {
                user.addBalance((Float) quest.get("prize"));
                user.addScore((Float) quest.get("prize"));
                //we need to update balance and score in the file
                user.updateCredentials();
                break;
            }
        }

        //add completed quest to the user quest file
        FileWriter writerUserQuest = new FileWriter(this.userQuestFile, true);

        writerUserQuest.write(String.format("%s,%s\n", this.user.getUserId(), questId));
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

package Database;

public enum QuestColumns
{
    QUEST_ID(0),
    LOCATION(1),
    DESCRIPTION(2),
    PRIZE(3);

    public final int value;

    private QuestColumns(int value){this.value = value;}
}

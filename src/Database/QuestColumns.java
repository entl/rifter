package Database;

public enum QuestColumns
{
    QUEST_ID(0),
    LOCATIONX(1),
    LOCATIONY(2),
    NAME(3),
    DESCRIPTION(4),
    PRIZE(5);

    public final int value;

    private QuestColumns(int value){this.value = value;}
}

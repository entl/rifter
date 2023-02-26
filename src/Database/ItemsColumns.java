package Database;

public enum ItemsColumns
{
    ITEM_ID(0),
    COMPANY_NAME(1),
    NAME(2),
    PRICE(3),
    STOCK(4),
    DESCRIPTION(5);

    public final int value;

    private ItemsColumns(int value){this.value = value;}
}

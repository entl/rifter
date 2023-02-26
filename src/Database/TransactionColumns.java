package Database;

public enum TransactionColumns
{
    TRANSACTION_ID(0),
    USER_ID(1),
    TRANSACTION_TYPE(2),
    ITEM_ID(3),
    QUANTITY(4),
    AMOUNT(5),
    TRANSACTION_DATE(6);

    public final int value;

    private TransactionColumns(int value){this.value = value;}
}

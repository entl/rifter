package Database;

public enum TransactionType
{
    TOP_UP("top up"),
    BUY("buy");

    public final String value;

    private TransactionType(String value){this.value = value;}
}

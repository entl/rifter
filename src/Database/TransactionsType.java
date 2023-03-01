package Database;

public enum TransactionsType
{
    TOP_UP("top up"),
    BUY("buy");

    public final String value;

    private TransactionsType(String value){this.value = value;}
}

//https://www.baeldung.com/java-enum-values
public enum AccountColumns
{
    USER_ID(0),
    EMAIL(1),
    USERNAME(2),
    PASSWORD(3),
    PAYMENT_DETAILS(4),
    BALANCE(5),
    SCORE(6);

    public final int value;

    private AccountColumns(int value)
    {
        this.value = value;
    }
}

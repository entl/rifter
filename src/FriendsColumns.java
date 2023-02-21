//https://www.baeldung.com/java-enum-values
public enum FriendsColumns
{
    USER_ID(0),
    FRIEND_ID(1),
    STATUS(2);

    public final int value;

    private FriendsColumns(int value){this.value = value;}
}

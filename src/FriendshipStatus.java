public enum FriendshipStatus
{
    ACCEPTED("accepted"),
    PENDING("pending");

    public final String value;

    private FriendshipStatus(String value){this.value = value;}
}

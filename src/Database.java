import java.io.*;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Arrays;


public class Database
{
    protected static final String ACCOUNTS_DB = "accounts.csv";
    public static void main(String[] args) throws FileNotFoundException
    {
        //create file ACCOUNTS_DB if it is not exist
        try
        {
            File file = new File(ACCOUNTS_DB);
            file.createNewFile();
        }
        catch (IOException e){
            System.out.println("Error: " + e);
        }
        Account user = new Account(ACCOUNTS_DB);
        user.setUsername("notEntl");
        user.loadAccount();
        System.out.println(user.getUserId());
        System.out.println(user.getEmail());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getPaymentDetails().get("holderName"));
        System.out.println(user.getBalance());
    }

}

class Account
{
    //filename of the database with accounts
    private String filename;

    //class attributes represent columns in database
    //they have private modifiers because we will access them by using getters and setters
    private int userId;
    private String email;
    private String username;
    private String password;

    //use hashmap in order to store card details more convenient
    private HashMap<String, String> paymentDetails = new HashMap<>();

    private float balance;

    //we need constructor to make possible pass filename when creating an instance
    protected Account(String filename)
    {
        this.filename = filename;
    }

    //getters and setters to make program more secure
    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public HashMap<String, String> getPaymentDetails()
    {
        return paymentDetails;
    }

    public void setPaymentDetails(HashMap<String, String> paymentDetails)
    {
        this.paymentDetails = paymentDetails;
    }

    public float getBalance()
    {
        return balance;
    }

    public void setBalance(float balance)
    {
        this.balance = balance;
    }

    //this function loads account details from database
    protected void loadAccount() throws FileNotFoundException
    {
        File accounts = new File(this.filename);
        Scanner sc = new Scanner(accounts);
        //explicitly set delimiter to '\n' to make possible to store cardholder name with space
        sc.useDelimiter("\n");
        while (sc.hasNext())
        {
            //get values from row
            String[] Account = sc.next().split(",");
            //check whether row has appropriate username
            if (Arrays.toString(Account).contains(this.username))
            {
                this.userId = Integer.parseInt(Account[0]);
                this.email = Account[1];
                this.username = Account[2];
                this.password = Account[3];
                //check if payment details saved
                if (!Account[4].equals("Null"))
                {
                    String[] paymentDetails = Account[4].split("-");
                    this.paymentDetails.put("cardNumber", paymentDetails[0]);
                    this.paymentDetails.put("cardValid", paymentDetails[1]);
                    this.paymentDetails.put("holderName", paymentDetails[2]);
                }
                this.balance = Float.parseFloat(Account[5]);
                break;
            }
        }
        sc.close();
    }

}
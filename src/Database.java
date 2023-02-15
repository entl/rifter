import java.io.*;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDateTime;


public class Database
{
    protected static final String ACCOUNTS_DB = "accounts.csv";

    public static void main(String[] args) throws FileNotFoundException
    {

//        //create file ACCOUNTS_DB if it is not exist
//        try
//        {
//            File file = new File(ACCOUNTS_DB);
//            file.createNewFile();
//        } catch (IOException e)
//        {
//            System.out.println("Error: " + e);
//        }
//        HashMap <String, String> paymentsDetails = new HashMap<String, String>();
//        paymentsDetails.put("cardNumber", "1111222233334444");
//        paymentsDetails.put("cardDate", "04/22");
//        paymentsDetails.put("holderName", "Vorobyov Maksym");
        Account user = new Account(ACCOUNTS_DB);
        user.setPassword("12345678");
        System.out.println(user.getPassword());
//        user.setPaymentDetails(paymentsDetails);
//        user.setUsername("notEntl");
//        user.loadAccountFromFile();
//        user.setEmail("test@test.com");
//        System.out.println(user.getUserId());
//        System.out.println(user.getEmail());
//        System.out.println(user.getUsername());
//        System.out.println(user.getPassword());
//        System.out.println(user.getPaymentDetails().get("cardNumber"));
//        System.out.println(user.getPaymentDetails().get("cardDate"));
//        System.out.println(user.getPaymentDetails().get("holderName"));
//        System.out.println(user.getBalance());
//        System.out.println(Account.getNumberOfUsersFromFile(ACCOUNTS_DB));
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

    public void setUserId()
    {
        try
        {
            int lastId = getNumberOfUsersFromFile(this.filename);
            this.userId = lastId+1;
        }
        catch (FileNotFoundException e)
        {
            System.out.println("[!] Error: " + e);
        }
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        try
        {
            if (validateEmail(email))
            {
                this.email = email;
            }
        }
        catch (FileNotFoundException e){
            System.out.println("[!] Error: " + e);
        }

    }

    private boolean validateEmail(String email) throws FileNotFoundException
    {
        String emailRegex = "^[A-Za-z0-9-_.]+@[A-Za-z0-9-_.]+$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(email);

        //we need to check if email address is free to use
        File accounts = new File(this.filename);
        Scanner sc = new Scanner(accounts);
        //explicitly set delimiter to '\n' so cardholder name with space does not stop the scanner
        sc.useDelimiter("\n");
        while (sc.hasNext())
        {
            //get values from row
            String[] Account = sc.next().split(",");
            String emailFromDatabase = Account[1];
            if(email.equals(emailFromDatabase))
            {
                System.out.println("[-] Sorry, this email is already taken");
                return false;
            }
        }
        sc.close();

        if (!emailMatcher.matches())
        {
            System.out.println("[-] Unfortunately, email is not valid");
            return false;
        }
        return true;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        if(!username.contains(","))
        {
            this.username = username;
        }
        else
        {
            System.out.println("[-] Please enter username without ','");
        }
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        if (validatePassword(password))
        {
            //hash password in order to store it securely
            int hashPassword = password.hashCode();
            this.password = String.valueOf(hashPassword);
        }
    }

    private boolean validatePassword(String password)
    {
        if (password.length() >= 8)
        {
            return true;
        }
        else
        {
            System.out.println("[-] Password must be at least 8 characters long");
            return false;
        }
    }

    public HashMap<String, String> getPaymentDetails()
    {
        return paymentDetails;
    }

    public void setPaymentDetails(HashMap<String, String> paymentDetails)
    {
        //set payment details if all tests are passed
        if (validatePaymentDetails(paymentDetails))
        {
            this.paymentDetails = paymentDetails;
        }
    }

    private boolean validatePaymentDetails(HashMap<String, String> paymentDetails)
    {
        //get current date in order to verify card
        LocalDateTime now = LocalDateTime.now();
        // %100 used to get last 2 digits of the year
        int currentYear = now.getYear()%100;
        int currentMonth = now.getMonthValue();


        String cardNumber = paymentDetails.get("cardNumber");
        //split card date by '/' to get month and year
        String[] cardDate = paymentDetails.get("cardDate").split("/");


        //basic regex for card number
        String cardNumberRegex = "^[0-9]{16}$";
        Pattern cardNumberPattern = Pattern.compile(cardNumberRegex);

        //check whether cardNumber matches regular expression
        if(!cardNumberPattern.matcher(cardNumber).matches())
        {
            System.out.println("[-] Make sure you have inputted correct card details");
            return false;
        }
        //check if card expired
        if (Integer.parseInt(cardDate[0]) < currentMonth)
        {
            System.out.println("[-] Card has expired");
            return false;
        }
        //check if card expired
        if (Integer.parseInt(cardDate[1]) < currentYear)
        {
            System.out.println("[-] Card has expired");
            return false;
        }
        return true;
    }

    public float getBalance()
    {
        return balance;
    }

    public void setBalance(float balance)
    {
        this.balance = balance;
    }

    //this function loads account details from database based on this.username
    protected void loadAccountFromFile() throws FileNotFoundException
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
                    this.paymentDetails.put("cardDate", paymentDetails[1]);
                    this.paymentDetails.put("holderName", paymentDetails[2]);
                }
                this.balance = Float.parseFloat(Account[5]);
                break;
            }
        }
        sc.close();
    }

    protected void saveAccountToFile()
    {

    }

    static int getNumberOfUsersFromFile(String filename) throws FileNotFoundException
    {
        File accounts = new File(filename);
        Scanner sc = new Scanner(accounts);
        sc.useDelimiter("\n");
        //skip first row
        sc.next();
        int counter = 0;
        while (sc.hasNext())
        {
            sc.next();
            counter++;
        }
        return counter;
    }

}
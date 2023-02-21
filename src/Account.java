import java.io.File; // used for reading from file
import java.io.FileNotFoundException;
import java.io.FileWriter; //used to write in file
import java.io.IOException; //exception during writing to file
import java.time.LocalDateTime; //used to validate card date
import java.util.Arrays; //used to convert separate values into array. Also, method of this class are used.
import java.util.HashMap; //used to store payment details easily
import java.util.Scanner; //used to read lines from file
import java.util.regex.Matcher; //used in combination with regex to validate user input
import java.util.regex.Pattern; //used to create pattern which is appropriate for our program

public class Account
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

    private float score;

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
            this.userId = lastId + 1;
        } catch (FileNotFoundException e)
        {
            System.out.println("[!] Error: " + e);
        }
    }

    public String getEmail()
    {
        return email;
    }

    public boolean setEmail(String email)
    {
        try
        {
            if (validateEmail(email))
            {
                this.email = email;
                return true;
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("[!] Error: " + e);
        }
        return false;
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
            if (email.equals(emailFromDatabase))
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

    public boolean setUsername(String username) throws FileNotFoundException
    {
        if (validateUserName(username))
        {
            this.username = username;
            return true;
        }
        return false;
    }

    private boolean validateUserName(String username) throws FileNotFoundException
    {
        //check if username valid
        if (username.contains(","))
        {
            System.out.println("[-] Please enter username without ','");
            return false;
        }

        //we need to check if username address is unique
        File accounts = new File(this.filename);
        Scanner sc = new Scanner(accounts);
        //explicitly set delimiter to '\n' so cardholder name with space does not stop the scanner
        sc.useDelimiter("\n");
        while (sc.hasNext())
        {
            //get values from row
            String[] Account = sc.next().split(",");
            String usernameFromDatabase = Account[2];
            if (username.equals(usernameFromDatabase))
            {
                System.out.println("[-] Sorry, this username is already taken");
                return false;
            }
        }
        sc.close();

        return true;
    }

    public String getPassword()
    {
        return password;
    }

    public boolean setPassword(String password)
    {
        if (validatePassword(password))
        {
            //hash password in order to store it securely
            int hashPassword = password.hashCode();
            this.password = String.valueOf(hashPassword);
            return true;
        }
        return false;
    }

    private boolean validatePassword(String password)
    {
        if (password.length() >= 8)
        {
            return true;
        }
        System.out.println("[-] Password must be at least 8 characters long");
        return false;
    }

    public HashMap<String, String> getPaymentDetails()
    {
        return paymentDetails;
    }

    public boolean setPaymentDetails(HashMap<String, String> paymentDetails)
    {
        //set payment details if all tests are passed
        if (validatePaymentDetails(paymentDetails))
        {
            this.paymentDetails = paymentDetails;
            return true;
        }
        return false;
    }

    private boolean validatePaymentDetails(HashMap<String, String> paymentDetails)
    {
        //get current date in order to verify card
        LocalDateTime now = LocalDateTime.now();
        // %100 used to get last 2 digits of the year
        int currentYear = now.getYear() % 100;
        int currentMonth = now.getMonthValue();


        String cardNumber = paymentDetails.get("cardNumber");
        //split card date by '/' to get month and year
        String[] cardDate = paymentDetails.get("cardDate").split("/");
        String holderName = paymentDetails.get("holderName");


        //basic regex for card number, which checks whether card has 16 numbers
        String cardNumberRegex = "^[0-9]{16}$";
        Pattern cardNumberPattern = Pattern.compile(cardNumberRegex);
        //basic pattern for holder name. User can have only letters and spaces in holder name
        String holderNameRegex = "^[a-zA-Z\s]+$";
        Pattern holderNamePattern = Pattern.compile(holderNameRegex);

        //check whether cardNumber matches regular expression
        if (!cardNumberPattern.matcher(cardNumber).matches())
        {
            System.out.println("[-] Make sure you have inputted correct card details");
            return false;
        }
        if (!holderNamePattern.matcher(holderName).matches())
        {
            System.out.println("[-] Holder name can contain only letters");
            return false;
        }

        try
        {
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
        }
        catch (NumberFormatException e)
        {
            System.out.println("[!] Error: " + e);
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

    public float getScore()
    {
        return score;
    }

    public void setScore(float score)
    {
        this.score = score;
    }

    //this function loads account details from database based on this.username
    protected void loadAccountFromFile(String username) throws FileNotFoundException
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
            if (Arrays.toString(Account).contains(username))
            {
                this.userId = Integer.parseInt(Account[0]);
                this.email = Account[1];
                this.username = Account[2];
                this.password = Account[3];
                //check if payment details saved
                if (!Account[4].equals("null"))
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

    protected void saveAccountToFile() throws IOException
    {
        FileWriter writer = new FileWriter(this.filename, true);

        //https://www.geeksforgeeks.org/stream-anymatch-java-examples/
        boolean readyToWrite = !Arrays.asList(this.username, this.email, this.password).stream().anyMatch(element -> element == null);

        if (!readyToWrite)
        {
            System.out.println("[!] Unable to write data");
            return;
        }
        if (this.paymentDetails.size() == 0)
        {
            writer.write(String.format("%s,%s,%s,%s,null,%s\n", this.userId, this.email, this.username, this.password, this.balance));
        }
        else
        {
            writer.write(String.format("%s,%s,%s,%s,%s-%s-%s,%s\n", this.userId, this.email, this.username,
                                                                    this.password, paymentDetails.get("cardNumber"), paymentDetails.get("cardDate"),
                                                                    paymentDetails.get("holderName"), this.balance));
        }
        writer.close();
    }

    public int getNumberOfUsersFromFile(String filename) throws FileNotFoundException
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

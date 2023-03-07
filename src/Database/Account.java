package Database;

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
    //database file with accounts
    private File accountFile;

    //class attributes represent columns in database
    //they have private modifiers because we will access them by using getters and setters
    private String userId;
    private String email;
    private String username;
    private String password;
    //use hashmap in order to store card details more convenient
    private HashMap<String, String> paymentDetails = new HashMap<>();
    private float balance;
    private float score;

    //we need constructor to make possible pass filename when creating an instance
    public Account(String filename)
    {
        this.accountFile = new File(filename);
    }

    //getters and setters to make program more secure
    public String getUserId()
    {
        return userId;
    }
    //assign unique id to user
    public void setUserId() throws FileNotFoundException
    {
            int lastId = getNumberOfUsersFromFile();
            int newId = lastId + 1;
            this.userId = String.valueOf(newId);
    }

    public String getEmail()
    {
        return email;
    }

    public boolean setEmail(String email) throws FileNotFoundException
    {
        if (validateEmail(email))
        {
            this.email = email;
            return true;
        }
        return false;
    }

    private boolean validateEmail(String email) throws FileNotFoundException
    {
        //create regular expression in order to check email later
        String emailRegex = "^[A-Za-z0-9-_.]+@[A-Za-z0-9-_.]+$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(email);

        //we need to check if email address is free to use
        Scanner sc = new Scanner(this.accountFile);
        //explicitly set delimiter to '\n' so cardholder name with space does not stop the scanner
        sc.useDelimiter("\n");
        while (sc.hasNext())
        {
            //get values from row
            String[] account = sc.next().split(",");
            String emailFromDatabase = account[1];
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

        //we need to check if username is unique
        Scanner sc = new Scanner(this.accountFile);
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

    public boolean verifyPassword(String password)
    {
        //hash password in order to store it securely
        int hashPassword = password.hashCode();
        String hashedPassword = String.valueOf(hashPassword);

        if (hashedPassword.equals(this.password))
        {
            return true;
        }
        System.out.println("[-] Passwords do not match");
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

    //set to protected because we use it in transaction class as well
    protected boolean validatePaymentDetails(HashMap<String, String> paymentDetails)
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
            if (Integer.parseInt(cardDate[1]) >= currentYear && Integer.parseInt(cardDate[0]) >= currentMonth)
            {
                return true;
            }
            else
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
    }

    public float getBalance()
    {
        return balance;
    }

    public void setBalance(float balance)
    {
        this.balance = balance;
    }

    public void addBalance(float value)
    {
        //we need to round value. sometimes java have vague calculations and sets many symbols after a decimal point
        this.balance += Math.round(value * 100)/100.0f;
    }

    public void subtractBalance(float value)
    {
        //we need to round value. sometimes java have vague calculations and sets many symbols after a decimal point
        this.balance -= Math.round(value * 100)/100.0f;
    }

    public float getScore()
    {
        return score;
    }

    public void setScore(float score)
    {
        this.score = score;
    }

    public void addScore(float value)
    {
        //we need to round value. sometimes java have vague calculations and sets many symbols after a decimal point
        this.score += Math.round(value * 100)/100.0f;
    }
    //this function loads account details from database based on this.username

    public boolean loadAccountFromFile(String email, String password) throws FileNotFoundException
    {
        Scanner sc = new Scanner(this.accountFile);

        //hash password in order to store it securely
        int hashPassword = password.hashCode();
        String hashedPassword = String.valueOf(hashPassword);

        //explicitly set delimiter to '\n' to make possible to store cardholder name with space
        sc.useDelimiter("\n");
        while (sc.hasNext())
        {
            //get values from row
            String[] account = sc.next().split(",");
            //check whether row has appropriate username
            if (account[AccountColumns.EMAIL.value].equals(email) && account[AccountColumns.PASSWORD.value].equals(hashedPassword))
            {
                this.userId = account[AccountColumns.USER_ID.value];
                this.email = account[AccountColumns.EMAIL.value];
                this.username = account[AccountColumns.USERNAME.value];
                this.password = account[AccountColumns.PASSWORD.value];
                //check if payment details saved
                if (!account[AccountColumns.PAYMENT_DETAILS.value].equals("null"))
                {
                    String[] paymentDetails = account[AccountColumns.PAYMENT_DETAILS.value].split("-");
                    this.paymentDetails.put("cardNumber", paymentDetails[0]);
                    this.paymentDetails.put("cardDate", paymentDetails[1]);
                    this.paymentDetails.put("holderName", paymentDetails[2]);
                }
                float balance = Float.parseFloat(account[AccountColumns.BALANCE.value]);
                float score = Float.parseFloat(account[AccountColumns.SCORE.value]);
                //we need to round value. sometimes java have vague calculations and sets many symbols after a decimal point
                this.balance = Math.round(balance * 100)/100.0f;
                this.score = Math.round(score * 100)/100.0f;

                sc.close();
                return true;
            }
        }
        sc.close();
        System.out.println("[-] Sorry, email or password are incorrect");
        return false;
    }
    public void saveAccountToFile() throws IOException
    {
        FileWriter writer = new FileWriter(this.accountFile, true);

        //https://www.geeksforgeeks.org/stream-anymatch-java-examples/
        //we need to check whether any of the element is not set, so required values are not empty in database
        boolean readyToWrite = !Arrays.asList(this.username, this.email, this.password).stream().anyMatch(element -> element == null);

        if (!readyToWrite)
        {
            System.out.println("[!] Unable to write data");
            return;
        }
        //define 2 types of string to write, based on payments details.
        if (this.paymentDetails.size() == 0)
        {
            writer.write(String.format("%s,%s,%s,%s,null,%s,%s\n", this.userId, this.email, this.username, this.password, this.balance,this.score));
        }
        else
        {
            writer.write(String.format("%s,%s,%s,%s,%s-%s-%s,%s,%s\n", this.userId, this.email, this.username,
                                                                    this.password, paymentDetails.get("cardNumber"), paymentDetails.get("cardDate"),
                                                                    paymentDetails.get("holderName"), this.balance, this.score));
        }
        writer.close();
    }

    //this method is used to change values in the database of the current user
    public void updateCredentials() throws IOException
    {
        //create a temp file in order to rewrite
        File tempFile = new File("temp.txt");
        FileWriter tempWriter = new FileWriter(tempFile);

        Scanner scannerAccounts = new Scanner(this.accountFile);
        //explicitly set delimiter to '\n' to make possible to store cardholder name with space
        scannerAccounts.useDelimiter("\n");
        while (scannerAccounts.hasNext())
        {
            String currentLine = scannerAccounts.next();
            String[] account = currentLine.split(",");

            //check if account contains user id
            if (account[AccountColumns.USER_ID.value].equals(this.userId))
            {
                if (this.paymentDetails.size() == 0)
                {
                    tempWriter.write(String.format("%s,%s,%s,%s,null,%s,%s\n", this.userId, this.email, this.username, this.password, this.balance,this.score));
                }
                else
                {
                    tempWriter.write(String.format("%s,%s,%s,%s,%s-%s-%s,%s,%s\n", this.userId, this.email, this.username,
                            this.password, paymentDetails.get("cardNumber"), paymentDetails.get("cardDate"),
                            paymentDetails.get("holderName"), this.balance, this.score));
                }
            }
            else
            {
                tempWriter.write(currentLine + "\n");
            }
        }
        scannerAccounts.close();
        tempWriter.close();
        //delete old file
        this.accountFile.delete();
        //rename temp file to old name
        tempFile.renameTo(this.accountFile);
    }

    //based on return of this method we set user id
    private int getNumberOfUsersFromFile() throws FileNotFoundException
    {
        Scanner sc = new Scanner(this.accountFile);
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

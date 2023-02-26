package Database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Transactions
{
    private final String userId;
    private final File transactionsFile;

    private final Account user;


    public Transactions(String transactionsFilename, String userId, Account user) throws FileNotFoundException
    {
        this.userId = userId;
        this.transactionsFile = new File(transactionsFilename);
        this.user = user;
    }

    public ArrayList<HashMap<String, Object>> getTopUps() throws FileNotFoundException
    {
        ArrayList<HashMap<String, Object>> topUps = new ArrayList<>();

        Scanner scannerTransactions = new Scanner(this.transactionsFile);
        //helps to avoid spaces in file
        scannerTransactions.useDelimiter("\n");
        //skip column names
        scannerTransactions.next();

        while (scannerTransactions.hasNext())
        {
            String currentLine = scannerTransactions.next().replace("\r", "");
            String[] record = currentLine.split(",");

            //use enum to easily operate with csv columns
            if(record[TransactionColumns.USER_ID.value].equals(this.userId) && record[TransactionColumns.TRANSACTION_TYPE.value].equals(TransactionType.TOP_UP.value))
            {
                HashMap<String,Object> temp = new HashMap<>();

                temp.put("transactionId", record[TransactionColumns.TRANSACTION_ID.value]);
                temp.put("amount", record[TransactionColumns.AMOUNT.value]);
                temp.put("transactionDate", record[TransactionColumns.TRANSACTION_DATE.value]);

                topUps.add(temp);
            }
        }
        scannerTransactions.close();
        return topUps;
    }

    public ArrayList<HashMap<String, Object>> getPurchases() throws FileNotFoundException
    {
        ArrayList<HashMap<String, Object>> purchases = new ArrayList<>();

        Scanner scannerPurchases = new Scanner(this.transactionsFile);
        //helps to avoid spaces in file
        scannerPurchases.useDelimiter("\n");
        //skip column names
        scannerPurchases.next();

        while (scannerPurchases.hasNext())
        {
            String currentLine = scannerPurchases.next().replace("\r", "");
            String[] record = currentLine.split(",");

            //use enum to easily operate with csv columns
            if(record[TransactionColumns.USER_ID.value].equals(this.userId) && record[TransactionColumns.TRANSACTION_TYPE.value].equals(TransactionType.BUY.value))
            {
                HashMap<String,Object> temp = new HashMap<>();

                temp.put("transactionId", record[TransactionColumns.TRANSACTION_ID.value]);
                temp.put("itemId", record[TransactionColumns.ITEM_ID.value]); //change to item name
                temp.put("amount", record[TransactionColumns.AMOUNT.value]);
                temp.put("transactionDate", record[TransactionColumns.TRANSACTION_DATE.value]);

                purchases.add(temp);
            }
        }
        scannerPurchases.close();
        return purchases;
    }

    public void topUpCard() throws IOException
    {
        HashMap<String, String> paymentDetails = new HashMap<>();
        Scanner inputScanner = new Scanner(System.in);

        if (this.user.getPaymentDetails().size() == 0)
        {
            createPaymentMethod(paymentDetails, inputScanner);
        }
        else
        {
            while (true)
            {
                System.out.println(this.user.getPaymentDetails());
                System.out.print("[+] Would you like to use this payment details?(y/n): ");
                String userChoice = inputScanner.nextLine();
                if (userChoice.equals("y"))
                {
                    paymentDetails = this.user.getPaymentDetails();
                    break;
                }
                else if (userChoice.equals("n"))
                {
                    createPaymentMethod(paymentDetails, inputScanner);
                    break;
                }
                else
                {
                    System.out.println("[-] Wrong value");
                }
            }
        }

        while (true)
        {
            System.out.print("[+] Enter amount:");
            float amount = inputScanner.nextFloat();

            if (addMoney(amount))
            {
                saveTopUp(amount);
                break;
            }
        }

        inputScanner.close();
    }

    private void createPaymentMethod(HashMap<String, String> paymentDetails, Scanner inputScanner) throws IOException
    {
        while (true)
        {

            System.out.print("[+] Enter card number: ");
            paymentDetails.put("cardNumber", inputScanner.nextLine());
            System.out.print("[+] Enter expiration date (e.g. 05/23): ");
            paymentDetails.put("cardDate", inputScanner.nextLine());
            System.out.print("[+] Enter card holder name: ");
            paymentDetails.put("holderName", inputScanner.nextLine());

            if(this.user.validatePaymentDetails(paymentDetails))
            {
                System.out.print("[+] Would you like to save payment details?(y/n): ");
                String userChoice = inputScanner.nextLine();
                if (userChoice.equals("y"))
                {
                    this.user.setPaymentDetails(paymentDetails);
                    this.user.updateCredentials();
                    System.out.println("[+] Saved successfully");
                }
                else
                {
                    System.out.println("[-] Your payment details were not saved");
                }
                break;
            }
        }
    }

    private boolean addMoney(float amount) throws IOException
    {
        if (amount > 0)
        {
            this.user.addBalance(amount);
            this.user.updateCredentials();
            System.out.println("[+] Money successfully added");
            return true;
        }

        System.out.println("[-] Value is incorrect");
        return false;
    }

    private void saveTopUp(float amount) throws IOException
    {
        FileWriter writerTransactions = new FileWriter(transactionsFile, true);
        writerTransactions.write(String.format("%s,%s,%s,%s,%s,%s,%s\n", getNumberOfTransactions(), this.user.getUserId(), TransactionType.TOP_UP.value, "null", "null", amount, LocalDate.now()));
        writerTransactions.close();
    }

    private void savePurchase(String itemId, int quantity, float amount) throws IOException
    {
        FileWriter writerTransactions = new FileWriter(transactionsFile, true);
        writerTransactions.write(String.format("%s,%s,%s,%s,%s,%s,%s\n", getNumberOfTransactions(), this.user.getUserId(), TransactionType.BUY.value, itemId, quantity, amount, LocalDate.now()));
        writerTransactions.close();
    }

    private int getNumberOfTransactions() throws FileNotFoundException
    {
        Scanner sc = new Scanner(this.transactionsFile);
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

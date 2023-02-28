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
    private final File transactionsFile;
    private final File itemFile;

    private final Account user;


    public Transactions(String transactionsFilename, String itemFile, Account user) throws FileNotFoundException
    {
        this.transactionsFile = new File(transactionsFilename);
        this.itemFile = new File(itemFile);
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
            if(record[TransactionColumns.USER_ID.value].equals(this.user.getUserId()) && record[TransactionColumns.TRANSACTION_TYPE.value].equals(TransactionType.TOP_UP.value))
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
            if(record[TransactionColumns.USER_ID.value].equals(this.user.getUserId()) && record[TransactionColumns.TRANSACTION_TYPE.value].equals(TransactionType.BUY.value))
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

    public void purchaseItem(String itemId, int quantity) throws IOException
    {
        Scanner scannerItems = new Scanner(this.itemFile);
        scannerItems.useDelimiter("\n");
        scannerItems.next();
        while (scannerItems.hasNext())
        {
            String[] item = scannerItems.next().split(",");
            if (item[ItemsColumns.ITEM_ID.value].equals(itemId))
            {
                float toPay = Float.parseFloat(item[ItemsColumns.PRICE.value])  * quantity;
                this.user.subtractBalance(toPay);
                this.user.updateCredentials();
                item[ItemsColumns.STOCK.value] =  String.valueOf(Integer.parseInt(item[ItemsColumns.STOCK.value]) - quantity);
                String updatedItem = String.join(",", item)+"\n";
                //need to close scanner before updating file, we cannot delete and rename it because file is in operation
                scannerItems.close();
                updateItemFile(updatedItem, item[ItemsColumns.ITEM_ID.value]);
                savePurchase(item[ItemsColumns.ITEM_ID.value], quantity, toPay);
                break;
            }
        }

    }

    public void updateItemFile(String item, String itemId) throws IOException
    {
        //create a temp file in order to rewrite friends.csv
        File tempFile = new File("temp.txt");
        FileWriter tempWriter = new FileWriter(tempFile);

        Scanner scannerItems = new Scanner(this.itemFile);
        scannerItems.useDelimiter("\n");

        while (scannerItems.hasNext())
        {
            String currentLine = scannerItems.next().replace("\r", "");
            String[] currentItem = currentLine.split(",");

            System.out.println(currentItem[ItemsColumns.ITEM_ID.value].equals(itemId));

            //check whether line has friend id, user id, and status pending
            if (currentItem[ItemsColumns.ITEM_ID.value].equals(itemId))
            {
                tempWriter.write(item);
            }
            else
            {
                tempWriter.write(currentLine + "\n");
            }
        }
        scannerItems.close();
        tempWriter.close();
        //delete old file
        this.itemFile.delete();
        //rename temp file to old name
        tempFile.renameTo(this.itemFile);
    }

    private void savePurchase(String itemId, int quantity, float amount) throws IOException
    {
        FileWriter writerTransactions = new FileWriter(transactionsFile, true);
        writerTransactions.write(String.format("%s,%s,%s,%s,%s,%s,%s\n", getNumberOfTransactions(), this.user.getUserId(), TransactionType.BUY.value, itemId, quantity, amount, LocalDate.now()));
        writerTransactions.close();
    }

    public ArrayList<HashMap<String, Object>> getItems() throws FileNotFoundException
    {
        ArrayList<HashMap<String, Object>> items = new ArrayList<>();

        Scanner scannerItems = new Scanner(itemFile);
        scannerItems.useDelimiter("\n");
        scannerItems.next();
        while (scannerItems.hasNext())
        {
            String[] item = scannerItems.next().replace("\r", "").split(",");
            HashMap<String, Object> tempItem = new HashMap<>();

            tempItem.put("itemId", item[ItemsColumns.ITEM_ID.value]);
            tempItem.put("company", item[ItemsColumns.COMPANY_NAME.value]);
            tempItem.put("name", item[ItemsColumns.NAME.value]);
            tempItem.put("price", Float.parseFloat(item[ItemsColumns.PRICE.value]));
            tempItem.put("stock", Integer.parseInt(item[ItemsColumns.STOCK.value]));
            tempItem.put("description", item[ItemsColumns.DESCRIPTION.value]);

            items.add(tempItem);
        }

        scannerItems.close();
        return items;
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

package Database;

import java.io.File; //used to open file
import java.io.FileNotFoundException; //exception for file opening
import java.io.FileWriter; //used to write into a file
import java.io.IOException; // exception for writing
import java.time.LocalDate; //used to write date of the transaction
import java.util.ArrayList; //used to make flexible list of quests
import java.util.HashMap; //used to store key:value pairs
import java.util.Scanner; //used to read from file

public class Transactions
{
    private final File transactionsFile;
    private final File itemFile;
    private final Account user;

    //constructor for Transactions class. pass instance of user to access its attributes and methods
    public Transactions(String transactionsFilename, String itemFile, Account user)
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
            //replace "\r" (return) because when we get last line it may break hashmap and later comparison
            String currentLine = scannerTransactions.next().replace("\r", "");
            String[] record = currentLine.split(",");

            //use enum to easily operate with csv columns
            if(record[TransactionsColumns.USER_ID.value].equals(this.user.getUserId()) && record[TransactionsColumns.TRANSACTION_TYPE.value].equals(TransactionsType.TOP_UP.value))
            {
                //create temp hashmap to add it later to the arraylist
                HashMap<String,Object> temp = new HashMap<>();

                temp.put("transactionId", record[TransactionsColumns.TRANSACTION_ID.value]);
                temp.put("amount", record[TransactionsColumns.AMOUNT.value]);
                temp.put("transactionDate", record[TransactionsColumns.TRANSACTION_DATE.value]);

                topUps.add(temp);
            }
        }
        scannerTransactions.close();
        return topUps;
    }

    public void topUpCard() throws IOException
    {
        HashMap<String, String> paymentDetails = new HashMap<>();
        //scanner of user input
        Scanner inputScanner = new Scanner(System.in);

        if (this.user.getPaymentDetails().size() == 0)
        {
            createPaymentMethod(paymentDetails, inputScanner);
        }
        else
        {
            //validation of user choice
            while (true)
            {
                System.out.println(this.user.getPaymentDetails());
                System.out.print("[+] Would you like to use this payment details?(y/n): ");
                String userChoice = inputScanner.nextLine();
                if (userChoice.equals("y"))
                {
                    //we do not use actually payment details in the program
                    //but still I get it from the user instance
                    //later it can be used to send details to the bank for the approval
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

        //validation of amount that user has inputted
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
    }

    private void createPaymentMethod(HashMap<String, String> paymentDetails, Scanner inputScanner) throws IOException
    {
        //iterate till user has not entered correct details
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

    public void topUpApplePay() throws IOException
    {
        Scanner inputScanner = new Scanner(System.in);
        //validation of amount that user has inputted
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
    }
    private boolean addMoney(float amount) throws IOException
    {
        if (amount > 0)
        {
            this.user.addBalance(amount);
            this.user.updateCredentials();
            return true;
        }

        System.out.println("[-] Value is incorrect");
        return false;
    }

    private void saveTopUp(float amount) throws IOException
    {
        FileWriter writerTransactions = new FileWriter(transactionsFile, true);
        writerTransactions.write(String.format("%s,%s,%s,%s,%s,%s,%s\n", getNumberOfTransactions(), this.user.getUserId(), TransactionsType.TOP_UP.value, "null", "null", amount, LocalDate.now()));
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

        ArrayList<HashMap<String, Object>> items = getItems();

        while (scannerPurchases.hasNext())
        {
            String currentLine = scannerPurchases.next().replace("\r", "");
            String[] record = currentLine.split(",");

            //use enum to easily operate with csv columns
            if(record[TransactionsColumns.USER_ID.value].equals(this.user.getUserId()) && record[TransactionsColumns.TRANSACTION_TYPE.value].equals(TransactionsType.BUY.value))
            {
                HashMap<String,Object> temp = new HashMap<>();

                temp.put("transactionId", record[TransactionsColumns.TRANSACTION_ID.value]);
                for (HashMap<String, Object> item :items)
                {
                    if (item.get("itemId").equals(record[TransactionsColumns.ITEM_ID.value]))
                    {
                        temp.put("itemName", item.get("name"));
                        temp.put("itemPrice", item.get("price"));
                    }
                }
                temp.put("quantity", record[TransactionsColumns.QUANTITY.value]);
                temp.put("amount", record[TransactionsColumns.AMOUNT.value]);
                temp.put("transactionDate", record[TransactionsColumns.TRANSACTION_DATE.value]);

                purchases.add(temp);
            }
        }
        scannerPurchases.close();
        return purchases;
    }

    public void purchaseItem(String itemId, int quantity) throws IOException
    {
        Scanner scannerItems = new Scanner(this.itemFile);
        //use \n to avoid issues with spaces
        scannerItems.useDelimiter("\n");
        scannerItems.next();
        while (scannerItems.hasNext())
        {
            //get item values separately
            String[] item = scannerItems.next().split(",");
            if (item[ItemsColumns.ITEM_ID.value].equals(itemId))
            {
                //check stock
                if (quantity > Integer.parseInt(item[ItemsColumns.STOCK.value]))
                {
                    System.out.println("[-] Sorry, we do not have so many items in stock");
                    return;
                }

                float toPay = Float.parseFloat(item[ItemsColumns.PRICE.value])  * quantity;
                //check if user has balance to pay
                if (toPay > this.user.getBalance())
                {
                    System.out.println("[-] Sorry, your balance is lower that the price.");
                    return;
                }
                //update balance
                this.user.subtractBalance(toPay);
                this.user.updateCredentials();

                //subtract purchased quantity from initial
                item[ItemsColumns.STOCK.value] =  String.valueOf(Integer.parseInt(item[ItemsColumns.STOCK.value]) - quantity);
                //create a string to write to the file
                String updatedItem = String.join(",", item)+"\n";

                //need to close scanner before updating file, we cannot delete and rename it because file is in operation
                scannerItems.close();

                //update item values in the file
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
            //replace "\r" is used to avoid problems with comparison.
            //after investigation, I suppose that we need to replace "\r" only when we manually add values to files
            String currentLine = scannerItems.next().replace("\r", "");
            String[] currentItem = currentLine.split(",");

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
        //add new record to the purchase file
        FileWriter writerTransactions = new FileWriter(transactionsFile, true);
        writerTransactions.write(String.format("%s,%s,%s,%s,%s,%s,%s\n", getNumberOfTransactions(), this.user.getUserId(), TransactionsType.BUY.value, itemId, quantity, amount, LocalDate.now()));
        writerTransactions.close();
    }

    public ArrayList<HashMap<String, Object>> getItems() throws FileNotFoundException
    {
        //create local array list to return it from the method
        ArrayList<HashMap<String, Object>> items = new ArrayList<>();

        Scanner scannerItems = new Scanner(itemFile);
        //used to avoid problems with spaces
        scannerItems.useDelimiter("\n");
        scannerItems.next();
        while (scannerItems.hasNext())
        {
            //replace "\r" is used to avoid problems with comparison.
            //after investigation, I suppose that we need to replace "\r" only when we manually add values to files
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

    //we use this method to make unique id based on the number of lines
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

package Database;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Transactions
{
    private final String userId;
    private final File transactionsFile;

    public Transactions(String transactionsFilename, String userId) throws FileNotFoundException
    {
        this.userId = userId;
        this.transactionsFile = new File(transactionsFilename);
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
                temp.put("itemId", record[TransactionColumns.ITEM_ID.value]); //change to item name
                temp.put("amount", record[TransactionColumns.AMOUNT.value]);
                temp.put("transactionDate", record[TransactionColumns.TRANSACTION_DATE.value]);

                System.out.println(temp);

                topUps.add(temp);
            }
        }
        scannerTransactions.close();
        return topUps;
    }
}

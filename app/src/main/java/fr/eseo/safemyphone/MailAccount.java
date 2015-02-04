package fr.eseo.safemyphone;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by etudiant on 04/02/2015.
 */
public class MailAccount {
    Account[] accounts;
    public static Account[] findAssociatedAccount(Context context){
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;

            }
        }
        return accounts;
    }
}

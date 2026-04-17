import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bank {
    private final Random rand = new Random();
    private final List<Account> accounts;

    private static final int ACCOUNT_NUMBER_LENGTH = 12;
    private static final int PIN_LENGTH = 4;

    public Bank() {
        this.accounts = new ArrayList<>();
    }

    private String generateAccountNumber() {
        String finalNumber;

        do {
            StringBuilder accountNumber = new StringBuilder();
            for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
                int choice = rand.nextInt(2);

                if (choice == 1) accountNumber.append((char) (rand.nextInt(26) + 'A'));
                else accountNumber.append(rand.nextInt(10));
            }
            finalNumber = accountNumber.toString();
        } while (findAccount(finalNumber) != null); // Checks if Account Number already exists

        return finalNumber;
    }

    private int generatePIN() {
        StringBuilder pin = new StringBuilder();

        for (int i = 0; i < PIN_LENGTH; i++) {
            pin.append(rand.nextInt(1, 10));
        }

        return Integer.parseInt(String.valueOf(pin));
    }

    public boolean checkPin(Account a, int givenPin) {
        return givenPin == a.getPin();
    }

    public boolean changePin(Account a, int givenPin, int newPin) {
        if (checkPin(a, givenPin)) {
            a.changePin(newPin == 0 ? generatePIN() : newPin);
            return true;
        }
        return false;
    }

    public Account createAccount(String name, double balance) {
        String accountNumber = generateAccountNumber();
        int pin = generatePIN();

        Account newAccount = new Account(name, accountNumber, balance, pin);
        accounts.add(newAccount);
        return newAccount;
    }

    public Account findAccount(String accountNumber) {
        for (Account a : accounts) {
            if (a.getAccountNumber().equals(accountNumber)) {
                return a;
            }
        }
        return null;
    }

     public boolean transfer(String fromNum, String toNum, double amount) {
        Account sender = findAccount(fromNum);
        Account receiver = findAccount(toNum);

        if (sender != null && receiver != null) {
            if (sender.withdraw(amount)) {
                receiver.deposit(amount);
                return true;
            }
        }
        return false;
    }
}
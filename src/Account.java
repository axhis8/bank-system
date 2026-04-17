public class Account {
    private String holder;
    private final String accountNumber;
    private double balance;
    private int pin;

    public Account(String holder, String accountNumber, double balance, int pin) {
        this.holder = holder;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.pin = pin;

    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolder() {
        return holder;
    }

    public void changeHolder(String holder) {
        this.holder = holder;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "\nName: %s\nNumber: %s\nBalance: %s€\nPIN: %s\n".formatted(holder, accountNumber, balance, pin);
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    public int getPin() {
        return pin;
    }

    public void changePin(int pin) {
        this.pin = pin;
    }
}

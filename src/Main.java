public class Main {
    public static void main(String[] args) {
        Bank myBank = new Bank();
        BankCLI cli = new BankCLI(myBank);
        cli.start();
    }
}
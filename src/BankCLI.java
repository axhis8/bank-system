import java.util.Scanner;

public class BankCLI {
    private static final String LINE = " ---------- ";
    private static final String INVALID_INP = "Invalid Input. Please try again.";

    private final Scanner scn = new Scanner(System.in);
    private final Bank bank;
    private Account userAccount;
    private final String[] choices = {"Show Balance",
                                      "Deposit Money",
                                      "Withdraw Money",
                                      "Transfer Money",
                                      "Change PIN",
                                      "Exit"};


    public BankCLI(Bank bank) {
        this.bank = bank;
    }

    public void start() {
        loginState();
        if (userAccount != null) {
            boolean running = true;
            while (running) {
                running = switchState();
            }
        }
    }

    private String menuState() {

        while (true) {
            try {
                System.out.println(LINE + "myBank" + LINE);
                for (int i = 0; i < choices.length; i++) {
                    System.out.printf("%s. %s\n", i + 1, choices[i]);
                }

                System.out.printf("\nEnter your choice (1 - %s): ", choices.length);
                int choiceIndex = Integer.parseInt(scn.nextLine());
                return choices[choiceIndex - 1];

            } catch (NumberFormatException e) {
                System.out.println(INVALID_INP);
            }
        }
    }

    private boolean switchState() {
        String choice = menuState();

        switch (choice) {
            case "Show Balance": {
                showBalance();
                break;
            }
            case "Deposit Money": {
                depositState();
                break;
            }
            case "Withdraw Money": {
                withdrawState();
                break;
            }
            case "Transfer Money": {
                transferState();
                break;
            }
            case "Change PIN": {
                changePin();
                break;
            }
            case "Exit": {
                return false;
            }
            default: {
                System.out.println(INVALID_INP);
                break;
            }

        }
        return true;
    }

    private void showBalance() {
        System.out.printf("\nBalance: %.2f€\n", userAccount.getBalance());
        scn.nextLine();
    }

    private void transferState() {
        if (!checkPin()) return;

        while (true) {
            try {
                System.out.println("Account Number ('e' to exit): ");
                String accountNum = scn.nextLine();
                if (accountNum.equals("e")) return;

                System.out.println("Amount in € ('e' to exit): ");
                String amountInput = scn.nextLine();
                if (amountInput.equals("e")) return;

                double amount = Double.parseDouble(amountInput);
                if (bank.transfer(userAccount.getAccountNumber(), accountNum, amount)) {
                    Account transferredAccount = bank.findAccount(accountNum);
                    System.out.printf("\nSuccessfully transferred %.2f€ to:", amount);
                    System.out.println("Name: " + transferredAccount.getHolder());
                    System.out.println("Account Number:" + transferredAccount.getAccountNumber() + "\n");
                    return;
                } else {
                    System.out.println("Couldn't find Account. Did you type it right?");
                }

            } catch (NumberFormatException e) {
                System.out.println(INVALID_INP);
            }
        }
    }

    private void withdrawState() {
        if (!checkPin()) return;

        while (true) {
            try {
                System.out.print("Enter amount to withdraw ('e' to exit): ");
                String input = scn.nextLine();
                if (input.equals("e")) return;

                double withdrawnAmount = Double.parseDouble(input);
                boolean success = userAccount.withdraw(withdrawnAmount);

                if (success) {
                    System.out.printf("\nSuccessfully withdrawn %.2f€ from your Account", withdrawnAmount);
                    System.out.printf("\nBalance: %.2f€", userAccount.getBalance());
                } else {
                    System.out.printf("\nCouldn't withdraw %.2f€ from your Account.", withdrawnAmount);
                    System.out.println("You don't have enough money in your account.");
                    System.out.printf("\nCurrent Balance: %.2f", userAccount.getBalance());
                }
                return;
            } catch (NumberFormatException e) {
                System.out.printf(INVALID_INP);
            }
        }
    }

    private void depositState() {
        if (!checkPin()) return;

        while (true) {
            try {
                System.out.print("Enter amount to deposit ('e' to exit): ");
                String input = scn.nextLine();
                if (input.equals("e")) return;

                double depositAmount = Double.parseDouble(input);
                userAccount.deposit(depositAmount);

                System.out.printf("\nSuccessfully deposited %.2f in your Account", depositAmount);
                System.out.printf("\nBalance: %.2f\n", userAccount.getBalance());
                return;
            } catch (NumberFormatException e) {
                System.out.printf(INVALID_INP);
            }
        }
    }

    private void changePin() {
        while (true) {
            try {
                System.out.print("Enter your current PIN: ");
                int oldPin = Integer.parseInt(scn.nextLine());

                System.out.print("Enter your new Pin (type '0' to generate a random PIN: ");
                int newPin = Integer.parseInt(scn.nextLine());

                boolean success = bank.changePin(userAccount, oldPin, newPin);
                if (success) {
                    System.out.println("Successfully changed PIN!");
                    return;
                }
                else System.out.println(INVALID_INP);

            } catch (NumberFormatException e) {
                System.out.println(INVALID_INP);
            }
        }
    }

    private void registerState() {
        System.out.print("Enter your name ('e' to exit): ");
        String holder = scn.nextLine().toLowerCase().strip();
        if (holder.equals("e")) return;

        // Try to get Balance
        double balance;
        while (true) {
            try {
                System.out.print("Enter your balance in € (Press Enter for 0 or 'e' to exit & cancel): ");
                String input = scn.nextLine();
                if (input.equals("e")) return;

                balance = Double.parseDouble(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println(INVALID_INP);
            }
        }
        this.userAccount = bank.createAccount(holder, balance);

        System.out.println("Successfully created Account!");
        System.out.println(userAccount);
        System.out.println("Please remember your PIN as it won't be displayed again.");
    }

    private void loginState() {
        Account a = null;
        int pin = 0;
        do {
            System.out.print("Enter your Account Number ('r' to register or 'e' to exit): ");
            String num = scn.nextLine().strip();
            if (num.equals("e")) return;
            if (num.equals("r")) {
                registerState();
                continue;
            }

            try {
                System.out.print("Enter your PIN: ");
                pin = Integer.parseInt(scn.nextLine());

            } catch (NumberFormatException e) {
                System.out.println(INVALID_INP);
                continue;
            }

            a = bank.findAccount(num);
            if (a == null || pin != a.getPin()) System.out.println(INVALID_INP);

        } while (a == null || pin != a.getPin());
        // "a=null" must be written first, same as the if statement above
        // so that the first condition is checked and if it's wrong, a.getPin() won't be called
        // (as a=null, and it would throw an exception)

        userAccount = a;
        System.out.println("\nLogged in successfully! Welcome, " + capitalize(userAccount.getHolder()) + ".");
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean checkPin() {
        while (true) {
            try {
                System.out.print("Enter your PIN to verify: ");
                int givenPin = Integer.parseInt(scn.nextLine());

                boolean verify = bank.checkPin(userAccount, givenPin);
                if (verify) {
                    return true;
                } else {
                    System.out.println("PIN is invalid.");
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println(INVALID_INP);
            }
        }
    }

    public static String capitalize(String input) {
        String[] words = input.split("\\s");

        StringBuilder result = new StringBuilder();
        for (String word : words) {
            result.append(Character.toTitleCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }

        return result.toString().trim();
    }
}

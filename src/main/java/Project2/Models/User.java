package Project2.Models;

public class User {
    private String Username;
    private String Password;
    private double AccountBalance;

    public User(String username, String password) {
        Username = username;
        Password = password;
    }

    public void ShowAccountBalance() {
        System.out.println("Users " + Username + " account balance is: " + AccountBalance);
    }

    // GETTERS, SETTERS deleted due to not using
    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public double getAccountBalance() {
        return AccountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.AccountBalance = accountBalance;
    }
}

package Project2.Repos.ListDependent;

import Project2.Models.User;
import Project2.Repos.Interfaces.ILoggedUser;

import java.util.ArrayList;
import java.util.List;

public class LoggedUserRepository implements ILoggedUser {

    private List<User> loggedUsersList = new ArrayList<User>();

    @Override
    public List GetUsers() {
        return loggedUsersList;
    }

    @Override
    public User GetUser(String username) {
        return loggedUsersList.stream().filter(x -> x.getUsername().equals(username)).findFirst().orElse(null);
    }

    @Override
    public User GetUser(String username, String password) {
        return loggedUsersList.stream().filter(x -> x.getUsername().equals(username) && x.getPassword().equals(password)).findFirst().orElse(null);
    }

    @Override
    public void AddUser(User user) {
        loggedUsersList.add(user);
    }

    @Override
    public void DeleteUser(String username) {
        loggedUsersList.removeIf(x -> x.getUsername().equals(username));

    }

    @Override
    public boolean UserExists(String username) {
        return loggedUsersList.stream().anyMatch(x -> x.getUsername().equals(username));
    }

    @Override
    public void AddMoneyToUsersAccount(String username, String sAmount) {
        double amount;
        User user = loggedUsersList.stream().filter(x -> x.getUsername().equals(username)).findFirst().orElse(null);
        if(user == null)
            return;
        try {
            amount = Double.parseDouble(sAmount);
            double newBalance = user.getAccountBalance() + amount;
            user.setAccountBalance(newBalance);
        } catch (Exception ex) {
            PrintFormatWarning();
        }
    }

    private void PrintFormatWarning() {
        System.out.println("Invalid data! Please make sure given amount is a double format number [00.0]!");
    }
}

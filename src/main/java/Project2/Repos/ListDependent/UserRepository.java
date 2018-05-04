package Project2.Repos.ListDependent;

import Project2.Models.User;
import Project2.Repos.Interfaces.IUser;

import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUser {

    private List<User> usersList = new ArrayList<User>();

    @Override
    public List GetUsers() {
        return usersList;
    }

    @Override
    public User GetUser(String username) {
        return usersList.stream().filter(x -> x.getUsername().equals(username)).findFirst().orElse(null);
    }

    @Override
    public User GetUser(String username, String password) {
        return usersList.stream().filter(x -> x.getUsername().equals(username) && x.getPassword().equals(password)).findFirst().orElse(null);
    }

    @Override
    public void AddUser(User user) {
        usersList.add(user);
    }

    @Override
    public void DeleteUser(String username) {
        usersList.removeIf(x -> x.getUsername().equals(username));
    }

    @Override
    public boolean UserExists(String username) {
        return usersList.stream().anyMatch(x -> x.getUsername().equals(username));
    }
}

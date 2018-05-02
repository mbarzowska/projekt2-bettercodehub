package Projekt2.Repos.Interfaces;

import Projekt2.Models.User;

import java.util.List;

public interface ILoggedUser {
    List GetUsers();
    User GetUser(String username);
    User GetUser(String username, String password);
    void AddUser(User user);
    void DeleteUser(String username);
    boolean UserExists(String username);

    void AddMoneyToUsersAccount(String username, String sAmount);
}

package Project2.Models;

import Project2.Models.Interfaces.IValidator;

public class Validator implements IValidator {
    public boolean ValidateUser(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean ValidateGame(String gameName, double price, int ahs, int ahe, boolean[] days, int uhs, int uhe, String des) {
        if (gameName == null || gameName.isEmpty() ||
            price == 0.0 ||
            ahs == 0 || ahe == 0 ||
            days.length != 7 ||
            uhs == 0 || uhe == 0 ||
            des == null || des.isEmpty()) {
            return false;
        }
        return true;
    }
}



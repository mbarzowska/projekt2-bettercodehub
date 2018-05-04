package Project2.Models.Interfaces;

public interface IValidator {
    boolean ValidateUser(String username, String password);
    boolean ValidateGame(String gameName, double price, int ahs, int ahe, boolean[] days, int uhs, int uhe, String des);
}

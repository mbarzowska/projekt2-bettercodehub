package Project2.Models.Interfaces;

import Project2.Models.Game;

import java.time.LocalDate;

public interface IValidator {
    boolean ValidateUser(String username, String password);
    boolean ValidateGame(String gameName, double price, int ahs, int ahe, boolean[] days, int uhs, int uhe, String des);
    int ValidateBookingTime(Game game, String sTime);
    LocalDate ValidateBookingDate(Game game, String sDate);
    String transformDaysToWords(boolean[] table);
    void ValidateGameName(String gameName);
}

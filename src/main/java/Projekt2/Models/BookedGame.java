package Projekt2.Models;

import java.time.LocalDate;

public class BookedGame {
    private String Username;
    private Projekt2.Models.Game Game;
    private LocalDate Date;
    private int Time;
    private String BookingID;

    public BookedGame(String username, Projekt2.Models.Game game, LocalDate date, int time, String bookingID) {
        this.Username = username;
        this.Game = game;
        this.Date = date;
        this.Time = time;
        this.BookingID = bookingID;
    }

    // GETTERS, SETTERS deleted due to not using
    public String getUsername() {
        return Username;
    }

    public Projekt2.Models.Game getGame() {
        return Game;
    }

    public LocalDate getDate() {
        return Date;
    }

    public int getTime() {
        return Time;
    }

    public String getBookingID() {
        return BookingID;
    }
}

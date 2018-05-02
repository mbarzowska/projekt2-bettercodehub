package Projekt2.Repos.Interfaces;

import Projekt2.Models.BookedGame;

import java.util.List;

public interface IBookedGame {
    List GetBookedGames();
    BookedGame GetBookedGame(String gameName);
    BookedGame GetBookedGame(String username, String gameName);
    void AddBookedGame(BookedGame game);
    void DeleteBookedGame(String gameName);
    boolean BookedGameExists(String gameName);
}

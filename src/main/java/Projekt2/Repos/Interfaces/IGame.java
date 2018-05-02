package Projekt2.Repos.Interfaces;

import Projekt2.Models.Game;

import java.util.List;

public interface IGame {
    List GetGames();
    Game GetGame(String gameName);
    void AddGame(Game game);
    void DeleteGame(String gameName);
    boolean GameExists(String gameName);
}

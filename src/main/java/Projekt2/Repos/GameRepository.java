package Projekt2.Repos;

import Projekt2.Models.Game;
import Projekt2.Repos.Interfaces.IGame;

import java.util.ArrayList;
import java.util.List;

public class GameRepository implements IGame {

    private List<Game> gamesList = new ArrayList<Game>();

    @Override
    public List GetGames() {
        return gamesList;
    }

    @Override
    public Game GetGame(String gameName) {
        return gamesList.stream().filter(x -> x.getName().equals(gameName)).findFirst().orElse(null);
    }

    @Override
    public void AddGame(Game game) {
        gamesList.add(game);
    }

    @Override
    public void DeleteGame(String gameName) {
        gamesList.removeIf(x -> x.getName().equals(gameName));
    }

    @Override
    public boolean GameExists(String gameName) {
        return gamesList.stream().anyMatch(x -> x.getName().equals(gameName));
    }
}

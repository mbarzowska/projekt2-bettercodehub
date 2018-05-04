package Project2.Repos.ListDependent;

import Project2.Models.BookedGame;
import Project2.Repos.Interfaces.IBookedGame;

import java.util.ArrayList;
import java.util.List;

public class BookedGameRepository implements IBookedGame {

    private List<BookedGame> bookedGamesList = new ArrayList<BookedGame>();

    @Override
    public List GetBookedGames() {
        return bookedGamesList;
    }

    @Override
    public BookedGame GetBookedGame(String gameName) {
        return bookedGamesList.stream().filter(x -> x.getGame().getName().equals(gameName)).findFirst().orElse(null);
    }

    @Override
    public BookedGame GetBookedGame(String username, String gameName) {
        return bookedGamesList.stream().filter(x -> x.getUsername().equals(username) && x.getGame().getName().equals(gameName)).findFirst().orElse(null);
    }

    @Override
    public void AddBookedGame(BookedGame game) {
        bookedGamesList.add(game);
    }

    @Override
    public void DeleteBookedGame(String gameName) {
        bookedGamesList.removeIf(x -> x.getGame().getName().equals(gameName));
    }

    @Override
    public boolean BookedGameExists(String gameName) {
        return bookedGamesList.stream().anyMatch(x -> x.getGame().getName().equals(gameName));
    }
}

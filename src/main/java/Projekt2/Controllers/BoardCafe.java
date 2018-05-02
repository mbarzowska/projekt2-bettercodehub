package Projekt2.Controllers;

import Projekt2.Models.BookedGame;
import Projekt2.Models.Game;
import Projekt2.Models.Interfaces.IValidator;
import Projekt2.Models.User;
import Projekt2.Repos.Interfaces.IBookedGame;
import Projekt2.Repos.Interfaces.IGame;
import Projekt2.Repos.Interfaces.ILoggedUser;
import Projekt2.Repos.Interfaces.IUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BoardCafe {

    private IGame _gameRepository;
    private IBookedGame _bookedGameRepository;
    private IUser _userRepository;
    private ILoggedUser _loggedUserRepository;
    private IValidator _validator;

    public BoardCafe(IGame gameRepository, IBookedGame bookedGameRepository, IUser userRepository, ILoggedUser loggedUserRepository, IValidator validator) {
        _gameRepository = gameRepository;
        _bookedGameRepository = bookedGameRepository;
        _userRepository = userRepository;
        _loggedUserRepository = loggedUserRepository;
        _validator = validator;
    }

    public void LoadDatabase(String databasePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(databasePath));
        while(scanner.hasNextLine()) {
            try {
                String[] gameData = scanner.nextLine().split(";");

                String name = gameData[0];
                double price = Double.parseDouble(gameData[1]);
                // godziny dostepnosci   gameData[2]
                // niedostepne dni   gameData[3]
                // niedostepne godziny  gameData[4]
                // opis gameData[5]

                String[] tmpOpenHours = gameData[2].split("-");
                int availableHoursStart = Integer.parseInt(tmpOpenHours[0]);
                int availableHoursEnd = Integer.parseInt(tmpOpenHours[1]);

                String[] tmpClosedDays = gameData[3].split(" ");
                boolean days[] = new boolean[7];
                for (int i = 0; i < tmpClosedDays.length; i++) {
                    if (Integer.parseInt(tmpClosedDays[i]) == 1) {
                        days[i] = true;
                    } else {
                        days[i] = false;
                    }
                }

                String[] tmpUnavailableHours = gameData[4].split("-");
                int unavailableHoursStart = Integer.parseInt(tmpUnavailableHours[0]);
                int unavailableHoursEnd = Integer.parseInt(tmpUnavailableHours[1]);

                String description = gameData[5];
                if (_gameRepository.GameExists(name) == false) {
                    _gameRepository.AddGame(new Game(name, price, availableHoursStart, availableHoursEnd, days, unavailableHoursStart, unavailableHoursEnd, description));
                }
            } catch(Exception e) {
                continue;
            }
        }
        scanner.close();
    }

    public boolean Register(String username, String password) {
        if (_userRepository.UserExists(username)) {
            throw new IllegalArgumentException("Username " + username + " is already taken!");
        }

        if (!_validator.ValidateUser(username, password)) {
            throw new IllegalArgumentException("Username or password is an empty string!");
        }

        User newUser = new User(username, password);
        _userRepository.AddUser(newUser);
        return true;
    }

    public boolean LogIn(String username, String password) {
        if (!_validator.ValidateUser(username, password)) {
            throw new IllegalArgumentException("Username or password is an empty string!");
        }

        if (_userRepository.UserExists(username)) {
            User user = _userRepository.GetUser(username);
            if (user.getPassword().equals(password)) {
               _loggedUserRepository.AddUser(user);
                System.out.println("User " + username + " logged in successfully!");
                return true;
            } else {
                throw new IllegalArgumentException("Incorrect password!");
            }
        } else {
            throw new UnsupportedOperationException("User: " + username + " doesn't exist");
        }
    }

    public boolean LogOut(User user) {
        if (!_userRepository.UserExists(user.getUsername())) {
            throw new IllegalArgumentException("User does not exist!");
        }

        User userToLogout = _loggedUserRepository.GetUser(user.getUsername());
        if (userToLogout == null) {
            throw new UnsupportedOperationException("User is currently not logged in, can't perform logout!");
        } else {
            _loggedUserRepository.DeleteUser(userToLogout.getUsername());
            System.out.println("User " + user.getUsername() + " logged out successfully!");
            return true;
        }
    }

    public boolean UserBookGame(User user, String gameName, String sDate, String sTime) throws ParseException, IOException {
        if (gameName == null || gameName.isEmpty()) {
            throw new IllegalArgumentException("Wrong input!\nGame name is an empty string!");
        }

        Game game = _gameRepository.GetGame(gameName);
        if (game == null) {
            throw new UnsupportedOperationException("This game does not exist in our database!");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(sDate, formatter);

        if (transformDaysToWords(game.getDays()).contains(date.getDayOfWeek().toString())) {
            throw new UnsupportedOperationException("You can't book a game for a date it's not available to book, sorry!");
        }

        int time;

        try {
            time = Integer.parseInt(sTime);
        } catch (Exception ex) {
            System.out.println("Invalid data! Please make sure given time is a number!");
            return false;
        }

        if (time < game.getAvailableHoursStart() || time >= game.getAvailableHoursEnd()) {
            throw new UnsupportedOperationException("You can't book a game for a time period when we're closed, sorry!");
        }

        if (time >= game.getAvailableHoursStart() && time < game.getAvailableHoursEnd()) {
            if (time >= game.getUnavailableHoursStart() && time < game.getUnavailableHoursEnd()) {
                throw new UnsupportedOperationException("You can't book a game for a time period when it's not available to book, sorry!");
            }
        }

        if (_loggedUserRepository.UserExists(user.getUsername())) {
            User loggedUser = _loggedUserRepository.GetUser(user.getUsername());

            if (_bookedGameRepository.GetBookedGame(loggedUser.getUsername(), gameName) != null) {
                throw new UnsupportedOperationException("User " + user.getUsername() + "already booked the game " + gameName);
            }

            List<BookedGame> bookedGames = _bookedGameRepository.GetBookedGames();
            for (BookedGame item : bookedGames) {
                    if (item.getGame().getName().equals(gameName) && item.getDate().equals(date) && item.getTime() == time) {
                        throw new UnsupportedOperationException("User " + item.getUsername() + " already booked the game " + gameName + " on " + item.getDate().toString() + ", at " + time);
                    }
            }

            if (loggedUser.getAccountBalance() >= game.getPrice()) {
                Random rand = new Random();
                int  randomValue = rand.nextInt(50000) + 1;
                String bookingID = randomValue + "/"
                        + loggedUser.getUsername() + "/"
                        + game.getName() + "/"
                        + date + "/"
                        + time;
                BookedGame bookedGame = new BookedGame(loggedUser.getUsername(), game, date, time, bookingID);

                _bookedGameRepository.AddBookedGame(bookedGame);
                loggedUser.setAccountBalance(loggedUser.getAccountBalance() - game.getPrice());

                List<String> lines = Arrays.asList("CONFIRMATION\n Successfully booked a game: ", game.getName(), " for: ", loggedUser.getUsername(), " on: ", date.toString(), " time: ", String.valueOf(time), " ID: ", String.valueOf(randomValue));
                Path file = Paths.get("src/main/resources/confirmations/","bookingNo" + randomValue +".txt");
                Files.write(file, lines, Charset.forName("UTF-8"));

                System.out.println(lines.toString().replaceAll(", ", "").replaceAll("\\[", "").replaceAll("]", ""));
            } else {
                throw new UnsupportedOperationException("User " + user.getUsername() + " doesn't have enough money to book " + gameName);
            }
        }
        return true;
    }

    public void ShowUsersBookedGamesList(String username) {
        List<BookedGame> list = _bookedGameRepository.GetBookedGames();

        if (list.isEmpty()) {
            System.out.println("The list is currently empty");
        }

        for (BookedGame item : list) {
            if (item.getUsername().equals(username)) {
                System.out.println("ID: " + item.getBookingID()
                        + "; booked game name: " + item.getGame().getName()
                        + "; booked game description: " + item.getGame().getDescription()
                        + "; date: " + item.getDate()
                        + "; time: " + item.getTime());
            }
        }
    }

    public User FindLoggedUser(String username){
        return _loggedUserRepository.GetUser(username);
    }

    public boolean UserIsLoggedIn(User user){
        if (user == null) return false;
        else return _loggedUserRepository.UserExists(user.getUsername());
    }

    public boolean GameExists(String gameName){
        return _gameRepository.GameExists(gameName);
    }

    public boolean AddMoneyToUsersAccount(String username, String sAmount){
        if(FindLoggedUser(username) == null) return false;
        _loggedUserRepository.AddMoneyToUsersAccount(username, sAmount);
        return true;
    }

    private static String transformDaysToWords(boolean[] table) {
        String[] daysInWords = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
        StringBuilder wordedDaysBuilder = new StringBuilder();
        for (int i = 0; i < table.length; i++) {
            if (table[i] == true) {
                wordedDaysBuilder.append(daysInWords[i] + "; ");
            } else {
                continue;
            }
        }
        String wordedDays = wordedDaysBuilder.toString();
        return wordedDays;
    }

    public String ShowGamesList() {
        StringBuilder gamesListBuilder = new StringBuilder();
        List<Game> list = _gameRepository.GetGames();

        if (list.isEmpty()) {
            return "The list is currently empty";
        }

        for (Game item : list) {
            gamesListBuilder.append("Name: " + item.getName()
                    + " | price: " + item.getPrice()
                    + " | hours: from " + item.getAvailableHoursStart() + " 'till " + item.getAvailableHoursEnd()
                    + " | unavailable: " + transformDaysToWords(item.getDays())
                    + "| unavailable hours: from " + item.getUnavailableHoursStart() + " 'till " + item.getUnavailableHoursEnd() + "\n");
        }

        String gamesList = gamesListBuilder.toString();
        return gamesList;
    }
}
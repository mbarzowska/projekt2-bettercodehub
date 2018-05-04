package Project2;

import Project2.Controllers.BoardCafe;
import Project2.Models.Game;
import Project2.Models.Interfaces.IValidator;
import Project2.Models.Validator;
import Project2.Models.User;
import Project2.Repos.ListDependent.BookedGameRepository;
import Project2.Repos.ListDependent.GameRepository;
import Project2.Repos.Interfaces.IBookedGame;
import Project2.Repos.Interfaces.IGame;
import Project2.Repos.Interfaces.ILoggedUser;
import Project2.Repos.Interfaces.IUser;
import Project2.Repos.ListDependent.LoggedUserRepository;
import Project2.Repos.ListDependent.UserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.assertj.core.api.Assertions;

public class BoardCafeFakesTest {

    IUser userRepository;
    ILoggedUser loggedUserRepository;
    IGame gameRepository;
    IBookedGame bookedGameRepository;
    IValidator validator;

    BoardCafe bc;

    User testDefaultUser;
    String testDefaultUsersUsername, testDefaultUsersPassword;
    User testDefaultSecondUser;
    String testDefaultSecondUsersUsername, testDefaultSecondUsersPassword;
    Game testDefaultGame;
    String testDefaultGamesName; double testDefaultGamesPrice; int testDefaultGamesAHS; int testDefaultGamesAHE;
    boolean[] testDefaultGamesDays; int testDefaultGamesUHS; int testDefaultGamesUHE; String testDefaultGamesDescription;
    String testDefaultGamesBookDateUnavailDay, testDefaultGamesBookDateAvailDay,
            testDefaultGamesBookAvailTime, testDefaultGamesBookUnavailTime, testDefaultGamesBookTimeClosed;
    String pathToMixedDatabase, pathToCorrectDatabase, pathToIncorrectDatabase, incorrectDatabasePath;

    @BeforeEach
    public void SetUp() {
        userRepository = new UserRepository();
        loggedUserRepository = new LoggedUserRepository();
        gameRepository = new GameRepository();
        bookedGameRepository = new BookedGameRepository();
        validator = new Validator();
        bc = new BoardCafe(gameRepository, bookedGameRepository, userRepository, loggedUserRepository, validator);

        testDefaultUser = new User("Mika", "qwerty");
        testDefaultUsersUsername = testDefaultUser.getUsername();
        testDefaultUsersPassword = testDefaultUser.getPassword();
        testDefaultSecondUser = new User("Thor", "qwerty");
        testDefaultSecondUsersUsername = testDefaultSecondUser.getUsername();
        testDefaultSecondUsersPassword = testDefaultSecondUser.getPassword();
        testDefaultGame = new Game ("Ego", 55.0, 10, 20, new boolean[]{false, false, false, true, true, true, false}, 16, 20, "OK");
        testDefaultGamesName = testDefaultGame.getName();
        testDefaultGamesPrice = testDefaultGame.getPrice();
        testDefaultGamesAHS = testDefaultGame.getAvailableHoursStart();
        testDefaultGamesAHE = testDefaultGame.getAvailableHoursEnd();
        testDefaultGamesDays = testDefaultGame.getDays();
        testDefaultGamesUHS = testDefaultGame.getUnavailableHoursStart();
        testDefaultGamesUHE = testDefaultGame.getUnavailableHoursEnd();
        testDefaultGamesDescription = testDefaultGame.getDescription();
        testDefaultGamesBookDateUnavailDay = "28/04/2018";
        testDefaultGamesBookDateAvailDay = "29/04/2018";
        testDefaultGamesBookAvailTime = "11";
        testDefaultGamesBookUnavailTime = "16";
        testDefaultGamesBookTimeClosed = "21";
        pathToMixedDatabase = "src/test/resources/database.csv";
        pathToCorrectDatabase = "src/test/resources/dbCorrectRecord.csv";
        pathToIncorrectDatabase = "src/test/resources/dbIncorrectRecord.csv";
        incorrectDatabasePath = "src/test/resources/doesNotExist.csv";
    }



    @Test
    public void UserBookGame_WhenAllConditionsAreMet_ShouldCreateConfirmationFile() throws IOException, ParseException {
        bc.Register(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.LogIn(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.AddMoneyToUsersAccount(testDefaultUsersUsername, "1000");
        gameRepository.AddGame(testDefaultGame);
        Path file = Paths.get("src/main/resources/confirmations/");
        File parentDir =  file.toFile();
        int filesBefore = parentDir.list().length;

        bc.UserBookGame(testDefaultUser, testDefaultGamesName, testDefaultGamesBookDateAvailDay, testDefaultGamesBookAvailTime);

        Assertions.assertThat(filesBefore + 1).isEqualTo(parentDir.list().length);
    }

   @Test
    public void UserBookGame_WhenAllConditionsAreMet_ShouldAddGameToUsersAccount() throws IOException, ParseException {
        bc.Register(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.LogIn(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.AddMoneyToUsersAccount(testDefaultUsersUsername, "1000");
        gameRepository.AddGame(testDefaultGame);

        bc.UserBookGame(testDefaultUser, testDefaultGamesName, testDefaultGamesBookDateAvailDay, testDefaultGamesBookAvailTime);

        Assertions.assertThat(bookedGameRepository.GetBookedGame(testDefaultUsersUsername, testDefaultGamesName)).isNotNull();
    }

    @Test
    public void UserBookGame_WhenGameAlreadyBookedByUser_ShouldThrowException() throws IOException, ParseException {
        bc.Register(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.LogIn(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.AddMoneyToUsersAccount(testDefaultUsersUsername, "1000");
        gameRepository.AddGame(testDefaultGame);

        bc.UserBookGame(testDefaultUser, testDefaultGamesName, testDefaultGamesBookDateAvailDay, "11");

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> { bc.UserBookGame(testDefaultUser, testDefaultGamesName, testDefaultGamesBookDateAvailDay, "12"); })
                .withMessage("%s", "User " + testDefaultUsersUsername + "already booked the game " + testDefaultGamesName);
    }


    @Test
    public void UserBookGame_WhenGameAlreadyBookedBySomeone_ShouldThrowException() throws IOException, ParseException {
        bc.Register(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.LogIn(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.AddMoneyToUsersAccount(testDefaultUsersUsername, "1000");
        bc.Register(testDefaultSecondUsersUsername, testDefaultSecondUsersPassword);
        bc.LogIn(testDefaultSecondUsersUsername, testDefaultSecondUsersPassword);
        bc.AddMoneyToUsersAccount(testDefaultSecondUsersUsername, "1000");
        gameRepository.AddGame(testDefaultGame);

        bc.UserBookGame(testDefaultSecondUser, testDefaultGamesName, testDefaultGamesBookDateAvailDay, testDefaultGamesBookAvailTime);

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> { bc.UserBookGame(testDefaultUser, testDefaultGamesName, testDefaultGamesBookDateAvailDay, testDefaultGamesBookAvailTime); })
                .withMessage("%s", "User " + testDefaultSecondUsersUsername + " already booked the game " + testDefaultGamesName + " on " + LocalDate.parse(testDefaultGamesBookDateAvailDay, DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString() + ", at " + "11");
    }

    @Test
    public void UserBookGame_WhenUserDontHaveMoney_ShouldThrowException() throws IOException, ParseException {
        bc.Register(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.LogIn(testDefaultUsersUsername, testDefaultUsersPassword);
        gameRepository.AddGame(testDefaultGame);

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> { bc.UserBookGame(testDefaultUser, testDefaultGamesName, testDefaultGamesBookDateAvailDay, "11"); })
                .withMessage("%s", "User " + testDefaultUsersUsername + " doesn't have enough money to book " + testDefaultGamesName);
    }

    @Test
    public void UserBookGame_WhenTimeUnavailable_ShouldThrowException() throws IOException, ParseException {
        bc.Register(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.LogIn(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.AddMoneyToUsersAccount(testDefaultUsersUsername, "1000");
        gameRepository.AddGame(testDefaultGame);

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> { bc.UserBookGame(testDefaultUser, testDefaultGamesName, testDefaultGamesBookDateAvailDay, testDefaultGamesBookUnavailTime); })
                .withMessage("%s", "You can't book a game for a time period when it's not available to book, sorry!");
    }

    @Test
    public void UserBookGame_WhenTimeClosed_ShouldThrowException() throws IOException, ParseException {
        bc.Register(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.LogIn(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.AddMoneyToUsersAccount(testDefaultUsersUsername, "1000");
        gameRepository.AddGame(testDefaultGame);

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> { bc.UserBookGame(testDefaultUser, testDefaultGamesName, testDefaultGamesBookDateAvailDay, testDefaultGamesBookTimeClosed); })
                .withMessage("%s", "You can't book a game for a time period when we're closed, sorry!");
    }

    @Test
    public void UserBookGame_WhenDateUnavailable_ShouldThrowException() throws IOException, ParseException {
        bc.Register(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.LogIn(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.AddMoneyToUsersAccount(testDefaultUsersUsername, "1000");
        gameRepository.AddGame(testDefaultGame);

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> { bc.UserBookGame(testDefaultUser, testDefaultGamesName, testDefaultGamesBookDateUnavailDay, testDefaultGamesBookAvailTime); })
                .withMessage("%s", "You can't book a game for a date it's not available to book, sorry!");
    }

    @Test
    public void UserBookGame_WhenShopDoesntOwnTheGame_ShouldThrowException() throws IOException, ParseException {
        bc.Register(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.LogIn(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.AddMoneyToUsersAccount(testDefaultUsersUsername, "1000");

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> { bc.UserBookGame(testDefaultUser, testDefaultGamesName, testDefaultGamesBookDateAvailDay, testDefaultGamesBookAvailTime); })
                .withMessage("%s", "This game does not exist in our database!");
    }

    @Test
    public void UserBookGame_WhenGivenNullGameName_ShouldThrowException() throws IOException, ParseException {
        bc.Register(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.LogIn(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.AddMoneyToUsersAccount(testDefaultUsersUsername, "1000");

        Assertions.assertThatIllegalArgumentException().isThrownBy(() -> { bc.UserBookGame(testDefaultUser, null, testDefaultGamesBookDateAvailDay, testDefaultGamesBookAvailTime); })
                .withMessage("%s", "Wrong input!\nGame name is an empty string!");
    }

    @Test
    public void UserBookGame_WhenGivenEmptyGameName_ShouldThrowException() throws IOException, ParseException {
        bc.Register(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.LogIn(testDefaultUsersUsername, testDefaultUsersPassword);
        bc.AddMoneyToUsersAccount(testDefaultUsersUsername, "1000");

        Assertions.assertThatIllegalArgumentException().isThrownBy(() -> { bc.UserBookGame(testDefaultUser, null, testDefaultGamesBookDateAvailDay, testDefaultGamesBookAvailTime); })
                .withMessage("%s", "Wrong input!\nGame name is an empty string!");
    }



    @Test
    public void ShowGamesList_ListIsEmpty_ReturnsStringSayingThat() {
        String result = bc.ShowGamesList();

        Assertions.assertThat(result).isEqualTo("The list is currently empty");
    }

    @Test
    public void ShowGamesList_ListContainsGameX_ReturnsStringContainingGameX() {
        Game game = new Game("Eurobiznes", 40.0, 10, 20, new boolean[]{false, false, false, false, true, true, false}, 14, 18, "OK");
        gameRepository.AddGame(game);

        String result = bc.ShowGamesList();

        Assertions.assertThat(result).contains(game.getName());
    }



    @Test
    public void FindLoggedUser_UserIsNotLoggedIn_ReturnsNull() {
        User mika = new User("Mika", "psswd");

        User result = bc.FindLoggedUser(mika.getUsername());

        Assertions.assertThat(result).isNull();
    }

    @Test
    public void FindLoggedUser_UserIsLoggedIn_ReturnsUser() {
        User mika = new User("Mika", "psswd");
        loggedUserRepository.AddUser(mika);

        User result = bc.FindLoggedUser(mika.getUsername());

        Assertions.assertThat(result).isEqualTo(mika);
    }



    @Test
    public void UserIsLoggedIn_InvalidDataNull_ReturnsFalse() {
        boolean result = bc.UserIsLoggedIn(null);

        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void UserIsLoggedIn_IsNotLoggedIn_ReturnsFalse() {
        User mika = new User("Mika", "psswd");

        boolean result = bc.UserIsLoggedIn(mika);

        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void UserIsLoggedIn_IsLoggedIn_ReturnsFalse() {
        User mika = new User("Mika", "psswd");
        loggedUserRepository.AddUser(mika);

        boolean result = bc.UserIsLoggedIn(mika);

        Assertions.assertThat(result).isTrue();
    }



    @Test
    public void GameExists_GameDoesNotExist_ReturnsFalse() {
        Game game = new Game("Eurobiznes", 40.0, 10, 20, new boolean[]{false, false, false, false, true, true, false}, 14, 18, "OK");

        boolean result = bc.GameExists(game.getName());

        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void GameExists_GameDoesExist_ReturnsFalse() {
        Game game = new Game("Eurobiznes", 40.0, 10, 20, new boolean[]{false, false, false, false, true, true, false}, 14, 18, "OK");
        gameRepository.AddGame(game);

        boolean result = bc.GameExists(game.getName());

        Assertions.assertThat(result).isTrue();
    }



    @AfterEach
    public void TearDown() {
        userRepository = null;
        loggedUserRepository = null;
        gameRepository = null;
        bookedGameRepository = null;
        validator = null;
        bc = null;
    }
}

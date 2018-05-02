package Projekt2;

import Projekt2.Controllers.BoardCafe;
import Projekt2.Models.Interfaces.IValidator;
import Projekt2.Models.User;
import Projekt2.Repos.Interfaces.IBookedGame;
import Projekt2.Repos.Interfaces.IGame;
import Projekt2.Repos.Interfaces.ILoggedUser;
import Projekt2.Repos.Interfaces.IUser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import Projekt2.Extensions.MockitoExtension;
import org.mockito.Mockito;
import static org.mockito.Mockito.doReturn;

@SuppressWarnings("deprecation")
@ExtendWith(MockitoExtension.class)
public class BoardCafeMockitoTest {

    IUser userRepository;
    ILoggedUser loggedUserRepository;
    IGame gameRepository;
    IBookedGame bookedGameRepository;
    IValidator validator;

    BoardCafe bc;

    @BeforeEach
    public void SetUp() {
        userRepository = Mockito.mock(IUser.class);
        loggedUserRepository = Mockito.mock(ILoggedUser.class);
        gameRepository = Mockito.mock(IGame.class);
        bookedGameRepository = Mockito.mock(IBookedGame.class);
        validator = Mockito.mock(IValidator.class);
        bc = new BoardCafe(gameRepository, bookedGameRepository, userRepository, loggedUserRepository, validator);
    }



    @Test
    public void LogInUser_InvalidDataNull_ThrowsException() {
        User mika = new User(null, null);
        doReturn(false).when(validator).ValidateUser(mika.getUsername(), mika.getPassword());

        assertThrows(IllegalArgumentException.class, () -> { bc.LogIn(mika.getUsername(), mika.getPassword()); });
    }

    @Test
    public void LogInUser_InvalidDataNull_ThrowsProperException() {
        User mika = new User(null, null);
        doReturn(false).when(validator).ValidateUser(mika.getUsername(), mika.getPassword());

        Throwable e = assertThrows(IllegalArgumentException.class, () -> { bc.LogIn(mika.getUsername(), mika.getPassword()); });

        assertEquals("Username or password is an empty string!", e.getMessage());
    }

    @Test
    public void LogInUser_InvalidDataEmpty_ThrowsException() {
        User mika = new User("", "");
        doReturn(false).when(validator).ValidateUser(mika.getUsername(), mika.getPassword());

        assertThrows(IllegalArgumentException.class, () -> { bc.LogIn(mika.getUsername(), mika.getPassword()); });
    }

    @Test
    public void LogInUser_InvalidDataEmpty_ThrowsProperException() {
        User mika = new User("", "");
        doReturn(false).when(validator).ValidateUser(mika.getUsername(), mika.getPassword());

        Throwable e = assertThrows(IllegalArgumentException.class, () -> { bc.LogIn(mika.getUsername(), mika.getPassword()); });

        assertEquals("Username or password is an empty string!", e.getMessage());
    }

    @Test
    public void LogInUser_UserDoesExist_ReturnsTrue() {
        User mika = new User("Mika", "psswd");
        doReturn(true).when(validator).ValidateUser(mika.getUsername(), mika.getPassword());
        doReturn(true).when(userRepository).UserExists(mika.getUsername());
        doReturn(mika).when(userRepository).GetUser(mika.getUsername());

        boolean result = bc.LogIn(mika.getUsername(), mika.getPassword());

        assertTrue(result);
    }

    @Test
    public void LogInUser_UserDoesNotExist_ThrowsException() {
        User mika = new User("Mika", "psswd");
        doReturn(true).when(validator).ValidateUser(mika.getUsername(), mika.getPassword());
        doReturn(false).when(userRepository).UserExists(mika.getUsername());

        assertThrows(UnsupportedOperationException.class, () -> { bc.LogIn(mika.getUsername(), mika.getPassword()); });
    }

    @Test
    public void LogInUser_UserDoesNotExist_ThrowsProperException() {
        User mika = new User("Mika", "psswd");
        doReturn(true).when(validator).ValidateUser(mika.getUsername(), mika.getPassword());
        doReturn(false).when(userRepository).UserExists(mika.getUsername());

        Throwable e = assertThrows(UnsupportedOperationException.class, () -> { bc.LogIn(mika.getUsername(), mika.getPassword()); });

        assertEquals("User: " + mika.getUsername() + " doesn't exist", e.getMessage());
    }



    @Test
    public void LogOutUser_UserDoesNotExist_ThrowsException() {
        User mika = new User("Mika", "psswd");
        doReturn(false).when(userRepository).UserExists(mika.getUsername());

        assertThrows(IllegalArgumentException.class, () -> { bc.LogOut(mika); });
    }

    @Test
    public void LogOutUser_UserDoesNotExist_ThrowsProperException() {
        User mika = new User("Mika", "psswd");
        doReturn(false).when(userRepository).UserExists(mika.getUsername());

        Throwable e = assertThrows(IllegalArgumentException.class, () -> { bc.LogOut(mika); });

        assertEquals("User does not exist!", e.getMessage());
    }

    @Test
    public void LogOutUser_UserDoesExistIsNotLoggedIn_ThrowsException() {
        User mika = new User("Mika", "psswd");
        doReturn(true).when(userRepository).UserExists(mika.getUsername());
        doReturn(null).when(loggedUserRepository).GetUser(mika.getUsername());

        assertThrows(UnsupportedOperationException.class, () -> { bc.LogOut(mika); });
    }

    @Test
    public void LogOutUser_UserDoesExistIsNotLoggedIn_ThrowsProperException() {
        User mika = new User("Mika", "psswd");
        doReturn(true).when(userRepository).UserExists(mika.getUsername());
        doReturn(null).when(loggedUserRepository).GetUser(mika.getUsername());

        Throwable e = assertThrows(UnsupportedOperationException.class, () -> { bc.LogOut(mika); });

        assertEquals("User is currently not logged in, can't perform logout!", e.getMessage());
    }

    @Test
    public void LogOutUser_UserDoesExistIsLoggedIn_ReturnsTrue() {
        User mika = new User("Mika", "psswd");
        doReturn(true).when(userRepository).UserExists(mika.getUsername());
        doReturn(mika).when(loggedUserRepository).GetUser(mika.getUsername());

        boolean result = bc.LogOut(mika);

        assertTrue(result);
    }



    @AfterEach
    public void TearDown() {
        bc = null;
        bookedGameRepository = null;
        gameRepository = null;
        userRepository = null;
        loggedUserRepository = null;
        validator = null;
    }
}

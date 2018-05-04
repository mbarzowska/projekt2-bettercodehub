package Project2;

import Project2.Controllers.BoardCafe;
import Project2.Models.Interfaces.IValidator;
import Project2.Models.User;
import Project2.Repos.Interfaces.IBookedGame;
import Project2.Repos.Interfaces.IGame;
import Project2.Repos.Interfaces.ILoggedUser;
import Project2.Repos.Interfaces.IUser;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import Project2.Extensions.EasyMockExtension;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

@ExtendWith(EasyMockExtension.class)
public class BoardCafeEasyMockTest {

    IUser userRepository;
    ILoggedUser loggedUserRepository;
    IGame gameRepository;
    IBookedGame bookedGameRepository;
    IValidator validator;

    BoardCafe bc;

    @BeforeEach
    public void SetUp() {
        userRepository = EasyMock.createNiceMock(IUser.class);
        loggedUserRepository = EasyMock.createNiceMock(ILoggedUser.class);
        gameRepository = EasyMock.createNiceMock(IGame.class);
        bookedGameRepository = EasyMock.createNiceMock(IBookedGame.class);
        validator = EasyMock.createNiceMock(IValidator.class);
        bc = new BoardCafe(gameRepository, bookedGameRepository, userRepository, loggedUserRepository, validator);
    }



    @Test
    public void RegisterUser_UserDoesNotAlreadyExist_ReturnsTrue() {
        User mika = new User("Mika", "psswd");
        expect(userRepository.UserExists(mika.getUsername())).andReturn(false);
        expect(validator.ValidateUser(mika.getUsername(), mika.getPassword())).andReturn(true);
        replay(userRepository);
        replay(validator);

        boolean result = bc.Register(mika.getUsername(), mika.getPassword());

        assertTrue(result);
    }

    @Test
    public void RegisterUser_UserDoesAlreadyExist_ThrowsException() {
        User mika = new User("Mika", "psswd");
        expect(userRepository.UserExists(mika.getUsername())).andReturn(true);
        expect(validator.ValidateUser(mika.getUsername(), mika.getPassword())).andReturn(true);
        replay(userRepository);
        replay(validator);

        assertThrows(IllegalArgumentException.class, () -> { bc.Register(mika.getUsername(), mika.getPassword()); });
    }

    @Test
    public void RegisterUser_UserDoesAlreadyExist_ThrowsProperException() {
        User mika = new User("Mika", "psswd");
        expect(userRepository.UserExists(mika.getUsername())).andReturn(true);
        expect(validator.ValidateUser(mika.getUsername(), mika.getPassword())).andReturn(true);
        replay(userRepository);
        replay(validator);

        Throwable e = assertThrows(IllegalArgumentException.class, () -> { bc.Register(mika.getUsername(), mika.getPassword()); });

        assertEquals("Username " + mika.getUsername() + " is already taken!", e.getMessage());
    }

    @Test
    public void RegisterUser_InvalidDataNull_ThrowsException() {
        User mika = new User(null, null);
        expect(validator.ValidateUser(mika.getUsername(), mika.getPassword())).andReturn(false);
        replay(validator);

        assertThrows(IllegalArgumentException.class, () -> { bc.Register(mika.getUsername(), mika.getPassword()); });
    }

    @Test
    public void RegisterUser_InvalidDataNull_ThrowsProperException() {
        User mika = new User(null, null);
        expect(validator.ValidateUser(mika.getUsername(), mika.getPassword())).andReturn(false);
        replay(validator);

        Throwable e = assertThrows(IllegalArgumentException.class, () -> { bc.Register(mika.getUsername(), mika.getPassword()); });

        assertEquals("Username or password is an empty string!", e.getMessage());
    }

    @Test
    public void RegisterUser_InvalidDataEmpty_ThrowsException() {
        User mika = new User("", "");
        expect(validator.ValidateUser(mika.getUsername(), mika.getPassword())).andReturn(false);
        replay(validator);

        assertThrows(IllegalArgumentException.class, () -> { bc.Register(mika.getUsername(), mika.getPassword()); });
    }

    @Test
    public void RegisterUser_InvalidDataEmpty_ThrowsProperException() {
        User mika = new User("", "");
        expect(validator.ValidateUser(mika.getUsername(), mika.getPassword())).andReturn(false);
        replay(validator);

        Throwable e = assertThrows(IllegalArgumentException.class, () -> { bc.Register(mika.getUsername(), mika.getPassword()); });

        assertEquals("Username or password is an empty string!", e.getMessage());
    }



    @Test
    public void AddMoneyToUsersAccount_UserIsLoggedIn_ReturnsFalse() {
        User mika = new User("Mika", "psswd");
        expect(userRepository.UserExists(mika.getUsername())).andReturn(true);
        expect(loggedUserRepository.GetUser(mika.getUsername())).andReturn(null);
        replay(userRepository);
        replay(loggedUserRepository);

        boolean result = bc.AddMoneyToUsersAccount(mika.getUsername(), "1000");

        assertFalse(result);
    }

    @Test
    public void AddMoneyToUsersAccount_UserIsLoggedIn_ReturnsTrue() {
        User mika = new User("Mika", "psswd");
        expect(userRepository.UserExists(mika.getUsername())).andReturn(true);
        expect(loggedUserRepository.GetUser(mika.getUsername())).andReturn(mika);
        replay(userRepository);
        replay(loggedUserRepository);

        boolean result = bc.AddMoneyToUsersAccount(mika.getUsername(), "1000");

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

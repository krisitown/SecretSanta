package bg.sofia.uni.fmi.mjt.wish.list;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import bg.sofia.uni.fmi.mjt.wish.list.models.User;
import bg.sofia.uni.fmi.mjt.wish.list.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    private UserService userService;

    @Mock
    private Map<String, User> mockedUsers;

    @Before
    public void setup(){
        this.userService = new UserService(mockedUsers);
    }

    @Test
    public void registerWhenValidUsernameShouldCreateUserAndSetItAsPrincipal(){
        UserService emptyRegisterService = new UserService();
        String username = "usr";
        String password = "1234";
        String expected = String.format("Username %s successfully registered", username);

        String result = emptyRegisterService.register(username, password);

        assertEquals(expected, result);
        assertNotNull(emptyRegisterService.getPrincipal());
    }

    @Test
    public void registerWhenTakenUsernameShouldNotCreateUserAndNotSetAsPrincipal(){
        String username = "usr";
        String password = "1234";
        when(mockedUsers.containsKey(username)).thenReturn(true);
        String expected = String.format("Username %s is already taken, select another one", username);

        String result = this.userService.register(username, password);

        assertEquals(expected, result);
        assertNull(this.userService.getPrincipal());
    }

    @Test
    public void registerWhenInvalidUsernameShouldNotCreateUserAndNotSetAsPrincipal(){
        String badUsername = "Ko$io";
        String password = "1234";
        String expected = String.format("Username %s is invalid, select a valid one", badUsername);

        String result = this.userService.register(badUsername, password);

        assertEquals(expected, result);
        assertNull(this.userService.getPrincipal());
    }

    @Test
    public void loginWhenCorrectUsernameAndPasswordShouldLoginAndSetAsPrincipal(){
        String username = "usr";
        String password = "1234";
        when(mockedUsers.containsKey(username)).thenReturn(true);
        when(mockedUsers.get(username)).thenReturn(new User(username, password));
        String expected = String.format("User %s successfully logged in", username);

        String result = this.userService.login(username, password);

        assertEquals(expected, result);
        assertNotNull(this.userService.getPrincipal());
    }

    @Test
    public void loginWhenIncorrectUsernameShouldNotLogin(){
        String username = "usr";
        String password = "1234";
        when(mockedUsers.containsKey(username)).thenReturn(false);
        String expected = "Invalid username/password combination";

        String result = this.userService.login(username, password);

        assertEquals(expected, result);
        assertNull(this.userService.getPrincipal());
    }

    @Test
    public void loginWhenIncorrectPasswordShouldNotLogin(){
        String username = "usr";
        String badPassword = "1234";
        String correctPassword = "12345";
        when(mockedUsers.containsKey(username)).thenReturn(true);
        when(mockedUsers.get(username)).thenReturn(new User(username, correctPassword));
        String expected = "Invalid username/password combination";

        String result = this.userService.login(username, badPassword);

        assertEquals(expected, result);
        assertNull(this.userService.getPrincipal());
    }
}

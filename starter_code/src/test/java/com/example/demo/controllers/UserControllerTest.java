package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    private CartRepository cartRepository = Mockito.mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = Mockito.mock((BCryptPasswordEncoder.class));

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);

        User expectedUser = new User();
        expectedUser.setUsername("Mika");
        expectedUser.setPassword("MyPassword");

        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(expectedUser);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(expectedUser));
    }

    @Test
    public void create_user_happy_path() {
        Mockito.when(bCryptPasswordEncoder.encode("MyPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Mika");
        r.setPassword("MyPassword");
        r.setConfirmPassword("MyPassword");

        ResponseEntity<User> response = userController.createUser(r);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        Assert.assertNotNull(u);
        Assert.assertEquals(0, u.getId());
        Assert.assertEquals("Mika", u.getUsername());
        Assert.assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void test_createUserBadRequest() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Mika");
        r.setPassword("test");
        r.setConfirmPassword("test");

        ResponseEntity<User> response = userController.createUser(r);

        Assert.assertNotNull(response);
        Assert.assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void test_findByUsername() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Mika");
        r.setPassword("MyPassword");
        r.setConfirmPassword("MyPassword");

        ResponseEntity<User> response = userController.createUser(r);

        ResponseEntity<User> userByName = userController.findByUserName("Mika");

        Assert.assertNotNull(response.getBody());

        User u = userByName.getBody();

        Assert.assertNotNull(u);
        Assert.assertEquals("Mika", u.getUsername());
        Assert.assertEquals(response.getBody().getUsername(), u.getUsername());
    }

    @Test
    public void test_findUserById() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Mika");
        r.setPassword("MyPassword");
        r.setConfirmPassword("MyPassword");

        ResponseEntity<User> response = userController.createUser(r);

        Assert.assertNotNull(response.getBody());

        ResponseEntity<User> userById = userController.findById(response.getBody().getId());

        User u = userById.getBody();

        Assert.assertNotNull(u);
        Assert.assertEquals("Mika", u.getUsername());
        Assert.assertEquals("MyPassword", u.getPassword());
    }
}

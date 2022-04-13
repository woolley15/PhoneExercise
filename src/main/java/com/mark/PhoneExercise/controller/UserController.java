package com.mark.PhoneExercise.controller;

import com.mark.PhoneExercise.model.User;
import com.mark.PhoneExercise.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("user")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /*
     *  Add a new user to the system
     */
    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody User user){
        logger.debug("Attempting to create user");
        User createdUser = userService.createUser(user);
        if(nonNull(createdUser)){
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     *  Delete a user from the system
     */
    @DeleteMapping("/deleteUser/{userName}")
    public ResponseEntity<String> deleteUser(@PathVariable String userName){
        logger.debug("Attempting to delete user with username : " + userName);
        boolean isDeleted = userService.deleteUser(userName);
        if(isDeleted){
            return new ResponseEntity<>("User Successfully deleted.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User could not be found. Please check for correct User Name.", HttpStatus.NOT_FOUND);
        }
    }

    /*
     *  Return a list of users in the system
     */
    @GetMapping("/findAll")
    public List<User> getAllUsers(){
        logger.debug("Attempting to find all users");
        return userService.findAllUsers();
    }

    /*
     *  Update a user's preferred phone number
     */
    @PostMapping("{userName}/setPreferredNumber")
    public ResponseEntity<String> setUserPreferredNo(@PathVariable String userName,
            @RequestParam("preferredNo") String number){
        logger.debug("Attempting to update " + userName + " preferred number to " + number);
        return userService.setPreferredNoForUser(userName, number);
    }
}

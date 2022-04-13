package com.mark.PhoneExercise.services;

import com.mark.PhoneExercise.model.Phone;
import com.mark.PhoneExercise.model.User;
import com.mark.PhoneExercise.repository.PhoneRepository;
import com.mark.PhoneExercise.repository.UserRepository;
import org.hibernate.PropertyValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


import static java.util.Objects.nonNull;

@Service
public class UserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PhoneRepository phoneRepository;
    @Autowired
    private PhoneService phoneService;

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public User createUser(User user) {
        logger.info("Creating user for " + user.getUserName());
        validateUser(user);
        return userRepository.save(user);
    }

    private void validateUser(User user) {
        if(!nonNull(user.getEmailAddress())){
            throw new PropertyValueException(" cannot be null for entity ", "User", "emailAddress");
        } else if (!nonNull(user.getUserName())){
            throw new PropertyValueException("cannot be null for entity ", "User", "userName");
        } else if (!nonNull(user.getPassword())){
            throw new PropertyValueException("cannot be null for entity ", "User", "password");
        }
    }

    @Transactional
    public boolean deleteUser(String userName) {
        User user = userRepository.findByUserName(userName);
        if(nonNull(user)){
            // Delete phones associated to this user before deleting user
            phoneRepository.deleteByUserId(user.getUserId());
            userRepository.deleteById(user.getUserId());
            logger.info("Deleted all phones and User for userName " + userName);
            return true;
        } else {
            logger.error("Unable to delete user with username " + userName + " as user cannot be found.");
            return false;
        }
    }

    public ResponseEntity<String> setPreferredNoForUser(String userName, String number) {
        ResponseEntity<String> responseEntity = null;
        User user = userRepository.findByUserName(userName);
        if(nonNull(user)){
            List<Phone> phoneListForUser = phoneRepository.findByUser(user.getUserId());
            // Check number being passed is linked to this users phone list
            if(phoneListForUser.size() > 0 ) {
                for (Phone phone : phoneListForUser) {
                    if (phone.getPhoneNumber().equals(number)) {
                        user.setPreferredPhoneNumber(number);
                        userRepository.save(user);
                        logger.info("Updated " + userName + " preferred No to be " + number);
                        responseEntity = new ResponseEntity<>("Successfully updated.", HttpStatus.OK);
                    } else {
                        logger.error("Cannot set preferred No for " + userName + " as " + number + " is not listed as a Phone linked to them.");
                        responseEntity = new ResponseEntity<>("This number is not linked to this user", HttpStatus.NOT_FOUND);
                    }
                }
            } else {
                logger.error("Cannot set preferred No for " + userName + " as user has no phones linked to them");
                responseEntity = new ResponseEntity<>("This user has no phones linked to them so unable to set preferred number.", HttpStatus.NOT_FOUND);
            }
        } else {
            logger.error("Cannot set preferred No as no user found with user name of " + userName);
            responseEntity = new ResponseEntity<>("No user found with this user name", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }
}

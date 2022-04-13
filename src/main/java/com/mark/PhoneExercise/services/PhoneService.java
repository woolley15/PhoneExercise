package com.mark.PhoneExercise.services;

import com.mark.PhoneExercise.controller.UserController;
import com.mark.PhoneExercise.model.Phone;
import com.mark.PhoneExercise.model.User;
import com.mark.PhoneExercise.repository.PhoneRepository;
import com.mark.PhoneExercise.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
public class PhoneService {

    private static Logger logger = LoggerFactory.getLogger(PhoneService.class);

    @Autowired
    private PhoneRepository phoneRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<String> addPhoneToUser(String userName, Phone phone) {
        ResponseEntity<String> response = null;
        User user = userRepository.findByUserName(userName);
        if(nonNull(user)){
            phone.setUserId(user.getUserId());
            phoneRepository.save(phone);
            logger.info(userName + " added phone with number of " + phone.getPhoneNumber());
            response = new ResponseEntity<>("Successfully added phone to user.", HttpStatus.CREATED);
        } else {
            logger.error("Unable to add phone to user as userName " + userName + " does not exist.");
            response = new ResponseEntity<>("Unable to add phone to user as userName " + userName + " does not exist.", HttpStatus.NOT_FOUND);
        }
        return  response;
    }

    public boolean deletePhoneFromUser(String userName, String number) {
        User user = userRepository.findByUserName(userName);
        if(nonNull(user)) {
            List<Phone> phoneList = phoneRepository.findByUser(user.getUserId());
            for(Phone phone : phoneList){
                if(phone.getPhoneNumber().equals(number)){
                    phoneRepository.delete(phone);
                    logger.info(userName + " deleted its phone with number of " + number);
                    return true;
                } else {
                    logger.error("Unable to delete phone for userName " + userName + ". As "+ number + " is not linked to this user.");
                }
            }
        } else {
            logger.error("Unable to delete phone for userName " + userName + ". As user does not exist.");
        }
        return false;
    }

    public List<Phone> findAllPhonesForUser(String userName) {
        User user = userRepository.findByUserName(userName);
        if(nonNull(user)) {
            return phoneRepository.findByUser(user.getUserId());
        } else {
            logger.error("Unable to find phones for userName " + userName + ". As user does not exist.");
            return new ArrayList<>();
        }
    }
}

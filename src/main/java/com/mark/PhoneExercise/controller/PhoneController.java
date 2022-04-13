package com.mark.PhoneExercise.controller;

import com.mark.PhoneExercise.model.Phone;
import com.mark.PhoneExercise.model.User;
import com.mark.PhoneExercise.services.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("phone")
public class PhoneController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private PhoneService phoneService;

    /*
     *  Add a phone to a user
     */
    @PostMapping("/addPhone/{userName}")
    public ResponseEntity<String> addPhone(@PathVariable String userName, @RequestBody Phone phone){
        logger.debug("Attempting to add a phone for userName : " + userName);
        return phoneService.addPhoneToUser(userName, phone);
    }

    /*
     *  Delete a user's phone
     */
    @DeleteMapping("/deletePhone")
    public ResponseEntity<String> deletePhone(@RequestParam("userName") String userName,
                                              @RequestParam("phoneNo") String number){
        logger.debug("Attempting to delete a phone for userName : " + userName + " with the number : " + number);
        boolean isDeleted = phoneService.deletePhoneFromUser(userName, number);
        if(isDeleted){
            return new ResponseEntity<>("Phone Successfully deleted.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Phone or User incorrect.", HttpStatus.NOT_FOUND);
        }
    }

    /*
     *  List a user's phones
     */
    @GetMapping("/findAllPhones")
    public List<Phone> findPhonesForUser(@RequestParam("userName") String userName){
        logger.debug("Attempting to find all phones for userName : " + userName);
        return phoneService.findAllPhonesForUser(userName);
    }
}

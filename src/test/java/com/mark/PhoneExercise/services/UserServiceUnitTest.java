package com.mark.PhoneExercise.services;


import com.mark.PhoneExercise.model.Phone;
import com.mark.PhoneExercise.model.User;
import com.mark.PhoneExercise.repository.PhoneRepository;
import com.mark.PhoneExercise.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @InjectMocks
    UserService userService = new UserService();
    @Mock
    private PhoneRepository phoneRepository;
    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void setPreferredNoForUser_NoUserFound() {
        // No user found
        ResponseEntity<String> actualResult = userService.setPreferredNoForUser("", "");
        assertTrue(actualResult.getBody().equals("No user found with this user name"));
        assertTrue(actualResult.getStatusCode().equals(HttpStatus.NOT_FOUND));
    }

    @Test
    public void setPreferredNoForUser_NoPhonesFound(){
        // User found but no phones linked to that user
        Mockito.when(userRepository.findByUserName(anyString())).thenReturn(mock(User.class));
        Mockito.when(phoneRepository.findByUser(any())).thenReturn(Collections.emptyList()) ;
        ResponseEntity<String> actualResult = userService.setPreferredNoForUser("", "");
        assertTrue(actualResult.getBody().equals("This user has no phones linked to them so unable to set preferred number."));
        assertTrue(actualResult.getStatusCode().equals(HttpStatus.NOT_FOUND));
    }

    @Test
    public void setPreferredNoForUser_NumberPassedNotMatchingPhoneListForUser(){
        Phone phone = Phone.builder().phoneNumber("1").build();
        List<Phone> phoneList = new ArrayList<>();
        phoneList.add(phone);

        // User found and phones found but number passed is not same as phone found
        Mockito.when(userRepository.findByUserName(anyString())).thenReturn(mock(User.class));
        Mockito.when(phoneRepository.findByUser(any())).thenReturn(phoneList) ;
        ResponseEntity<String> actualResult = userService.setPreferredNoForUser("", "2");
        assertTrue(actualResult.getBody().equals("This number is not linked to this user"));
        assertTrue(actualResult.getStatusCode().equals(HttpStatus.NOT_FOUND));
    }

    @Test
    public void setPreferredNoForUser_Success(){
        Phone phone = Phone.builder().phoneNumber("1").build();
        List<Phone> phoneList = new ArrayList<>();
        phoneList.add(phone);

        // User found and phone found but number passed is not same as phone found
        Mockito.when(userRepository.findByUserName(anyString())).thenReturn(mock(User.class));
        Mockito.when(phoneRepository.findByUser(any())).thenReturn(phoneList) ;
        ResponseEntity<String> actualResult = userService.setPreferredNoForUser("", "1");
        assertTrue(actualResult.getBody().equals("Successfully updated."));
        assertTrue(actualResult.getStatusCode().equals(HttpStatus.OK));
    }
}

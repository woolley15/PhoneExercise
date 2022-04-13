package com.mark.PhoneExercise.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mark.PhoneExercise.model.Phone;
import com.mark.PhoneExercise.model.PhoneModels;
import com.mark.PhoneExercise.model.User;
import com.mark.PhoneExercise.repository.PhoneRepository;
import com.mark.PhoneExercise.repository.UserRepository;
import com.mark.PhoneExercise.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

@AutoConfigureMockMvc
@SpringBootTest
public class PhoneControllerTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PhoneRepository phoneRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PhoneController phoneController;

    /*
     * Tests adding a phone to a user
     */
    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    public void addPhoneTest() throws Exception {
        userRepository.deleteAll();
        User userForPhoneToBeAddedTo = User.builder().userName("TestUser").emailAddress("test@test.com").password("admin").build();
        userRepository.save(userForPhoneToBeAddedTo);
        Phone phoneToBeCreated = Phone.builder().phoneName("TestPhone").phoneNumber("1").phoneModel(PhoneModels.ANDROID).userId(userForPhoneToBeAddedTo.getUserId()).build();
        String inputJson = mapToJson(phoneToBeCreated);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/phone/addPhone/TestUser")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        assertTrue(mvcResult.getResponse().getStatus()== HttpStatus.CREATED.value());
        List<Phone> phoneList = phoneRepository.findByUser(userForPhoneToBeAddedTo.getUserId());
        assertTrue(phoneList.size()==1);
        assertTrue(phoneList.get(0).getPhoneName().equals("TestPhone"));
        assertTrue(phoneList.get(0).getPhoneNumber().equals("1"));
        assertTrue(phoneList.get(0).getPhoneModel().equals(PhoneModels.ANDROID));
        userRepository.deleteAll();
    }

    /*
     * Tests deletion of a phone
     */
    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    public void deletePhoneTest() throws Exception {
        User user = User.builder().userName("TestUser").emailAddress("test@test.com").password("admin").build();
        userRepository.save(user);
        Phone phone = Phone.builder().phoneModel(PhoneModels.ANDROID).phoneName("TEST1").phoneNumber("987654321").userId(user.getUserId()).build();
        phoneRepository.save(phone);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/phone/deletePhone?userName=TestUser&phoneNo=987654321")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        assertTrue(mvcResult.getResponse().getStatus()==HttpStatus.OK.value());
        List<Phone> phoneList = phoneRepository.findByUser(user.getUserId());
        assertTrue(phoneList.size()==0);
    }

    /*
     * Tests generating list of user phones
     */
    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    public void findPhonesForUserTest() throws Exception {
        User user = User.builder().userName("TestUser2").emailAddress("test@test2.com").password("admin").build();
        userRepository.save(user);
        for(int i = 0; i <3; i ++){
            Phone phone = Phone.builder().phoneModel(PhoneModels.DESK_PHONE).phoneName("TEST"+i).phoneNumber("087123123"+i).userId(user.getUserId()).build();
            phoneRepository.save(phone);
        }

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/phone/findAllPhones/?userName=TestUser2")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String contentString = mvcResult.getResponse().getContentAsString();
        Phone[] phoneArr = mapFromJson(contentString, Phone[].class);
        assertTrue(phoneArr.length == 3);
    }

    /*
     * Test to make sure Basic Auth is working
     */
    @Test
    public void findPhonesForUserTest_NoBasicAuth() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/phone/findAllPhones/?userName=TestUser2")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertTrue(mvcResult.getResponse().getStatus()== HttpStatus.UNAUTHORIZED.value());
    }

    /*
     * Convert JSON String to java objects
     */
    public <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        return new ObjectMapper().readValue(json, clazz);
    }
    /*
     * Convert object into JSON
     */
    protected String mapToJson(Object obj) throws JsonProcessingException {
        return  new ObjectMapper().writeValueAsString(obj);
    }

}

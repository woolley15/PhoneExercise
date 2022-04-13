package com.mark.PhoneExercise.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mark.PhoneExercise.model.Phone;
import com.mark.PhoneExercise.model.PhoneModels;
import com.mark.PhoneExercise.model.User;
import com.mark.PhoneExercise.repository.PhoneRepository;
import com.mark.PhoneExercise.repository.UserRepository;
import com.mark.PhoneExercise.services.UserService;
import org.hibernate.PropertyValueException;
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
public class UserControllerTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PhoneRepository phoneRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserController userController;

    /*
     * Tests creation of a new user
     */
    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    public void createUserTest_Success() throws Exception {
        userRepository.deleteAll();
        User userToBeCreated = User.builder().userName("Test").emailAddress("test@test.com").password("admin").build();
        String inputJson = mapToJson(userToBeCreated);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/user/createUser/")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        assertTrue(mvcResult.getResponse().getStatus()==HttpStatus.CREATED.value());
        List<User> userPersisted = userRepository.findAll();
        assertTrue(userPersisted.size()==1);
        assertTrue(userPersisted.get(0).getUserName().equals("Test"));
        assertTrue(userPersisted.get(0).getEmailAddress().equals("test@test.com"));
        assertTrue(userPersisted.get(0).getPassword().equals("admin"));
        userRepository.deleteAll();
    }

    /*
     * Tests deletion of user and ensures it deletes all phones linked to it
     */
    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    public void deleteUserTest_Success() throws Exception {
        User user = User.builder().userName("ToBeDeleted").emailAddress("test@test.com").password("admin").build();
        User toBeDeleted =userRepository.save(user);
        Phone phone1 = Phone.builder().phoneModel(PhoneModels.ANDROID).phoneName("TEST1").phoneNumber("123456789").userId(toBeDeleted.getUserId()).build();
        Phone phone2 = Phone.builder().phoneModel(PhoneModels.DESK_PHONE).phoneName("TEST2").phoneNumber("987654321").userId(toBeDeleted.getUserId()).build();
        phoneRepository.save(phone1);
        phoneRepository.save(phone2);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/user/deleteUser/ToBeDeleted")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        assertTrue(mvcResult.getResponse().getStatus()==HttpStatus.OK.value());
        assertTrue(userRepository.findByUserName("ToBeDeleted")==null);
        List<Phone> phoneList = phoneRepository.findByUser(toBeDeleted.getUserId());
        assertTrue(phoneList.size()==0);
    }

    /*
     * Tests to return a list of all users
     */
    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    public void findAllTest() throws Exception {
        userRepository.deleteAll();
        for(int i = 0; i <3; i ++){
            User user = User.builder().userName("Test"+i).emailAddress("test@test.com"+i).password("admin"+i).build();
            userRepository.save(user);
        }

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/user/findAll")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String contentString = mvcResult.getResponse().getContentAsString();
        User[] userArr = mapFromJson(contentString, User[].class);
        assertTrue(userArr.length == 3);
    }


    /*
     * Tests updating of a users Preferred Number
     */
    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    public void setUserPreferredNoTest() throws Exception {
        User user = User.builder().userName("User1").emailAddress("test@test.com").password("admin").preferredPhoneNumber("1").build();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/user/User1/setPreferredNumber&number=2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

    }

    /*
     * Test GlobalException handler for when a mandatory field in not passed in Request Body (PropertyValueException) to create a user. (No email)
     */
    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    public void createUserTest_Throw_PropertyValueException() throws Exception {
        String exceptionMessage = "emailAddress cannot be null for entity  : User.emailAddressUser";
        User userToBeCreated = User.builder().userName("Test").password("admin").build();
        String inputJson = mapToJson(userToBeCreated);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/user/createUser/")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        assertTrue(mvcResult.getResponse().getStatus()==HttpStatus.NOT_FOUND.value());
        assertTrue(mvcResult.getResolvedException() instanceof PropertyValueException);
        assertTrue(mvcResult.getResponse().getContentAsString().contains(exceptionMessage));
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

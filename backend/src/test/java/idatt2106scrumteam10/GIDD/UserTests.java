package idatt2106scrumteam10.GIDD;

import idatt2106scrumteam10.GIDD.models.User;
import idatt2106scrumteam10.GIDD.repos.UserRepository;
import idatt2106scrumteam10.GIDD.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class) // JUnit
@SpringBootTest(webEnvironment = MOCK, classes = GiddApplication.class) // Spring
@AutoConfigureMockMvc
class UserTests {

	@Autowired
    MockMvc mockMvc;

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRep;
	

	final String password = "123";
	final String email = "a@b";

	@Test
	void userEqualsUnitTest() {
		User user1 =  new User("email", "password", "navn", "navn", LocalDate.now(), 1, true);
		User user2 =  new User("email", "ikkepassword", "Samme", "Email", LocalDate.now(), 2, true);
		assertTrue(user1.equals(user2));
	}

	@Test
	void userRegisterEndpointTest() throws Exception {
		String postString = "{\" firstName\" :\"a\" ,\"lastname\":\"b\",\"dateOfBirth\":\"1991-04-17\",\"email\":\""+email+"\",\"password\":\""+password+"\",\"rePassword\":\""+password+"\",\"intensity\":2,\"public\":true}";
		ResultActions result = mockMvc.perform(post("/register")
			.content(postString)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON));

		// Tests if is ok, and that certain elements are there or is lacking
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.email").value(email));
		result.andExpect(jsonPath("$.password").doesNotExist());
	}

	@Test
	void userLoginEndpointTest() throws Exception {
		mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .content(buildUrlEncodedFormEntity(
			"username", email, 
			"password", password
  		  ))).andExpect(status().isOk());
	}

	@Test
	void userCreateServiceTest() {
		User user =  new User("b@a", "password", "navn", "navn", LocalDate.now(), 1, true);
		assertTrue(userService.createUser(user) != null);
	}

	@Test
	@WithUserDetails("jens@loe")
	void userGetCurrentUserTest() {
		assertTrue(userService.getUser() != null);
		assertTrue(userService.getUser().getEmail().equals("jens@loe"));
	}
		
	@Test
	@WithUserDetails("jens@loe")
	void userGetEndpointTest() throws Exception {
		mockMvc.perform(get("/profile")).andExpect(status().isOk());
	}

	private String buildUrlEncodedFormEntity(String... params) {
		if( (params.length % 2) > 0 ) {
		   throw new IllegalArgumentException("Need to give an even number of parameters");
		}
		StringBuilder result = new StringBuilder();
		for (int i=0; i<params.length; i+=2) {
			if( i > 0 ) {
				result.append('&');
			}
			try {
				result.
				append(URLEncoder.encode(params[i], StandardCharsets.UTF_8.name())).
				append('=').
				append(URLEncoder.encode(params[i+1], StandardCharsets.UTF_8.name()));
			}
			catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		return result.toString();
	 }

}

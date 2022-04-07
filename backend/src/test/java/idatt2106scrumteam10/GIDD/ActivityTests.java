package idatt2106scrumteam10.GIDD;

import idatt2106scrumteam10.GIDD.models.Activity;
import idatt2106scrumteam10.GIDD.models.Location;
import idatt2106scrumteam10.GIDD.models.Tag;
import idatt2106scrumteam10.GIDD.repos.TagRepository;
import idatt2106scrumteam10.GIDD.services.ActivityService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class) // JUnit
@SpringBootTest(webEnvironment = MOCK, classes = GiddApplication.class) // Spring
@AutoConfigureMockMvc
class ActivityTests {

	@Autowired
    MockMvc mockMvc;

	@Autowired
	ActivityService activityService;

	@Autowired
	TagRepository tagRepository;

	Location location;
	Activity activity;
	Activity activity2;

	@Before
	void beforeTests() {
		tagRepository.save(new Tag("Test"));
	}

	@BeforeEach
    void createObjects() {
		List<String> equipments = new ArrayList<>();
		Set<Tag> tags = new HashSet<Tag>();
		for (Tag t : tagRepository.findAll()) tags.add(t);
		equipments.add("1 Kul ting");
		location = new Location("tomt", 63.2, 10.3);
		activity2 = new Activity(
			"Best Activity", 
			"This is my first and best thingy planned.", 
			LocalDateTime.now(),
			LocalDateTime.now().plusDays(5),
			12,
			new Location("tomt", 63.1, 10.3), 
			1,
			tags,
			equipments);

		
		activity = new Activity(
			"Worst Activity", 
			"This is my first and best thingy planned.", 
			LocalDateTime.now(),
			LocalDateTime.now(),
			12,
			location, 
			1,
			new HashSet<Tag>(),
			new ArrayList<String>());
    }

	@Test
	@WithUserDetails("jens@loe")
	void createActivityEndPointTest() throws Exception{
        mockMvc.perform(post("/activity")
                .content(activity.PostJSONString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Worst Activity"));
	}

	@Test
	@WithUserDetails("jens@loe")
	void createAndDeleteActivityIntegrationTest() throws Exception{
		
		activityService.createActivity(activity);
		assertTrue(activity.getID() != null);
		assertTrue(activity.getCreatedBy() != null);

		activityService.deleteActivity(activity.getID());
		try {
			activityService.findById(activity.getID());
			fail();
		} catch (ResponseStatusException e) {
		}
	}


	@Test
	@WithUserDetails("jens@loe")
	void getActivitiesEndpointTest() throws Exception{
		mockMvc.perform(get("/activities").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk()).andExpect(jsonPath("$['content']", hasSize(greaterThanOrEqualTo(1))));
	}

	@Test
	@WithUserDetails("jens@loe")
	void filterActivitiesIntegrationTest() throws Exception{
		MvcResult result = mockMvc.perform(post("/activity").content(activity2.PostJSONString()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		String a = result.getResponse().getContentAsString();
		int id = Integer.parseInt(a.substring(a.length() - 3, a.length() - 1));

		mockMvc.perform(get("/activities")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$['content']", hasSize(greaterThanOrEqualTo(1))))
			.andExpect(jsonPath("$['content'][0].title").value("Konsert med DDE"));

		mockMvc.perform(delete("/activity/"+id))
				.andExpect(status().isOk()).andExpect(content().string("true"));
	}


	@Test
	@WithUserDetails("jens@loe")
	void createEditDeleteActivityIntegrationTest() throws Exception{
		MvcResult result = mockMvc.perform(post("/activity").content(activity.PostJSONString()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		String a = result.getResponse().getContentAsString();
		String id = a.substring(a.length() - 3, a.length() - 1);

		mockMvc.perform(get("/activity/"+id)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Worst Activity"));

		mockMvc.perform(put("/activity/"+id)
			.content(activity2.PostJSONString())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Best Activity"));

		mockMvc.perform(delete("/activity/"+id))
			.andExpect(status().isOk()).andExpect(content().string("true"));
	}

}

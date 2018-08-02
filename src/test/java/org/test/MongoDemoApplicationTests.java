package org.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoDemoApplicationTests {

	@Test
	public void testPojoToJson() throws IOException {
		Account a = new Account();
		a.setUserName("blah");
		a.setEmail("blah@no.com");
		a.setLastName("gates");
		ObjectMapper om = new ObjectMapper();
		String jsonString = om.writeValueAsString(a);
		System.out.println(jsonString);

		Account a1 = om.readValue(jsonString, Account.class);
		a1.getEmail();
	}

//	@Test
//	public void testPojoToJson2() throws IOException {
//		Account a = new Account();
//		Position b = new Position();
//		b.setTitle("Junior Software Developer");
//		b.setSalary("80000");
//		b.setRecommendedExperience("100,000 thousand years");
//		b.setLocation("Lol, get lost");
//		b.setLevel("2....million");
//		b.setDescription("We don't need no stinkin description");
//		a.setLastName("Gates");
//		a.setFirstName("Sean");
//		a.setEmail("noneOfYourBusinessMan@getLost.com");
//		a.setUserName("smgates");
//		a.setAge("21");
//		a.setDegree("B.A Computer Science");
//		a.setPassword("We don't need no stinkin passwords!");
//		a.setPhoneNumber("1-800-Leave-Me-Alone");
//		a.setPositions(b, a);
//		ObjectMapper om = new ObjectMapper();
//		String jsonString = om.writeValueAsString(a);
//		System.out.println(jsonString);
//
//		Account a1 = om.readValue(jsonString, Account.class);
//		a1.getPositions();
	}


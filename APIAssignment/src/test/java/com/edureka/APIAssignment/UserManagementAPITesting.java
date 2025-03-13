package com.edureka.APIAssignment;

import static io.restassured.RestAssured.*;// This will allow Rest assured methods without class reference
import static org.hamcrest.Matchers.*;// When you want to use assertions you can add these hamcrest matchers which will make our tests more readible  
import org.testng.annotations.BeforeClass;// JUnit annotation to  run setup code before test methods
import org.testng.annotations.Test;// JUnit annotation to define test methods
import java.io.File;// this will allow you to read a json file which could be in different 


/**
 * Unit test for simple App.
 */
public class UserManagementAPITesting {

    /**
     * @Beforemoethod is being used here to create a base URI. This will execute once before any test methods run.
     * 
     * 
     */
	
	@BeforeClass
	public static void setup() {
		
		baseURI= "https://fakestoreapi.com";
	}
    @Test
    public void CreateNewUser() {
    	File jsonFile = new File("/Users/raghava/eclipse-workspace/APIAssignment/src/test/resources/JsonFiles.json");
        given().log().all().header("Content-Type", "application/json").body(jsonFile)
        .when().post("/users").then().log().all().assertThat().statusCode(200);
        System.out.println("=============================================================================================");
    }
    
    @Test(priority=1)
    	public void ValidateNewUser() {
    		given().log().all().when().get("https://fakestoreapi.com/users/1").then().log().all().assertThat().statusCode(200);
    		System.out.println("=============================================================================================");
    	}
    
    private String token;
    	
    @Test(priority=2)
    	public void LoginNewUser() {
    	    given()
    	            .log().all()
    	            .header("Content-Type", "application/json")
    	            .auth().basic("johnd", "m38rmF$") // Authenticate first
    	            .body("{\"username\": \"johnd\", \"password\": \"m38rmF$\"}") 
    	            .when()
    	            .post("/auth/login")
    	            .then()
    	            .log().all()
    	            .assertThat()
    	            .statusCode(200).extract().path("token");
    	    System.out.println("=============================================================================================");    
    	}
    }
   

	


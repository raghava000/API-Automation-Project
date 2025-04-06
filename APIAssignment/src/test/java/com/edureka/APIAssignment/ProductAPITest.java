package com.edureka.APIAssignment; // Declares the package where this class belongs.

import static io.restassured.RestAssured.*; // Imports RestAssured static methods for fluent API calls.
import static org.hamcrest.Matchers.*; // Imports Hamcrest matchers for assertions in RestAssured.

import org.apache.commons.io.FileUtils; // Utility class for file handling.
import org.json.JSONException; // Exception handling for JSON operations.
import org.json.JSONObject; // JSON object handling.
import org.testng.Assert; // TestNG assertion class.
import org.testng.annotations.BeforeClass; // TestNG annotation to run a setup method before any test.
import org.testng.annotations.Test; // Marks a method as a TestNG test.

import io.restassured.RestAssured; // Main class for RestAssured API testing.

import java.io.File; // File handling class.
import java.io.IOException; // Exception handling for IO operations.
import java.nio.charset.StandardCharsets; // Ensures proper character encoding.

public class ProductAPITest { // Defines the class for API testing.

    public static Long createProductId; // Stores the ID of the created product for later use.

    @BeforeClass // Runs before any test in this class.
    public void setupAndInitialize() throws IOException {
        // Set the base URI for API requests.
        baseURI = "http://localhost:3000"; 

        // Clear existing products before creating a new one.
        System.out.println("Attempting to clear products.json");
        RestAssured.given()
            .when()
            .delete("/products") // Sends a DELETE request to remove all products.
            .then()
            .statusCode(200); // Ensures the deletion was successful.
        System.out.println("Finished attempting to clear products.json");

        // Calls createNewProduct() to ensure a product exists before running dependent tests.
        createNewProduct(); 
    }

    @Test // Marks this method as a TestNG test case.
    public void createNewProduct() throws IOException {
        // Path to the JSON file containing product data.
        File jsonFile = new File("src/test/resources/Addnewproduct.json");

        // Reads the file content into a string with UTF-8 encoding.
        String jsonContent = FileUtils.readFileToString(jsonFile, StandardCharsets.UTF_8);
        System.out.println("JSON Content being sent: " + jsonContent); // Logs the JSON data for debugging.

        // Sends a POST request to create a new product and stores the response ID.
        createProductId = given() // Starts the request setup.
                .log().all() // Logs request details for debugging.
                .header("Content-Type", "application/json") // Sets the request header to indicate JSON data.
                .body(jsonContent) // Attaches the JSON payload from the file.
                .when()
                .post("/products/") // Sends a POST request to create a product.
                .then()
                .log().all() // Logs response details.
                .assertThat()
                .statusCode(201) // Asserts that the response status is 201 (Created).
                .extract().path("id"); // Extracts the "id" field from the response and stores it in createProductId.

        // Logs the created product ID for verification.
        System.out.println("=============================================================================================");
        System.out.println("Created Product ID: " + createProductId);
    }

    @Test(dependsOnMethods = "createNewProduct") // Runs this test only after createNewProduct() passes.
    public void getProductDetails() {
        if (createProductId != null) { // Ensures createProductId is not null before proceeding.
            String response = given()
                .log().all() // Logs request details.
                .when()
                .get("/products/" + createProductId) // Sends a GET request to retrieve product details.
                .then()
                .log().all() // Logs response details.
                .assertThat()
                .statusCode(200) // Asserts that the response status is 200 (OK).
                .extract().asString(); // Extracts the entire response as a string.

            // Logs the retrieved product details.
            System.out.println("Response Body: " + response);
        } else {
            // Logs and fails the test if createProductId is null.
            System.out.println("createProductId is null. Skipping getProductDetails test.");
            Assert.fail("createProductId is null. createNewProduct test failed."); // Fails the test if product creation failed.
        }
    }
}

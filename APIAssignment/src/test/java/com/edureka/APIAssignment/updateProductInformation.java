package com.edureka.APIAssignment; // Declares the package where this class belongs.

import static io.restassured.RestAssured.*; // Imports RestAssured static methods for making API requests.

import org.apache.commons.io.FileUtils; // Utility class for handling file operations.

import org.testng.annotations.BeforeClass; // Marks a method to be executed before the first test in this class.
import org.testng.annotations.Test; // Marks a method as a TestNG test case.

import java.io.File; // Handles file operations.
import java.io.IOException; // Handles exceptions related to file operations.
import java.nio.charset.StandardCharsets; // Ensures proper character encoding when reading files.

import io.restassured.RestAssured; // Main class of RestAssured for API testing.

public class updateProductInformation { // Defines the class for updating product details.
    
    private ProductAPITest ProductAPITest; // Creates an instance of ProductAPITest to access its methods and variables.

    @BeforeClass // Runs before any test method in this classs.s
    public void setup() throws IOException {
        // Creates a new instance of ProductAPITestss.
        ProductAPITest = new ProductAPITest();
        
        // Calls setupAndInitialize() from ProductAPITest to ensure the baseURI is set and a product is created.
        ProductAPITest.setupAndInitialize();
    }

    @Test(dependsOnMethods = "com.edureka.APIAssignment.ProductAPITest.createNewProduct") // Ensures this test runs only after createNewProduct().
    public void updateInformation() throws IOException {
        // Checks if createProductId is null, which would indicate that product creation failed.
        if (ProductAPITest.createProductId == null) {
            throw new IllegalStateException("createProductId is null. Ensure createNewProduct() ran successfully.");
        }

        // Path to the JSON file containing the updated product information.
        File jsonFile = new File("/Users/raghava/eclipse-workspace/APIAssignment/src/test/resources/custom-mock-server/custom-mock-server/Updateproduct.json");

        // Reads the content of the file into a String with UTF-8 encoding.
        String jsonContent = FileUtils.readFileToString(jsonFile, StandardCharsets.UTF_8);

        // Sends a PUT request to update the product with the new information.
        String response = given()
                .log().all() // Logs request details.
                .header("Content-Type", "application/json") // Sets the request header to indicate JSON data.
                .body(jsonContent) // Sends the JSON content read from the file.
                .when()
                .put("/products/" + ProductAPITest.createProductId) // Sends a PUT request to update the specific product.
                .then()
                .log().all() // Logs response details.
                .assertThat()
                .statusCode(200) // Ensures the response status is 200 (OK), indicating a successful update.
                .extract().asString(); // Extracts the response as a string.

        // Logs the response body for verification.
        System.out.println("Response Body: " + response);
    }
}

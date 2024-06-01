package cucumber.stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import records.Pet;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

/**
 * This class contains the step definitions for the Pet Store API tests.
 */
public class PetStoreSteps {

    private Pet pet;
    private Response response;
    private static final Map<Integer, Pet> pets = new HashMap<>();

    /**
     * A Given step that creates a new Pet object with the provided id, name, and status, and adds it to the pets map.
     */
    @Given("I have a pet with id {string}, name {string}, and status {string}")
    public void i_have_a_pet_with_id_name_and_status(String id, String name, String status) {
        pet = new Pet(Integer.parseInt(id), name, status);
        pets.put(pet.id(), pet);
    }

    /**
     * A Given step that retrieves a Pet object with the provided id from the pets map.
     */
    @Given("I have a pet with id {string}")
    public void i_have_a_pet_with_id(String id) {
        pet = pets.get(Integer.parseInt(id));
    }

    /**
     * A When step that sends a POST request to the Pet Store API to create the pet.
     */
    @When("I create the pet")
    public void i_create_the_pet() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .post("https://petstore.swagger.io/v2/pet");
    }

    /**
     * A When step that sends a GET request to the Pet Store API to read the pet.
     */
    @When("I read the pet")
    public void i_read_the_pet() {
        response = RestAssured.given()
                .pathParam("petId", pet.id())
                .when()
                .get("https://petstore.swagger.io/v2/pet/{petId}");
    }

    /**
     * A When step that updates the name and status of the pet and sends a PUT request to the Pet Store API to update the pet.
     */
    @When("I update the pet with name {string} and status {string}")
    public void i_update_the_pet_with_name_and_status(String name, String status) {
        pet.setName(name);
        pet.setStatus(status);
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .put("https://petstore.swagger.io/v2/pet");
    }

    /**
     * A When step that sends a DELETE request to the Pet Store API to delete the pet.
     */
    @When("I delete the pet")
    public void i_delete_the_pet() {
        response = RestAssured.given()
                .pathParam("petId", pet.id())
                .when()
                .delete("https://petstore.swagger.io/v2/pet/{petId}");
    }

    /**
     * A Then step that asserts that the id, name, and status in the response match those of the pet.
     */
    @Then("the response should contain the updated pet details")
    public void the_response_should_contain_the_updated_pet_details() {
        assertEquals(pet.id(), response.jsonPath().getInt("id"));
        assertEquals(pet.name(), response.jsonPath().getString("name"));
        assertEquals(pet.status(), response.jsonPath().getString("status"));
    }

    /**
     * A Then step that sends a GET request to the Pet Store API to check that the pet no longer exists.
     */
    @Then("the pet should no longer exist")
    public void the_pet_should_no_longer_exist() {
        Response getResponse = RestAssured.given()
                .pathParam("petId", pet.id())
                .when()
                .get("https://petstore.swagger.io/v2/pet/{petId}");
        assertNotEquals(200, getResponse.getStatusCode());
    }

    /**
     * A Then step that asserts that the status code of the response matches the expectedStatus.
     */
    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int expectedStatus) {
        assertEquals(expectedStatus, response.getStatusCode());
    }

    /**
     * A Then step that asserts that the Pet object in the response matches the pet.
     */
    @Then("the response should contain the pet details")
    public void theResponseShouldContainThePetDetails() {
        Pet actualPet = response.as(Pet.class);
        assertEquals(pet, actualPet);
    }
}
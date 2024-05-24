import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hillel.enums.PetStoreEnums;
import org.hillel.records.Pet;
import org.junit.jupiter.api.*;
import org.testng.asserts.SoftAssert;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PetStoreTest {

    private Pet pet;
    private Properties prop;
    private SoftAssert softAssert;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        prop = new Properties();
        objectMapper = new ObjectMapper();
        softAssert = new SoftAssert();
        try {
            FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/config.properties");
            prop.load(ip);
        } catch (IOException e) {
            e.printStackTrace();
        }

        RestAssured.baseURI = prop.getProperty("baseURI");

        pet = new Pet(Integer.parseInt(prop.getProperty("petId")), prop.getProperty("petName"), prop.getProperty("petStatus"));
    }

    @Test
    @Order(1)
    public void createPet() {
        pet = new Pet(Integer.parseInt(prop.getProperty("petId")), prop.getProperty("petName"), prop.getProperty("petStatus"));
        String petBody = null;
        try {
            petBody = objectMapper.writeValueAsString(pet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Response response = given().contentType(ContentType.JSON)
                .body(petBody)
                .when().post(PetStoreEnums.PET.getPath())
                .then().statusCode(200)
                .extract().response();
        softAssert = new SoftAssert();
        softAssert.assertEquals(pet.id(), response.jsonPath().getInt("id"), "Pet ID does not match the expected value");
        softAssert.assertAll();
        System.out.println("Created Pet JSON: " + response.asString());
    }

    @Test
    @Order(2)
    public void readPet() {
        Response response = given().pathParam("petId", pet.id())
                .when().get(PetStoreEnums.PET_BY_ID.getPath())
                .then().statusCode(200)
                .extract().response();
        String petName = response.jsonPath().getString("name");
        softAssert.assertEquals(pet.name(), petName, "Pet name does not match the expected value");
        softAssert.assertAll();
        System.out.println("Read Pet JSON: " + response.asString());
    }

    @Test
    @Order(3)
    public void updatePet() {
        pet = new Pet(pet.id(), prop.getProperty("updatedPetName"), prop.getProperty("updatedPetStatus"));
        String updatedPetBody = null;
        try {
            updatedPetBody = objectMapper.writeValueAsString(pet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Response response = given().contentType(ContentType.JSON)
                .body(updatedPetBody)
                .when().put(PetStoreEnums.PET.getPath())
                .then().statusCode(200)
                .extract().response();
        String updatedPetName = response.jsonPath().getString("name");
        String updatedPetStatus = response.jsonPath().getString("status");
        softAssert.assertEquals(pet.name(), updatedPetName, "Updated pet name does not match the expected value");
        softAssert.assertEquals(pet.status(), updatedPetStatus, "Updated pet status does not match the expected value");
        softAssert.assertAll();
        System.out.println("Updated Pet JSON: " + response.asString());
    }

    @Test
    @Order(4)
    public void deletePet() {
        given().pathParam("petId", pet.id())
                .when().delete(PetStoreEnums.PET_BY_ID.getPath())
                .then().statusCode(200);
        Response response = given().pathParam("petId", pet.id())
                .when().get(PetStoreEnums.PET_BY_ID.getPath())
                .then().extract().response();
        softAssert.assertEquals(response.statusCode(), 404, "Pet was not deleted successfully");
        softAssert.assertTrue(response.asString().contains("Pet not found"), "Response body does not contain 'Pet not found'");
        softAssert.assertAll();
        System.out.println("Deleted Pet Response: " + response.asString());
    }
}
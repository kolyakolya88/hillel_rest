package enums;

/**
 * This enum represents the different API paths for the Pet Store.
 */
public enum PetStoreEnums {
    PET("/pet"),
    PET_BY_ID("/pet/{petId}");

    private final String path;

    /**
     * Constructor for the enum constants.
     * @param path The API path associated with the enum constant.
     */
    PetStoreEnums(String path) {
        this.path = path;
    }

    /**
     * Getter for the API path associated with the enum constant.
     * @return The API path associated with the enum constant.
     */
    public String getPath() {
        return path;
    }
}
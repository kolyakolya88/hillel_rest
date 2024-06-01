package records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This record represents a Pet in the Pet Store.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Pet(int id, String name, String status) {
    public void setName(String name) {

    }

    public void setStatus(String status) {

    }

    public void setId(String id) {

    }
}
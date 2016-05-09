package accounting.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import javax.validation.constraints.Min;

/**
 * Just a basic POJO to represent the body of a request to the /inventory endpoint.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryRequest {

    @Min(0)
    private double costOfGoods;
    private String description;

    public InventoryRequest(@JsonProperty("costOfGoods") double costOfGoods,
                            @JsonProperty("description") String description) {
        this.costOfGoods = costOfGoods;
        this.description = description;
    }

    public double getCostOfGoods() {
        return costOfGoods;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("pay", costOfGoods)
                .add("description", description)
                .toString();
    }
}

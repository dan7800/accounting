package accounting.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import javax.validation.constraints.Min;

/**
 * Just a basic POJO to represent the body of a request to the /sale endpoint.
 */
public class SaleRequest {

    @Min(0)
    private double costOfGoodsSold;

    @Min(0)
    private double salePrice;
    private String description;

    public SaleRequest(@JsonProperty("costOfGoodsSold") double costOfGoodsSold,
                       @JsonProperty("salePrice") double salePrice,
                       @JsonProperty("description") String description) {
        this.costOfGoodsSold = costOfGoodsSold;
        this.salePrice = salePrice;
        this.description = description;
    }

    public double getCostOfGoodsSold() {
        return costOfGoodsSold;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("costOfGoodsSold", costOfGoodsSold)
                .add("salePrice", salePrice)
                .add("description", description)
                .toString();
    }
}

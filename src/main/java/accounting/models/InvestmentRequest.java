package accounting.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public class InvestmentRequest {

    private double amount;
    private String description;

    public InvestmentRequest(@JsonProperty("amount") double amount,
                             @JsonProperty("description") String description) {
        this.amount = amount;
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("amount", amount)
                .add("description", description)
                .toString();
    }
}

package accounting.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import javax.validation.constraints.Min;

public class RefundRequest {

    @Min(0)
    private double refundAmount;

    @Min(0)
    private double valueOfReturns;
    private String description;

    public RefundRequest(@JsonProperty("refundAmount") double refundAmount,
                         @JsonProperty("valueOfReturns") double valueOfReturns,
                         @JsonProperty("description") String description) {
        this.refundAmount = refundAmount;
        this.valueOfReturns = valueOfReturns;
        this.description = description;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public double getValueOfReturns() {
        return valueOfReturns;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("refundAmount", refundAmount)
                .add("valueOfReturns", valueOfReturns)
                .add("description", description)
                .toString();
    }
}

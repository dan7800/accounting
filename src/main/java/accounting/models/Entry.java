package accounting.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Entry {

    private long id;
    private long transactionId;
    private long toAccountId;
    private long fromAccountId;
    private double amount;

    public Entry(@JsonProperty("id") long id,
                 @JsonProperty("transactionId") long transactionId,
                 @JsonProperty("toAccountId") long toAccountId,
                 @JsonProperty("fromAccountId") long fromAccountId,
                 @JsonProperty("amount") double amount) {
        this.id = id;
        this.transactionId = transactionId;
        this.toAccountId = toAccountId;
        this.fromAccountId = fromAccountId;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public String getToAccount() { return Account.getName((int)this.getToAccountId());}

    public String getFromAccount() { return Account.getName((int)this.getFromAccountId());}

    public long getFromAccountId() {
        return fromAccountId;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("transactionId", transactionId)
                .add("toAccountId", toAccountId)
                .add("fromAccountId", fromAccountId)
                .add("amount", amount)
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if(other == null ||  !(other instanceof Entry)) {
            return false;
        } else {
            Entry e = (Entry) other;
            if (e.id != id) return false;
            if (e.transactionId != transactionId) return false;
            if (e.toAccountId != toAccountId) return false;
            if (e.fromAccountId != fromAccountId) return false;
            if (e.amount != amount) return false;

            return true;
        }
    }
}

package accounting.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public class Account {
    private long id;
    private String name;
    private double balance;

    public Account(@JsonProperty("id") long id,
                   @JsonProperty("name") String name,
                   @JsonProperty("balance") double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBalance() { return balance; }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("balance", balance)
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if(other == null ||  !(other instanceof Account)) {
            return false;
        } else {
            Account t = (Account) other;
            if (t.id != id) return false;
            if (!t.name.equals(name)) return false;
            if (t.balance != balance) return false;

            return true;
        }
    }
}

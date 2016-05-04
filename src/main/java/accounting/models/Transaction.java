package accounting.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.time.LocalDate;
import java.util.List;

public class Transaction {

    private long id;
    private LocalDate timestamp;
    private String description;
    private List<Entry> entries;

    public Transaction(@JsonProperty("id") long id,
                       @JsonProperty("timestamp") LocalDate timestamp,
                       @JsonProperty("description") String description,
                       @JsonProperty("entries") List<Entry> entries) {
        this.id = id;
        this.timestamp = timestamp;
        this.description = description;
        this.entries = entries;
    }

    public long getId() {
        return id;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("timestamp", timestamp)
                .add("description", description)
                .add("entries", entries)
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if(other == null ||  !(other instanceof Transaction)) {
            return false;
        } else {
            Transaction t = (Transaction) other;
            if (t.id != id) return false;
            if (!t.timestamp.equals(timestamp)) return false;
            if (!t.description.equals(description)) return false;
            if (!t.entries.equals(entries)) return false;

            return true;
        }
    }
}

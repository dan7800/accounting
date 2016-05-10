package accounting.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

    private long id;
    private Date timestamp;
    private String description;
    private List<Entry> entries;

    public Transaction(@JsonProperty("id") long id,
                       @JsonProperty("timestamp") Date timestamp,
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

    public Date getTimestamp() {
        return timestamp;
    }

    public String getTimestampString() { return timestamp.toString(); }

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

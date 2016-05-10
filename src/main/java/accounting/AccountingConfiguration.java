package accounting;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * This class is just sending back any objects that the module might need.
 */
public class AccountingConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();

    public DataSourceFactory getDatabase() {
        return database;
    }

    @NotNull
    @JsonProperty("inventoryKey")
    private String inventoryKey;

    public String getInventoryKey() { return inventoryKey; }

    @NotNull
    @JsonProperty("humanResourcesKey")
    private String humanResourcesKey;

    public String getHumanResourcesKey() { return humanResourcesKey; }

    @NotNull
    @JsonProperty("salesKey")
    private String salesKey;

    public String getSalesKey() { return salesKey; }

    @NotNull
    @JsonProperty("investmentKey")
    private String investmentKey;

    public String getInvestmentKey() {
        return investmentKey;
    }
}

package accounting;

import accounting.data.DummyTransactionDAO;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import accounting.data.TransactionDAO;
import org.skife.jdbi.v2.DBI;

import javax.inject.Named;

/**
 * Anything that needs to be provided by dependency injection will get provided
 * through here.  Whenever the @Inject annotation appears (e.g. in a resource
 * class), DropWizard will search the modules for @Provides methods that are of
 * the given type or that share an @Named string.
 */
public class AccountingModule implements Module {

    private DBI jdbi;

    public void configure(Binder binder) {}

    public void setJdbi(DBI jdbi) {
        this.jdbi = jdbi;
    }

    @Provides
    public TransactionDAO providesTransactionDAO() {
        //TODO Use acctual DAO: return jdbi.onDemand(TransactionDAO.class);
        return new DummyTransactionDAO();
    }

    @Provides
    @Named("inventoryKey")
    public String providesInventoryKey(AccountingConfiguration configuration) {
        return configuration.getInventoryKey();
    }

    @Provides
    @Named("humanResourcesKey")
    public String providesHumanResourcesKey(AccountingConfiguration configuration) {
        return configuration.getHumanResourcesKey();
    }

    @Provides
    @Named("salesKey")
    public String providesSalesKey(AccountingConfiguration configuration) {
        return configuration.getSalesKey();
    }

}

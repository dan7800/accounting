package accounting.models;

import com.hubspot.rosetta.annotations.RosettaCreator;
import com.hubspot.rosetta.annotations.RosettaValue;

public enum Account {
    UNKNOWN(0),
    EMPLOYEES(1),
    INVENTORY(2),
    CASH(3),
    REVENUES(4),
    COGS(5),
    SALES_TAX_PAYABLE(6),
    REFUNDS_PAID(7),
    INVESTMENT(8);

    private final int state;
    private final static String[] names = {
        "Unknown","Employees","Inventory","Cash","Revenues",
        "Cost of Goods Sold","Sales Tax Payable","Refunds Paid"};

    Account(int state) {
        this.state = state;
    }

    public static String getName(int state) {
        if (state < 0 || state > 7) state = 0;
        return names[state];
    }

    @RosettaValue
    public int getState() {
        return state;
    }

    @RosettaCreator
    public static Account fromInt(int state) {
        if (state > 8) return UNKNOWN;
        return Account.values()[state];
    }
}

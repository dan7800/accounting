package accounting.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hubspot.rosetta.annotations.RosettaCreator;
import com.hubspot.rosetta.annotations.RosettaValue;

@JsonIgnoreProperties(ignoreUnknown = true)
public enum AccountEnum {
    UNKNOWN(0),
    EMPLOYEES(1),
    INVENTORY(2),
    CASH(3),
    REVENUES(4),
    COGS(5),
    SALES_TAX_PAYABLE(6),
    REFUNDS_PAID(7),
    INVESTMENT(8);

    private final static int size = 8;
    private final int state;
    private final static String[] names = {
        "Unknown","Employees","Inventory","Cash","Revenues",
        "Cost of Goods Sold","Sales Tax Payable","Refunds Paid","Investment"};

    AccountEnum(int state) {
        this.state = state;
    }

    public static String getName(int state) {
        if (state < 0 || state > size) state = 0;
        return names[state];
    }

    @RosettaValue
    public int getState() {
        return state;
    }

    @RosettaCreator
    public static AccountEnum fromInt(int state) {
        if (state > size) return UNKNOWN;
        return AccountEnum.values()[state];
    }
}

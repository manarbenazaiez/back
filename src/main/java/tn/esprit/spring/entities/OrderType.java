package tn.esprit.spring.entities;
public enum OrderType {
    BUY("Buy"),
    SELL("Sell");

    private final String displayName;

    OrderType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}


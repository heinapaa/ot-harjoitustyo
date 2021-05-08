package generator.models;

public enum Unit {
    KG("WEIGHT"),
    G("WEIGHT"),
    L("VOLUME"),
    DL("VOLUME"),
    KPL("PCS");

    String unitType;

    Unit(String unitType) {
        this.unitType = unitType;
    }

    public boolean isWeight() {
        return unitType.equals("WEIGHT");
    }

    public boolean isVolume() {
        return unitType.equals("VOLUME");
    }

    public boolean isAmount() {
        return unitType.equals("PCS");
    }

    public boolean hasSameType(Unit otherUnit) {
        if (this.isAmount() && otherUnit.isAmount()) {
            return true;
        } else if (this.isWeight() && otherUnit.isWeight()) {
            return true;
        } else if (this.isVolume() && otherUnit.isVolume()) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }  
    
}

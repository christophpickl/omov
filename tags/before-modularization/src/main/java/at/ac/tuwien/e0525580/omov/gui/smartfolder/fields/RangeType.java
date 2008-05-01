package at.ac.tuwien.e0525580.omov.gui.smartfolder.fields;


public enum RangeType {
    
    DAYS(1), WEEKS(7), MONTHS(30);
    
    private final int dayAmount;
    
    private RangeType(int dayAmount) {
        this.dayAmount = dayAmount;
    }
    
    public int getDayAmount() {
        return this.dayAmount;
    }
    
    static RangeType getByString(String string) {
        if(string.equals("days")) {
            return RangeType.DAYS;
        } else if(string.equals("weeks")) {
            return RangeType.WEEKS;
        } else if(string.equals("months")) {
            return RangeType.MONTHS;
        } else {
            throw new IllegalArgumentException("unkown string: '"+string+"'");
        }
    }
}

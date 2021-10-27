package common.data;


public enum UnitOfMeasure {
    SQUARE_METERS,
    PCS,
    LITERS,
    GRAMS;

    public static String nameList() {
        String nameList = "";
        for (UnitOfMeasure category : values()) {
            nameList += category.name() + ", ";
        }
        return nameList.substring(0, nameList.length() - 2);
    }
}
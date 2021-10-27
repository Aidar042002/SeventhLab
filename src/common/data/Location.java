package common.data;

public enum Location {
    TOWN,
    CITY,
    VILLAGE;


    public static String nameList() {
        String nameList = "";
        for (Location location : values()) {
            nameList += location.name() + ", ";
        }
        return nameList.substring(0, nameList.length() - 2);
    }
}
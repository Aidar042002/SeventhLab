package common.data;

public enum OrganizationType {
    TRUST,
    PRIVATE_LIMITED_COMPANY,
    OPEN_JOINT_STOCK_COMPANY;


    public static String nameList() {
        String nameList = "";
        for (OrganizationType organizationTypeType : values()) {
            nameList += organizationTypeType.name() + ", ";
        }
        return nameList.substring(0, nameList.length() - 2);
    }
}
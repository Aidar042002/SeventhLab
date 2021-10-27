package common.data;

import java.io.Serializable;

/**
 * Organization
 */
public class Organization implements Serializable {
    private String name;
    private long marinesCount;

    public Organization(String name, long marinesCount) {
        this.name = name;
        this.marinesCount = marinesCount;
    }

    /**
     * @return Name
     */
    public String getName() {
        return name;
    }


    public long getMarinesCount() {
        return marinesCount;
    }

    @Override
    public String toString() {
        return name + " (" + marinesCount + " product)";
    }

    @Override
    public int hashCode() {
        return name.hashCode() + (int) marinesCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Organization) {
            Organization organizationObj = (Organization) obj;
            return name.equals(organizationObj.getName()) && (marinesCount == organizationObj.getMarinesCount());
        }
        return false;
    }
}
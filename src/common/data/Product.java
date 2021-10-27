package common.data;

import common.util.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Main in the collection.
 */
public class Product implements Comparable<Product> {
    public static final int MAX_Y = 262;
    public static final double MIN_HEALTH = 0;
    public static final long MIN_MARINES = 1;
    public static final long MAX_MARINES = 1000;

    private Long id;
    private String name;
    private Coordinates coordinates;
    private LocalDateTime creationDate;
    private double price;
    private UnitOfMeasure unitofmeasure;
    private OrganizationType organizationTypeType;
    private Location location;
    private Organization organization;
    private User owner;

    public Product(Long id, String name, Coordinates coordinates, LocalDateTime creationDate, double price,
                   UnitOfMeasure unitofmeasure, OrganizationType organizationTypeType, Location location, Organization organization,
                   User owner) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.unitofmeasure = unitofmeasure;
        this.organizationTypeType = organizationTypeType;
        this.location = location;
        this.organization = organization;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getPrice() {
        return price;
    }

    public UnitOfMeasure getUnitofmeasure() {
        return unitofmeasure;
    }

    public OrganizationType getWeaponType() {
        return organizationTypeType;
    }

    public Location getMeleeWeapon() {
        return location;
    }

    public Organization getChapter() {
        return organization;
    }


    public User getOwner() {
        return owner;
    }

    @Override
    public int compareTo(Product marineObj) {
        return id.compareTo(marineObj.getId());
    }

    @Override
    public String toString() {
        String info = "";
        info += "Продукт id:" + id;
        info += " [" + owner.getUsername() + " " + creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                " в " + creationDate.format(DateTimeFormatter.ofPattern("HH:mm")) + "]";
        info += "\n Имя: " + name;
        info += "\n Местоположение: " + coordinates;
        info += "\n Цена: " + price;
        info += "\n Единица измерения: " + unitofmeasure;
        info += "\n Тип организации: " + organizationTypeType;
        info += "\n Локация: " + location;
        info += "\n Организация: " + organization;
        return info;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + coordinates.hashCode() + (int) price + unitofmeasure.hashCode() + organizationTypeType.hashCode() +
                location.hashCode() + organization.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Product) {
            Product marineObj = (Product) obj;
            return name.equals(marineObj.getName()) && coordinates.equals(marineObj.getCoordinates()) &&
                    (price == marineObj.getPrice()) && (unitofmeasure == marineObj.getUnitofmeasure()) &&
                    (organizationTypeType == marineObj.getWeaponType()) && (location == marineObj.getMeleeWeapon()) &&
                    organization.equals(marineObj.getChapter());
        }
        return false;
    }
}
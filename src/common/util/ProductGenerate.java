package common.util;

import common.data.*;

import java.io.Serializable;


public class ProductGenerate implements Serializable {
    private String name;
    private Coordinates coordinates;
    private double price;
    private UnitOfMeasure unitOfMeasure;
    private OrganizationType organizationTypeType;
    private Location location;
    private Organization organization;

    public ProductGenerate(String name, Coordinates coordinates, double price, UnitOfMeasure unitOfMeasure,
                           OrganizationType organizationTypeType, Location location, Organization organization) {
        this.name = name;
        this.coordinates = coordinates;
        this.price = price;
        this.unitOfMeasure = unitOfMeasure;
        this.organizationTypeType = organizationTypeType;
        this.location = location;
        this.organization = organization;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public double getPrice() {
        return price;
    }


    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
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

    @Override
    public String toString() {
        String info = "";
        info += "Продукт";
        info += "\n Имя: " + name;
        info += "\n Местоположение: " + coordinates;
        info += "\n Цена: " + price;
        info += "\n Единица измерения: " + unitOfMeasure;
        info += "\n Дальнее оружие: " + organizationTypeType;
        info += "\n Ближнее оружие: " + location;
        info += "\n Орден: " + organization;
        return info;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + coordinates.hashCode() + (int) price + unitOfMeasure.hashCode() + organizationTypeType.hashCode() +
                location.hashCode() + organization.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Product) {
            Product marineObj = (Product) obj;
            return name.equals(marineObj.getName()) && coordinates.equals(marineObj.getCoordinates()) &&
                    (price == marineObj.getPrice()) && (unitOfMeasure == marineObj.getUnitofmeasure()) &&
                    (organizationTypeType == marineObj.getWeaponType()) && (location == marineObj.getMeleeWeapon()) &&
                    organization.equals(marineObj.getChapter());
        }
        return false;
    }
}
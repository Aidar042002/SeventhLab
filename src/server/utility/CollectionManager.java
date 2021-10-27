package server.utility;

import common.data.Location;
import common.data.Product;
import common.data.OrganizationType;
import common.exceptions.CollectionIsEmptyException;
import common.exceptions.DatabaseHandlingException;
import common.utility.Outputer;

import java.time.LocalDateTime;
import java.util.NavigableSet;
import java.util.TreeSet;

public class CollectionManager {
    private NavigableSet<Product> marinesCollection;
    private LocalDateTime lastInitTime;
    private DatabaseCollectionManager databaseCollectionManager;

    public CollectionManager(DatabaseCollectionManager databaseCollectionManager) {
        this.databaseCollectionManager = databaseCollectionManager;

        loadCollection();
    }

    public NavigableSet<Product> getCollection() {
        return marinesCollection;
    }

    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }


    public String collectionType() {
        return marinesCollection.getClass().getName();
    }


    public int collectionSize() {
        return marinesCollection.size();
    }


    public Product getFirst() {
        return marinesCollection.stream().findFirst().orElse(null);
    }


    public Product getById(Long id) {
        return marinesCollection.stream().filter(marine -> marine.getId().equals(id)).findFirst().orElse(null);
    }


    public Product getByValue(Product marineToFind) {
        return marinesCollection.stream().filter(marine -> marine.equals(marineToFind)).findFirst().orElse(null);
    }


    public double getSumOfHealth() {
        return marinesCollection.stream()
                .reduce(0.0, (sum, p) -> sum += p.getPrice(), Double::sum);
    }

    public String showCollection() {
        if (marinesCollection.isEmpty()) return "Коллекция пуста!";
        return marinesCollection.stream().reduce("", (sum, m) -> sum += m + "\n\n", (sum1, sum2) -> sum1 + sum2).trim();
    }


    public String maxByMeleeWeapon() throws CollectionIsEmptyException {
        if (marinesCollection.isEmpty()) throw new CollectionIsEmptyException();

        Location maxLocation = marinesCollection.stream().map(marine -> marine.getMeleeWeapon())
                .max(Enum::compareTo).get();
        return marinesCollection.stream()
                .filter(marine -> marine.getMeleeWeapon().equals(maxLocation)).findFirst().get().toString();
    }


    public String weaponFilteredInfo(OrganizationType organizationTypeToFilter) {
        return marinesCollection.stream().filter(marine -> marine.getWeaponType().equals(organizationTypeToFilter))
                .reduce("", (sum, m) -> sum += m + "\n\n", (sum1, sum2) -> sum1 + sum2).trim();
    }


    public NavigableSet<Product> getGreater(Product marineToCompare) {
        return marinesCollection.stream().filter(marine -> marine.compareTo(marineToCompare) > 0).collect(
                TreeSet::new,
                TreeSet::add,
                TreeSet::addAll
        );
    }


    public void addToCollection(Product marine) {
        marinesCollection.add(marine);
    }


    public void removeFromCollection(Product marine) {
        marinesCollection.remove(marine);
    }


    public void clearCollection() {
        marinesCollection.clear();
    }


    private void loadCollection() {
        try {
            marinesCollection = databaseCollectionManager.getCollection();
            lastInitTime = LocalDateTime.now();
            Outputer.println("Коллекция загружена.");
        } catch (DatabaseHandlingException exception) {
            marinesCollection = new TreeSet<>();
            Outputer.printerror("Коллекция не может быть загружена!");
        }
    }
}
package server.commands;

import common.data.*;
import common.exceptions.*;
import common.util.ProductGenerate;
import common.util.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutputer;

import java.time.LocalDateTime;


public class UpdateCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public UpdateCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("update", "<ID> {element}", "обновить значение элемента коллекции по ID");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }


    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();

            long id = Long.parseLong(stringArgument);
            if (id <= 0) throw new NumberFormatException();
            Product oldMarine = collectionManager.getById(id);
            if (oldMarine == null) throw new ProductNotFoundException();
            if (!oldMarine.getOwner().equals(user)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkMarineUserId(oldMarine.getId(), user)) throw new DatabaseEditException();
            ProductGenerate productGenerate = (ProductGenerate) objectArgument;

            databaseCollectionManager.updateMarineById(id, productGenerate);

            String name = productGenerate.getName() == null ? oldMarine.getName() : productGenerate.getName();
            Coordinates coordinates = productGenerate.getCoordinates() == null ? oldMarine.getCoordinates() : productGenerate.getCoordinates();
            LocalDateTime creationDate = oldMarine.getCreationDate();
            double health = productGenerate.getPrice() == -1 ? oldMarine.getPrice() : productGenerate.getPrice();
            UnitOfMeasure category = productGenerate.getUnitOfMeasure() == null ? oldMarine.getUnitofmeasure() : productGenerate.getUnitOfMeasure();
            OrganizationType organizationTypeType = productGenerate.getWeaponType() == null ? oldMarine.getWeaponType() : productGenerate.getWeaponType();
            Location location = productGenerate.getMeleeWeapon() == null ? oldMarine.getMeleeWeapon() : productGenerate.getMeleeWeapon();
            Organization organization = productGenerate.getChapter() == null ? oldMarine.getChapter() : productGenerate.getChapter();

            collectionManager.removeFromCollection(oldMarine);
            collectionManager.addToCollection(new Product(
                    id,
                    name,
                    coordinates,
                    creationDate,
                    health,
                    category,
                    organizationTypeType,
                    location,
                    organization,
                    user
            ));
            ResponseOutputer.appendln("Элемента успешно изменен!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appenderror("ID должен быть представлен положительным числом!");
        } catch (ProductNotFoundException exception) {
            ResponseOutputer.appenderror("Элемента с таким ID в коллекции нет!");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("Произошла ошибка при обращении к базе данных!");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.appenderror("Недостаточно прав для выполнения данной команды!");
            ResponseOutputer.appendln("Принадлежащие другим пользователям объекты доступны только для чтения.");
        } catch (DatabaseEditException exception) {
            ResponseOutputer.appenderror("Произошло прямое изменение базы данных!");
            ResponseOutputer.appendln("Перезапустите клиент для избежания возможных ошибок.");
        }
        return false;
    }
}
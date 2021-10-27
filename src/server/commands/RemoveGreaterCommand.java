package server.commands;

import common.data.Product;
import common.exceptions.*;
import common.util.ProductGenerate;
import common.util.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutputer;

import java.time.LocalDateTime;

public class RemoveGreaterCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public RemoveGreaterCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_greater", "{element}", "удалить из коллекции все элементы, превышающие заданный");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }


    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            ProductGenerate productGenerate = (ProductGenerate) objectArgument;
            Product marineToFind = new Product(
                    0L,
                    productGenerate.getName(),
                    productGenerate.getCoordinates(),
                    LocalDateTime.now(),
                    productGenerate.getPrice(),
                    productGenerate.getUnitOfMeasure(),
                    productGenerate.getWeaponType(),
                    productGenerate.getMeleeWeapon(),
                    productGenerate.getChapter(),
                    user
            );
            Product marineFromCollection = collectionManager.getByValue(marineToFind);
            if (marineFromCollection == null) throw new ProductNotFoundException();
            for (Product marine : collectionManager.getGreater(marineFromCollection)) {
                if (!marine.getOwner().equals(user)) throw new PermissionDeniedException();
                if (!databaseCollectionManager.checkMarineUserId(marine.getId(), user)) throw new DatabaseEditException();
            }
            for (Product marine : collectionManager.getGreater(marineFromCollection)) {
                databaseCollectionManager.deleteMarineById(marine.getId());
                collectionManager.removeFromCollection(marine);
            }
            ResponseOutputer.appendln("Элементы успешно удалены!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        } catch (ProductNotFoundException exception) {
            ResponseOutputer.appenderror("Элемента с такими характеристиками в коллекции нет!");
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
package server.commands;

import common.data.OrganizationType;
import common.exceptions.CollectionIsEmptyException;
import common.exceptions.WrongAmountOfElementsException;
import common.util.User;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

public class FilterByOrgTypeCommand extends AbstractCommand {
    private CollectionManager collectionManager;

    public FilterByOrgTypeCommand(CollectionManager collectionManager) {
        super("filter_by_org_type", "<org_type>",
                "вывести элементы, значение поля orgType которых равно заданному");
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            OrganizationType organizationType = OrganizationType.valueOf(stringArgument.toUpperCase());
            String filteredInfo = collectionManager.weaponFilteredInfo(organizationType);
            if (!filteredInfo.isEmpty()) ResponseOutputer.appendln(filteredInfo);
            else ResponseOutputer.appendln("В коллекции нет солдат с выбранным типом оружия!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        } catch (IllegalArgumentException exception) {
            ResponseOutputer.appenderror("Оружия нет в списке!");
            ResponseOutputer.appendln("Список оружия дальнего боя - " + OrganizationType.nameList());
        }
        return false;
    }
}
package server.commands;

import common.exceptions.WrongAmountOfElementsException;
import common.util.User;
import server.utility.ResponseOutputer;


public class HistoryCommand extends AbstractCommand {

    public HistoryCommand() {
        super("history", "", "вывести историю использованных команд");
    }

    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}
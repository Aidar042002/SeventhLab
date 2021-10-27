package server.utility;

import common.exceptions.HistoryIsEmptyException;
import common.util.User;
import server.commands.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CommandManager {
    private final int COMMAND_HISTORY_SIZE = 8;

    private String[] commandHistory = new String[COMMAND_HISTORY_SIZE];
    private List<Command> commands = new ArrayList<>();
    private Command helpCommand;
    private Command infoCommand;
    private Command showCommand;
    private Command addCommand;
    private Command updateCommand;
    private Command removeByIdCommand;
    private Command clearCommand;
    private Command exitCommand;
    private Command executeScriptCommand;
    private Command addIfMinCommand;
    private Command removeGreaterCommand;
    private Command historyCommand;
    private Command sumOfPriceCommand;
    private Command maxByLocationCommand;
    private Command filterByOrgTypeCommand;
    private Command serverExitCommand;
    private Command loginCommand;
    private Command registerCommand;

    private ReadWriteLock historyLocker = new ReentrantReadWriteLock();
    private ReadWriteLock collectionLocker = new ReentrantReadWriteLock();

    public CommandManager(Command helpCommand, Command infoCommand, Command showCommand, Command addCommand, Command updateCommand,
                          Command removeByIdCommand, Command clearCommand, Command exitCommand, Command executeScriptCommand,
                          Command addIfMinCommand, Command removeGreaterCommand, Command historyCommand, Command sumOfPriceCommand,
                          Command maxByLocationCommand, Command filterByOrgTypeCommand, Command serverExitCommand,
                          Command loginCommand, Command registerCommand) {
        this.helpCommand = helpCommand;
        this.infoCommand = infoCommand;
        this.showCommand = showCommand;
        this.addCommand = addCommand;
        this.updateCommand = updateCommand;
        this.removeByIdCommand = removeByIdCommand;
        this.clearCommand = clearCommand;
        this.exitCommand = exitCommand;
        this.executeScriptCommand = executeScriptCommand;
        this.addIfMinCommand = addIfMinCommand;
        this.removeGreaterCommand = removeGreaterCommand;
        this.historyCommand = historyCommand;
        this.sumOfPriceCommand = sumOfPriceCommand;
        this.maxByLocationCommand = maxByLocationCommand;
        this.filterByOrgTypeCommand = filterByOrgTypeCommand;
        this.serverExitCommand = serverExitCommand;
        this.loginCommand = loginCommand;
        this.registerCommand = registerCommand;

        commands.add(helpCommand);
        commands.add(infoCommand);
        commands.add(showCommand);
        commands.add(addCommand);
        commands.add(updateCommand);
        commands.add(removeByIdCommand);
        commands.add(clearCommand);
        commands.add(exitCommand);
        commands.add(executeScriptCommand);
        commands.add(addIfMinCommand);
        commands.add(removeGreaterCommand);
        commands.add(historyCommand);
        commands.add(sumOfPriceCommand);
        commands.add(maxByLocationCommand);
        commands.add(filterByOrgTypeCommand);
        commands.add(serverExitCommand);
    }


    public void addToHistory(String commandToStore, User user) {
        historyLocker.writeLock().lock();
        try {
            for (Command command : commands) {
                if (command.getName().equals(commandToStore)) {
                    for (int i = COMMAND_HISTORY_SIZE - 1; i > 0; i--) {
                        commandHistory[i] = commandHistory[i - 1];
                    }
                    commandHistory[0] = commandToStore + " (" + user.getUsername() + ')';
                }
            }
        } finally {
            historyLocker.writeLock().unlock();
        }
    }


    public boolean help(String stringArgument, Object objectArgument, User user) {
        if (helpCommand.execute(stringArgument, objectArgument, user)) {
            for (Command command : commands) {
                ResponseOutputer.appendtable(command.getName() + " " + command.getUsage(), command.getDescription());
            }
            return true;
        } else return false;
    }


    public boolean info(String stringArgument, Object objectArgument, User user) {
        collectionLocker.readLock().lock();
        try {
            return infoCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }

    public boolean show(String stringArgument, Object objectArgument, User user) {
        collectionLocker.readLock().lock();
        try {
            return showCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }

    public boolean add(String stringArgument, Object objectArgument, User user) {
        collectionLocker.writeLock().lock();
        try {
            return addCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }


    public boolean update(String stringArgument, Object objectArgument, User user) {
        collectionLocker.writeLock().lock();
        try {
            return updateCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }


    public boolean removeById(String stringArgument, Object objectArgument, User user) {
        collectionLocker.writeLock().lock();
        try {
            return removeByIdCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    public boolean clear(String stringArgument, Object objectArgument, User user) {
        collectionLocker.writeLock().lock();
        try {
            return clearCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    public boolean exit(String stringArgument, Object objectArgument, User user) {
        return exitCommand.execute(stringArgument, objectArgument, user);
    }


    public boolean executeScript(String stringArgument, Object objectArgument, User user) {
        return executeScriptCommand.execute(stringArgument, objectArgument, user);
    }


    public boolean addIfMin(String stringArgument, Object objectArgument, User user) {
        collectionLocker.writeLock().lock();
        try {
            return addIfMinCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }


    public boolean removeGreater(String stringArgument, Object objectArgument, User user) {
        collectionLocker.writeLock().lock();
        try {
            return removeGreaterCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }


    public boolean history(String stringArgument, Object objectArgument, User user) {
        if (historyCommand.execute(stringArgument, objectArgument, user)) {
            historyLocker.readLock().lock();
            try {
                if (commandHistory.length == 0) throw new HistoryIsEmptyException();
                ResponseOutputer.appendln("Последние использованные команды:");
                for (String command : commandHistory) {
                    if (command != null) ResponseOutputer.appendln(" " + command);
                }
                return true;
            } catch (HistoryIsEmptyException exception) {
                ResponseOutputer.appendln("Ни одной команды еще не было использовано!");
            } finally {
                historyLocker.readLock().unlock();
            }
        }
        return false;
    }


    public boolean sumOfHealth(String stringArgument, Object objectArgument, User user) {
        collectionLocker.readLock().lock();
        try {
            return sumOfPriceCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }


    public boolean maxByMeleeWeapon(String stringArgument, Object objectArgument, User user) {
        collectionLocker.readLock().lock();
        try {
            return maxByLocationCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }


    public boolean filterByWeaponType(String stringArgument, Object objectArgument, User user) {
        collectionLocker.readLock().lock();
        try {
            return filterByOrgTypeCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.readLock().lock();
        }
    }


    public boolean serverExit(String stringArgument, Object objectArgument, User user) {
        return serverExitCommand.execute(stringArgument, objectArgument, user);
    }


    public boolean login(String stringArgument, Object objectArgument, User user) {
        return loginCommand.execute(stringArgument, objectArgument, user);
    }

    public boolean register(String stringArgument, Object objectArgument, User user) {
        return registerCommand.execute(stringArgument, objectArgument, user);
    }
}
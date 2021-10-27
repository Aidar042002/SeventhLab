package server.commands;

import common.util.User;

/**
 * Interface for all commands.
 */
public interface Command {
    String getName();

    String getUsage();

    String getDescription();

    boolean execute(String commandStringArgument, Object commandObjectArgument, User user);
}
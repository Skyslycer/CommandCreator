package de.skyslycer.commandcreator.command.custom;

import de.skyslycer.commandcreator.CommandCreator;
import de.skyslycer.commandcreator.condition.Conditions;
import de.skyslycer.commandcreator.util.MessageUtil;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CustomCommand {

    private final List<String> aliases;
    private final List<String> messages;
    private final List<String> playerCommands;
    private final List<String> consoleCommands;
    private final List<String> conditions;
    private final String name;
    private final String conditionMessage;
    private final String description;
    private final boolean playerOnly;

    private final CommandCreator plugin;

    public CustomCommand(List<String> aliases, List<String> messages, List<String> playerCommands,
            List<String> consoleCommands, List<String> conditions, String name, String conditionMessage,
            String description, boolean playerOnly, CommandCreator plugin) {
        this.aliases = aliases;
        this.messages = messages;
        this.playerCommands = playerCommands;
        this.consoleCommands = consoleCommands;
        this.conditions = conditions;
        this.name = name;
        this.conditionMessage = conditionMessage;
        this.description = description;
        this.playerOnly = playerOnly;
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender) {
        if (isPlayerOnly() && !(sender instanceof Player)) {
            sender.spigot().sendMessage(MessageUtil.color(sender, plugin.getPlayerOnlyMessage()));
            return false;
        }

        for (String condition : getConditions()) {
            if (!Conditions.check(condition, sender)) {
                sender.spigot().sendMessage(MessageUtil.color(sender, getConditionMessage()));
                return false;
            }
        }

        for (String message : getMessages()) {
            sender.spigot().sendMessage(MessageUtil.color(sender, message));
        }
        for (String command : getPlayerCommands()) {
            Bukkit.dispatchCommand(sender, MessageUtil.replacePAPI(command, sender));
        }
        for (String command : getConsoleCommands()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MessageUtil.replacePAPI(command, sender));
        }
        return false;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public List<String> getMessages() {
        return messages;
    }

    public List<String> getPlayerCommands() {
        return playerCommands;
    }

    public List<String> getConsoleCommands() {
        return consoleCommands;
    }

    public List<String> getConditions() {
        return conditions;
    }

    public String getConditionMessage() {
        return conditionMessage;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

}

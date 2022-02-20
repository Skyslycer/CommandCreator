package de.skyslycer.commandcreator;

import com.tchristofferson.configupdater.ConfigUpdater;
import de.skyslycer.commandcreator.command.CommandInterceptor;
import de.skyslycer.commandcreator.command.block.BlockedCommandList;
import de.skyslycer.commandcreator.command.block.MatchingMode;
import de.skyslycer.commandcreator.command.custom.CustomCommand;
import de.skyslycer.commandcreator.command.custom.RegisterableCommand;
import de.skyslycer.commandcreator.commands.CommandCreatorCommand;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import me.mattstudios.mf.base.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandCreator extends JavaPlugin {

    private static final Path PLUGIN_FOLDER = Paths.get("plugins", "CommandCreator");
    private static final Path CONFIG_PATH = PLUGIN_FOLDER.resolve("config.yml");

    private Mode commandMode;
    private String playerOnlyMessage;
    private String blockedCommandMessage;
    private final HashMap<String, CustomCommand> commands = new HashMap<>();
    private final HashSet<BlockedCommandList> blockedCommands = new HashSet<>();
    private final HashSet<RegisterableCommand> registeredCommands = new HashSet<>();

    @Override
    public void onEnable() {
        loadConfig();
        Bukkit.getPluginManager().registerEvents(new CommandInterceptor(this), this);
        register();
        CommandManager manager = new CommandManager(this, true);
        manager.register(new CommandCreatorCommand(this));
    }

    @Override
    public void onDisable() {
        commands.clear();
        blockedCommands.clear();
        unregister();
    }

    public void loadConfig() {
        saveDefaultConfig();
        try {
            ConfigUpdater.update(this, CONFIG_PATH.getFileName().toString(), CONFIG_PATH.toFile(), "commands.mycommand");
            reloadConfig();
        } catch (IOException exception) {
            getLogger().warning("An error occurred while trying to load the config:");
            exception.printStackTrace();
        }
        FileConfiguration config = getConfig();
        commandMode = Mode.valueOf(config.getString("mode"));
        playerOnlyMessage = config.getString("player-only-message");
        blockedCommandMessage = config.getString("disabled-commands-message");

        for (String key : config.getConfigurationSection("disabled-commands").getKeys(false)) {
            BlockedCommandList list = loadBlockedCommands(config.getConfigurationSection("disabled-commands").getConfigurationSection(key));
            if (list != null) {
                blockedCommands.add(list);
            }
        }

        for (String key : config.getConfigurationSection("commands").getKeys(false)) {
            CustomCommand command = loadCommand(config.getConfigurationSection("commands").getConfigurationSection(key), key);
            if (command != null) {
                commands.put(command.getName(), command);
            }
        }
    }

    private BlockedCommandList loadBlockedCommands(ConfigurationSection section) {
        if (section == null) {
            return null;
        }

        List<String> commands;
        List<String> conditions;
        String mode;

        if (section.get("commands") == null) {
            return null;
        }
        commands = section.getStringList("commands");
        if (section.get("conditions") == null) {
            return null;
        }
        conditions = section.getStringList("conditions");
        if (section.get("mode") == null) {
            return null;
        }
        mode = section.getString("mode");
        return new BlockedCommandList(commands, conditions, MatchingMode.match(mode));
    }

    private CustomCommand loadCommand(ConfigurationSection section, String name) {
        if (section == null) {
            return null;
        }

        List<String> aliases;
        List<String> messages;
        List<String> playerCommands;
        List<String> consoleCommands;
        List<String> conditions;
        String conditionMessage;
        String description;
        boolean playerOnly;

        if (section.get("aliases") == null) {
            return null;
        }
        aliases = section.getStringList("aliases");
        if (section.get("messages") == null) {
            return null;
        }
        messages = section.getStringList("messages");
        if (section.get("player-commands") == null) {
            return null;
        }
        playerCommands = section.getStringList("player-commands");
        if (section.get("console-commands") == null) {
            return null;
        }
        consoleCommands = section.getStringList("console-commands");
        if (section.get("conditions") == null) {
            return null;
        }
        conditions = section.getStringList("conditions");
        if (section.get("condition-message") == null) {
            return null;
        }
        conditionMessage = section.getString("condition-message");
        if (section.get("description") == null) {
            return null;
        }
        description = section.getString("description");
        if (section.get("player-only") == null) {
            return null;
        }
        playerOnly = section.getBoolean("player-only");

        return new CustomCommand(aliases, messages, playerCommands, consoleCommands, conditions, name, conditionMessage, description, playerOnly, this);
    }

    public void register() {
        if (commandMode == Mode.REGISTER) {
            try {
                Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                bukkitCommandMap.setAccessible(true);
                CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

                for (CustomCommand command : getCommands().values()) {
                    RegisterableCommand registerableCommand = new RegisterableCommand(command);
                    registerableCommand.register(commandMap);
                    registeredCommands.add(registerableCommand);
                }
            } catch (IllegalAccessException | NoSuchFieldException exception) {
                getLogger().warning("An error occurred while trying to register commands:");
                exception.printStackTrace();
            }
        }
    }

    public void unregister() {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            for (RegisterableCommand command : registeredCommands) {
                command.unregister(commandMap);
                registeredCommands.remove(command);
            }
        } catch (IllegalAccessException | NoSuchFieldException exception) {
            getLogger().warning("An error occurred while trying to register commands:");
            exception.printStackTrace();
        }
    }

    public Mode getCommandMode() {
        return commandMode;
    }

    public String getPlayerOnlyMessage() {
        return playerOnlyMessage;
    }

    public HashMap<String, CustomCommand> getCommands() {
        return commands;
    }

    public HashSet<BlockedCommandList> getBlockedCommands() {
        return blockedCommands;
    }

    public String getBlockedCommandMessage() {
        return blockedCommandMessage;
    }

}

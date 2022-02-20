package de.skyslycer.commandcreator.commands;

import de.skyslycer.commandcreator.CommandCreator;
import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;

@Command("commandcreatorreload")
@Alias({"ccreload", "ccr"})
public class CommandCreatorCommand extends CommandBase {

    private final CommandCreator plugin;

    public CommandCreatorCommand(CommandCreator plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("commandcreator.admin")
    public void reloadCommand(CommandSender sender) {
        plugin.getCommands().clear();
        plugin.getBlockedCommands().clear();
        plugin.unregister();
        plugin.loadConfig();
        plugin.register();
        sender.sendMessage("§aReloaded CommandCreator!");
    }

}

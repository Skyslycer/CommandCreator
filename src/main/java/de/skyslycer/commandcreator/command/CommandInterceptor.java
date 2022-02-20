package de.skyslycer.commandcreator.command;

import de.skyslycer.commandcreator.CommandCreator;
import de.skyslycer.commandcreator.Mode;
import de.skyslycer.commandcreator.command.block.BlockedCommandList;
import de.skyslycer.commandcreator.command.custom.CustomCommand;
import de.skyslycer.commandcreator.util.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandInterceptor implements Listener {

    private final CommandCreator plugin;

    public CommandInterceptor(CommandCreator plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String commandText = event.getMessage().replace("/", "").split(" ")[0];
        for (BlockedCommandList list : plugin.getBlockedCommands()) {
            if (list.contains(commandText)) {
                if (!list.canExecute(event.getPlayer()) && !event.getPlayer().hasPermission("commandcreator.bypass")) {
                    event.setCancelled(true);
                    event.getPlayer().spigot().sendMessage(MessageUtil.color(event.getPlayer(), plugin.getBlockedCommandMessage()));
                }
            }
        }
        if (plugin.getCommandMode() == Mode.INTERCEPT) {
            CustomCommand command = plugin.getCommands().get(commandText);
            if (command == null) {
                return;
            }
            event.setCancelled(true);
            command.execute(event.getPlayer());
        }
    }

}

package de.skyslycer.commandcreator.command.custom;

import de.skyslycer.commandcreator.command.custom.CustomCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

public class RegisterableCommand extends BukkitCommand {

    private final CustomCommand command;

    public RegisterableCommand(CustomCommand command) {
        super(command.getName(), command.getDescription(), "", command.getAliases());
        this.command = command;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        return command.execute(sender);
    }

}

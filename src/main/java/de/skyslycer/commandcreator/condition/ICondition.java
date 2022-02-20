package de.skyslycer.commandcreator.condition;

import org.bukkit.command.CommandSender;

public interface ICondition {

    boolean check(CommandSender sender, String text);

}

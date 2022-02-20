package de.skyslycer.commandcreator.condition.conditions;

import de.skyslycer.commandcreator.condition.ICondition;
import org.bukkit.command.CommandSender;

public class PermissionCondition implements ICondition {

    @Override
    public boolean check(CommandSender sender, String text) {
        return sender.hasPermission(text);
    }

}

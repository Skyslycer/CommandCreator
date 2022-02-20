package de.skyslycer.commandcreator.command.block;

import de.skyslycer.commandcreator.condition.Conditions;
import java.util.List;
import org.bukkit.command.CommandSender;

public class BlockedCommandList {

    private final List<String> blockedCommands;
    private final List<String> conditions;

    private final MatchingMode mode;

    public BlockedCommandList(List<String> blockedCommands, List<String> conditions,
            MatchingMode mode) {
        this.blockedCommands = blockedCommands;
        this.conditions = conditions;
        this.mode = mode;
    }

    public boolean canExecute(CommandSender sender) {
        for (String condition : conditions) {
            if (!Conditions.check(condition, sender)) {
                return false;
            }
        }
        return true;
    }

    public boolean contains(String matcher) {
        for (String command : blockedCommands) {
            if (mode.match(command, matcher)) {
                return true;
            }
        }
        return false;
    }

}

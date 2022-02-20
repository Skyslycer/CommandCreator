package de.skyslycer.commandcreator.condition;

import de.skyslycer.commandcreator.condition.conditions.PermissionCondition;
import de.skyslycer.commandcreator.condition.conditions.WorldGuardCondition;
import org.bukkit.command.CommandSender;

public enum Conditions {

    HAS_PERMISSION(new PermissionCondition()),
    IN_REGION(new WorldGuardCondition());

    private final ICondition conditionClass;

    <T extends ICondition> Conditions(ICondition conditionClass) {
        this.conditionClass = conditionClass;
    }

    public static boolean check(String stringCondition, CommandSender sender) {
        boolean negated = stringCondition.startsWith("!");
        Conditions condition = Conditions.valueOf(stringCondition.replace("!", "").substring(0, stringCondition.indexOf(":")));
        String text = stringCondition.substring(stringCondition.indexOf(":"));
        return Boolean.logicalXor(negated, condition.conditionClass.check(sender, text));
    }

}

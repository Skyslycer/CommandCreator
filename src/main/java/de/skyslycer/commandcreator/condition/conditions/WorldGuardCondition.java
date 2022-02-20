package de.skyslycer.commandcreator.condition.conditions;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.skyslycer.commandcreator.condition.ICondition;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldGuardCondition implements ICondition {

    @Override
    public boolean check(CommandSender sender, String text) {
        if (sender instanceof Player player) {
            var container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            var regions = container.get(BukkitAdapter.adapt(player.getWorld()));
            var applicableRegions = regions.getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation()));
            for (ProtectedRegion region : applicableRegions) {
                if (region.getId().equals(text)) {
                    return true;
                }
            }
        }
        return false;
    }

}

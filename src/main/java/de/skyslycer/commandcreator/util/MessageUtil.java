package de.skyslycer.commandcreator.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtil {

    public static BaseComponent[] color(CommandSender sender, String message) {
        String newMessage = replacePAPI(message, sender);
        newMessage = ChatColor.translateAlternateColorCodes('&', newMessage);
        return BungeeComponentSerializer.get().serialize(MiniMessage.miniMessage().deserialize(newMessage));
    }

    public static String replacePAPI(String message, CommandSender sender) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI") && sender instanceof Player) {
            return PlaceholderAPI.setPlaceholders((Player) sender, message);
        }
        return message;
    }

}

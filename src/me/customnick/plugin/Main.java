package me.customnick.plugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;
// This is my first build of Custom Nick, bugs will be fixed.
public class Main extends JavaPlugin implements Listener {
    private Logger log = Bukkit.getLogger();
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        log.info("CustomNick - Enabled.");
    }
    public void onDisable() {
        log.info("CustomNick - Disabled.");
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String nick = getConfig().getString(p.getName());
        if(p.hasPermission("nick.commands")) {
            p.setDisplayName(ChatColor.translateAlternateColorCodes('&', nick));
        }
        else {
        	return;
        }
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("nicknames")) {
            // These are the sender, and intended person to nick!
            Player p = (Player) sender;

            if(p.hasPermission("nick.commands")) {
                if(args.length <= 0) {
                    p.sendMessage(ChatColor.GOLD + "CustomNick by MarcTheDev - Help");
                    p.sendMessage(ChatColor.RED + "Usage: /nick <nick>");
                    p.sendMessage(ChatColor.RED + "Usage: /nick <player> <nick>");
                    return true;
                }
                // Fixed error which if nickname contain 'off' like offtest it would reset //
                if(args[0].equalsIgnoreCase("off")) {
                    p.setDisplayName(p.getName());
                    p.sendMessage(ChatColor.GREEN + "Nickname is now disabled.");
                    getConfig().set(p.getName(), p.getDisplayName());
                    saveConfig();
                    return true;
                }

                if (args.length <= 1) {
                    p.setDisplayName(ChatColor.translateAlternateColorCodes('&', args[0]));
                    p.sendMessage(ChatColor.GREEN + "Successfully set nick!");
                    getConfig().set(p.getName(), p.getDisplayName());
                    saveConfig();
                    return true;
                }
                Player target = Bukkit.getPlayer(args[0]);
                if(args.length <= 3 && target.isOp()) {
                    p.sendMessage(ChatColor.RED + "Error: User is OP and can not be nicknamed.");
                }
                if (args.length <= 3 && target != null) {
                    target.setDisplayName(ChatColor.translateAlternateColorCodes('&', args[2]));
                    target.sendMessage(ChatColor.GREEN + "Nick has been set by " + p.getName());
                    p.sendMessage(ChatColor.AQUA + "Successfully set nick!");
                    getConfig().set(p.getName(), p.getDisplayName());
                    saveConfig();
                    return true;
                }
                if (args.length <= 3 && target == null) {
                    p.sendMessage(ChatColor.RED + "Error: User is not online.");
                }
                if (args.length >= 4) {
                    p.sendMessage(ChatColor.RED + "Error: Too many characters.");
                    return true;
                }
            } else {
                p.sendMessage(ChatColor.RED + "No permission.");
                return false;
            }
            return true;
        }
        return false;
    }
}
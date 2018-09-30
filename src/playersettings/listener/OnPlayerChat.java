package playersettings.listener;

import addon.ericlam.Variable;
import com.caxerx.mc.PlayerSettingManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static addon.ericlam.Variable.config;
import static addon.ericlam.Variable.messagefile;


public class OnPlayerChat implements Listener {
    private Variable var = new Variable();
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (config.getBoolean("ChatFilter.Enable")) {
            Player player = event.getPlayer();
            String msg = event.getMessage();
            BukkitScheduler scheduler = Bukkit.getScheduler();
            List<String> keys = config.getStringList("ChatFilter.List");
            boolean chatbypass = player.hasPermission("settings.chat.bypass");
            for (String keyword : keys) {
                if (msg.contains(keyword) && !chatbypass) {
                    event.setCancelled(true);
                    player.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Functions.ChatResponse.block-message"));
                    break;
                }
            }
        }
    }
    @EventHandler
    public void onHideChat(AsyncPlayerChatEvent event) throws SQLException {
        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        PlayerSettingManager psm = PlayerSettingManager.getInstance();
        if (psm.getPlayerSetting(puuid).isHideChat()) {
            event.setCancelled(true);
            event.getRecipients().remove(player);
            player.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Commands.HideChat.hidden"));
        }
        for (Player pl : event.getRecipients()) {
            if (psm.getPlayerSetting(pl.getUniqueId()).isHideChat()) event.getRecipients().remove(pl);
        }
    }



}
package com.ericlam.listener.playersettings;

import com.caxerx.mc.PlayerSettingManager;
import com.ericlam.addon.ConfigManager;
import main.HyperNiteMC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class OnPlayerInteractEntity implements Listener {
    private ConfigManager var;
    private PlayerSettingManager psm;

    public OnPlayerInteractEntity(){
        var = HyperNiteMC.getConfigManager();
        psm = PlayerSettingManager.getInstance();
    }

    @EventHandler
    public void onPlayerStacker(PlayerInteractEntityEvent event) throws SQLException {
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if (!var.config.getBoolean("Stacker.Enable")) {
            return;
        }

        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        Entity entity = event.getRightClicked();

        if (!(entity instanceof Player) || !player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            return;
        }

        Player target = (Player) entity;

        if (!psm.getPlayerSetting(puuid).isStacker()) {
            player.sendMessage(var.getMessage("Commands.Stacker.disactive"));
            return;
        }
        if (!psm.getPlayerSetting(target.getUniqueId()).isStacker()) {
            player.sendMessage(var.getMessage("Commands.Stacker.be-disactive"));
            return;
        }


        int maxStack = var.config.getInt("Stacker.Max-Stack");
        int stack = 0;
        Player top = player;
        /*Bukkit.broadcastMessage(ChatColor.RED + "======DEBUG======");
        Bukkit.broadcastMessage("Click Player: " + event.getPlayer().getName());
        Bukkit.broadcastMessage("Clicked Player: " + event.getRightClicked().getName());
        Bukkit.broadcastMessage("Layer" + stack + ": " + top.getName());*/
        Set<Player> stacks = new HashSet<>();
        stacks.add(top);
        while (top.getPassengers().size() > 0) {
            if (top.getPassengers().size() != 1) {
                Bukkit.broadcastMessage("returned");
                return;
            }
            Entity topEntity = top.getPassengers().get(0);
            if (!(topEntity instanceof Player)) {
                Bukkit.broadcastMessage("returned");
                return;
            }
            top = (Player) topEntity;
            stacks.add(top);
            stack++;
            //Bukkit.broadcastMessage("Layer" + stack + ": " + top.getName());
        }
        //Bukkit.broadcastMessage(ChatColor.RED + "=================");

        if (!stacks.contains(target)) {
            if (stack >= maxStack) {
                player.sendMessage(var.getMessage("Commands.Stacker.Max"));
                return;
            }
            top.addPassenger(target);
            player.sendMessage(var.getMessage("Commands.Stacker.stacked").replace("<player>", ((Player) entity).getDisplayName()));
        }

    }
}

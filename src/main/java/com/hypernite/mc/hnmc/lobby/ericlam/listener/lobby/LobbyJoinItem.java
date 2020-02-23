package com.hypernite.mc.hnmc.lobby.ericlam.listener.lobby;

import com.hypernite.mc.hnmc.lobby.ericlam.addon.JoinItemBuilder;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class LobbyJoinItem implements Listener {
    private JoinItemBuilder joinitem = new JoinItemBuilder();

    @EventHandler
    public void GiveItemOnJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        joinitem.givePlayerJoinItem(player);
    }

    @EventHandler
    public void GiveItemOnRespawn(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(HNMCLobby.plugin, () ->{
            joinitem.givePlayerJoinItem(player);
        });
    }

    @EventHandler
    public void NoItemMovementOnNotCreative(InventoryClickEvent e){
        if (!e.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) e.setCancelled(true);
    }

    @EventHandler
    public void NoItemPick(InventoryPickupItemEvent e){
        ItemStack item = e.getItem().getItemStack();
        Player player = (Player) e.getInventory().getHolder();
        if (player == null) return;
        if (joinitem.getJoinItems().containsKey(item)) {
            if (!player.getGameMode().equals(GameMode.CREATIVE)) e.setCancelled(true);
        }
    }
    @EventHandler
    public void NoItemDrop(PlayerDropItemEvent e){
        ItemStack item = e.getItemDrop().getItemStack();
        Player player = e.getPlayer();
        if (joinitem.getJoinItems().containsKey(item)) {
            if (!player.getGameMode().equals(GameMode.CREATIVE)) e.setCancelled(true);
        }
    }
}
package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.managers.IslandManager;
import com.iridium.iridiumskyblock.utils.LocationUtils;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Objects;

public class PlayerPortalListener implements Listener {

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        IslandManager islandManager = IridiumSkyblock.getInstance().getIslandManager();
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getFrom()).ifPresent(island -> {
            if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) {
                if (IridiumSkyblock.getInstance().getConfiguration().netherIslands) {
                    World world = Objects.equals(event.getFrom().getWorld(), islandManager.getNetherWorld()) ? islandManager.getWorld() : islandManager.getNetherWorld();
                    event.setTo(island.getCenter(world));
                } else {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().netherIslandsDisabled.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            }
            if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)) {
                if (IridiumSkyblock.getInstance().getConfiguration().endIslands) {
                    World world = Objects.equals(event.getFrom().getWorld(), islandManager.getEndWorld()) ? islandManager.getWorld() : islandManager.getEndWorld();
                    event.setCancelled(true);
                    event.getPlayer().teleport(LocationUtils.getSafeLocation(island.getCenter(world), island));
                } else {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().endIslandsDisabled.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            }
        });
    }

}

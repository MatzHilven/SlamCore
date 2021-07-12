package me.matzhilven.slamcore.listeners;

import me.matzhilven.slamcore.SlamCore;
import me.matzhilven.slamcore.data.User;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.ChunkSection;
import net.minecraft.server.v1_16_R3.IBlockData;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final SlamCore main;
    private final FileConfiguration config;

    public PlayerListener(SlamCore main) {
        this.main = main;
        this.config = main.getConfig();
        main.getServer().getPluginManager().registerEvents(this,main);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        User user =  main.getDatabaseHandler().getUserByPlayer(e.getPlayer());

        System.out.println(user.getGems());
    }

    @EventHandler
    private void onLeave(PlayerQuitEvent e) {
        main.getDatabaseHandler().savePlayer(e.getPlayer());
    }


    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {

    }

    public static void setBlockInNativeDataPalette(World world, int x, int y, int z, int blockId, byte data, boolean applyPhysics) {
        net.minecraft.server.v1_16_R3.World nmsWorld = ((CraftWorld) world).getHandle();
        net.minecraft.server.v1_16_R3.Chunk nmsChunk = nmsWorld.getChunkAt(x >> 4, z >> 4);
        IBlockData ibd = net.minecraft.server.v1_16_R3.Block.getByCombinedId(blockId + (data << 12));

        ChunkSection cs = nmsChunk.getSections()[y >> 4];
        if (cs == nmsChunk.a()) {
            cs = new ChunkSection(y >> 4 << 4);
            nmsChunk.getSections()[y >> 4] = cs;
        }

        if (applyPhysics)
            cs.getBlocks().setBlock(x & 15, y & 15, z & 15, ibd);
        else
            cs.getBlocks().b(x & 15, y & 15, z & 15, ibd);
    }
}

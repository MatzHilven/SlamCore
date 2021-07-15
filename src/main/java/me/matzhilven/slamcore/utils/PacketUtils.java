package me.matzhilven.slamcore.utils;

import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PacketUtils {

    public static void sendActionBar(Player p, String message) {

        IChatBaseComponent chat = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");

        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(chat, ChatMessageType.GAME_INFO, UUID.randomUUID());

        CraftPlayer player = (CraftPlayer) p;

        player.getHandle().playerConnection.sendPacket(packetPlayOutChat);
    }
}

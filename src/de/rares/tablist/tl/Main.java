package de.rares.tablist.tl;


import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;

public class Main extends JavaPlugin implements Listener {

    public static JavaPlugin pl;
    @Override
    public void onEnable() {
        pl = this;
        Bukkit.getPluginManager().registerEvents(this, this);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        FileConfiguration cfg = pl.getConfig();

        if(!cfg.isSet("tl.footer")){
            cfg.set("ts.footer", "abc");
        }
        if(!cfg.isSet("tl.header")){
            cfg.set("ts.header", "abc");
        }
        pl.saveConfig();
        try {
            sendTablist(e.getPlayer(),
                    cfg.get("ts.header").toString().replace("&","§"), cfg.get("ts.footer").toString().replace("&","§"));
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        }
    }

    public void sendTablist(Player p, String header, String footer) throws IllegalAccessException, NoSuchFieldException {


        IChatBaseComponent tabHeader = ChatSerializer.a("{\"text\":\"" + header + "\"}");
        IChatBaseComponent tabFooter = ChatSerializer.a("{\"text\":\"" + footer + "\"}");

        PacketPlayOutPlayerListHeaderFooter headerPacket = new PacketPlayOutPlayerListHeaderFooter(tabHeader);

            Field field = headerPacket.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(headerPacket, tabFooter);

            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(headerPacket);

        }


}
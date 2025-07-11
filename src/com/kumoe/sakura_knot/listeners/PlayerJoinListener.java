package com.kumoe.sakura_knot.listeners;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PlayerJoinListener implements Listener {
	public static final String channel = "gts:main";

	@EventHandler
	public void onJoin(PlayerJoinEvent event) throws IllegalAccessException, NoSuchMethodException {
		Player player = event.getPlayer();
		try {
			Class<? extends CommandSender> senderClass = player.getClass();
			Method addChannel = senderClass.getDeclaredMethod("addChannel", String.class);
			addChannel.setAccessible(true);
			addChannel.invoke(player, channel);
		} catch (InvocationTargetException | IllegalArgumentException exception) {
			exception.printStackTrace();
		}
	}
}
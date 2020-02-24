package com.demeng7215.antisafecombatlog.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class CombatLogEvent implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onCombatLog(PlayerQuitEvent e) {

		final String uuid = e.getPlayer().getUniqueId().toString();

		if (CombatEngageEvent.combatTagged.containsKey(uuid))
			if (CombatEngageEvent.combatTagged.get(uuid) > System.currentTimeMillis())
				e.getPlayer().setHealth(0);

		CombatEngageEvent.combatTagged.remove(uuid);
	}
}

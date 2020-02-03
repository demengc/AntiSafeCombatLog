package com.demeng7215.antisafecombatlog.listeners;

import com.demeng7215.antisafecombatlog.AntiSafeCombatLog;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class CombatEngageEvent implements Listener {

	private AntiSafeCombatLog i;

	public CombatEngageEvent(AntiSafeCombatLog i) {
		this.i = i;
	}

	// UUID of Player Tagged, Tag Expiration Time (ms)
	public static Map<String, Long> combatTagged = new HashMap<>();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCombatEngage(EntityDamageByEntityEvent e) {

		if (e.getEntityType() != EntityType.PLAYER || e.getDamager().getType() != EntityType.PLAYER) return;

		Player attacker = (Player) e.getDamager();
		Player target = (Player) e.getEntity();

		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

		RegionManager regions1 = container.get(BukkitAdapter.adapt(target.getLocation().getWorld()));
		RegionManager regions2 = container.get(BukkitAdapter.adapt(attacker.getLocation().getWorld()));

		ApplicableRegionSet set1 = regions1.getApplicableRegions(BukkitAdapter.asBlockVector(target.getLocation()));
		ApplicableRegionSet set2 = regions2.getApplicableRegions(BukkitAdapter.asBlockVector(attacker.getLocation()));

		LocalPlayer localPlayer1 = WorldGuardPlugin.inst().wrapPlayer(target);
		LocalPlayer localPlayer2 = WorldGuardPlugin.inst().wrapPlayer(attacker);

		if (set1.testState(localPlayer1, Flags.PVP) && set2.testState(localPlayer2, Flags.PVP)) {

			long tagTimeout = System.currentTimeMillis() + (i.getSettings().getLong("timer") * 1000);

			combatTagged.put(attacker.getUniqueId().toString(), tagTimeout);
			combatTagged.put(target.getUniqueId().toString(), tagTimeout);
		}

		if (e.isCancelled()) {

			if (combatTagged.containsKey(attacker.getUniqueId().toString()) &&
					combatTagged.containsKey(target.getUniqueId().toString())) {

				if (combatTagged.get(attacker.getUniqueId().toString()) > System.currentTimeMillis() &&
						combatTagged.get(target.getUniqueId().toString()) > System.currentTimeMillis()) {

					long tagTimeout = System.currentTimeMillis() + (i.getSettings().getLong("timer") * 1000);

					combatTagged.put(attacker.getUniqueId().toString(), tagTimeout);
					combatTagged.put(target.getUniqueId().toString(), tagTimeout);

					e.setCancelled(false);
				}
			}
		}
	}
}
package dev.demeng.ascl.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class TaggedPlayerDeathEvent implements Listener {

  @EventHandler
  public void onTaggedPlayerDeath(PlayerDeathEvent e) {
    CombatEngageEvent.combatTagged.remove(e.getEntity().getUniqueId().toString());
  }
}

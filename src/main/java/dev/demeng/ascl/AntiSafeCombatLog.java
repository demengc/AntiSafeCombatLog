package dev.demeng.ascl;

import dev.demeng.ascl.command.ASCLCmd;
import dev.demeng.ascl.listener.CombatEngageEvent;
import dev.demeng.ascl.listener.CombatLogEvent;
import dev.demeng.ascl.listener.TaggedPlayerDeathEvent;
import dev.demeng.demlib.Common;
import dev.demeng.demlib.Registerer;
import dev.demeng.demlib.command.CommandMessages;
import dev.demeng.demlib.core.DemLib;
import dev.demeng.demlib.file.YamlFile;
import dev.demeng.demlib.message.MessageUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class AntiSafeCombatLog extends JavaPlugin {

  public YamlFile settingsFile;
  public static Map<Player, Long> taggedPlayers;

  @Override
  public void onEnable() {

    DemLib.setPlugin(this);
    MessageUtils.setPrefix("&8[&4AntiSafeCombatLog&8]");

    taggedPlayers = new HashMap<>();

    getLogger().info("Loading configuration files...");
    try {
      this.settingsFile = new YamlFile("settings.yml");
    } catch (final Exception ex) {
      MessageUtils.error(ex, "Failed to load configuration files.", true);
      return;
    }

    DemLib.setCommandMessages(
        new CommandMessages("", getSettings().getString("no-permission"), ""));

    getLogger().info("Registering commands...");
    Registerer.registerCommand(new ASCLCmd(this));

    getLogger().info("Registering listeners...");
    Registerer.registerListener(new CombatEngageEvent(this));
    Registerer.registerListener(new CombatLogEvent());
    Registerer.registerListener(new TaggedPlayerDeathEvent());

    MessageUtils.setPrefix(getSettings().getString("prefix"));

    MessageUtils.console(
        "&aAntiSafeCombatLog v" + Common.getVersion() + " has been successfully enabled!");
  }

  @Override
  public void onDisable() {
    MessageUtils.console(
        "&cAntiSafeCombatLog v" + Common.getVersion() + " has been successfully disabled.");
  }

  public FileConfiguration getSettings() {
    return this.settingsFile.getConfig();
  }
}

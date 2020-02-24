package com.demeng7215.antisafecombatlog;

import com.demeng7215.antisafecombatlog.commands.ASCLCmd;
import com.demeng7215.antisafecombatlog.listeners.CombatEngageEvent;
import com.demeng7215.antisafecombatlog.listeners.CombatLogEvent;
import com.demeng7215.demlib.DemLib;
import com.demeng7215.demlib.api.Common;
import com.demeng7215.demlib.api.DeveloperNotifications;
import com.demeng7215.demlib.api.Registerer;
import com.demeng7215.demlib.api.connections.WebUtils;
import com.demeng7215.demlib.api.files.CustomConfig;
import com.demeng7215.demlib.api.messages.MessageUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class AntiSafeCombatLog extends JavaPlugin {

	public CustomConfig settingsFile;

	public static Map<Player, Long> taggedPlayers;

	@Override
	public void onEnable() {

		DemLib.setPlugin(this);
		MessageUtils.setPrefix("&8[&4AntiSafeCombatLog&8]");

		taggedPlayers = new HashMap<>();

		try {
			if (new WebUtils("https://demeng7215.com/antipiracy/blacklist.txt").contains("GGcC5uumxUm9bsGz")) {
				MessageUtils.error(null, 0, "Plugin has been forcibly disabled.", true);
				return;
			}
		} catch (final IOException ex) {
			MessageUtils.error(ex, 2, "Failed to connect to auth server.", false);
		}

		getLogger().info("Loading configuration files...");
		try {
			this.settingsFile = new CustomConfig("settings.yml");
		} catch (final Exception ex) {
			MessageUtils.error(ex, 1, "Failed to load configuration files.", true);
			return;
		}

		getLogger().info("Registering commands...");
		Registerer.registerCommand(new ASCLCmd(this));

		getLogger().info("Registering listeners...");
		Registerer.registerListeners(new CombatEngageEvent(this));
		Registerer.registerListeners(new CombatLogEvent());
		DeveloperNotifications.enableNotifications("ca19af04-a156-482e-a35d-3f5f434975b5");

		MessageUtils.setPrefix(getSettings().getString("prefix"));

		MessageUtils.console("&aAntiSafeCombatLog v" + Common.getVersion() + " has been successfully enabled!");
	}

	@Override
	public void onDisable() {
		MessageUtils.console("&cAntiSafeCombatLog v" + Common.getVersion() + " has been successfully disabled.");
	}

	public FileConfiguration getSettings() {
		return this.settingsFile.getConfig();
	}
}

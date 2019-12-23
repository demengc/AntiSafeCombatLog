package com.demeng7215.antisafecombatlog.commands;

import com.demeng7215.antisafecombatlog.AntiSafeCombatLog;
import com.demeng7215.demlib.api.Common;
import com.demeng7215.demlib.api.CustomCommand;
import com.demeng7215.demlib.api.messages.MessageUtils;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class ASCLCmd extends CustomCommand {

	private AntiSafeCombatLog i;

	public ASCLCmd(AntiSafeCombatLog i) {
		super("antisafecombatlog");

		this.i = i;

		setDescription("Main command for AntiSafeCombatLog.");
		setAliases(Arrays.asList("combatlog", "ascl"));
	}

	@Override
	protected void run(CommandSender sender, String[] args) {

		if (args.length < 1 || !args[0].equalsIgnoreCase("reload")) {
			MessageUtils.tellWithoutPrefix(sender, "&cRunning AntiSafeCombatLog v" + Common.getVersion() + ".");
			return;
		}

		if (!checkHasPerm("combatlog.reload", sender,
				i.getSettings().getString("no-permission"))) return;

		i.settingsFile.reloadConfig();

		MessageUtils.tell(sender, i.getSettings().getString("successfully-reloaded"));
	}
}

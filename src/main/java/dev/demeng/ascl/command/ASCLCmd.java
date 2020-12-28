package dev.demeng.ascl.command;

import dev.demeng.ascl.AntiSafeCombatLog;
import dev.demeng.demlib.Common;
import dev.demeng.demlib.command.model.BaseCommand;
import dev.demeng.demlib.message.MessageUtils;
import org.bukkit.command.CommandSender;

public class ASCLCmd implements BaseCommand {

  private final AntiSafeCombatLog i;

  public ASCLCmd(AntiSafeCombatLog i) {
    this.i = i;
  }

  @Override
  public String getName() {
    return "antisafecombatlog";
  }

  @Override
  public String getDescription() {
    return "Main command for AntiSafeCombatLog.";
  }

  @Override
  public String[] getAliases() {
    return new String[] {"combatlog", "ascl"};
  }

  @Override
  public String getUsage() {
    return "[reload]";
  }

  @Override
  public boolean isPlayersOnly() {
    return false;
  }

  @Override
  public String getPermission() {
    return null;
  }

  @Override
  public int getMinArgs() {
    return 0;
  }

  @Override
  public String execute(CommandSender sender, String label, String[] args) {

    if (args.length < 1 || !args[0].equalsIgnoreCase("reload")) {
      MessageUtils.tellClean(sender, "&cRunning AntiSafeCombatLog v" + Common.getVersion() + ".");
      return null;
    }

    if (!sender.hasPermission("combatlog.reload")) {
      return i.getSettings().getString("no-permission");
    }

    i.settingsFile.reloadConfig();

    return i.getSettings().getString("successfully-reloaded");
  }
}

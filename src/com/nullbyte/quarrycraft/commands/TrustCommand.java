package com.nullbyte.quarrycraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nullbyte.quarrycraft.Main;
import com.nullbyte.quarrycraft.Quarry;

public class TrustCommand {

	private Main plugin;

	public TrustCommand(Main plugin) {
		this.plugin = plugin;
	}

	public boolean execute(CommandSender sender, Command command, String alias, String[] args) {
		// This command should really have a delay, since it can call a for-loop and spamming it could cause the server to lag
		if (!(sender instanceof Player)) {
			sender.sendMessage(plugin.translateColors("&cOnly players can use this command!"));
			return true;
		}

		if (args.length < 2)
			return false;

		Player player = (Player) sender;

		@SuppressWarnings("deprecation")
		OfflinePlayer playerToTrust = Bukkit.getOfflinePlayer(args[1]);
		if (playerToTrust == null) {
			sender.sendMessage(plugin.translateColors("&c%s has never joined this server before.", args[1]));
			return true;
		}

		Block block = player.getLocation().getBlock();

		if (!(block.getType().equals(Material.CHEST))) {
			player.sendMessage(plugin.translateColors("&cThe block you are standing on is not a quarry."));
			return true;
		}

		Quarry quarry = plugin.getQuarry(block);

		if (quarry == null) {
			player.sendMessage(plugin.translateColors("&cThe block you are standing on is not a quarry."));
			return true;
		}

		if (quarry.getTrustedPlayers().size() >= plugin.getMaxTrustedPlayers()) {
			sender.sendMessage(plugin.translateColors("&cYou can only trust &e%d&c players.", plugin.getMaxTrustedPlayers()));
			return true;
		}

		if (!(quarry.isOwner(player))) {
			player.sendMessage(plugin.translateColors("&cYou do not own this quarry.")); // Maybe just merge this to the first if statement, this could reveal quarry locations to players that are not supposed to know about
			return true;
		}

		if (player.equals(playerToTrust)) {
			player.sendMessage(plugin.translateColors("&cYou cannot trust yourself."));
			return true;
		}

		if (quarry.isTrusted(playerToTrust))
			quarry.untrust(playerToTrust);
		else
			quarry.trust(playerToTrust);
		
		player.sendMessage(plugin.translateColors("&aThe player &e%s&a has been %s&a.", playerToTrust.getName(), (quarry.isTrusted(playerToTrust) ? "trusted" : "&cuntrusted")));
		plugin.saveQuarries();

		return false;
	}

}
package com.nullbyte.quarrycraft;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class GriefPreventionHandler {
	
	public static boolean checkLocation(Player owner, Location l) {
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(l, true, null);
		return claim == null || claim.allowBuild(owner, null) == null;
	}
}

package com.nullbyte.quarrycraft;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.ProtectionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class WorldGuardHandler {
	public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }
	
	public static boolean doWGProtection(World world, int minX, int maxX, int minZ, int maxZ, String owner) {
		if(Bukkit.getPlayer(owner) == null) {
			return true;
		}
		WorldGuardPlugin wg = getWorldGuard();
		ProtectionQuery pq = wg.createProtectionQuery();
		
		for(int x=minX-1; x <= maxX+1; x++) {
			for(int y=Main.minWGY; y<= Main.maxWGY; y++) {
				for(int z=minZ-1; z<=maxZ+1; z++) {
					if(!pq.testBlockBreak(Bukkit.getPlayer(owner), world.getBlockAt(x,y,z)))
						return false;
				}
			}
		}
		return true;
	}
	
}

package com.nullbyte.quarrycraft;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class Main extends JavaPlugin implements Listener {
	public JavaPlugin plugin;
	public ArrayList<Quarry> quarries;

	@Override
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		this.plugin = this;
		
		String fileSeparator = System.getProperty("file.separator");
		File pluginDir = new File("plugins" + fileSeparator + "QuarryCraft");
		pluginDir.mkdir();
		quarries = new ArrayList<Quarry>();
		loadQuarries();
		new QuarryCleaner().runTaskTimer(plugin, 10, 10);
	}
	
	@EventHandler
	public void onPlayerLeftClick(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			if(!e.getPlayer().isSneaking()) return;
			Block clicked = e.getClickedBlock();
			if(clicked.getType().equals(Material.CHEST)) {
				Chest centreChest = (Chest) clicked.getState();
				if(isQuarryLayout(centreChest)) {
					if(addQuarry(centreChest)) {
						e.getPlayer().sendMessage(ChatColor.GREEN+ "You have created a new quarry.");
					}
					else {
						e.getPlayer().sendMessage(getQuarry(centreChest).toggleEndermining());
					}
					saveQuarries();
				}
			}
		}
	}
	
	public boolean isQuarryLayout(Chest centreChest) {
		Location centreLoc = centreChest.getLocation();
		int cx = centreLoc.getBlockX();
		int cy = centreLoc.getBlockY();
		int cz = centreLoc.getBlockZ();
		World w = centreLoc.getWorld();
		
		if(!w.getBlockAt(cx-1, cy, cz).getType().equals(Material.DIAMOND_BLOCK)) return false;
		if(!w.getBlockAt(cx+1, cy, cz).getType().equals(Material.DIAMOND_BLOCK)) return false;
		if(!w.getBlockAt(cx, cy, cz-1).getType().equals(Material.DIAMOND_BLOCK)) return false;
		if(!w.getBlockAt(cx, cy, cz+1).getType().equals(Material.DIAMOND_BLOCK)) return false;
		
		if(!w.getBlockAt(cx-1, cy, cz-1).getType().equals(Material.REDSTONE_BLOCK)) return false;
		if(!w.getBlockAt(cx-1, cy, cz+1).getType().equals(Material.REDSTONE_BLOCK)) return false;
		if(!w.getBlockAt(cx+1, cy, cz-1).getType().equals(Material.REDSTONE_BLOCK)) return false;
		if(!w.getBlockAt(cx+1, cy, cz+1).getType().equals(Material.REDSTONE_BLOCK)) return false;
		
		return true;
	}
	
	public Quarry getQuarry(Chest centreChest) {
		for(Quarry quarry : quarries) {
			if(quarry.isSameCentreChest(centreChest)) return quarry;
		}
		return null;
	}
	
	public void removeQuarry(Quarry q) {
		for(int i=0; i<quarries.size(); i++) {
			if(q.getLocation().equals(quarries.get(i).getLocation()) ) {
				quarries.get(i).cancel();
				quarries.remove(i);
				saveQuarries();
				return;
			}
		}
	}
	
	public void saveQuarries() {
		String fileSeparator = System.getProperty("file.separator");
		String path = "plugins" + fileSeparator + "QuarryCraft" + fileSeparator + "quarries.txt";
		File quarryFile = new File(path);
		quarryFile.delete();
		String fileString = "";
		for(Quarry q : quarries) {
			Location quarryLoc = q.getLocation();
			int minX = q.minX;
			int minZ = q.minZ;
			int maxX = q.maxX;
			int maxZ = q.maxZ;
			fileString += quarryLoc.getWorld().getName() + ";" + quarryLoc.getBlockX() + ";" + quarryLoc.getBlockY() + ";" + quarryLoc.getBlockZ() + ";" + minX + ";" + minZ + ";" + maxX + ";" + maxZ + ";" + q.classicMode + "\n";		
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(fileString.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	public void loadQuarries() {
		String fileSeparator = System.getProperty("file.separator");
		String path = "plugins" + fileSeparator + "QuarryCraft" + fileSeparator + "quarries.txt";
		try {
			BufferedReader inFile = new BufferedReader(new FileReader(path));
			String currentCoords;
			String[] locString;
			int minX, minZ, maxX, maxZ;
			boolean classicMode;
			int x,y,z;
			Location currentLocation;
			do {
				currentCoords = inFile.readLine();
				if(currentCoords == null) break;
				locString = currentCoords.split(";");
				x = Integer.parseInt(locString[1]);
				y = Integer.parseInt(locString[2]);
				z = Integer.parseInt(locString[3]);
				minX = Integer.parseInt(locString[4]);
				minZ = Integer.parseInt(locString[5]);
				maxX = Integer.parseInt(locString[6]);
				maxZ = Integer.parseInt(locString[7]);
				classicMode = locString[8].trim().contentEquals("true");
				Location quarryLoc = new Location(Bukkit.getWorld(locString[0]), x, y, z);
				addQuarry(quarryLoc, minX, maxX, minZ, maxZ, classicMode);
					
			} while(currentCoords != null);
			
			saveQuarries();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	public boolean addQuarry(Chest centreChest) {
		if(getQuarry(centreChest) == null) {
			Quarry quarry = new Quarry(centreChest);
			quarries.add(quarry);
			quarry.runTaskTimer(plugin, 0, 0);
			return true;
		}
		return false;
	}
	
	public boolean addQuarry(Location centreChestLocation, int minX, int maxX, int minZ, int maxZ, boolean mode) {
		if(centreChestLocation.getWorld().getBlockAt(centreChestLocation).getType().equals(Material.CHEST)) {
			Quarry quarry = new Quarry((Chest)centreChestLocation.getWorld().getBlockAt(centreChestLocation).getState(), minX, maxX, minZ, maxZ, mode);
			quarries.add(quarry);
			quarry.runTaskTimer(plugin, 0, 0);
			return true;
		}
		return false;
	}
	
	class QuarryCleaner extends BukkitRunnable {
		@Override
		public void run() {
			for(Quarry q : quarries)
				if(q.markedForDeletion || !isQuarryLayout(q.centreChest)) {
					Bukkit.broadcastMessage(ChatColor.DARK_RED + "Quarry at " + q.centreChestLocation.toVector().toString() + " destroyed");
					removeQuarry(q);
					return;
				}
		}
	}
}

package com.nullbyte.quarrycraft;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class Main extends JavaPlugin implements Listener {
	public JavaPlugin plugin;
	public ArrayList<Quarry> quarries;
	
	GuideBookGiver gbGiver;
	
	public static int quarryLimit = 5;
	public static int maxQuarryWidth = 50;
	public static int maxQuarryLength = 50;
	public static boolean welcomeMessages = true;
	public static long guideBookCooldown = 1000*60*120;
	
	public static ConfigurableMessages configurableMessages;

	class guideCommand implements CommandExecutor{

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
			if(args.length == 1 && sender.isOp() && args[0].equals("reload")) {
				configurableMessages.loadMessages();
				sender.sendMessage(configurableMessages.reloading1());
				loadConfig();
				sender.sendMessage(configurableMessages.reloading2());
				return true;
			}
			
			
			if(!(sender instanceof Player))
				return true;
			
			Player p = (Player) sender;
			if(args.length == 1 && args[0].equals("guide")) {
				gbGiver.giveGuideBook(p);
			}
			
			return true;
		}
	}
	
	class GuideBookGiver{
		private ArrayList<Date> lastUsed;
		private ArrayList<Player> playersUsed;
		
		public GuideBookGiver() {
			lastUsed = new ArrayList<Date>();
			playersUsed = new ArrayList<Player>();
		}
		
		public long getTimeSinceLastUse(Player p) {
			for(int i=0; i < playersUsed.size(); i++) {
				Player pl = playersUsed.get(i);
				if(pl.getName().equals(p.getName()))
					return new Date().getTime() - lastUsed.get(i).getTime();
			}
			return guideBookCooldown + 1;
		}
		
		public int getPlayerIndex(Player p) {
			for(int i=0; i < playersUsed.size(); i++) {
				if(playersUsed.get(i).getName().equals(p.getName()))
					return i;
			}
			return -1;
		}
		
		public void giveGuideBook(Player p) {
			if(getTimeSinceLastUse(p) > guideBookCooldown) {
				int playerIndex = getPlayerIndex(p);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"give @a[name="+p.getName()+"] written_book{pages:[\"[\\\"\\\",{\\\"text\\\":\\\"QuarryCraft Guide\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\nThis plugin was coded by Warren Hood.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"\\\\n\\\"},{\\\"text\\\":\\\" \\\\u0020 \\\\u0020 \\\\u0020 \\\",\\\"bold\\\":true,\\\"italic\\\":true},{\\\"text\\\":\\\"Contents\\\",\\\"bold\\\":true,\\\"italic\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"3. Building the quarry\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":3}},{\\\"text\\\":\\\"\\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"7. Fuel\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":7}},{\\\"text\\\":\\\"\\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"8. Fuel Efficiency\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":8}},{\\\"text\\\":\\\"\\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"9. Upgrades\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":9}},{\\\"text\\\":\\\"\\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"12. Block Filters\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":12}},{\\\"text\\\":\\\"\\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"13. Mining Modes\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":13}},{\\\"text\\\":\\\"\\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"14. Pausing the quarry\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":14}},{\\\"text\\\":\\\"\\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"14. Viewing progress\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":14}},{\\\"text\\\":\\\"\\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"16. Video Tutorial\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":16}}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Building the quarry\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Step 1.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\\nPlace down a chest\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Step 2.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\\nPlace a diamond block against each side of the chest. You will need 4 diamond blocks.\\\\n \\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Step 3.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\\nPlace a redstone block in each of the 4 corners.\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Step 4.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\\nPlace iron bars running outwards from the diamond blocks. These will define your quarry's mining area. It is only necessary to run it from 2 sides.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Step 5.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\\nPlace chests on either side of the iron bars or on top of the iron bars for storage of mined items. They must touch the iron bars and be in the defined quarry area.\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Step 6.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\\nPlace fuel in the centre chest. (Look on page 7)\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"\\\\n\\\"},{\\\"text\\\":\\\"Step 6.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\\nSneak-left-click the chest to create the quarry. It should now begin mining.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020\\\"},{\\\"text\\\":\\\"Fuel\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\nQuarries will accept the following types of fuel:\\\\n\\\\n- Charcoal\\\\n- Coal\\\\n- Coal Blocks\\\\n- Redstone\\\\n- Redstone Blocks\\\\n\\\\n \\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\\u0020\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"Fuel Efficiency\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Redstone\\\",\\\"bold\\\":true},{\\\"text\\\":\\\" is the most efficient(Provides most energy). Redstone blocks are equivalent to 9 redstone dusts.\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Coal\\\",\\\"bold\\\":true},{\\\"text\\\":\\\" is half as efficient as redstone. Coal blocks are equivalent to 9 coal.\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Charcoal\\\",\\\"bold\\\":true},{\\\"text\\\":\\\" is half as efficient as coal.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\\u0020 \\\\u0020\\\"},{\\\"text\\\":\\\"Upgrades\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\\nQuarries are upgradeable. Place a chest on of the the 4 redstone corners.\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Mining delay\\\",\\\"bold\\\":true},{\\\"text\\\":\\\" - A max of 76 emerald blocks can be placed in the upgrade chest to reduce delay between each block(s) mined.\\\\n \\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Blocks mined at a time\\\",\\\"bold\\\":true},{\\\"text\\\":\\\" - A max of 36 diamond blocks can be placed in the upgrade chest to increase the number of blocks mined after each delay.\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Efficiency - \\\",\\\"bold\\\":true},{\\\"text\\\":\\\"A max of 100 gold blocks can be placed in the upgrade chest to increase efficiency.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Ender quarry replaces mined blocks with dirt - \\\",\\\"bold\\\":true},{\\\"text\\\":\\\"Place a nether star in the upgrade chest to make ender quarrying replace mined blocks with dirt. This will prevent holes under the ground.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\\u0020 \\\\u0020\\\"},{\\\"text\\\":\\\"Block Filters\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\\nPlace a shulker box on one of the 4 redstone corners. Then place blocks which you would like to void into this box. Your quarry will still mine those blocks and use energy on them. But it will save storage space!\\\\n \\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\\u0020\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"Mining Modes\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\\nSneak-left click your centre chest with an empty hand to toggle between clasic and ender mode. Classic mode mines everything. Ender mode ignores stone, grass and dirt, which is a lot faster, but uses 50x energy.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\",\\\"bold\\\":true},{\\\"text\\\":\\\"Pausing the Quarry\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\\nLeft click one of the 4 diamond blocks to pause/unpause your quarry.\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Viewing Quarry Progress\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\\nSneak-right-click the centre chest with an empty hand to view quarry progress.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Resetting mining level\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\\nIf you'd like to make the quarry start mining from the top again, sneak-left-click one of the 4 diamond blocks with an empty hand\\\\n \\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\\u0020\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"Video Tutorial\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\\nI also made a video explaining most of this stuff:\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Watch it on youtube by clicking here.\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"open_url\\\",\\\"value\\\":\\\"https://www.youtube.com/watch?v=3A0aurYeOUA&feature=youtu.be\\\"}}]\"],title:\"QuarryCraft Guide\",author:\"Warren Hood\",display:{Lore:[\"A guide to building and using QuarryCraft quarries.\"]}}");
				
				if(playerIndex == -1) {
					playersUsed.add(p);
					lastUsed.add(new Date());
				}
				else {
					lastUsed.set(playerIndex, new Date());
				}
			}
			
			else {
				p.sendMessage(configurableMessages.pleaseWaitBeforeNumSeconds() + " " + (long)(((double)(guideBookCooldown - getTimeSinceLastUse(p)))/1000.0) +  " " + configurableMessages.pleaseWaitAfterNumSeconds());
			}
		}
	}
	
	class QuarryCraftTabCompleter implements TabCompleter {
		@Override
		public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
			ArrayList<String> ls = new ArrayList<String>();
			
			ls.add("guide");
			if(sender.isOp()) ls.add("reload");
			
			/*
			if(args.length == 1 && args[0].equals("permission") && sender.isOp()) {
				ls.add("allow");
				ls.add("deny");
			}
			
			if(args.length == 2 && args[0].equals("permission") && sender.isOp() && (args[1].equals("allow") || args[1].equals("deny")) ) {
				ls.add("all-online");
				for(World w : Bukkit.getWorlds())
					for(Player p : w.getPlayers())
						ls.add(p.getName());
			}*/
			
			
			return ls;
		}
	}
	
	
	@EventHandler 
	public void onPlayerJoin(PlayerJoinEvent e) {
		if(welcomeMessages) {
			e.getPlayer().sendMessage(configurableMessages.playerJoin1() + " " + e.getPlayer().getName());
			e.getPlayer().sendMessage(configurableMessages.playerJoin2());
			e.getPlayer().sendMessage(configurableMessages.playerJoin3());
		}
			
	}
	
	@Override
	public void onEnable() {
		String fileSeparator = System.getProperty("file.separator");
		File pluginDir = new File("plugins" + fileSeparator + "QuarryCraft");
		pluginDir.mkdir();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		this.plugin = this;
		configurableMessages = new ConfigurableMessages();
		configurableMessages.loadMessages();
		loadConfig();
		gbGiver = new GuideBookGiver();
		
		
		getCommand("quarrycraft").setExecutor(new guideCommand());
		getCommand("quarrycraft").setTabCompleter(new QuarryCraftTabCompleter());
		
		
		quarries = new ArrayList<Quarry>();
		
		loadQuarries();
		new QuarryCleaner().runTaskTimer(plugin, 10, 10);
	}
	
	@EventHandler
	public void pistonRetractEvent(BlockPistonRetractEvent e) {
		Location ploc = e.getBlock().getLocation();
		if(!pistonAllowed(ploc.getWorld(), ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ())) {
			//Bukkit.broadcastMessage("Cancelling retract event");
			e.setCancelled(true);
		}
			
	}
	
	@EventHandler
	public void pistonExtendEvent(BlockPistonExtendEvent e) {
		Location ploc = e.getBlock().getLocation();
		if(!pistonAllowed(ploc.getWorld(), ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ())) {
			//Bukkit.broadcastMessage("Cancelling extend event");
			e.setCancelled(true);
		}
			
	}
	
	@EventHandler
	public void onPlayerLeftClick(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(!canInteract(e.getClickedBlock().getLocation(), e.getPlayer())) {
				e.getPlayer().sendMessage(configurableMessages.dontHaveInteractPermission());
				e.setCancelled(true);
				return;
			}
			if(e.getAction().equals(Action.LEFT_CLICK_BLOCK) && !canBreak(e.getClickedBlock().getLocation(), e.getPlayer())) {
				e.getPlayer().sendMessage(configurableMessages.blockCannotBeBroken());
				e.setCancelled(true);
				return;
			}
		}
		
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			Block clicked = e.getClickedBlock();
			if(!e.getPlayer().isSneaking()) {
				if(clicked.getType().equals(Material.DIAMOND_BLOCK) && e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
					for(Quarry q : quarries) {
						if(q.isIn3x3(clicked)) {
							q.togglePause(e.getPlayer());
							e.setCancelled(true);
							saveQuarries();
							return;
						}
					}
				}
				return;
			}
			
			if(clicked.getType().equals(Material.CHEST)) {
				Chest centreChest = (Chest) clicked.getState();
				if(isQuarryLayout(centreChest)) {
					if(hasPermission(e.getPlayer(),"quarrycraft.buildquarries") && countQuarries(e.getPlayer()) < quarryLimit && addQuarry(centreChest, e.getPlayer().getName())) {
						e.getPlayer().sendMessage(configurableMessages.quarryCreated());
						e.setCancelled(true);
					}
					else if(getQuarry(centreChest) != null){
						e.getPlayer().sendMessage(getQuarry(centreChest).toggleEndermining());
						e.setCancelled(true);
					}
					else if(!hasPermission(e.getPlayer(),"quarrycraft.buildquarries")) {
						e.getPlayer().sendMessage(configurableMessages.noBuildPerm());
						e.setCancelled(true);
					}
					else if(countQuarries(e.getPlayer()) >= quarryLimit) {
						e.getPlayer().sendMessage(configurableMessages.quarryLimBeforeLimit()+quarryLimit+configurableMessages.quarryLimAfterLimit());
						e.setCancelled(true);
					}
					else {
						e.getPlayer().sendMessage(configurableMessages.quarryIntersectError());
						e.setCancelled(true);
					}
					saveQuarries();
				}
			}
			if(clicked.getType().equals(Material.DIAMOND_BLOCK)) {
				for(Quarry q : quarries) {
					if(q.isIn3x3(clicked)) {
						q.resetMiningCursor();
						e.getPlayer().sendMessage(configurableMessages.miningCursorReset() + " y=" + ChatColor.DARK_GREEN + q.nextY);
						e.setCancelled(true);
						return;
					}
				}
			}
		}
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(!e.getPlayer().isSneaking()) return;
			Block clicked = e.getClickedBlock();
			if(clicked.getType().equals(Material.CHEST)) {
				Chest centreChest = (Chest) clicked.getState();
				if(isQuarryLayout(centreChest)) {
					Quarry q = getQuarry(centreChest);
					if(q != null && e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)){
						q.sendProgress(e.getPlayer());
						e.setCancelled(true);
					}
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
	
	public boolean ptIntersects(World w, int x, int z) {
		for (Quarry q : quarries)
			if(q.ptIntersects(w, x, z)) return true;
		return false;
	}
	
	public boolean quarryIntersects(Quarry qc) {
		for(Quarry q : quarries) {
			if(qc.centreChestLocation.equals(q.centreChestLocation)) continue;
			World w = qc.world;
			if(q.ptIntersects(w, qc.minX, qc.minZ)) return true;
			if(q.ptIntersects(w, qc.minX, qc.maxZ)) return true;
			if(q.ptIntersects(w, qc.maxX, qc.minZ)) return true;
			if(q.ptIntersects(w, qc.maxX, qc.maxZ)) return true;
			if(qc.ptIntersects(q.world, q.minX, q.minZ)) return true;
			if(qc.ptIntersects(q.world, q.minX, q.maxZ)) return true;
			if(qc.ptIntersects(q.world, q.maxX, q.minZ)) return true;
			if(qc.ptIntersects(q.world, q.maxX, q.maxZ)) return true;
		}
		return false;
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
		for(World w : Bukkit.getWorlds()) {
			String wName = w.getName();
			String fileSeparator = System.getProperty("file.separator");
			File folder = new File(wName + fileSeparator + "QuarryCraft");
			folder.mkdir();
			String path = wName + fileSeparator + "QuarryCraft" + fileSeparator + "quarries.txt";
			File quarryFile = new File(path);
			quarryFile.delete();
			String fileString = "";
			for(Quarry q : quarries) {
				Location quarryLoc = q.getLocation();
				if(!quarryLoc.getWorld().getName().equals(wName)) continue;
				int minX = q.minX;
				int minZ = q.minZ;
				int maxX = q.maxX;
				int maxZ = q.maxZ;
				fileString += quarryLoc.getWorld().getName() + ";" + quarryLoc.getBlockX() + ";" + quarryLoc.getBlockY() + ";" + quarryLoc.getBlockZ() + ";" + minX + ";" + minZ + ";" + maxX + ";" + maxZ + ";" + q.classicMode + ";" + q.owner+";"+q.paused+"\n";		
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
		
	}
	
	public void loadQuarries() {
		for(World w : Bukkit.getWorlds()) {
			String wName = w.getName();
			String fileSeparator = System.getProperty("file.separator");
			String path = wName + fileSeparator + "QuarryCraft" + fileSeparator + "quarries.txt";
			try {
				BufferedReader inFile = new BufferedReader(new FileReader(path));
				String currentCoords;
				String[] locString;
				int minX, minZ, maxX, maxZ;
				boolean classicMode;
				int x,y,z;
				Location currentLocation;
				String ownerName;
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
					ownerName = locString[9].trim();
					boolean isPaused = false;
					if(locString.length == 11) isPaused = locString[10].trim().equals("true");
					classicMode = locString[8].trim().contentEquals("true");
					Location quarryLoc = new Location(Bukkit.getWorld(locString[0]), x, y, z);
					addQuarry(quarryLoc, minX, maxX, minZ, maxZ, classicMode,ownerName, isPaused);
						
				} while(currentCoords != null);
				
				saveQuarries();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}
	
	public int countQuarries(Player p) {
		int count = 0;
		for(Quarry q : quarries)
			if(q.isOwner(p)) count++;
		return count;
	}
	
	public boolean canInteract(Location l, Player p) {
		for(Quarry q : quarries)
			if(!q.canInteractAt(l, p)) return false;
		return true;
	}
	
	public boolean canBreak(Location l, Player p) {
		for(Quarry q : quarries)
			if(!q.canBreak(l, p)) return false;
		return true;
	}
	
	public boolean pistonAllowed(World w, int x, int y, int z) {
		for(Quarry q: quarries) {
			if(!q.pistonAllowed(w, x, y, z)) return false;
		}
		return true;
	}
	
	public boolean hasPermission(Player p, String perm) {
		if(p.isOp()) return true;
		
		return p.hasPermission(perm);
	}
	
	public boolean addQuarry(Chest centreChest, String name) {
		if(getQuarry(centreChest) == null) {
			Quarry quarry = new Quarry(centreChest, name);
			if(quarryIntersects(quarry)) return false;
			quarries.add(quarry);
			quarry.runTaskTimer(plugin, 0, 0);
			return true;
		}
		return false;
	}
	
	public boolean addQuarry(Location centreChestLocation, int minX, int maxX, int minZ, int maxZ, boolean mode, String name, boolean paused) {
		if(centreChestLocation.getWorld().getBlockAt(centreChestLocation).getType().equals(Material.CHEST)) {
			Quarry quarry = new Quarry((Chest)centreChestLocation.getWorld().getBlockAt(centreChestLocation).getState(), minX, maxX, minZ, maxZ, mode, name);
			if(quarryIntersects(quarry)) {
				return false;
			}
			quarry.paused = paused;
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
					q.clearPlatform();
					q.tellOwner(configurableMessages.quarryDestroyedBeforeCoords() + " " + q.centreChestLocation.toVector().toString() + " " + configurableMessages.quarryDestroyedAfterCoords());
					removeQuarry(q);
					return;
				}
		}
	}
	
	public void loadConfig() {
		String fileSeparator = System.getProperty("file.separator");
		String path = "plugins" + fileSeparator + "QuarryCraft" + fileSeparator + "config.conf";
		try {
			BufferedReader inFile = new BufferedReader(new FileReader(path));
			String currentString;
			String[] splitString;
			String var;
			String val; 
			do {
				currentString = inFile.readLine();
				if(currentString == null) break;
				splitString = currentString.split("=");
				if(splitString == null || splitString.length != 2) continue;
				var = splitString[0].trim().toLowerCase();
				val = splitString[1].trim().toLowerCase();
				
				switch(var) {
				case "quarries-per-player":
					quarryLimit = Integer.parseInt(val);
					break;
				case "max-quarry-width":
					maxQuarryWidth = Integer.parseInt(val);
					break;
				case "max-quarry-length":
					maxQuarryLength = Integer.parseInt(val);
					break;
				case "enable-quarrycraft-welcome-message":
					if(val.equals("false") || val.equals("0"))
						welcomeMessages = false;
					break;
				case "guidebook-cooldown-milliseconds":
					guideBookCooldown = Long.parseLong(val);
					break;
				}
				
			} while(currentString != null);
			inFile.close();
		} catch (IOException e) {
			String fileString = "";
			fileString += "quarries-per-player = " + quarryLimit + "\n\n";
			fileString += "(Width and length below are multiplied to find the area, then quarry areas are checked against this)\n";
			fileString += "max-quarry-width = " + maxQuarryWidth + "\n";
			fileString += "max-quarry-length = " + maxQuarryLength + "\n";
			fileString += "enable-quarrycraft-welcome-message = " + welcomeMessages + "\n\n";
			fileString += "guidebook-cooldown-milliseconds = " + guideBookCooldown + "\n";
			
			
			try {
				FileOutputStream fos = new FileOutputStream(path);
				fos.write(fileString.getBytes());
				fos.flush();
				fos.close();
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
		}
	}
}

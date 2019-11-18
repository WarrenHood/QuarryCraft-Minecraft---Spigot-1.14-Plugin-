package com.nullbyte.quarrycraft;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class Quarry extends BukkitRunnable {
	Location centreChestLocation;
	Chest centreChest;
	float storedEnergy;
	int minX, minZ, maxX, maxZ;
	int nextX, nextY, nextZ;
	boolean classicMode = true;
	boolean enderReplaceDirt = false;
	boolean alerted = false;
	boolean markedForDeletion = false;
	World world;
	int blocksPerTick = 1;
	int miningDelay = 20;
	int counter = 0;
	String owner;
	boolean paused;
	boolean finished;
	
	int emeraldBlocks = 0;
	int diamondBlocks = 0;
	int goldBlocks = 0;
	boolean platformDone;
	int platX,platZ;
	
	float energyMod = 1.0f;
	
	static Material[] ignored = {Material.BEDROCK, Material.AIR, Material.WATER, Material.LAVA, Material.GRASS, Material.GRASS_BLOCK, Material.GRASS_PATH, Material.STONE, Material.COBBLESTONE, Material.DIRT, Material.COARSE_DIRT};

	public Quarry(Chest centreChest, String owner) {
		this.owner = owner;
		this.centreChest = centreChest;
		this.centreChestLocation = centreChest.getLocation();
		world = centreChest.getWorld();
		storedEnergy = 0;
		setBounds();
		platX = minX-1;
		platZ = minZ-1;
		paused = false;
		finished = false;
	}
	
	public void togglePause() {
		paused = !paused;
		if(paused)
			tellOwner(ChatColor.YELLOW + "Your quarry at " + centreChestLocation.toVector().toString() + " has been paused.");
		else {
			tellOwner(ChatColor.GREEN + "Your quarry at " + centreChestLocation.toVector().toString() + " is no longer paused.");
			alerted = false;
		}
			
	}
	
	public boolean isOwner(Player p) {
		return (owner != null) && p.getName().equals(owner);
	}
	
	public boolean pistonAllowed(int x, int y, int z) {
		if(x >= minX-3 && x <= maxX + 3 && z >= minZ-3 && z <= maxZ+3 && y >= centreChestLocation.getBlockY()-3 && y <= centreChestLocation.getBlockY() + 3) return false;
		return true;
	}
	
	public Quarry(Chest centreChest, int minX, int maxX, int minZ, int maxZ, boolean mode, String owner) {
		this(centreChest, owner);
		classicMode = mode;
		this.minX = minX;
		this.maxX = maxX;
		this.minZ = minZ;
		this.maxZ = maxZ;
		nextX = minX;
		nextY = centreChestLocation.getBlockY()-2;
		nextZ = minZ;
		platX = minX-1;
		platZ = minZ-1;
	}
	
	public void movePlatformCursor() {
		if(platformDone) return;
		platX++;
		if(platX > maxX+1) {
			if(platZ < maxZ+1) {
				platX = minX - 1;
				platZ++;
			}
			else {
				platX = maxX + 1;
				platformDone = true;
			}
		}
	}
	
	public void resetPlatformCursor() {
		platX = minX-1;
		platZ = minZ-1;
	}
	
	public void buildPlatform() {
		if(platformDone) return;
		Block currentBlock = world.getBlockAt(platX, centreChestLocation.getBlockY()-1, platZ);
		while(!platformDone) {
			// Do borders and centres
			if(platZ == minZ-1 || platZ == maxZ+1 || platX == minX-1 || platX == maxX+1 || (platZ >= centreChestLocation.getBlockZ()-3 && platZ <= centreChestLocation.getBlockZ()+3) || (platX >= centreChestLocation.getBlockX()-3 && platX <= centreChestLocation.getBlockX()+3)) {
				currentBlock = world.getBlockAt(platX, centreChestLocation.getBlockY()-1, platZ);
				//if(currentBlock.getType().equals(Material.AIR) || currentBlock.getType().equals(Material.WATER) || currentBlock.getType().equals(Material.LAVA) || currentBlock.getType().equals(Material.DIRT) || currentBlock.getType().equals(Material.GRASS) || currentBlock.getType().equals(Material.GRASS_BLOCK)) {
				if(platZ == minZ-1 || platZ == maxZ+1 || platX == minX-1 || platX == maxX+1) {
					if(classicMode)
						currentBlock.setType(Material.BLACK_STAINED_GLASS);
					else
						currentBlock.setType(Material.WHITE_STAINED_GLASS);
				}
					
				else {
					if(classicMode)
						currentBlock.setType(Material.GREEN_STAINED_GLASS);
					else {
						currentBlock.setType(Material.PURPLE_STAINED_GLASS);
						world.spawnParticle(Particle.DRAGON_BREATH, currentBlock.getLocation(), 1);
					}
						
				}
				//}
				
			}
			else {
				currentBlock = world.getBlockAt(platX, centreChestLocation.getBlockY()-1, platZ);
				//if(currentBlock.getType().equals(Material.AIR) || currentBlock.getType().equals(Material.WATER) || currentBlock.getType().equals(Material.LAVA) || currentBlock.getType().equals(Material.DIRT) || currentBlock.getType().equals(Material.GRASS) || currentBlock.getType().equals(Material.GRASS_BLOCK) || currentBlock.getType().equals(Material.STONE) || currentBlock.getType().equals(Material.COBBLESTONE)) {
				if(classicMode)
					currentBlock.setType(Material.CYAN_STAINED_GLASS);
				else
					currentBlock.setType(Material.BLACK_STAINED_GLASS);
				
				//}
			}
			if(classicMode)
				world.playSound(currentBlock.getLocation(), Sound.BLOCK_GLASS_PLACE, 1f, 1f);
			else
				world.playSound(currentBlock.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
			movePlatformCursor();
		}
		
			
		
		
	}
	
	public boolean ptIntersects(int x, int z) {
		return x >= minX - 1 && x <= maxX + 1 && z >= minZ - 1 && z <= maxZ + 1;
	}
	
	public void clearPlatform() {
		for(int x=minX-1; x <=maxX+1; x++) {
			for(int z=minZ-1; z<=maxZ+1; z++) {
				Block currentBlock = world.getBlockAt(x,centreChestLocation.getBlockY()-1,z);
				if(currentBlock.getType().equals(Material.BLACK_STAINED_GLASS) || currentBlock.getType().equals(Material.GREEN_STAINED_GLASS) || currentBlock.getType().equals(Material.CYAN_STAINED_GLASS) || currentBlock.getType().equals(Material.PURPLE_STAINED_GLASS) || currentBlock.getType().equals(Material.WHITE_STAINED_GLASS)) {
					world.playSound(currentBlock.getLocation(), Sound.BLOCK_GLASS_BREAK, 1f, 1f);
					currentBlock.setType(Material.AIR);
				}
			}
		}
	}
	
	public void calculateUpgrades() {
		int emeraldBlockCount = 0; // Emeralds blocks increase the mining rate. 4 emerald blocks reduces mining delay by 1 tick
		int diamondBlockCount = 0; // Diamond blocks increase the blocks mined at a time. 4 diamond blocks give 1 more block at a time
		int goldBlockCount = 0;
		boolean hasNetherStar = false;
		
		int cx = centreChestLocation.getBlockX();
		int cy = centreChestLocation.getBlockY();
		int cz = centreChestLocation.getBlockZ();
		
		
		
		if(world.getBlockAt(cx-1,cy+1,cz-1).getType().equals(Material.CHEST) || world.getBlockAt(cx-1,cy+1,cz-1).getType().equals(Material.TRAPPED_CHEST)) {
			Inventory chestInv = ((Chest)world.getBlockAt(cx-1,cy+1,cz-1).getState()).getInventory();
			for(int i=0; i<chestInv.getSize(); i++) {
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.EMERALD_BLOCK))emeraldBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.DIAMOND_BLOCK))diamondBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.GOLD_BLOCK))goldBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.NETHER_STAR))hasNetherStar = true;
			}
		}
		if(world.getBlockAt(cx-1,cy+1,cz+1).getType().equals(Material.CHEST) || world.getBlockAt(cx-1,cy+1,cz+1).getType().equals(Material.TRAPPED_CHEST)) {
			Inventory chestInv = ((Chest)world.getBlockAt(cx-1,cy+1,cz+1).getState()).getInventory();
			for(int i=0; i<chestInv.getSize(); i++) {
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.EMERALD_BLOCK))emeraldBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.DIAMOND_BLOCK))diamondBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.GOLD_BLOCK))goldBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.NETHER_STAR))hasNetherStar = true;
			}
		}
		if(world.getBlockAt(cx+1,cy+1,cz-1).getType().equals(Material.CHEST) || world.getBlockAt(cx+1,cy+1,cz-1).getType().equals(Material.TRAPPED_CHEST)) {
			Inventory chestInv = ((Chest)world.getBlockAt(cx+1,cy+1,cz-1).getState()).getInventory();
			for(int i=0; i<chestInv.getSize(); i++) {
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.EMERALD_BLOCK))emeraldBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.DIAMOND_BLOCK))diamondBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.GOLD_BLOCK))goldBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.NETHER_STAR))hasNetherStar = true;
			}
		}
		if(world.getBlockAt(cx+1,cy+1,cz+1).getType().equals(Material.CHEST) || world.getBlockAt(cx+1,cy+1,cz+1).getType().equals(Material.TRAPPED_CHEST)) {
			Inventory chestInv = ((Chest)world.getBlockAt(cx+1,cy+1,cz+1).getState()).getInventory();
			for(int i=0; i<chestInv.getSize(); i++) {
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.EMERALD_BLOCK))emeraldBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.DIAMOND_BLOCK))diamondBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.GOLD_BLOCK))goldBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.NETHER_STAR))hasNetherStar = true;
			}
		}
		
		if(emeraldBlockCount > 76) emeraldBlockCount = 76;
		if(diamondBlockCount > 36) diamondBlockCount = 36;
		if(goldBlockCount > 100) goldBlockCount = 100;
		
		if(emeraldBlockCount != emeraldBlocks || diamondBlockCount != diamondBlocks || (enderReplaceDirt != hasNetherStar) || goldBlockCount != goldBlocks) {
			emeraldBlocks = emeraldBlockCount;
			diamondBlocks = diamondBlockCount;
			goldBlocks = goldBlockCount;
			enderReplaceDirt = hasNetherStar;
			
			energyMod = (101.0f-(float)goldBlocks)/101.0f;
			float efficiency = 100.0f*((float)goldBlocks)/101.0f;
			
			miningDelay = 20 - ((int)(Math.floor(((float)emeraldBlocks)/4.0)));
			blocksPerTick = 1 + (int)Math.floor(((float)diamondBlocks)/4.0 );
			
			int emeraldsToNext = 4-emeraldBlocks%4;
			int diamondsToNext = 4-diamondBlocks%4;
			
			if(emeraldBlocks == 76) emeraldsToNext = 0;
			if(diamondBlocks == 36) diamondsToNext = 0;
			
			tellOwner("Your quarry has been modified:\nMining Delay: " + ChatColor.DARK_GREEN + miningDelay + ChatColor.WHITE + "\nEmerald blocks to next upgrade: " + ChatColor.GREEN + emeraldsToNext + ChatColor.WHITE + "\nBlocks mined at a time: " + ChatColor.DARK_BLUE + blocksPerTick + ChatColor.WHITE + "\nDiamond blocks to next upgrade: " + ChatColor.AQUA + diamondsToNext + ChatColor.WHITE + "\nEfficiency: "+ ChatColor.YELLOW + efficiency + "%"+ChatColor.WHITE +  "\nEnder mining replaces blocks with dirt: " + ChatColor.GOLD + enderReplaceDirt);
		}
		
	}
	
	public void sendProgress() {
		if (finished)
			tellOwner("The quarry is finished at y=" + ChatColor.DARK_BLUE + nextY);
		else if(!paused)
			tellOwner("The quarry is mining at y=" + ChatColor.DARK_GREEN + nextY);
		else
			tellOwner("The quarry is paused at y=" + ChatColor.YELLOW + nextY);
	}
	
	public boolean isSameCentreChest(Chest someCentreChest) {
		return centreChest != null && centreChest.equals(someCentreChest);
	}
	
	public boolean checkCentreChest() {
		return world.getBlockAt(centreChestLocation) != null && world.getBlockAt(centreChestLocation).getType().equals(Material.CHEST);
	}
	
	public Location getLocation() {
		return centreChestLocation;
	}
	
	public String toggleEndermining() {
		alerted = false;
		classicMode = !classicMode;
		resetMiningCursor();
		resetPlatformCursor();
		clearPlatform();
		platformDone = false;
		if(classicMode) return "Mining mode toggled: "+ ChatColor.GREEN + "Classic";
		return "Mining mode toggled: "+ ChatColor.BLUE + "Ender";
	}
	
	public boolean isIn3x3(Block b) {
		Location bLoc = b.getLocation();
		return bLoc.getBlockX() >= centreChestLocation.getBlockX()-1 && bLoc.getBlockX() <= centreChestLocation.getBlockX()+1 && bLoc.getBlockZ() >= centreChestLocation.getBlockZ()-1 && bLoc.getBlockZ() <= centreChestLocation.getBlockZ()+1 && bLoc.getBlockY() == centreChestLocation.getBlockY();
		
	}
	
	public void resetMiningCursor() {
		nextY = centreChestLocation.getBlockY()-2;
		nextX = minX;
		nextZ = minZ;
		finished = false;
	}
	
	public boolean hasFuel(float amount) {
		centreChest = (Chest)world.getBlockAt(centreChestLocation).getState();
		Inventory centreInv = centreChest.getInventory();
		float energySoFar = storedEnergy;
		if(energySoFar >= amount) {
			//Bukkit.broadcastMessage("Needs " + amount + "energy, has " + energySoFar);
			return true;
		}
		QuarryFuel currentFuel;
		for(int i=0; i<centreInv.getSize(); i++) {
			ItemStack currentItem = centreInv.getItem(i);
			if(currentItem == null) continue;
			currentFuel = QuarryFuel.getFuel(currentItem.getType());
			if(currentFuel != null) energySoFar += currentFuel.getEnergyValue()*centreInv.getItem(i).getAmount();
			if(energySoFar >= amount) {
				//Bukkit.broadcastMessage("Needs " + amount + "energy, has " + energySoFar);
				return true;
			}
		}
		return false;
	}
	
	public boolean consumeFuel(float amount) {
		float energyToConsume = amount;
		float tempStoredEnergy = storedEnergy;
		if(storedEnergy > 0) {
			if(storedEnergy >= energyToConsume) {
				storedEnergy -= energyToConsume;
				return true;
			}
			else {
				energyToConsume -= tempStoredEnergy;
				tempStoredEnergy = 0;
			}
		}
		if(hasFuel(energyToConsume)) {
			Inventory centreInv = centreChest.getInventory();
			QuarryFuel currentFuel;
			for(int i=0; i<centreInv.getSize(); i++) {
				ItemStack currentItem = centreInv.getItem(i);
				if(currentItem != null)
					currentFuel = QuarryFuel.getFuel(centreInv.getItem(i).getType());
				else currentFuel = null;
				
				if(currentFuel != null) {
					
					float stackEnergy = currentFuel.getEnergyValue()*centreInv.getItem(i).getAmount();
					
					if(stackEnergy <= energyToConsume) {
						// Take the whole stack
						centreInv.setItem(i, new ItemStack(Material.AIR));
						
						energyToConsume -= stackEnergy;
						
						if(energyToConsume == 0) {
							storedEnergy = tempStoredEnergy;
							return true;
						}
					}
					else {
						// Take part of the stack
						int numToTake = (int)Math.ceil(energyToConsume/currentFuel.getEnergyValue());
						centreInv.getItem(i).setAmount(centreInv.getItem(i).getAmount() - numToTake);
						tempStoredEnergy += ((float)numToTake)*currentFuel.getEnergyValue();
						
						tempStoredEnergy -= energyToConsume;
						storedEnergy = tempStoredEnergy;
						
						return true;
					}

				}
			}
			
			// This should never happen
			return false;
		}
		else {
			return false;
		}
	}
	
	public boolean isShulkerBox(Material mat) {
		return mat.equals(Material.SHULKER_BOX) || mat.equals(Material.BLACK_SHULKER_BOX) || mat.equals(Material.BLUE_SHULKER_BOX) || mat.equals(Material.BROWN_SHULKER_BOX) || mat.equals(Material.CYAN_SHULKER_BOX) || mat.equals(Material.GRAY_SHULKER_BOX) || mat.equals(Material.GREEN_SHULKER_BOX) || mat.equals(Material.LIGHT_BLUE_SHULKER_BOX) || mat.equals(Material.LIGHT_GRAY_SHULKER_BOX) || mat.equals(Material.LIME_SHULKER_BOX) || mat.equals(Material.MAGENTA_SHULKER_BOX) || mat.equals(Material.ORANGE_SHULKER_BOX) || mat.equals(Material.PINK_SHULKER_BOX) || mat.equals(Material.PURPLE_SHULKER_BOX) || mat.equals(Material.RED_SHULKER_BOX);
	}
	
	public boolean isFiltered(Material mat) {
		int cx = centreChestLocation.getBlockX();
		int cy = centreChestLocation.getBlockY();
		int cz = centreChestLocation.getBlockZ();
		
		if(isShulkerBox(world.getBlockAt(cx-1,cy+1,cz-1).getType())) {
			Inventory chestInv = ((ShulkerBox)world.getBlockAt(cx-1,cy+1,cz-1).getState()).getInventory();
			if(chestInv.contains(mat)) return true;
		}
		
		if(isShulkerBox(world.getBlockAt(cx-1,cy+1,cz+1).getType())) {
			Inventory chestInv = ((ShulkerBox)world.getBlockAt(cx-1,cy+1,cz+1).getState()).getInventory();
			if(chestInv.contains(mat)) return true;
		}
		
		if(isShulkerBox(world.getBlockAt(cx+1,cy+1,cz-1).getType())) {
			Inventory chestInv = ((ShulkerBox)world.getBlockAt(cx+1,cy+1,cz-1).getState()).getInventory();
			if(chestInv.contains(mat)) return true;
		}
		
		if(isShulkerBox(world.getBlockAt(cx+1,cy+1,cz+1).getType())) {
			Inventory chestInv = ((ShulkerBox)world.getBlockAt(cx+1,cy+1,cz+1).getState()).getInventory();
			if(chestInv.contains(mat)) return true;
		}
		
		return false;
	}
	
	public boolean addMined(Material mat) {
		if(isFiltered(mat)) {
			return true;
		}
		int cx = centreChestLocation.getBlockX();
		int cz = centreChestLocation.getBlockZ();
		int y = centreChestLocation.getBlockY();
		
		// Do x = minX to maxX for z=cz
		for(int x=minX; x <= maxX; x++) {
			// Check for an iron bar
			if(!world.getBlockAt(x,y, cz).getType().equals(Material.IRON_BARS)) continue;
			
			// Check for chest at cz-1
			if(world.getBlockAt(x, y, cz-1).getType().equals(Material.CHEST) || world.getBlockAt(x, y, cz-1).getType().equals(Material.TRAPPED_CHEST)) {
				Chest chest = (Chest)world.getBlockAt(x, y, cz-1).getState();
				Inventory chestInv = chest.getInventory();
				for(int i=0; i<chestInv.getSize(); i++) {
					if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(mat) && chestInv.getItem(i).getAmount() < 64) {
						chestInv.getItem(i).setAmount(chestInv.getItem(i).getAmount()+1);
						return true;
					}
					if(chestInv.getItem(i) == null || chestInv.getItem(i).getAmount() == 0) {
						chestInv.setItem(i, new ItemStack(mat));
						return true;
					}
				}
			}
			
			// Check for chest at cz+1
			if(world.getBlockAt(x, y, cz+1).getType().equals(Material.CHEST) || world.getBlockAt(x, y, cz+1).getType().equals(Material.TRAPPED_CHEST)) {
				Chest chest = (Chest)world.getBlockAt(x, y, cz+1).getState();
				Inventory chestInv = chest.getInventory();
				for(int i=0; i<chestInv.getSize(); i++) {
					if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(mat) && chestInv.getItem(i).getAmount() < 64) {
						chestInv.getItem(i).setAmount(chestInv.getItem(i).getAmount()+1);
						return true;
					}
					if(chestInv.getItem(i) == null || chestInv.getItem(i).getAmount() == 0) {
						chestInv.setItem(i, new ItemStack(mat));
						return true;
					}
				}
			}
			
		}
		
		// Do z = minZ to maxZ for x=cx
		for(int z=minZ; z<=maxZ; z++) {
			// Check for an iron bar
			if(!world.getBlockAt(cx, y, z).getType().equals(Material.IRON_BARS)) continue;
			
			// Check for chest at cx-1
			if(world.getBlockAt(cx-1, y, z).getType().equals(Material.CHEST) || world.getBlockAt(cx-1, y, z).getType().equals(Material.TRAPPED_CHEST)) {
				Chest chest = (Chest)world.getBlockAt(cx-1, y, z).getState();
				Inventory chestInv = chest.getInventory();
				for(int i=0; i<chestInv.getSize(); i++) {
					if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(mat) && chestInv.getItem(i).getAmount() < 64) {
						chestInv.getItem(i).setAmount(chestInv.getItem(i).getAmount()+1);
						return true;
					}
					if(chestInv.getItem(i) == null || chestInv.getItem(i).getAmount() == 0) {
						chestInv.setItem(i, new ItemStack(mat));
						return true;
					}
				}
			}
			
			
			// Check for chest at cx+1
			if(world.getBlockAt(cx+1, y, z).getType().equals(Material.CHEST) || world.getBlockAt(cx+1, y, z).getType().equals(Material.TRAPPED_CHEST)) {
				Chest chest = (Chest)world.getBlockAt(cx+1, y, z).getState();
				Inventory chestInv = chest.getInventory();
				for(int i=0; i<chestInv.getSize(); i++) {
					if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(mat) && chestInv.getItem(i).getAmount() < 64) {
						chestInv.getItem(i).setAmount(chestInv.getItem(i).getAmount()+1);
						return true;
					}
					if(chestInv.getItem(i) == null || chestInv.getItem(i).getAmount() == 0) {
						chestInv.setItem(i, new ItemStack(mat));
						return true;
					}
				}
			}
			
		}
		
		return false;
	}
	
	public int getXRad() {
		int soFar = 1;
		int current = 1;
		Location currentPos = centreChestLocation.clone().add(2, 0, 0);
		while(world.getBlockAt(currentPos).getType().equals(Material.IRON_BARS)) {
			current++;
			currentPos = currentPos.add(1,0,0);
		}
		if(current > soFar) soFar = current;
		current = 1;
		currentPos = centreChestLocation.clone().add(-2,0,0);
		while(world.getBlockAt(currentPos).getType().equals(Material.IRON_BARS)) {
			current++;
			currentPos = currentPos.add(-1,0,0);
		}
		if(current > soFar) soFar = current;
		return soFar;
	}
	
	public int getZRad() {
		int soFar = 1;
		int current = 1;
		Location currentPos = centreChestLocation.clone().add(0, 0, 2);
		while(world.getBlockAt(currentPos).getType().equals(Material.IRON_BARS)) {
			current++;
			currentPos = currentPos.add(0,0,1);
		}
		if(current > soFar) soFar = current;
		current = 1;
		currentPos = centreChestLocation.clone().add(0,0,-2);
		while(world.getBlockAt(currentPos).getType().equals(Material.IRON_BARS)) {
			current++;
			currentPos = currentPos.add(0,0,-1);
		}
		if(current > soFar) soFar = current;
		return soFar;
	}
	
	public void tellOwner(String msg) {
		for(Player p : world.getPlayers()) {
			if(isOwner(p)) {
				p.sendMessage(msg);
			}
		}
	}
	
	public boolean canInteractAt(Location l, Player p) {
		if(isOwner(p)) {
			return true;
		}
		if(l.getBlockX() >= minX-1 && l.getBlockX() <= maxX+1 && l.getBlockZ() >= minZ-1 && l.getBlockZ() <= maxZ+1) return false;
		return true;
	}
	
	public boolean canBreak(Location l, Player p) {
		if(isOwner(p)) {
			if(l.getBlockX() >= minX-1 && l.getBlockX() <= maxX+1 && l.getBlockZ() >= minZ-1 && l.getBlockZ() <= maxZ+1 && l.getBlockY() == centreChestLocation.getBlockY()-1) return false;
			return true;
		}
		if(l.getBlockX() >= minX-1 && l.getBlockX() <= maxX+1 && l.getBlockZ() >= minZ-1 && l.getBlockZ() <= maxZ+1) return false;
		return true;
	}
	
	public void setBounds() {
		int xRad = getXRad();
		int zRad = getZRad();
		int cx = centreChestLocation.getBlockX();
		int cz = centreChestLocation.getBlockZ();
		
		minX = cx - xRad;
		maxX = cx + xRad;
		minZ = cz - zRad;
		maxZ = cz + zRad;
		
		nextX = minX;
		nextY = centreChestLocation.getBlockY()-2;
		nextZ = minZ;
	}
	
	public void moveMiningCursor() {
		if(nextX == maxX && nextY == 0 && nextZ == maxZ) {
			finished = true;
			return;
		}
		if(nextX == maxX) {
			// Move to next Z if possible
			if(nextZ < maxZ) {
				nextX = minX;
				nextZ++;
			}
			else {
				// Move to next layer
				nextX = minX;
				nextZ = minZ;
				nextY--;
			}
		}
		else {
			// Move one in x direction
			nextX++;
		}
		
	}
	
	public boolean isIgnored(Material mat) {
		for(int i=0; i < ignored.length; i++) {
			if(ignored[i].equals(mat)) return true;
		}
		return false;
	}
	
	public Block findNextBlock() {
		Block currentBlock = world.getBlockAt(nextX, nextY, nextZ);
		if(classicMode) {
			return currentBlock;
		}
		
		// Otherwise using endermining
		while(isIgnored(currentBlock.getType())) {
			moveMiningCursor();
			currentBlock = world.getBlockAt(nextX, nextY, nextZ);
			if(nextX == maxX && nextY == 0 && nextZ == maxZ) break;
		}
		return currentBlock;
	}
	
	public void mineNextBlock() {
		Block blockToMine = findNextBlock();
		
		if(classicMode) {
			// Ignore air, water, lava, or bedrock
			Material thisMaterial = blockToMine.getType();
			while(thisMaterial.equals(Material.AIR )|| thisMaterial.equals(Material.WATER) || thisMaterial.equals(Material.LAVA) || thisMaterial.equals(Material.BEDROCK)) {
				moveMiningCursor();
				blockToMine = findNextBlock();
				thisMaterial = blockToMine.getType();
				if(nextX == maxX && nextY == 0 && nextZ == maxZ) break;
			}
			if(thisMaterial.equals(Material.AIR )|| thisMaterial.equals(Material.WATER) || thisMaterial.equals(Material.LAVA) || thisMaterial.equals(Material.BEDROCK)) {
				if(nextX == maxX && nextY == 0 && nextZ == maxZ && !alerted) {
					alerted = true;
					finished = true;
					tellOwner(ChatColor.BLUE + "Your quarry at " + centreChestLocation.toVector().toString() + " is now finished");
				} 
					
				return;
			}
			/*
			if(blockToMine.getLocation().getBlockY() == centreChestLocation.getBlockY()-1) {
				while(blockToMine.getType().equals(Material.BLACK_STAINED_GLASS) || blockToMine.getType().equals(Material.GREEN_STAINED_GLASS) || blockToMine.getType().equals(Material.CYAN_STAINED_GLASS)) {
					moveMiningCursor();
					blockToMine = findNextBlock();
					if(nextX == maxX && nextY == 0 && nextZ == maxZ)return;
				}
				
			}*/
			
			float energyToUse = thisMaterial.getHardness()*energyMod;
			if(hasFuel(energyToUse)) {
				if(addMined(thisMaterial)) {
					consumeFuel(energyToUse);
					if(thisMaterial.equals(Material.DIRT) || thisMaterial.equals(Material.GRASS) || thisMaterial.equals(Material.GRASS_BLOCK)) world.playSound(blockToMine.getLocation(), Sound.BLOCK_GRASS_BREAK, 1.0f, 1.0f);
					else if(thisMaterial.equals(Material.GRAVEL)) world.playSound(blockToMine.getLocation(), Sound.BLOCK_GRAVEL_BREAK, 1.0f, 1.0f);
					else world.playSound(blockToMine.getLocation(), Sound.BLOCK_STONE_BREAK, 1.0f, 1.0f);
					blockToMine.setType(Material.AIR);
					if(alerted) {
						alerted = false;
						tellOwner(ChatColor.GREEN + "Your quarry at " + centreChestLocation.toVector().toString() + " has resumed working");
					}
				}
				else {
					paused = true;
					if(!alerted) {
						alerted = true;
						tellOwner(ChatColor.RED + "Your quarry at " + centreChestLocation.toVector().toString() + " has no space for new items and is now paused.");
					}
				}
			}
			else {
				paused = true;
				if(!alerted) {
					alerted = true;
					tellOwner(ChatColor.RED + "Your quarry at " + centreChestLocation.toVector().toString() + " has run out of fuel and is now paused.");
				}
			}
			
		}
		
		else {
			// Ender mode
			Material thisMaterial = blockToMine.getType();
			if(thisMaterial.equals(Material.AIR )|| thisMaterial.equals(Material.WATER) || thisMaterial.equals(Material.LAVA) || thisMaterial.equals(Material.BEDROCK)) {
				if(nextX == maxX && nextY == 0 && nextZ == maxZ && !alerted) {
					alerted = true;
					finished = true;
					tellOwner(ChatColor.BLUE + "Your quarry at " + centreChestLocation.toVector().toString() + " is now finished");
				} 
					
				return;
			}
			
			/*
			if(blockToMine.getLocation().getBlockY() == centreChestLocation.getBlockY()-1) {
				while(blockToMine.getType().equals(Material.BLACK_STAINED_GLASS) || blockToMine.getType().equals(Material.GREEN_STAINED_GLASS) || blockToMine.getType().equals(Material.CYAN_STAINED_GLASS)) {
					moveMiningCursor();
					blockToMine = findNextBlock();
					thisMaterial = blockToMine.getType();
					if(nextX == maxX && nextY == 0 && nextZ == maxZ) return;
				}
			}*/
			
			// Otherwise it is mineable and quarry isnt finished
			float energyToUse = thisMaterial.getHardness()*energyMod;
			energyToUse *= 50.0f;
			if(hasFuel(energyToUse)) {
				if(addMined(thisMaterial)) {
					consumeFuel(energyToUse);
					world.playSound(blockToMine.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
					if(enderReplaceDirt && !blockToMine.isPassable())
						blockToMine.setType(Material.DIRT);
					else
						blockToMine.setType(Material.AIR);
					if(alerted) {
						alerted = false;
						tellOwner(ChatColor.GREEN + "Your quarry at " + centreChestLocation.toVector().toString() + " has resumed working");
					}
				}
				else {
					paused = true;
					if(!alerted) {
						alerted = true;
						tellOwner(ChatColor.RED + "Your quarry at " + centreChestLocation.toVector().toString() + " has no space for new items and is now paused.");
					}
				}
			}
			else {
				paused = true;
				if(!alerted) {
					alerted = true;
					tellOwner(ChatColor.RED + "Your quarry at " + centreChestLocation.toVector().toString() + " has run out of fuel and is now paused.");
				}
			}
		}
		
	}
	
	
	
	@Override
	public void run() {
		if(checkCentreChest()) {
			if(!paused && counter%miningDelay == 0) {
				for(int i=0; i<blocksPerTick; i++) {
					mineNextBlock();
				}
			}
		}
		else {
			// This quarry should be removed from the world in some way
			markedForDeletion = true;
		}
		
		if(!platformDone /*&& nextY < centreChestLocation.getBlockY()-1*/) buildPlatform();
		
		
		
		if(counter%10 == 0) calculateUpgrades();
		
		counter++;
		if(counter == Integer.MAX_VALUE) counter = 1;
	}

}

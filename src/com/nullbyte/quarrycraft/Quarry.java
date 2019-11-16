package com.nullbyte.quarrycraft;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
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
	
	int emeraldBlocks = 0;
	int diamondBlocks = 0;
	
	static Material[] ignored = {Material.BEDROCK, Material.AIR, Material.WATER, Material.LAVA, Material.GRASS, Material.GRASS_BLOCK, Material.GRASS_PATH, Material.STONE, Material.COBBLESTONE, Material.DIRT, Material.COARSE_DIRT};

	public Quarry(Chest centreChest) {
		this.centreChest = centreChest;
		this.centreChestLocation = centreChest.getLocation();
		world = centreChest.getWorld();
		storedEnergy = 0;
		setBounds();
	}
	
	public Quarry(Chest centreChest, int minX, int maxX, int minZ, int maxZ, boolean mode) {
		this(centreChest);
		classicMode = mode;
		this.minX = minX;
		this.maxX = maxX;
		this.minZ = minZ;
		this.maxZ = maxZ;
		nextX = minX;
		nextY = centreChestLocation.getBlockY()-1;
		nextZ = minZ;
	}
	
	public void calculateUpgrades() {
		int emeraldBlockCount = 0; // Emeralds blocks increase the mining rate. 4 emerald blocks reduces mining delay by 1 tick
		int diamondBlockCount = 0; // Diamond blocks increase the blocks mined at a time. 4 diamond blocks give 1 more block at a time
		boolean hasNetherStar = false;
		
		int cx = centreChestLocation.getBlockX();
		int cy = centreChestLocation.getBlockY();
		int cz = centreChestLocation.getBlockZ();
		
		
		
		if(world.getBlockAt(cx-1,cy+1,cz-1).getType().equals(Material.CHEST) || world.getBlockAt(cx-1,cy+1,cz-1).getType().equals(Material.TRAPPED_CHEST)) {
			Inventory chestInv = ((Chest)world.getBlockAt(cx-1,cy+1,cz-1).getState()).getInventory();
			for(int i=0; i<chestInv.getSize(); i++) {
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.EMERALD_BLOCK))emeraldBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.DIAMOND_BLOCK))diamondBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.NETHER_STAR))hasNetherStar = true;
			}
		}
		if(world.getBlockAt(cx-1,cy+1,cz+1).getType().equals(Material.CHEST) || world.getBlockAt(cx-1,cy+1,cz+1).getType().equals(Material.TRAPPED_CHEST)) {
			Inventory chestInv = ((Chest)world.getBlockAt(cx-1,cy+1,cz+1).getState()).getInventory();
			for(int i=0; i<chestInv.getSize(); i++) {
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.EMERALD_BLOCK))emeraldBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.DIAMOND_BLOCK))diamondBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.NETHER_STAR))hasNetherStar = true;
			}
		}
		if(world.getBlockAt(cx+1,cy+1,cz-1).getType().equals(Material.CHEST) || world.getBlockAt(cx+1,cy+1,cz-1).getType().equals(Material.TRAPPED_CHEST)) {
			Inventory chestInv = ((Chest)world.getBlockAt(cx+1,cy+1,cz-1).getState()).getInventory();
			for(int i=0; i<chestInv.getSize(); i++) {
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.EMERALD_BLOCK))emeraldBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.DIAMOND_BLOCK))diamondBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.NETHER_STAR))hasNetherStar = true;
			}
		}
		if(world.getBlockAt(cx+1,cy+1,cz+1).getType().equals(Material.CHEST) || world.getBlockAt(cx+1,cy+1,cz+1).getType().equals(Material.TRAPPED_CHEST)) {
			Inventory chestInv = ((Chest)world.getBlockAt(cx+1,cy+1,cz+1).getState()).getInventory();
			for(int i=0; i<chestInv.getSize(); i++) {
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.EMERALD_BLOCK))emeraldBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.DIAMOND_BLOCK))diamondBlockCount += chestInv.getItem(i).getAmount();
				if(chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.NETHER_STAR))hasNetherStar = true;
			}
		}
		
		if(emeraldBlockCount > 76) emeraldBlockCount = 76;
		if(diamondBlockCount > 36) diamondBlockCount = 36;
		
		if(emeraldBlockCount != emeraldBlocks || diamondBlockCount != diamondBlocks || (enderReplaceDirt != hasNetherStar)) {
			emeraldBlocks = emeraldBlockCount;
			diamondBlocks = diamondBlockCount;
			enderReplaceDirt = hasNetherStar;
			
			
			
			if(emeraldBlocks > 76) emeraldBlocks = 76;
			if(diamondBlocks > 36) diamondBlocks = 36;
			
			miningDelay = 20 - ((int)(Math.floor(((float)emeraldBlocks)/4.0)));
			blocksPerTick = 1 + (int)Math.floor(((float)diamondBlocks)/4.0 );
			
			int emeraldsToNext = 4-emeraldBlocks%4;
			int diamondsToNext = 4-diamondBlocks%4;
			
			if(emeraldBlocks == 76) emeraldsToNext = 0;
			if(diamondBlocks == 36) diamondsToNext = 0;
			
			Bukkit.broadcastMessage("Quarry has been modified:\nMining Delay: " + ChatColor.DARK_GREEN + miningDelay + ChatColor.WHITE + "\nEmerald blocks to next upgrade: " + ChatColor.GREEN + emeraldsToNext + ChatColor.WHITE + "\nBlocks mined at a time: " + ChatColor.DARK_BLUE + blocksPerTick + ChatColor.WHITE + "\nDiamond blocks to next upgrade: " + ChatColor.AQUA + diamondsToNext + ChatColor.WHITE + "\nEnder mining replaces blocks with dirt: " + ChatColor.GOLD + enderReplaceDirt);
		}
		
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
		classicMode = !classicMode;
		if(classicMode) return "Mining mode toggled: "+ ChatColor.GREEN + "Classic";
		return "Mining mode toggled: "+ ChatColor.BLUE + "Ender";
	}
	
	public boolean hasFuel(float amount) {
		Inventory centreInv = centreChest.getInventory();
		float energySoFar = storedEnergy;
		if(energySoFar >= amount) return true;
		QuarryFuel currentFuel;
		for(int i=0; i<centreInv.getSize(); i++) {
			ItemStack currentItem = centreInv.getItem(i);
			if(currentItem == null) continue;
			currentFuel = QuarryFuel.getFuel(currentItem.getType());
			if(currentFuel != null) energySoFar += currentFuel.getEnergyValue()*centreInv.getItem(i).getAmount();
			if(energySoFar >= amount) return true;
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
	
	public boolean addMined(Material mat) {
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
		nextY = centreChestLocation.getBlockY()-1;
		nextZ = minZ;
	}
	
	public void moveMiningCursor() {
		if(nextX == maxX && nextY == 0 && nextZ == maxZ) return;
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
					Bukkit.broadcastMessage(ChatColor.BLUE + "Quarry at " + centreChestLocation.toVector().toString() + " is now finished");
				} 
					
				return;
			}
			
			float energyToUse = thisMaterial.getHardness();
			if(hasFuel(energyToUse)) {
				if(addMined(thisMaterial)) {
					consumeFuel(energyToUse);
					if(thisMaterial.equals(Material.DIRT) || thisMaterial.equals(Material.GRASS) || thisMaterial.equals(Material.GRASS_BLOCK)) world.playSound(blockToMine.getLocation(), Sound.BLOCK_GRASS_BREAK, 1.0f, 1.0f);
					else if(thisMaterial.equals(Material.GRAVEL)) world.playSound(blockToMine.getLocation(), Sound.BLOCK_GRAVEL_BREAK, 1.0f, 1.0f);
					else world.playSound(blockToMine.getLocation(), Sound.BLOCK_STONE_BREAK, 1.0f, 1.0f);
					blockToMine.setType(Material.AIR);
					if(alerted) {
						alerted = false;
						Bukkit.broadcastMessage(ChatColor.GREEN + "Quarry at " + centreChestLocation.toVector().toString() + " has resumed working");
					}
				}
				else {
					if(!alerted) {
						alerted = true;
						Bukkit.broadcastMessage(ChatColor.RED + "Quarry at " + centreChestLocation.toVector().toString() + " has no space for new items and is now paused.");
					}
				}
			}
			else {
				if(!alerted) {
					alerted = true;
					Bukkit.broadcastMessage(ChatColor.RED + "Quarry at " + centreChestLocation.toVector().toString() + " has run out of fuel and is now paused.");
				}
			}
			
		}
		
		else {
			// Ender mode
			Material thisMaterial = blockToMine.getType();
			if(thisMaterial.equals(Material.AIR )|| thisMaterial.equals(Material.WATER) || thisMaterial.equals(Material.LAVA) || thisMaterial.equals(Material.BEDROCK)) {
				if(nextX == maxX && nextY == 0 && nextZ == maxZ && !alerted) {
					alerted = true;
					Bukkit.broadcastMessage(ChatColor.BLUE + "Quarry at " + centreChestLocation.toVector().toString() + " is now finished");
				} 
					
				return;
			}
			
			// Otherwise it is mineable and quarry isnt finished
			float energyToUse = thisMaterial.getHardness();
			energyToUse *= 10;
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
						Bukkit.broadcastMessage(ChatColor.GREEN + "Quarry at " + centreChestLocation.toVector().toString() + " has resumed working");
					}
				}
				else {
					if(!alerted) {
						alerted = true;
						Bukkit.broadcastMessage(ChatColor.RED + "Quarry at " + centreChestLocation.toVector().toString() + " has no space for new items and is now paused.");
					}
				}
			}
			else {
				if(!alerted) {
					alerted = true;
					Bukkit.broadcastMessage(ChatColor.RED + "Quarry at " + centreChestLocation.toVector().toString() + " has run out of fuel and is now paused.");
				}
			}
		}
		
	}
	
	
	
	@Override
	public void run() {
		if(checkCentreChest()) {
			if(counter%miningDelay == 0) {
				for(int i=0; i<blocksPerTick; i++) {
					mineNextBlock();
				}
			}
		}
		
		else {
			// This quarry should be removed from the world in some way
			markedForDeletion = true;
		}
		
		if(counter%10 == 0) calculateUpgrades();
		
		counter++;
		if(counter == Integer.MAX_VALUE) counter = 1;
	}

}

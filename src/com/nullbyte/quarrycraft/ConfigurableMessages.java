package com.nullbyte.quarrycraft;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.bukkit.ChatColor;

public class ConfigurableMessages {
	
	private String reloading1;
	private String reloading2;
	private String pleaseWaitBeforeNumSeconds;
	private String pleaseWaitAfterNumSeconds;
	private String dontHaveInteractPermission;
	private String blockCannotBeBroken;
	private String quarryCreated;
	private String noBuildPerm;
	private String quarryLimBeforeLimit;
	private String quarryLimAfterLimit;
	private String quarryIntersectError;
	private String miningCursorReset;
	private String quarryDestroyedBeforeCoords;
	private String quarryDestroyedAfterCoords;
	private String quarryOversizedBeforeArea;
	private String quarryOversizedAfterArea;
	private String quarryPausedBeforeCoords;
	private String quarryPausedAfterCoords;
	private String quarryUnpausedBeforeCoords;
	private String quarryUnpausedAfterCoords;
	private String quarryModified;
	private String miningDelay;
	private String emeraldBlocksToUpgrade;
	private String blocksMinedAtATime;
	private String diamondBlocksToUpgrade;
	private String efficiency;
	private String enderReplaceDirt;
	private String quarryFinishedStatus;
	private String quarryMiningStatus;
	private String quarryPausedStatus;
	private String miningModeToggled;
	private String classic;
	private String ender;
	private String resumedBeforeCoords;
	private String resumedAfterCoords;
	private String noSpaceBeforeCoords;
	private String noSpaceAfterCoords;
	private String noFuelBeforeCoords;
	private String noFuelAfterCoords;
	private String finishedBeforeCoords;
	private String finishedAfterCoords;
	private String playerJoin1;
	private String playerJoin2;
	private String playerJoin3;
	
	public ConfigurableMessages() {
		reloading1 = ChatColor.GREEN + "[QuarryCraft]" + ChatColor.WHITE + " Reloading QuarryCraft config...";
		reloading2 = ChatColor.GREEN + "[QuarryCraft]" + ChatColor.WHITE + " Config reloaded";
		pleaseWaitBeforeNumSeconds = ChatColor.RED + "Please wait";
		pleaseWaitAfterNumSeconds = "seconds before using that command again!";
		dontHaveInteractPermission = ChatColor.DARK_RED + "You do not have permission to interact here.";
		blockCannotBeBroken = ChatColor.DARK_RED + "Sorry, this block may not be broken!";
		quarryCreated = ChatColor.GREEN+ "You have created a new quarry.";
		noBuildPerm = ChatColor.RED + "You do not have permission to build quarries.\nPlease ask an OP for permission.";
		quarryLimBeforeLimit = ChatColor.DARK_RED + "You have reached your quarry limit(";
		quarryLimAfterLimit = "). Please destroy old quarries or ask the server owner to change the limit in the QuarryCraft config.";
		quarryIntersectError = ChatColor.DARK_RED + "Quarries may not intersect!";
		miningCursorReset = "Mining cursor reset to";
		quarryDestroyedBeforeCoords = ChatColor.DARK_RED + "Quarry at ";
		quarryDestroyedAfterCoords = " destroyed";
		quarryOversizedBeforeArea = ChatColor.DARK_RED + "Your quarry is over the quarry area limit and is being restricted to a ";
		quarryOversizedAfterArea = " area.";
		quarryPausedBeforeCoords = ChatColor.YELLOW + "Your quarry at ";
		quarryPausedAfterCoords = " has been paused.";
		quarryUnpausedBeforeCoords = ChatColor.GREEN + "Your quarry at ";
		quarryUnpausedAfterCoords = " is no longer paused.";
		quarryModified = "Your quarry has been modified:";
		miningDelay = "Mining Delay";
		emeraldBlocksToUpgrade = "Emerald blocks to next upgrade";
		blocksMinedAtATime = "Blocks mined at a time";
		diamondBlocksToUpgrade = "Diamond blocks to next upgrade";
		efficiency = "Efficiency";
		enderReplaceDirt = "Ender mining replaces blocks with dirt";
		quarryFinishedStatus = "The quarry is finished at";
		quarryMiningStatus = "The quarry is mining at";
		quarryPausedStatus = "The quarry is paused at";
		miningModeToggled = "Mining mode toggled";
		classic = "Classic";
		ender = "Ender";
		resumedBeforeCoords = ChatColor.GREEN + "Your quarry at ";
		resumedAfterCoords = " has resumed working";
		noSpaceBeforeCoords = ChatColor.RED + "Your quarry at ";
		noSpaceAfterCoords = " has no space for new items and is now paused.";
		noFuelBeforeCoords = ChatColor.RED + "Your quarry at ";
		noFuelAfterCoords = " has run out of fuel and is now paused.";
		finishedBeforeCoords = ChatColor.BLUE + "Your quarry at ";
		finishedAfterCoords = " is now finished";
		playerJoin1 = ChatColor.GREEN + "[QuarryCraft] " +ChatColor.WHITE + "Welcome ";
		playerJoin2 = ChatColor.GREEN + "[QuarryCraft] " +ChatColor.WHITE + "This server has QuarryCraft installed.";
		playerJoin3 = ChatColor.GREEN + "[QuarryCraft] " +ChatColor.WHITE + "Type " + ChatColor.BLUE + "/quarrycraft guide" + ChatColor.WHITE + " to get started!";
		
		//loadMessages();
	}
	
	
	
	public String reloading1(){
    return reloading1;
  }
  public String reloading2(){
    return reloading2;
  }
  public String pleaseWaitBeforeNumSeconds(){
    return pleaseWaitBeforeNumSeconds;
  }
  public String pleaseWaitAfterNumSeconds(){
    return pleaseWaitAfterNumSeconds;
  }
  public String dontHaveInteractPermission(){
    return dontHaveInteractPermission;
  }
  public String blockCannotBeBroken(){
    return blockCannotBeBroken;
  }
  public String quarryCreated(){
    return quarryCreated;
  }
  public String noBuildPerm(){
    return noBuildPerm;
  }
  public String quarryLimBeforeLimit(){
    return quarryLimBeforeLimit;
  }
  public String quarryLimAfterLimit(){
    return quarryLimAfterLimit;
  }
  public String quarryIntersectError(){
    return quarryIntersectError;
  }
  public String miningCursorReset(){
    return miningCursorReset;
  }
  public String quarryDestroyedBeforeCoords(){
    return quarryDestroyedBeforeCoords;
  }
  public String quarryDestroyedAfterCoords(){
    return quarryDestroyedAfterCoords;
  }
  public String quarryOversizedBeforeArea(){
    return quarryOversizedBeforeArea;
  }
  public String quarryOversizedAfterArea(){
    return quarryOversizedAfterArea;
  }
  public String quarryPausedBeforeCoords(){
    return quarryPausedBeforeCoords;
  }
  public String quarryPausedAfterCoords(){
    return quarryPausedAfterCoords;
  }
  public String quarryUnpausedBeforeCoords(){
    return quarryUnpausedBeforeCoords;
  }
  public String quarryUnpausedAfterCoords(){
    return quarryUnpausedAfterCoords;
  }
  public String quarryModified(){
    return quarryModified;
  }
  public String miningDelay(){
    return miningDelay;
  }
  public String emeraldBlocksToUpgrade(){
    return emeraldBlocksToUpgrade;
  }
  public String blocksMinedAtATime(){
    return blocksMinedAtATime;
  }
  public String diamondBlocksToUpgrade(){
    return diamondBlocksToUpgrade;
  }
  public String efficiency(){
    return efficiency;
  }
  public String enderReplaceDirt(){
    return enderReplaceDirt;
  }
  public String quarryFinishedStatus(){
    return quarryFinishedStatus;
  }
  public String quarryMiningStatus(){
    return quarryMiningStatus;
  }
  public String quarryPausedStatus(){
    return quarryPausedStatus;
  }
  public String miningModeToggled(){
    return miningModeToggled;
  }
  public String classic(){
    return classic;
  }
  public String ender(){
    return ender;
  }
  public String resumedBeforeCoords(){
    return resumedBeforeCoords;
  }
  public String resumedAfterCoords(){
    return resumedAfterCoords;
  }
  public String noSpaceBeforeCoords(){
    return noSpaceBeforeCoords;
  }
  public String noSpaceAfterCoords(){
    return noSpaceAfterCoords;
  }
  public String noFuelBeforeCoords(){
    return noFuelBeforeCoords;
  }
  public String noFuelAfterCoords(){
    return noFuelAfterCoords;
  }
  public String finishedBeforeCoords(){
    return finishedBeforeCoords;
  }
  public String finishedAfterCoords(){
    return finishedAfterCoords;
  }
  public String playerJoin1(){
    return playerJoin1;
  }
  public String playerJoin2(){
    return playerJoin2;
  }
  public String playerJoin3(){
    return playerJoin3;
  }
	
	public void loadMessages() {
		String fileSeparator = System.getProperty("file.separator");
		String path = "plugins" + fileSeparator + "QuarryCraft" + fileSeparator + "messages.conf";
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
				var = splitString[0].trim();
				val = splitString[1].trim();
				
				System.out.println(ChatColor.GREEN + "Loading configurable message" + var + " = " + val);
				
				switch(var) {
				case "reloading1":
				  reloading1 = val;
				  break;
				case "reloading2":
				  reloading2 = val;
				  break;
				case "pleaseWaitBeforeNumSeconds":
				  pleaseWaitBeforeNumSeconds = val;
				  break;
				case "pleaseWaitAfterNumSeconds":
				  pleaseWaitAfterNumSeconds = val;
				  break;
				case "dontHaveInteractPermission":
				  dontHaveInteractPermission = val;
				  break;
				case "blockCannotBeBroken":
				  blockCannotBeBroken = val;
				  break;
				case "quarryCreated":
				  quarryCreated = val;
				  break;
				case "noBuildPerm":
				  noBuildPerm = val;
				  break;
				case "quarryLimBeforeLimit":
				  quarryLimBeforeLimit = val;
				  break;
				case "quarryLimAfterLimit":
				  quarryLimAfterLimit = val;
				  break;
				case "quarryIntersectError":
				  quarryIntersectError = val;
				  break;
				case "miningCursorReset":
				  miningCursorReset = val;
				  break;
				case "quarryDestroyedBeforeCoords":
				  quarryDestroyedBeforeCoords = val;
				  break;
				case "quarryDestroyedAfterCoords":
				  quarryDestroyedAfterCoords = val;
				  break;
				case "quarryOversizedBeforeArea":
				  quarryOversizedBeforeArea = val;
				  break;
				case "quarryOversizedAfterArea":
				  quarryOversizedAfterArea = val;
				  break;
				case "quarryPausedBeforeCoords":
				  quarryPausedBeforeCoords = val;
				  break;
				case "quarryPausedAfterCoords":
				  quarryPausedAfterCoords = val;
				  break;
				case "quarryUnpausedBeforeCoords":
				  quarryUnpausedBeforeCoords = val;
				  break;
				case "quarryUnpausedAfterCoords":
				  quarryUnpausedAfterCoords = val;
				  break;
				case "quarryModified":
				  quarryModified = val;
				  break;
				case "miningDelay":
				  miningDelay = val;
				  break;
				case "emeraldBlocksToUpgrade":
				  emeraldBlocksToUpgrade = val;
				  break;
				case "blocksMinedAtATime":
				  blocksMinedAtATime = val;
				  break;
				case "diamondBlocksToUpgrade":
				  diamondBlocksToUpgrade = val;
				  break;
				case "efficiency":
				  efficiency = val;
				  break;
				case "enderReplaceDirt":
				  enderReplaceDirt = val;
				  break;
				case "quarryFinishedStatus":
				  quarryFinishedStatus = val;
				  break;
				case "quarryMiningStatus":
				  quarryMiningStatus = val;
				  break;
				case "quarryPausedStatus":
				  quarryPausedStatus = val;
				  break;
				case "miningModeToggled":
				  miningModeToggled = val;
				  break;
				case "classic":
				  classic = val;
				  break;
				case "ender":
				  ender = val;
				  break;
				case "resumedBeforeCoords":
				  resumedBeforeCoords = val;
				  break;
				case "resumedAfterCoords":
				  resumedAfterCoords = val;
				  break;
				case "noSpaceBeforeCoords":
				  noSpaceBeforeCoords = val;
				  break;
				case "noSpaceAfterCoords":
				  noSpaceAfterCoords = val;
				  break;
				case "noFuelBeforeCoords":
				  noFuelBeforeCoords = val;
				  break;
				case "noFuelAfterCoords":
				  noFuelAfterCoords = val;
				  break;
				case "finishedBeforeCoords":
				  finishedBeforeCoords = val;
				  break;
				case "finishedAfterCoords":
				  finishedAfterCoords = val;
				  break;
				case "playerJoin1":
				  playerJoin1 = val;
				  break;
				case "playerJoin2":
				  playerJoin2 = val;
				  break;
				case "playerJoin3":
				  playerJoin3 = val;
				  break;
				default:
			      System.out.println(var + " unrecognised! Ignoring"); 
			      break;
				}
				
			} while(currentString != null);
			inFile.close();
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println(ChatColor.BLUE + "Generating QuarryCraft messages file");
			String fileString = "";
			fileString += "reloading1 = " + reloading1 + "\n";
			fileString += "reloading2 = " + reloading2 + "\n";
			fileString += "pleaseWaitBeforeNumSeconds = " + pleaseWaitBeforeNumSeconds + "\n";
			fileString += "pleaseWaitAfterNumSeconds = " + pleaseWaitAfterNumSeconds + "\n";
			fileString += "dontHaveInteractPermission = " + dontHaveInteractPermission + "\n";
			fileString += "blockCannotBeBroken = " + blockCannotBeBroken + "\n";
			fileString += "quarryCreated = " + quarryCreated + "\n";
			fileString += "noBuildPerm = " + noBuildPerm + "\n";
			fileString += "quarryLimBeforeLimit = " + quarryLimBeforeLimit + "\n";
			fileString += "quarryLimAfterLimit = " + quarryLimAfterLimit + "\n";
			fileString += "quarryIntersectError = " + quarryIntersectError + "\n";
			fileString += "miningCursorReset = " + miningCursorReset + "\n";
			fileString += "quarryDestroyedBeforeCoords = " + quarryDestroyedBeforeCoords + "\n";
			fileString += "quarryDestroyedAfterCoords = " + quarryDestroyedAfterCoords + "\n";
			fileString += "quarryOversizedBeforeArea = " + quarryOversizedBeforeArea + "\n";
			fileString += "quarryOversizedAfterArea = " + quarryOversizedAfterArea + "\n";
			fileString += "quarryPausedBeforeCoords = " + quarryPausedBeforeCoords + "\n";
			fileString += "quarryPausedAfterCoords = " + quarryPausedAfterCoords + "\n";
			fileString += "quarryUnpausedBeforeCoords = " + quarryUnpausedBeforeCoords + "\n";
			fileString += "quarryUnpausedAfterCoords = " + quarryUnpausedAfterCoords + "\n";
			fileString += "quarryModified = " + quarryModified + "\n";
			fileString += "miningDelay = " + miningDelay + "\n";
			fileString += "emeraldBlocksToUpgrade = " + emeraldBlocksToUpgrade + "\n";
			fileString += "blocksMinedAtATime = " + blocksMinedAtATime + "\n";
			fileString += "diamondBlocksToUpgrade = " + diamondBlocksToUpgrade + "\n";
			fileString += "efficiency = " + efficiency + "\n";
			fileString += "enderReplaceDirt = " + enderReplaceDirt + "\n";
			fileString += "quarryFinishedStatus = " + quarryFinishedStatus + "\n";
			fileString += "quarryMiningStatus = " + quarryMiningStatus + "\n";
			fileString += "quarryPausedStatus = " + quarryPausedStatus + "\n";
			fileString += "miningModeToggled = " + miningModeToggled + "\n";
			fileString += "classic = " + classic + "\n";
			fileString += "ender = " + ender + "\n";
			fileString += "resumedBeforeCoords = " + resumedBeforeCoords + "\n";
			fileString += "resumedAfterCoords = " + resumedAfterCoords + "\n";
			fileString += "noSpaceBeforeCoords = " + noSpaceBeforeCoords + "\n";
			fileString += "noSpaceAfterCoords = " + noSpaceAfterCoords + "\n";
			fileString += "noFuelBeforeCoords = " + noFuelBeforeCoords + "\n";
			fileString += "noFuelAfterCoords = " + noFuelAfterCoords + "\n";
			fileString += "finishedBeforeCoords = " + finishedBeforeCoords + "\n";
			fileString += "finishedAfterCoords = " + finishedAfterCoords + "\n";
			fileString += "playerJoin1 = " + playerJoin1 + "\n";
			fileString += "playerJoin2 = " + playerJoin2 + "\n";
			fileString += "playerJoin3 = " + playerJoin3 + "\n";
			
			
			
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

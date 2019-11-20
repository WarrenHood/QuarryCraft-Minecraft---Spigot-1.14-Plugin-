messages = ["reloading1",
			"reloading2",
			"pleaseWaitBeforeNumSeconds",
			"pleaseWaitAfterNumSeconds",
			"dontHaveInteractPermission",
			"blockCannotBeBroken",
			"quarryCreated",
			"noBuildPerm",
			"quarryLimBeforeLimit",
			"quarryLimAfterLimit",
			"quarryIntersectError",
			"miningCursorReset",
			"quarryDestroyedBeforeCoords",
			"quarryDestroyedAfterCoords",
			"quarryOversizedBeforeArea",
			"quarryOversizedAfterArea",
			"quarryPausedBeforeCoords",
			"quarryPausedAfterCoords",
			"quarryUnpausedBeforeCoords",
			"quarryUnpausedAfterCoords",
			"quarryModified",
			"miningDelay",
			"emeraldBlocksToUpgrade",
			"blocksMinedAtATime",
			"diamondBlocksToUpgrade",
			"efficiency",
			"enderReplaceDirt",
			"quarryFinishedStatus",
			"quarryMiningStatus",
			"quarryPausedStatus",
			"miningModeToggled",
			"classic",
			"ender",
			"resumedBeforeCoords",
			"resumedAfterCoords",
			"noSpaceBeforeCoords",
			"noSpaceAfterCoords",
			"noFuelBeforeCoords",
			"noFuelAfterCoords",
			"finishedBeforeCoords",
			"finishedAfterCoords",
			"playerJoin1",
			"playerJoin2",
			"playerJoin3",

]

def javacode(s):
	print("case \"" + s + "\":")
	print("    " + s + " = val;")
	print("    break;")

def savecode(s):
	print("fileString += \"" + s + " = \" + " + s + " + \"\\n\";" )

for msg in messages:
	javacode(msg)

print("\n\n\n\n")

for msg in messages:
	savecode(msg)

def mkFunc(s):
	print("    public static String " + s + "(){")
	print("        return " + s + ";")
	print("    }")

for msg in messages:
	mkFunc(msg)

for msg in messages:
	print("private String " + msg + ";" )
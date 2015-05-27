package chanceCubes.config;

public class CCubesSettings 
{
	public static int pendantUses = 100;
	
	public static boolean oreGeneration = true;
	public static boolean surfaceGeneration = true;
	public static String[] blockedWorlds = new String[]{};
	
	public static boolean isBlockedWorld(String world)
	{
		for(String blockedWorld: blockedWorlds)
			if(blockedWorld.equalsIgnoreCase(world))
				return true;
		return false;
	}
}

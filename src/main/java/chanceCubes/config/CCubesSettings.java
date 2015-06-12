package chanceCubes.config;

public class CCubesSettings 
{
	public static int pendantUses = 100;
	
	public static int rangeMin = 75;
	public static int rangeMax = 75;
	
	public static boolean oreGeneration = true;
	public static boolean surfaceGeneration = true;
	public static String[] blockedWorlds = new String[]{};
	
	public static int dropHeight = 20;
	
	
	public static boolean isBlockedWorld(String world)
	{
		for(String blockedWorld: blockedWorlds)
			if(blockedWorld.equalsIgnoreCase(world))
				return true;
		return false;
	}
}

package chanceCubes.rewards.profiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import chanceCubes.rewards.profiles.modules.DiffucultyTrigger;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.common.config.Configuration;

public class ProfileManager
{
	private static List<IProfile> enabledProfiles = new ArrayList<IProfile>();
	private static List<IProfile> disabledProfiles = new ArrayList<IProfile>();

	private static Configuration config;
	public static final String genCat = "Profile Status";

	private static void registerProfile(IProfile profile)
	{
		registerProfile(profile, false);
	}

	private static void registerProfile(IProfile profile, boolean enabled)
	{
		enabled = config.getBoolean(profile.getName(), genCat, enabled, profile.getDesc());
		if(enabled)
		{
			enabledProfiles.add(profile);
			profile.onEnable();
		}
		else
		{
			disabledProfiles.add(profile);
			profile.onDisable();
		}
	}

	public static void enableProfile(IProfile profile)
	{
		if(disabledProfiles.remove(profile))
		{
			enabledProfiles.add(profile);
			profile.onEnable();
			config.load();
			config.get(genCat, profile.getName(), "").setValue(true);
			config.save();
		}
	}

	public static void disableProfile(IProfile profile)
	{
		if(enabledProfiles.remove(profile))
		{
			disabledProfiles.add(profile);
			profile.onDisable();
			config.load();
			config.get(genCat, profile.getName(), "").setValue(false);
			config.save();
		}
	}

	public static void clearProfiles()
	{
		enabledProfiles.clear();
		disabledProfiles.clear();
	}

	public static List<IProfile> getAllProfiles()
	{
		List<IProfile> toReturn = new ArrayList<>();
		toReturn.addAll(enabledProfiles);
		toReturn.addAll(disabledProfiles);
		return toReturn;
	}

	public static List<String> getEnabledProfileNames()
	{
		List<String> toReturn = new ArrayList<>();
		for(IProfile prof : enabledProfiles)
			toReturn.add(prof.getName());
		return toReturn;
	}

	public static List<String> getDisabledProfileNames()
	{
		List<String> toReturn = new ArrayList<>();
		for(IProfile prof : disabledProfiles)
			toReturn.add(prof.getName());
		return toReturn;
	}

	public static List<String> getAllProfileNames(boolean sorted)
	{
		List<String> toReturn = new ArrayList<>();
		toReturn.addAll(getEnabledProfileNames());
		toReturn.addAll(getDisabledProfileNames());
		if(sorted)
			Collections.sort(toReturn);
		return toReturn;
	}

	public static IProfile getProfilefromName(String name)
	{
		for(IProfile prof : enabledProfiles)
			if(prof.getName().equals(name))
				return prof;
		for(IProfile prof : disabledProfiles)
			if(prof.getName().equals(name))
				return prof;
		return null;
	}

	public static boolean isProfileEnabled(IProfile prof)
	{
		return enabledProfiles.contains(prof);
	}

	public static void initProfiles()
	{
		BasicProfile profile;

		//@formatter:off
		config.load();
		profile = new BasicProfile("Default", "Rewards that are disabled by default");
		profile.setDisabledRewards("chancecubes:Clear_Inventory");
		registerProfile(profile, true);

		profile = new BasicProfile("No Explosions", "Disable all rewards that use explode");
		profile.setDisabledRewards("chancecubes:Tnt_Structure", "chancecubes:Explosion", "chancecubes:TNT_Cat", 
				"chancecubes:TNT_Diamond", "chancecubes:TNT_Bats", "chancecubes:Coal_To_Diamonds", "chancecubes:Help_Me", 
				"chancecubes:Nuke", "chancecubes:Pssst", "chancecubes:Surrounded_Creeper", "chancecubes:Troll_TNT", 
				"chancecubes:Ender_Crystal_Timer", "chancecubes:Wait_For_It", "chancecubes:Charged_Creeper", 
				"chancecubes:Torches_To_Creepers", "chancecubes:Cake", "chancecubes:Wolves_To_Creepers", 
				"chancecubes:Monty_Hall", "chancecubes:Countdown", "chancecubes:Heads_or_Tails");
		registerProfile(profile);
		
		profile = new BasicProfile("No Death Mini-games", "Disable all minigame rewards that kills the player if they lose");
		BasicProfile noMiniGames = profile;
		profile.setDisabledRewards("chancecubes:Maze", "chancecubes:Dig_Build_Reward", "chancecubes:Matching", 
				"chancecubes:Math", "chancecubes:Question");
		registerProfile(profile);
		
		profile = new BasicProfile("No Potions/Effects", "Disable all rewards that throw a potion or give a status effect");
		profile.setDisabledRewards("chancecubes:Poison", "chancecubes:Wither_Status_Effect", "chancecubes:Arrow_Trap", 
				"chancecubes:Random_Status_Effect", "chancecubes:Lingering_Potions_Ring", "chancecubes:Surrounded_Creeper", 
				"chancecubes:Mob_Abilities_Effects");
		registerProfile(profile);
		
		//TODO: Finish this one up
		profile = new BasicProfile("Hardcore", "For users who play on hardcore diffuculty");
		profile.setDisabledRewards("chancecubes:Heads_or_Tails", "chancecubes:Monty_Hall");
		profile.addSubProfile(noMiniGames);
		profile.addTriggers(new DiffucultyTrigger(profile, EnumDifficulty.HARD));
		registerProfile(profile);
		config.save();
		//@formatter:on
	}

	public static void setupConfig(Configuration configuration)
	{
		config = configuration;
	}
}

package chanceCubes.profiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chanceCubes.profiles.triggers.DifficultyTrigger;
import chanceCubes.profiles.triggers.DimensionChangeTrigger;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.IChanceCubeReward;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.common.config.Configuration;

public class ProfileManager
{
	private static List<IProfile> enabledProfiles = new ArrayList<IProfile>();
	private static List<IProfile> disabledProfiles = new ArrayList<IProfile>();

	private static Map<String, List<Integer>> chanceChangesCache = new HashMap<>();

	private static Configuration config;
	public static final String genCat = "Profile Status";

	public static void registerProfile(IProfile profile)
	{
		registerProfile(profile, false);
	}

	public static void registerProfile(IProfile profile, boolean enabled)
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

	public static IProfile getProfileFromID(String id)
	{
		for(IProfile prof : getAllProfiles())
			if(prof.getID().equals(id))
				return prof;
		return null;
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

	public static void setRewardChanceValue(String rewardName, int chance)
	{
		IChanceCubeReward reward = ChanceCubeRegistry.INSTANCE.getRewardByName(rewardName);
		if(reward != null)
		{
			List<Integer> cache = chanceChangesCache.get(rewardName);
			if(cache == null)
			{
				cache = new ArrayList<Integer>();
				cache.add(reward.getChanceValue());
				chanceChangesCache.put(rewardName, cache);
			}
			cache.add(chance);
			reward.setChanceValue(chance);
		}
	}

	public static void resetRewardChanceValue(String rewardName, int chanceFrom)
	{
		IChanceCubeReward reward = ChanceCubeRegistry.INSTANCE.getRewardByName(rewardName);
		if(reward != null)
		{
			List<Integer> cache = chanceChangesCache.get(rewardName);
			if(cache == null)
			{
				// Something dun messed up
				return;
			}
			cache.remove((Integer) chanceFrom);
			if(cache.size() == 0)
			{
				// Again, something dun messed up
				return;
			}
			reward.setChanceValue(cache.get(cache.size() - 1));
		}
	}

	public static Map<String, Object> getRewardSpawnSettings(IChanceCubeReward reward)
	{
		Map<String, Object> settings = new HashMap<String, Object>();

		for(IProfile prof : enabledProfiles)
		{
			Map<String, Object> rewardSettings = prof.getRewardSettings().get(reward.getName());
			if(rewardSettings != null && !rewardSettings.isEmpty())
				settings.putAll(prof.getRewardSettings().get(reward.getName()));
		}

		return settings;
	}

	public static void initProfiles()
	{
		BasicProfile profile;

		//@formatter:off
		config.load();
		profile = new BasicProfile("default", "Default", "Rewards that are disabled by default");
		profile.addDisabledRewards("chancecubes:clear_inventory");
		registerProfile(profile, true);

		profile = new BasicProfile("no_explosions", "No Explosions", "Disable all rewards that use explode");
		profile.addDisabledRewards("chancecubes:tnt_structure", "chancecubes:explosion", "chancecubes:tnt_cat",
				"chancecubes:tnt_diamond", "chancecubes:tnt_bats", "chancecubes:coal_to_diamonds", "chancecubes:help_me", 
				"chancecubes:nuke", "chancecubes:pssst", "chancecubes:surrounded_creeper", "chancecubes:troll_tnt", 
				"chancecubes:ender_crystal_timer", "chancecubes:wait_for_it", "chancecubes:charged_creeper", 
				"chancecubes:torches_to_creepers", "chancecubes:cake", "chancecubes:wolves_to_creepers", 
				"chancecubes:monty_hall", "chancecubes:countdown", "chancecubes:heads_or_tails");
		registerProfile(profile);
		
		profile = new BasicProfile("no_death_mg", "No Death Mini-games", "Disable all minigame rewards that kills the player if they lose");
		profile.addDisabledRewards("chancecubes:maze", "chancecubes:dig_build_reward", "chancecubes:matching", 
				"chancecubes:math", "chancecubes:question");
		registerProfile(profile);
		
		profile = new BasicProfile("no_status_effects", "No Potions/Effects", "Disable all rewards that throw a potion or give a status effect");
		profile.addDisabledRewards("chancecubes:poison", "chancecubes:wither_status_effect", "chancecubes:arrow_trap", 
				"chancecubes:random_status_effect", "chancecubes:lingering_potions_ring", "chancecubes:surrounded_creeper", 
				"chancecubes:mob_abilities_effects");
		registerProfile(profile);
		
		profile = new BasicProfile("hardcore", "Hardcore", "For users who play on hardcore diffuculty");
		profile.addDisabledRewards("chancecubes:heads_or_tails", "chancecubes:monty_hall", "chancecubes:ender_crystal_timer",
				"chancecubes:wither");
		profile.addSubProfile(getProfileFromID("no_death_mg"));
		profile.addRewardChanceChange("chancecubes:half_heart", -100);
		profile.addRewardChanceChange("chancecubes:cave_spider_web", -90);
		profile.addTriggers(new DifficultyTrigger(profile, EnumDifficulty.HARD));
		registerProfile(profile);
		
		profile = new BasicProfile("nether", "Nether", "Updates the reward pool for when players are in the nether");
		profile.addDisabledRewards("chancecubes:rain", "chancecubes:sail_away", "chancecubes:squid_horde", 
				"chancecubes:ice_cold", "chancecubes:hot_tub", "chancecubes:guardians", "chancecubes:nuke",
				"chancecubes:cats_and_dogs");
		profile.addTriggers(new DimensionChangeTrigger(profile, -1));
		registerProfile(profile);
		
		profile = new BasicProfile("peaceful", "Peaceful", "For users who play on peaceful diffuculty. Removes rewards that have hostile Mobs (Doesn't remove TNT)");
		profile.addDisabledRewards("chancecubes:pssst", "chancecubes:horde", "chancecubes:silverfish_surround",
				"chancecubes:slime_man", "chancecubes:witch", "chancecubes:spawn_jerry", "chancecubes:spawn_glenn", 
				"chancecubes:invisible_creeper", "chancecubes:knockback_zombie", "chancecubes:actual_invisible_ghast",
				"chancecubes:nether_jelly_fish", "chancecubes:quidditch", "chancecubes:one_man_army", "chancecubes:silvermite_stacks",
				"chancecubes:invizible_silverfish", "chancecubes:skeleton_bats", "chancecubes:cave_spider_web", "chancecubes:guardians", 
				"chancecubes:cookie_monster", "chancecubes:charged_creeper", "chancecubes:torches_to_creepers", "chancecubes:herobrine", 
				"chancecubes:surrounded", "chancecubes:surrounded_creeper", "chancecubes:wither", "chancecubes:wait_for_it", 
				"chancecubes:cake", "chancecubes:wolves_to_creepers", "chancecubes:countdown", "chancecubes:mob_tower");
		profile.addTriggers(new DifficultyTrigger(profile, EnumDifficulty.PEACEFUL));
		registerProfile(profile);
		
		profile = new BasicProfile("no_area_of_effects", "No Area of Effect Rewards", "Disables rewards that place blocks that have a 3x3x3 area of effect or greater (Does not include rewards that reset blocks to their original state after)");
		profile.addDisabledRewards("chancecubes:tnt_structure", "chancecubes:tnt_diamond", "chancecubes:string!",
				"chancecubes:carpet!", "chancecubes:squid_horde", "chancecubes:d-rude_sandstorm", "chancecubes:ice_cold", 
				"chancecubes:watch_world_burn", "chancecubes:coal_to_diamonds", "chancecubes:hot_tub",
				"chancecubes:arrow_trap", "chancecubes:trampoline", "chancecubes:cave_spider_web", "chancecubes:guardians",
				"chancecubes:path_to_succeed", "chancecubes:help_me", "chancecubes:beacon_build", "chancecubes:disco", 
				"chancecubes:5_prongs", "chancecubes:table_flip", "chancecubes:sky_block", "chancecubes:double_rainbow");
		registerProfile(profile);
		
		
		// MOD DIFFICULTY PROFILES
//		List<RadioGroupProfile> radioProfiles = new ArrayList<RadioGroupProfile>();
//		RadioGroupProfile exEasyMode = new RadioGroupProfile("extreme_easy_mode", "Childs Play Mode", "Configures the Chance Cubes mod to be really easy. Basically no bad rewards");
//		exEasyMode.addDisabledRewards("");
//		registerProfile(exEasyMode);
//		radioProfiles.add(exEasyMode);
//		
//		RadioGroupProfile easyMode = new RadioGroupProfile("easy_mode", "Easy Mode", "Configures the Chance Cubes mod to be easier than it normally is.");
//		easyMode.addDisabledRewards("");
//		registerProfile(easyMode);
//		radioProfiles.add(easyMode);
//		
//		RadioGroupProfile normalMode = new RadioGroupProfile("normal_mode", "Normal Mode", "Normal mod difficulty. Basically the mod as it is, the way it's meant to be played.");
//		registerProfile(normalMode);
//		radioProfiles.add(normalMode);
//		
//		RadioGroupProfile hardMode = new RadioGroupProfile("hard_mode", "Hard Mode", "Ok now were talking. The mod is a big harder than default, but the reward values aren't changed.");
//		hardMode.addDisabledRewards("");
//		registerProfile(hardMode);
//		radioProfiles.add(hardMode);
//		
//		RadioGroupProfile exHardMode = new RadioGroupProfile("extreme_hard_mode", "Abandon All Hope Ye Who Enter Here Mode", "I mean... I guess if you're into that kind of stuff. If you enabling this it doesn't really matter what I put here, since you probably don't care.");
//		exHardMode.addDisabledRewards("");
//		registerProfile(exHardMode);
//		radioProfiles.add(exHardMode);
//		
//		for(RadioGroupProfile rProf: radioProfiles)
//			rProf.assignOtherProfiles(normalMode, exEasyMode, easyMode, normalMode, hardMode, exHardMode);
		
		config.save();
		//@formatter:on
	}

	public static void setupConfig(Configuration configuration)
	{
		config = configuration;
	}
}

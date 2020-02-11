package chanceCubes.profiles;

import chanceCubes.CCubesCore;
import chanceCubes.profiles.triggers.DifficultyTrigger;
import chanceCubes.profiles.triggers.DimensionChangeTrigger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalProfileManager
{
	private static final JsonParser PARSER = new JsonParser();
	private static final Gson GSON = new GsonBuilder().create();

	private static boolean worldProfilesLoaded = false;

	private static Map<IProfile, Boolean> profileDefaults = new HashMap<>();
	private static Map<String, PlayerProfileManager> playerToProfiles = new HashMap<>();

	private static File profileSaveFile = null;
	private static JsonObject profileSaveJson = new JsonObject();

	public static void registerProfile(IProfile profile)
	{
		registerProfile(profile, false);
	}

	public static void registerProfile(IProfile profile, boolean enabled)
	{
		if(profileDefaults.containsKey(profile))
			return;

		profileDefaults.put(profile, enabled);

		if(profile instanceof BasicProfile)
		{
			for(IProfile subProfile : ((BasicProfile) profile).getSubProfiles())
			{
				if(subProfile instanceof BasicProfile)
					GlobalProfileManager.registerProfile(subProfile, subProfile.getTriggers().size() == 0);
				else
					GlobalProfileManager.registerProfile(subProfile);
			}
		}
	}

	public static PlayerProfileManager getPlayerProfileManager(EntityPlayer player)
	{
		return getPlayerProfileManager(player.getUniqueID().toString());
	}

	public static PlayerProfileManager getPlayerProfileManager(String playerUUID)
	{
		if(!worldProfilesLoaded)
			return null;

		return playerToProfiles.computeIfAbsent(playerUUID, (k) ->
		{
			PlayerProfileManager playerProfileManager = new PlayerProfileManager(playerUUID);
			playerProfileManager.loadFromDefaults(profileDefaults);
			return playerProfileManager;
		});
	}

	public static void updateProfilesForWorld(World world)
	{
		playerToProfiles.clear();
		profileSaveFile = new File(world.getSaveHandler().getWorldDirectory(), "data/chancecubes.json");
		try
		{
			if(!profileSaveFile.exists())
			{
				profileSaveFile.createNewFile();
				profileSaveJson = new JsonObject();
			}
			else
			{
				FileReader reader = new FileReader(profileSaveFile);
				JsonElement elem = PARSER.parse(reader);
				reader.close();
				if(elem.isJsonObject())
					profileSaveJson = elem.getAsJsonObject();
				else
					profileSaveJson = new JsonObject();
			}
		} catch(IOException e)
		{
			CCubesCore.logger.log(Level.ERROR, "Failed to load the World specific profile data");
			e.printStackTrace();
			return;
		}

		JsonObject profileJson;
		if(profileSaveJson.has("profiles") && profileSaveJson.get("profiles").isJsonObject())
		{
			profileJson = profileSaveJson.getAsJsonObject("profiles");
		}
		else
		{
			profileJson = new JsonObject();
			profileSaveJson.add("profiles", profileJson);
		}

		for(Map.Entry<String, JsonElement> playerProfilesElement : profileJson.entrySet())
		{
			JsonObject playerProfiles = playerProfilesElement.getValue().getAsJsonObject();
			for(IProfile profile : profileDefaults.keySet())
				if(!playerProfiles.has(profile.getID()))
					playerProfiles.addProperty(profile.getID(), profileDefaults.get(profile));
		}

		if(!saveWorldSaveFile())
			return;

		for(Map.Entry<String, JsonElement> playerProfilesElement : profileJson.entrySet())
		{
			JsonObject playerProfiles = playerProfilesElement.getValue().getAsJsonObject();
			playerToProfiles.put(playerProfilesElement.getKey(), loadPlayerProfilesFromJson(playerProfilesElement.getKey(), playerProfiles));
		}
		worldProfilesLoaded = true;
	}

	public static void loadPlayerProfile(String playerUUID)
	{
		if(!worldProfilesLoaded || playerToProfiles.containsKey(playerUUID))
			return;

		JsonObject profileJson = profileSaveJson.getAsJsonObject("profiles");

		for(Map.Entry<String, JsonElement> playerProfilesElement : profileJson.entrySet())
		{
			JsonObject playerProfiles = playerProfilesElement.getValue().getAsJsonObject();
			if(playerProfilesElement.getKey().equals(playerUUID))
			{
				playerToProfiles.put(playerUUID, loadPlayerProfilesFromJson(playerUUID, playerProfiles));
				return;
			}
		}

		JsonObject playerProfiles = new JsonObject();
		for(IProfile profile : profileDefaults.keySet())
			playerProfiles.addProperty(profile.getID(), profileDefaults.get(profile));

		playerToProfiles.put(playerUUID, loadPlayerProfilesFromJson(playerUUID, playerProfiles));
		profileJson.add(playerUUID, playerProfiles);

		saveWorldSaveFile();
	}

	private static PlayerProfileManager loadPlayerProfilesFromJson(String playerUUID, JsonObject playerProfilesJson)
	{
		PlayerProfileManager playerProfileManager = new PlayerProfileManager(playerUUID);
		playerProfileManager.loadFromJson(playerProfilesJson);
		return playerProfileManager;
	}

	public static void unloadProfilesForWorld()
	{
		worldProfilesLoaded = false;
		playerToProfiles.clear();

		profileSaveFile = null;
		profileSaveJson = new JsonObject();
	}

	public static void updateProfileSaveFile(String playerUUID, JsonObject json)
	{
		if(profileSaveFile != null && worldProfilesLoaded)
		{
			if(profileSaveJson.has("profiles") && profileSaveJson.get("profiles").isJsonObject())
			{
				profileSaveJson.getAsJsonObject("profiles").add(playerUUID, json);
				saveWorldSaveFile();
			}
		}
	}

	public static boolean saveWorldSaveFile()
	{
		try
		{
			FileWriter writer = new FileWriter(profileSaveFile);
			GSON.toJson(profileSaveJson, writer);
			writer.close();
		} catch(IOException e)
		{
			CCubesCore.logger.log(Level.ERROR, "Failed to save the World specific profile data");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static List<String> getAllProfileNames(boolean sorted)
	{
		List<String> toReturn = new ArrayList<>();
		for(IProfile prof : profileDefaults.keySet())
			toReturn.add(prof.getName());

		if(sorted)
			Collections.sort(toReturn);

		return toReturn;
	}


	public static void clearProfiles()
	{
		profileDefaults.clear();
		//TODO: Also clear player data?
	}

	public static void removePlayerProfile(String uuid)
	{
		playerToProfiles.remove(uuid);
	}

	public static List<IProfile> getAllProfiles()
	{
		return new ArrayList<>(profileDefaults.keySet());
	}

	public static IProfile getProfileFromID(String id)
	{
		for(IProfile prof : getAllProfiles())
			if(prof.getID().equals(id))
				return prof;
		return null;
	}

	public static IProfile getProfilefromName(String name)
	{
		for(IProfile prof : profileDefaults.keySet())
			if(prof.getName().equals(name))
				return prof;
		return null;
	}

	// Not Proper english hurts, but idk what to do! areWorldProfilesLoaded just doesn't seem like the right thing to do
	public static boolean isWorldProfilesLoaded()
	{
		return worldProfilesLoaded;
	}


	public static void initProfiles()
	{
		BasicProfile profile;

		//@formatter:off
		profile = new BasicProfile("default", "Default", "Rewards that are disabled by default");
		profile.addDisabledRewards("chancecubes:clear_inventory");
		profile.addDisabledRewards("chancecubes:giant_chance_cube");
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
		profile.addRewardChanceChange("chancecubes:guardians", -85);
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
//		List<RadioGroupProfile> radioProfiles = new ArrayList<>();
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

		//@formatter:on
	}
}

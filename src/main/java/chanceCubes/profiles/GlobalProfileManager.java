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
		CCubesCore.logger.log(Level.DEBUG, profile.getName() + " default:" + enabled);

		if(profile instanceof BasicProfile)
		{
			for(IProfile subProfile : ((BasicProfile) profile).getSubProfiles())
				GlobalProfileManager.registerProfile(subProfile);
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
			PlayerProfileManager playerProfileManager;
			playerProfileManager = new PlayerProfileManager();
			for(Map.Entry<IProfile, Boolean> profileEntry : profileDefaults.entrySet())
			{
				if(profileEntry.getValue())
					playerProfileManager.enableProfile(profileEntry.getKey(), playerUUID);
				else
					playerProfileManager.disableProfile(profileEntry.getKey(), playerUUID);
			}
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

	private static PlayerProfileManager loadPlayerProfilesFromJson(String playerUUID, JsonObject playerProfilesJson)
	{
		PlayerProfileManager playerProfileManager = new PlayerProfileManager();
		for(Map.Entry<String, JsonElement> profileEntry : playerProfilesJson.entrySet())
		{
			IProfile profile = GlobalProfileManager.getProfileFromID(profileEntry.getKey());
			if(profile == null || !profileEntry.getValue().isJsonPrimitive())
			{
				//TODO: Remove it?
				continue;
			}

			if(profileEntry.getValue().getAsBoolean())
				playerProfileManager.enableProfile(profile, playerUUID);
			else
				playerProfileManager.disableProfile(profile, playerUUID);
		}
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
		profile.addDisabledRewards("chancecubes:Clear_Inventory");
		registerProfile(profile, true);

		profile = new BasicProfile("no_explosions", "No Explosions", "Disable all rewards that use explode");
		profile.addDisabledRewards("chancecubes:Tnt_Structure", "chancecubes:Explosion", "chancecubes:TNT_Cat",
				"chancecubes:TNT_Diamond", "chancecubes:TNT_Bats", "chancecubes:Coal_To_Diamonds", "chancecubes:Help_Me",
				"chancecubes:Nuke", "chancecubes:Pssst", "chancecubes:Surrounded_Creeper", "chancecubes:Troll_TNT",
				"chancecubes:Ender_Crystal_Timer", "chancecubes:Wait_For_It", "chancecubes:Charged_Creeper",
				"chancecubes:Torches_To_Creepers", "chancecubes:Cake", "chancecubes:Wolves_To_Creepers",
				"chancecubes:Monty_Hall", "chancecubes:Countdown", "chancecubes:Heads_or_Tails");
		registerProfile(profile);

		profile = new BasicProfile("no_death_mg", "No Death Mini-games", "Disable all minigame rewards that kills the player if they lose");
		profile.addDisabledRewards("chancecubes:Maze", "chancecubes:Dig_Build_Reward", "chancecubes:Matching",
				"chancecubes:Math", "chancecubes:Question");
		registerProfile(profile);

		profile = new BasicProfile("no_status_effects", "No Potions/Effects", "Disable all rewards that throw a potion or give a status effect");
		profile.addDisabledRewards("chancecubes:Poison", "chancecubes:Wither_Status_Effect", "chancecubes:Arrow_Trap",
				"chancecubes:Random_Status_Effect", "chancecubes:Lingering_Potions_Ring", "chancecubes:Surrounded_Creeper",
				"chancecubes:Mob_Abilities_Effects");
		registerProfile(profile);

		profile = new BasicProfile("hardcore", "Hardcore", "For users who play on hardcore diffuculty");
		profile.addDisabledRewards("chancecubes:Heads_or_Tails", "chancecubes:Monty_Hall", "chancecubes:Ender_Crystal_Timer",
				"chancecubes:Wither");
		profile.addSubProfile(getProfileFromID("no_death_mg"));
		profile.addRewardChanceChange("chancecubes:Half_Heart", -100);
		profile.addRewardChanceChange("chancecubes:Cave_Spider_Web", -90);
		profile.addRewardChanceChange("chancecubes:Guardians", -85);
		profile.addTriggers(new DifficultyTrigger(profile, EnumDifficulty.HARD));
		registerProfile(profile);

		profile = new BasicProfile("nether", "Nether", "Updates the reward pool for when players are in the nether");
		profile.addDisabledRewards("chancecubes:Rain", "chancecubes:Sail_Away", "chancecubes:Squid_Horde",
				"chancecubes:Ice_Cold", "chancecubes:Hot_Tub", "chancecubes:Guardians", "chancecubes:Nuke",
				"chancecubes:Cats_And_Dogs");
		profile.addTriggers(new DimensionChangeTrigger(profile, -1));
		registerProfile(profile);

		profile = new BasicProfile("peaceful", "Peaceful", "For users who play on peaceful diffuculty. Removes rewards that have hostile Mobs (Doesn't remove TNT)");
		profile.addDisabledRewards("chancecubes:Pssst", "chancecubes:Horde", "chancecubes:Silverfish_Surround",
				"chancecubes:Slime_Man", "chancecubes:Witch", "chancecubes:Spawn_Jerry", "chancecubes:Spawn_Glenn",
				"chancecubes:Invisible_Creeper", "chancecubes:Knockback_Zombie", "chancecubes:Actual_Invisible_Ghast",
				"chancecubes:Nether_Jelly_Fish", "chancecubes:Quidditch", "chancecubes:One_Man_Army", "chancecubes:Silvermite_Stacks",
				"chancecubes:Invizible_Silverfish", "chancecubes:Skeleton_Bats", "chancecubes:Cave_Spider_Web", "chancecubes:Guardians",
				"chancecubes:Cookie_Monster", "chancecubes:Charged_Creeper", "chancecubes:Torches_To_Creepers", "chancecubes:Herobrine",
				"chancecubes:Surrounded", "chancecubes:Surrounded_Creeper", "chancecubes:Wither", "chancecubes:Wait_For_It",
				"chancecubes:Cake", "chancecubes:Wolves_To_Creepers", "chancecubes:Countdown", "chancecubes:Mob_Tower");
		profile.addTriggers(new DifficultyTrigger(profile, EnumDifficulty.PEACEFUL));
		registerProfile(profile);

		profile = new BasicProfile("no_area_of_effects", "No Area of Effect Rewards", "Disables rewards that place blocks that have a 3x3x3 area of effect or greater (Does not include rewards that reset blocks to their original state after)");
		profile.addDisabledRewards("chancecubes:Tnt_Structure", "chancecubes:TNT_Diamond", "chancecubes:STRING!",
				"chancecubes:CARPET!", "chancecubes:Squid_Horde", "chancecubes:D-rude_SandStorm", "chancecubes:Ice_Cold",
				"chancecubes:Watch_World_Burn", "chancecubes:Coal_To_Diamonds", "chancecubes:Hot_Tub",
				"chancecubes:Arrow_Trap", "chancecubes:Trampoline", "chancecubes:Cave_Spider_Web", "chancecubes:Guardians",
				"chancecubes:Path_To_Succeed", "chancecubes:Help_Me", "chancecubes:Beacon_Build", "chancecubes:Disco",
				"chancecubes:5_Prongs", "chancecubes:Table_Flip", "chancecubes:Sky_Block", "chancecubes:Double_Rainbow");
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

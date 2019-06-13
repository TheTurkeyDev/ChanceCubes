package chanceCubes.config;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.BlockChanceCube;
import chanceCubes.blocks.BlockChanceCube.EnumTexture;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.registry.GiantCubeRegistry;
import chanceCubes.rewards.defaultRewards.BasicReward;
import chanceCubes.rewards.rewardparts.ChestChanceItem;
import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.rewards.rewardparts.EffectPart;
import chanceCubes.rewards.rewardparts.EntityPart;
import chanceCubes.rewards.rewardparts.ExpirencePart;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.MessagePart;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.rewardparts.ParticlePart;
import chanceCubes.rewards.rewardparts.PotionPart;
import chanceCubes.rewards.rewardparts.SoundPart;
import chanceCubes.rewards.rewardparts.TitlePart;
import chanceCubes.rewards.rewardtype.BlockRewardType;
import chanceCubes.rewards.rewardtype.ChestRewardType;
import chanceCubes.rewards.rewardtype.CommandRewardType;
import chanceCubes.rewards.rewardtype.EffectRewardType;
import chanceCubes.rewards.rewardtype.EntityRewardType;
import chanceCubes.rewards.rewardtype.ExperienceRewardType;
import chanceCubes.rewards.rewardtype.IRewardType;
import chanceCubes.rewards.rewardtype.ItemRewardType;
import chanceCubes.rewards.rewardtype.MessageRewardType;
import chanceCubes.rewards.rewardtype.ParticleEffectRewardType;
import chanceCubes.rewards.rewardtype.PotionRewardType;
import chanceCubes.rewards.rewardtype.SchematicRewardType;
import chanceCubes.rewards.rewardtype.SoundRewardType;
import chanceCubes.rewards.rewardtype.TitleRewardType;
import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.FloatVar;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.NBTVar;
import chanceCubes.rewards.variableTypes.StringVar;
import chanceCubes.sounds.CCubesSounds;
import chanceCubes.sounds.CustomSoundsLoader;
import chanceCubes.util.CustomEntry;
import chanceCubes.util.CustomSchematic;
import chanceCubes.util.FileUtil;
import chanceCubes.util.HTTPUtil;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.SchematicUtil;
import net.minecraft.block.Block;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.ModList;

public class CustomRewardsLoader extends BaseLoader
{
	public static CustomRewardsLoader instance;

	private static JsonParser json;

	private boolean reSaveCurrentJson = false;

	public CustomRewardsLoader(File folder)
	{
		instance = this;
		this.folder = folder;
		json = new JsonParser();

		CustomSoundsLoader customSounds = new CustomSoundsLoader(folder, new File(folder.getAbsolutePath() + "/custom_sounds_resourcepack"), "Chance Cubes Resource Pack");
		customSounds.addCustomSounds();
		try
		{
			customSounds.assemble();
		} catch(Exception e)
		{
			e.printStackTrace();
			CCubesCore.logger.log(Level.ERROR, "Chance Cubes failed to create a file or folder neccesary to have custom sound rewards. No custom sounds will be added.");
		}
	}

	public void loadCustomRewards()
	{
		for(File f : folder.listFiles())
		{
			if(!f.isFile() || !f.getName().contains(".json"))
				continue;
			if(f.getName().substring(f.getName().indexOf(".")).equalsIgnoreCase(".json"))
			{
				JsonElement fileJson;
				try
				{
					CCubesCore.logger.log(Level.INFO, "Loading custom rewards file " + f.getName());
					fileJson = json.parse(new FileReader(f));
				} catch(Exception e)
				{
					CCubesCore.logger.log(Level.ERROR, "Unable to parse the file " + f.getName() + ". Skipping file loading.");
					CCubesCore.logger.log(Level.ERROR, "Parse Error: " + e.getMessage());
					continue;
				}

				for(Entry<String, JsonElement> reward : fileJson.getAsJsonObject().entrySet())
				{
					CustomEntry<BasicReward, Boolean> parsedReward = this.parseReward(reward);
					BasicReward basicReward = parsedReward.getKey();
					if(basicReward == null)
					{
						CCubesCore.logger.log(Level.ERROR, "Seems your reward is setup incorrectly, or is disabled for this version of minecraft with a depedency, and Chance Cubes was not able to parse the reward " + reward.getKey() + " for the file " + f.getName());
						continue;
					}

					if(parsedReward.getValue())
						GiantCubeRegistry.INSTANCE.registerReward(basicReward);
					else
						ChanceCubeRegistry.INSTANCE.registerReward(basicReward);

					ChanceCubeRegistry.INSTANCE.addCustomReward(basicReward);

					if(this.reSaveCurrentJson)
						FileUtil.writeJsonToFile(f, fileJson);
				}

				CCubesCore.logger.log(Level.INFO, "Loaded custom rewards file " + f.getName());
			}
		}
	}

	public void fetchRemoteInfo()
	{
		try
		{
			String today = new SimpleDateFormat("MM/dd").format(new Date());
			JsonObject json = HTTPUtil.getWebFile("POST", "https://api.theprogrammingturkey.com/chance_cubes/ChanceCubesAPI.php", new CustomEntry<String, String>("version", CCubesCore.VERSION), new CustomEntry<String, String>("date", today)).getAsJsonObject();
			this.loadDisabledRewards(json.get("Disabled Rewards").getAsJsonArray());
			this.loadHolidayRewards(json.get("Holiday Rewards"));
		} catch(Exception e)
		{
			CCubesCore.logger.log(Level.ERROR, "Failed to fetch remote information for the mod!");
			e.printStackTrace();
			return;
		}
	}

	private void loadHolidayRewards(JsonElement json)
	{
		if(!CCubesSettings.holidayRewards.get())
			return;

		JsonObject holidays = json.getAsJsonObject();
		if(holidays.has("Texture") && !(holidays.get("Texture") instanceof JsonNull))
		{
			CCubesSettings.hasHolidayTexture = true;
			CCubesSettings.holidayTextureName = holidays.get("Texture").getAsString();
		}
		else
		{
			CCubesSettings.hasHolidayTexture = false;
			CCubesSettings.holidayTextureName = "default";
		}

		for(EnumTexture t : EnumTexture.values())
			if(t.getName().equalsIgnoreCase(CCubesSettings.holidayTextureName))
				BlockChanceCube.textureToSet = t;

		if(!CCubesSettings.holidayRewardTriggered.get())
		{
			if(holidays.has("Holiday") && !(holidays.get("Holiday") instanceof JsonNull) && holidays.has("Reward") && !(holidays.get("Reward") instanceof JsonNull))
			{
				String holidayName = holidays.get("Holiday").getAsString();
				BasicReward basicReward = this.parseReward(new CustomEntry<String, JsonElement>(holidayName, holidays.get("Reward"))).getKey();
				if(basicReward != null)
				{
					CCubesSettings.doesHolidayRewardTrigger = true;
					CCubesSettings.holidayReward = basicReward;
					CCubesCore.logger.log(Level.ERROR, "Custom holiday reward \"" + holidayName + "\" loaded!");
				}
				else
				{
					CCubesCore.logger.log(Level.ERROR, "Failed to load the Custom holiday reward \"" + holidayName + "\"!");
				}
			}
		}

	}

	private void loadDisabledRewards(JsonArray disabledRewards)
	{
		if(CCubesSettings.disabledRewards.get())
		{
			for(JsonElement reward : disabledRewards)
			{
				boolean removed = ChanceCubeRegistry.INSTANCE.disableReward(reward.getAsString());
				if(!removed)
					removed = GiantCubeRegistry.INSTANCE.unregisterReward(reward.getAsString());
				CCubesCore.logger.log(Level.WARN, "The reward " + reward.getAsString() + " has been disabled by the mod author due to a bug or some other reason.");
			}
		}
	}

	public CustomEntry<BasicReward, Boolean> parseReward(Entry<String, JsonElement> reward)
	{
		currentParsingReward = reward.getKey();
		List<IRewardType> rewards = new ArrayList<IRewardType>();
		JsonObject rewardElements = reward.getValue().getAsJsonObject();
		int chance = 0;
		boolean isGiantCubeReward = false;
		for(Entry<String, JsonElement> rewardElement : rewardElements.entrySet())
		{
			if(rewardElement.getKey().equalsIgnoreCase("chance"))
			{
				chance = rewardElement.getValue().getAsInt();
				continue;
			}
			else if(rewardElement.getKey().equalsIgnoreCase("dependencies"))
			{
				boolean gameversion = false;
				boolean mcversionused = false;
				for(Entry<String, JsonElement> dependencies : rewardElement.getValue().getAsJsonObject().entrySet())
				{
					if(dependencies.getKey().equalsIgnoreCase("mod"))
					{
						if(!ModList.get().isLoaded(dependencies.getValue().getAsString()))
							return new CustomEntry<BasicReward, Boolean>(null, false);
					}
					else if(dependencies.getKey().equalsIgnoreCase("mcVersion"))
					{
						mcversionused = true;
						String[] versionsToCheck = dependencies.getValue().getAsString().split(",");
						for(String toCheckV : versionsToCheck)
						{
							String currentMCV = CCubesCore.gameVersion;
							if(toCheckV.contains("*"))
							{
								currentMCV = currentMCV.substring(0, currentMCV.lastIndexOf("."));
								toCheckV = toCheckV.substring(0, toCheckV.lastIndexOf("."));
							}
							if(currentMCV.equalsIgnoreCase(toCheckV))
								gameversion = true;
						}
					}
				}
				if(!gameversion && mcversionused)
					return new CustomEntry<BasicReward, Boolean>(null, false);
				continue;
			}
			else if(rewardElement.getKey().equalsIgnoreCase("isGiantCubeReward"))
			{
				isGiantCubeReward = rewardElement.getValue().getAsBoolean();
			}

			try
			{
				JsonArray rewardTypes = rewardElement.getValue().getAsJsonArray();
				currentParsingPart = rewardElement.getKey();
				if(rewardElement.getKey().equalsIgnoreCase("Item"))
					this.loadItemReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Block"))
					this.loadBlockReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Message"))
					this.loadMessageReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Command"))
					this.loadCommandReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Entity"))
					this.loadEntityReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Experience"))
					this.loadExperienceReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Potion"))
					this.loadPotionReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Schematic"))
					this.loadSchematicReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Sound"))
					this.loadSoundReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Chest"))
					this.loadChestReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Particle"))
					this.loadParticleReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Effect"))
					this.loadEffectReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Title"))
					this.loadTitleReward(rewardTypes, rewards);
			} catch(Exception ex)
			{
				CCubesCore.logger.log(Level.ERROR, "Failed to load a custom reward for some reason. The " + this.currentParsingPart + " part of the \"" + this.currentParsingReward + "\" reward may be the issue! I will try better next time.");
				ex.printStackTrace();
			}
		}
		return new CustomEntry<BasicReward, Boolean>(new BasicReward(reward.getKey(), chance, rewards.toArray(new IRewardType[rewards.size()])), isGiantCubeReward);
	}

	public List<IRewardType> loadItemReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<ItemPart> items = new ArrayList<ItemPart>();
		for(JsonElement fullelement : rawReward)
		{
			NBTVar nbt = this.getNBT(fullelement.getAsJsonObject(), "item");
			if(nbt == null)
				continue;

			// TODO: Make dynamic?
			ItemPart stack = new ItemPart(nbt);

			stack.setDelay(this.getInt(fullelement.getAsJsonObject(), "delay", stack.getDelay()));

			items.add(stack);
		}
		rewards.add(new ItemRewardType(items.toArray(new ItemPart[items.size()])));
		return rewards;
	}

	public List<IRewardType> loadBlockReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<OffsetBlock> blocks = new ArrayList<OffsetBlock>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			this.fixOldJsonKeys(element);

			IntVar x = this.getInt(element, "xOffSet", 0);
			IntVar y = this.getInt(element, "yOffSet", 0);
			IntVar z = this.getInt(element, "zOffSet", 0);
			// TODO: Change to Block instead of String
			String[] blockDataParts = this.getString(element, "block", "minecraft:dirt").getValue().split(":");
			String mod = blockDataParts[0];
			String blockName = blockDataParts[1];
			Block block = RewardsUtil.getBlock(mod, blockName);
			BoolVar falling = this.getBoolean(element, "falling", false);

			OffsetBlock offBlock = new OffsetBlock(x, y, z, block, falling);

			offBlock.setDelay(this.getInt(element, "delay", offBlock.getDelay()));
			offBlock.setRelativeToPlayer(this.getBoolean(element, "relativeToPlayer", offBlock.isRelativeToPlayer()));
			offBlock.setRemoveUnbreakableBlocks(this.getBoolean(element, "removeUnbreakableBlocks", offBlock.doesRemoveUnbreakableBlocks()));
			offBlock.setPlaysSound(this.getBoolean(element, "playSound", offBlock.doesPlaySound()));

			//TODO: 
			//			if(blockDataParts.length > 2)
			//				offBlock.setBlockState(RewardsUtil.getBlockStateFromBlockMeta(block, Integer.parseInt(blockDataParts[2])));

			blocks.add(offBlock);
		}
		rewards.add(new BlockRewardType(blocks.toArray(new OffsetBlock[blocks.size()])));
		return rewards;
	}

	public List<IRewardType> loadMessageReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<MessagePart> msgs = new ArrayList<MessagePart>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			MessagePart message = new MessagePart(this.getString(element, "message", "No message was specified to send lel"));

			message.setDelay(this.getInt(element, "delay", message.getDelay()));
			message.setServerWide(this.getBoolean(element, "serverWide", message.isServerWide()));
			message.setRange(this.getInt(element, "range", message.getRange()));

			msgs.add(message);
		}
		rewards.add(new MessageRewardType(msgs.toArray(new MessagePart[msgs.size()])));
		return rewards;
	}

	public List<IRewardType> loadCommandReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<CommandPart> commands = new ArrayList<CommandPart>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			CommandPart command = new CommandPart(this.getString(element, "command", "/help"));

			command.setDelay(this.getInt(element, "delay", command.getDelay()));

			commands.add(command);
		}
		rewards.add(new CommandRewardType(commands.toArray(new CommandPart[commands.size()])));
		return rewards;
	}

	public List<IRewardType> loadEntityReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<EntityPart> entities = new ArrayList<EntityPart>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			EntityPart ent;

			try
			{
				String jsonEdited = this.removedKeyQuotes(element.get("entity").getAsJsonObject().toString());
				NBTTagCompound nbtbase = JsonToNBT.getTagFromJson(jsonEdited);

				if(!(nbtbase instanceof NBTTagCompound))
				{
					CCubesCore.logger.log(Level.ERROR, "Failed to convert the JSON to NBT for: " + element.toString());
					continue;
				}
				else
				{
					ent = new EntityPart((NBTTagCompound) nbtbase);
				}
			} catch(Exception e1)
			{
				CCubesCore.logger.log(Level.ERROR, "The Entiy loading failed for custom reward \"" + this.currentParsingReward + "\"");
				CCubesCore.logger.log(Level.ERROR, "-------------------------------------------");
				e1.printStackTrace();
				continue;
			}

			ent.setDelay(this.getInt(element, "delay", ent.getDelay()));

			entities.add(ent);
		}
		rewards.add(new EntityRewardType(entities.toArray(new EntityPart[entities.size()])));
		return rewards;
	}

	public List<IRewardType> loadExperienceReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<ExpirencePart> exp = new ArrayList<ExpirencePart>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			ExpirencePart exppart = new ExpirencePart(this.getInt(element, "experienceAmount", 1));

			exppart.setDelay(this.getInt(element, "delay", exppart.getDelay()));
			exppart.setNumberofOrbs(this.getInt(element, "numberOfOrbs", 1));

			exp.add(exppart);
		}
		rewards.add(new ExperienceRewardType(exp.toArray(new ExpirencePart[exp.size()])));
		return rewards;
	}

	public List<IRewardType> loadPotionReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<PotionPart> potionEffects = new ArrayList<PotionPart>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			PotionPart potPart = new PotionPart(this.getString(element, "potionid", "speed"), this.getInt(element, "duration", 1), this.getInt(element, "amplifier", 0));

			potPart.setDelay(this.getInt(element, "delay", potPart.getDelay()));

			potionEffects.add(potPart);
		}
		rewards.add(new PotionRewardType(potionEffects.toArray(new PotionPart[potionEffects.size()])));
		return rewards;
	}

	public List<IRewardType> loadSoundReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<SoundPart> sounds = new ArrayList<SoundPart>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			// TODO: Handle sounds
			SoundPart sound = new SoundPart(CCubesSounds.registerSound(this.getString(element, "sound", "").getValue()));

			sound.setDelay(this.getInt(element, "delay", sound.getDelay()));
			sound.setServerWide(this.getBoolean(element, "serverWide", sound.isServerWide()));
			sound.setRange(this.getInt(element, "range", sound.getRange()));
			sound.setAtPlayersLocation(this.getBoolean(element, "playAtPlayersLocation", sound.playAtPlayersLocation()));
			sound.setVolume(this.getFloat(element, "volume", sound.getVolume()));
			sound.setPitch(this.getFloat(element, "pitch", sound.getPitch()));

			sounds.add(sound);
		}
		rewards.add(new SoundRewardType(sounds.toArray(new SoundPart[sounds.size()])));
		return rewards;
	}

	public List<IRewardType> loadChestReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<ChestChanceItem> items = Lists.newArrayList();
		for(JsonElement element : rawReward)
		{
			JsonObject obj = element.getAsJsonObject();
			IntVar meta = this.getInt(obj, "meta", 0);
			IntVar amount = this.getInt(obj, "amount", 1);
			IntVar chance = this.getInt(obj, "chance", 50);
			items.add(new ChestChanceItem(this.getString(obj, "item", "minecraft:dirt").getValue(), meta, chance, amount));
		}
		rewards.add(new ChestRewardType(items.toArray(new ChestChanceItem[items.size()])));
		return rewards;
	}

	public List<IRewardType> loadParticleReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<ParticlePart> particles = new ArrayList<ParticlePart>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			ParticlePart particle = new ParticlePart(this.getString(element, "particle", "explode"));

			particle.setDelay(this.getInt(element, "delay", particle.getDelay()));

			particles.add(particle);
		}
		rewards.add(new ParticleEffectRewardType(particles.toArray(new ParticlePart[particles.size()])));
		return rewards;
	}

	public List<IRewardType> loadSchematicReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			String fileName = element.get("fileName").getAsString();
			this.fixOldJsonKeys(element);

			// TODO: Make this support IntVar?
			int xoff = this.getInt(element, "xOffSet", 0).getIntValue();
			int yoff = this.getInt(element, "yOffSet", 0).getIntValue();
			int zoff = this.getInt(element, "zOffSet", 0).getIntValue();
			IntVar delay = this.getInt(element, "delay", 0);
			BoolVar falling = this.getBoolean(element, "falling", true);
			BoolVar relativeToPlayer = this.getBoolean(element, "relativeToPlayer", false);
			BoolVar includeAirBlocks = this.getBoolean(element, "includeAirBlocks", false);
			BoolVar playSound = this.getBoolean(element, "playSound", true);
			FloatVar spacingDelay = this.getFloat(element, "spacingDelay", 0.1f);

			CustomSchematic schematic = null;
			if(fileName.endsWith(".ccs"))
				schematic = SchematicUtil.loadCustomSchematic(fileName, xoff, yoff, zoff, spacingDelay, falling, relativeToPlayer, includeAirBlocks, playSound, delay);
			else if(fileName.endsWith(".schematic"))
				schematic = SchematicUtil.loadLegacySchematic(fileName, xoff, yoff, zoff, spacingDelay, falling, relativeToPlayer, includeAirBlocks, playSound, delay);
			if(schematic == null)
				CCubesCore.logger.log(Level.ERROR, "Failed to load a schematic reward with the file name " + fileName);
			else
				rewards.add(new SchematicRewardType(schematic));
		}
		return rewards;
	}

	public List<IRewardType> loadEffectReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<EffectPart> effects = new ArrayList<EffectPart>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			EffectPart effectPart = new EffectPart(this.getString(element, "potionID", "speed"), this.getInt(element, "duration", 1), this.getInt(element, "amplifier", 0));

			effectPart.setDelay(this.getInt(element, "radius", 1));

			effectPart.setDelay(this.getInt(element, "delay", effectPart.getDelay()));

			effects.add(effectPart);
		}
		rewards.add(new EffectRewardType(effects.toArray(new EffectPart[effects.size()])));
		return rewards;
	}

	public List<IRewardType> loadTitleReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<TitlePart> titles = new ArrayList<TitlePart>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			JsonElement message = element.get("message");
			TitlePart titlePart = new TitlePart(this.getString(element, "type", "TITLE"), message == null ? new JsonObject() : message.getAsJsonObject());

			titlePart.setFadeInTime(this.getInt(element, "fadeInTime", 0));
			titlePart.setDisplayTime(this.getInt(element, "displayTime", 0));
			titlePart.setFadeOutTime(this.getInt(element, "fadeOutTime", 0));
			titlePart.setServerWide(this.getBoolean(element, "isServerWide", false));
			titlePart.setRange(this.getInt(element, "range", 16));

			titlePart.setDelay(this.getInt(element, "delay", titlePart.getDelay()));

			titles.add(titlePart);
		}
		rewards.add(new TitleRewardType(titles.toArray(new TitlePart[titles.size()])));
		return rewards;
	}

	public String removedKeyQuotes(String raw)
	{
		StringBuilder sb = new StringBuilder(raw.toString());
		int index = 0;
		while((index = sb.indexOf("\"", index)) != -1)
		{
			int secondQuote = sb.indexOf("\"", index + 1);
			if(secondQuote == -1)
				break;
			if(sb.charAt(secondQuote + 1) == ':')
			{
				sb.deleteCharAt(index);
				sb.delete(secondQuote - 1, secondQuote);
				index = secondQuote;
			}
			else
			{
				index++;
			}
		}
		return sb.toString();
	}

	public IntVar getInt(JsonObject json, String key, int defaultVal)
	{
		String in = "";
		if(json.has(key))
			in = json.get(key).getAsString();

		return this.getInt(in, defaultVal);
	}

	public FloatVar getFloat(JsonObject json, String key, float defaultVal)
	{
		String in = "";
		if(json.has(key))
			in = json.get(key).getAsString();

		return this.getFloat(in, defaultVal);
	}

	public BoolVar getBoolean(JsonObject json, String key, boolean defaultVal)
	{
		String in = "";
		if(json.has(key))
			in = json.get(key).getAsString();

		return this.getBoolean(in, defaultVal);
	}

	public StringVar getString(JsonObject json, String key, String defaultVal)
	{
		String in = "";
		if(json.has(key))
			in = json.get(key).getAsString();

		return this.getString(in, defaultVal);
	}

	public NBTVar getNBT(JsonObject json, String key)
	{
		String in = "";
		if(json.has(key))
		{
			JsonElement value = json.get(key);
			if(value.isJsonPrimitive())
			{
				in = value.getAsString();
				in = this.removedKeyQuotes(in);
			}
			else
			{
				in = json.getAsJsonObject(key).toString();
			}
		}

		return this.getNBT(in);
	}

	public void fixOldJsonKeys(JsonObject json)
	{
		for(String s : new String[] { "XOffSet", "YOffSet", "ZOffSet", "RelativeToPlayer", "Block", "Falling", "IncludeAirBlocks" })
		{
			if(json.has(s))
			{
				reSaveCurrentJson = true;
				json.add(s.substring(0, 1).toLowerCase() + s.substring(1), json.get(s));
				json.remove(s);
			}
		}
	}

	public File getFolderFile()
	{
		return this.folder;
	}
}
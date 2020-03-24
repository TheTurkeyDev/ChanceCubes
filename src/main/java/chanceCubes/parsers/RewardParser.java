package chanceCubes.parsers;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BasicReward;
import chanceCubes.rewards.rewardparts.*;
import chanceCubes.rewards.rewardtype.*;
import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.FloatVar;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.NBTVar;
import chanceCubes.rewards.variableTypes.StringVar;
import chanceCubes.sounds.CCubesSounds;
import chanceCubes.util.CustomEntry;
import chanceCubes.util.RewardsUtil;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RewardParser
{

	public static CustomEntry<BasicReward, Boolean> parseReward(Map.Entry<String, JsonElement> reward)
	{
		List<IRewardType> rewards = new ArrayList<>();
		JsonObject rewardElements = reward.getValue().getAsJsonObject();
		int chance = 0;
		boolean isGiantCubeReward = false;
		for(Map.Entry<String, JsonElement> rewardElement : rewardElements.entrySet())
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
				for(Map.Entry<String, JsonElement> dependencies : rewardElement.getValue().getAsJsonObject().entrySet())
				{
					if(dependencies.getKey().equalsIgnoreCase("mod"))
					{
						if(!ModList.get().isLoaded(dependencies.getValue().getAsString()))
							return new CustomEntry<>(null, false);
					}
					else if(dependencies.getKey().equalsIgnoreCase("mcVersion"))
					{
						mcversionused = true;
						String[] versionsToCheck = dependencies.getValue().getAsString().split(",");
						for(String toCheckV : versionsToCheck)
						{

							String currentMCV;
							if(EffectiveSide.get() == LogicalSide.CLIENT)
								currentMCV = Minecraft.getInstance().getVersion();
							else
								currentMCV = ServerLifecycleHooks.getCurrentServer().getMinecraftVersion();

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
					return new CustomEntry<>(null, false);
				continue;
			}
			else if(rewardElement.getKey().equalsIgnoreCase("isGiantCubeReward"))
			{
				isGiantCubeReward = rewardElement.getValue().getAsBoolean();
			}

			String currentParsingPart = rewardElement.getKey();
			try
			{
				JsonArray rewardTypes = rewardElement.getValue().getAsJsonArray();
				if(rewardElement.getKey().equalsIgnoreCase("Item"))
					loadItemReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Block"))
					loadBlockReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Message"))
					loadMessageReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Command"))
					loadCommandReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Entity"))
					loadEntityReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Experience"))
					loadExperienceReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Potion"))
					loadPotionReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Schematic"))
					loadSchematicReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Sound"))
					loadSoundReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Chest"))
					loadChestReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Particle"))
					loadParticleReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Effect"))
					loadEffectReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Title"))
					loadTitleReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Area"))
					loadAreaReward(rewardTypes, rewards);
			} catch(Exception ex)
			{
				CCubesCore.logger.log(Level.ERROR, "Failed to load a custom reward for some reason. The " + currentParsingPart + " part of the reward may be the issue! I will try better next time.");
				ex.printStackTrace();
			}
		}
		return new CustomEntry<>(new BasicReward(reward.getKey(), chance, rewards.toArray(new IRewardType[0])), isGiantCubeReward);
	}

	public static List<IRewardType> loadItemReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<ItemPart> items = new ArrayList<>();
		for(JsonElement fullelement : rawReward)
		{
			NBTVar nbt = ParserUtil.getNBT(fullelement.getAsJsonObject(), "item");

			// TODO: Make dynamic?
			ItemPart stack = new ItemPart(nbt);

			stack.setDelay(ParserUtil.getInt(fullelement.getAsJsonObject(), "delay", stack.getDelay()));

			items.add(stack);
		}
		rewards.add(new ItemRewardType(items.toArray(new ItemPart[0])));
		return rewards;
	}

	public static List<IRewardType> loadBlockReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<OffsetBlock> blocks = new ArrayList<>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();

			IntVar x = ParserUtil.getInt(element, "xOffSet", 0);
			IntVar y = ParserUtil.getInt(element, "yOffSet", 0);
			IntVar z = ParserUtil.getInt(element, "zOffSet", 0);
			// TODO: Change to Block instead of String
			String[] blockDataParts = ParserUtil.getString(element, "block", "minecraft:dirt").getValue().split(":");
			String mod = blockDataParts[0];
			String blockName = blockDataParts[1];
			Block block = RewardsUtil.getBlock(mod, blockName);
			BoolVar falling = ParserUtil.getBoolean(element, "falling", false);

			OffsetBlock offBlock = new OffsetBlock(x, y, z, block, falling);

			offBlock.setDelay(ParserUtil.getInt(element, "delay", offBlock.getDelay()));
			offBlock.setRelativeToPlayer(ParserUtil.getBoolean(element, "relativeToPlayer", offBlock.isRelativeToPlayer()));
			offBlock.setRemoveUnbreakableBlocks(ParserUtil.getBoolean(element, "removeUnbreakableBlocks", offBlock.doesRemoveUnbreakableBlocks()));
			offBlock.setPlaysSound(ParserUtil.getBoolean(element, "playSound", offBlock.doesPlaySound()));

			//TODO
			if(blockDataParts.length > 2)
				//offBlock.setBlockState(RewardsUtil.getBlockStateFromBlockMeta(block, Integer.parseInt(blockDataParts[2])));

				blocks.add(offBlock);
		}
		rewards.add(new BlockRewardType(blocks.toArray(new OffsetBlock[0])));
		return rewards;
	}

	public static List<IRewardType> loadMessageReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<MessagePart> msgs = new ArrayList<>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			MessagePart message = new MessagePart(ParserUtil.getString(element, "message", "No message was specified to send lel"));

			message.setDelay(ParserUtil.getInt(element, "delay", message.getDelay()));
			message.setServerWide(ParserUtil.getBoolean(element, "serverWide", message.isServerWide()));
			message.setRange(ParserUtil.getInt(element, "range", message.getRange()));

			msgs.add(message);
		}
		rewards.add(new MessageRewardType(msgs.toArray(new MessagePart[0])));
		return rewards;
	}

	public static List<IRewardType> loadCommandReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<CommandPart> commands = new ArrayList<>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			CommandPart command = new CommandPart(ParserUtil.getString(element, "command", "/help"));

			command.setDelay(ParserUtil.getInt(element, "delay", command.getDelay()));
			command.setCopies(ParserUtil.getInt(element, "copies", 0));

			commands.add(command);
		}
		rewards.add(new CommandRewardType(commands.toArray(new CommandPart[0])));
		return rewards;
	}

	public static List<IRewardType> loadEntityReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<EntityPart> entities = new ArrayList<>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			EntityPart ent;

			try
			{
				String jsonEdited = ParserUtil.removedKeyQuotes(element.get("entity").getAsJsonObject().toString());
				ent = new EntityPart(JsonToNBT.getTagFromJson(jsonEdited));
			} catch(Exception e1)
			{
				CCubesCore.logger.log(Level.ERROR, "The Entity loading failed for this custom reward!");
				CCubesCore.logger.log(Level.ERROR, "Invalid json is: " + element.getAsString());
				e1.printStackTrace();
				continue;
			}

			ent.setRemovedBlocks(ParserUtil.getBoolean(element, "removeBlocks", true));
			ent.setCopies(ParserUtil.getInt(element, "copies", 0));
			ent.setDelay(ParserUtil.getInt(element, "delay", ent.getDelay()));

			entities.add(ent);
		}
		rewards.add(new EntityRewardType(entities.toArray(new EntityPart[0])));
		return rewards;
	}

	public static List<IRewardType> loadExperienceReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<ExpirencePart> exp = new ArrayList<>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			ExpirencePart exppart = new ExpirencePart(ParserUtil.getInt(element, "experienceAmount", 1));

			exppart.setDelay(ParserUtil.getInt(element, "delay", exppart.getDelay()));
			exppart.setNumberofOrbs(ParserUtil.getInt(element, "numberOfOrbs", 1));

			exp.add(exppart);
		}
		rewards.add(new ExperienceRewardType(exp.toArray(new ExpirencePart[0])));
		return rewards;
	}

	public static List<IRewardType> loadPotionReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<PotionPart> potionEffects = new ArrayList<>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			PotionPart potPart = new PotionPart(ParserUtil.getString(element, "potionid", "speed"), ParserUtil.getInt(element, "duration", 1), ParserUtil.getInt(element, "amplifier", 0));

			potPart.setDelay(ParserUtil.getInt(element, "delay", potPart.getDelay()));

			potionEffects.add(potPart);
		}
		rewards.add(new PotionRewardType(potionEffects.toArray(new PotionPart[0])));
		return rewards;
	}

	public static List<IRewardType> loadSoundReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<SoundPart> sounds = new ArrayList<>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			// TODO: Handle sounds
			SoundPart sound = new SoundPart(CCubesSounds.registerSound(ParserUtil.getString(element, "sound", "").getValue()));

			sound.setDelay(ParserUtil.getInt(element, "delay", sound.getDelay()));
			sound.setServerWide(ParserUtil.getBoolean(element, "serverWide", sound.isServerWide()));
			sound.setRange(ParserUtil.getInt(element, "range", sound.getRange()));
			sound.setAtPlayersLocation(ParserUtil.getBoolean(element, "playAtPlayersLocation", sound.playAtPlayersLocation()));
			sound.setVolume(ParserUtil.getFloat(element, "volume", sound.getVolume()));
			sound.setPitch(ParserUtil.getFloat(element, "pitch", sound.getPitch()));

			sounds.add(sound);
		}
		rewards.add(new SoundRewardType(sounds.toArray(new SoundPart[0])));
		return rewards;
	}

	public static List<IRewardType> loadChestReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<ChestChanceItem> items = Lists.newArrayList();
		for(JsonElement element : rawReward)
		{
			JsonObject obj = element.getAsJsonObject();
			IntVar amount = ParserUtil.getInt(obj, "amount", 1);
			IntVar chance = ParserUtil.getInt(obj, "chance", 50);

			// TODO: Handle items
			items.add(new ChestChanceItem(ParserUtil.getString(obj, "item", "minecraft:dirt").getValue(), chance, amount));

		}
		rewards.add(new ChestRewardType(items.toArray(new ChestChanceItem[0])));
		return rewards;
	}

	public static List<IRewardType> loadParticleReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<ParticlePart> particles = new ArrayList<>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			ParticlePart particle = new ParticlePart(ParserUtil.getString(element, "particle", "explode"));

			particle.setDelay(ParserUtil.getInt(element, "delay", particle.getDelay()));

			particles.add(particle);
		}
		rewards.add(new ParticleEffectRewardType(particles.toArray(new ParticlePart[0])));
		return rewards;
	}

	public static List<IRewardType> loadSchematicReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			String fileName = element.get("fileName").getAsString();

			IntVar xoff = ParserUtil.getInt(element, "xOffSet", 0);
			IntVar yoff = ParserUtil.getInt(element, "yOffSet", 0);
			IntVar zoff = ParserUtil.getInt(element, "zOffSet", 0);
			IntVar delay = ParserUtil.getInt(element, "delay", 0);
			BoolVar falling = ParserUtil.getBoolean(element, "falling", true);
			BoolVar relativeToPlayer = ParserUtil.getBoolean(element, "relativeToPlayer", false);
			BoolVar includeAirBlocks = ParserUtil.getBoolean(element, "includeAirBlocks", false);
			BoolVar playSound = ParserUtil.getBoolean(element, "playSound", true);
			FloatVar spacingDelay = ParserUtil.getFloat(element, "spacingDelay", 0.1f);

			SchematicPart part = new SchematicPart(fileName, xoff, yoff, zoff, spacingDelay, falling, relativeToPlayer, includeAirBlocks, playSound, delay);

			rewards.add(new SchematicRewardType(part));
		}
		return rewards;
	}

	public static List<IRewardType> loadEffectReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<EffectPart> effects = new ArrayList<>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			EffectPart effectPart = new EffectPart(ParserUtil.getString(element, "potionid", "speed"), ParserUtil.getInt(element, "duration", 1), ParserUtil.getInt(element, "amplifier", 0));

			effectPart.setRadius(ParserUtil.getInt(element, "radius", 1));

			effectPart.setDelay(ParserUtil.getInt(element, "delay", effectPart.getDelay()));

			effects.add(effectPart);
		}
		rewards.add(new EffectRewardType(effects.toArray(new EffectPart[0])));
		return rewards;
	}

	public static List<IRewardType> loadTitleReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<TitlePart> titles = new ArrayList<>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			JsonElement message = element.get("message");
			TitlePart titlePart = new TitlePart(ParserUtil.getString(element, "type", "TITLE"), message == null ? new JsonObject() : message.getAsJsonObject());

			titlePart.setFadeInTime(ParserUtil.getInt(element, "fadeInTime", 0));
			titlePart.setDisplayTime(ParserUtil.getInt(element, "displayTime", 0));
			titlePart.setFadeOutTime(ParserUtil.getInt(element, "fadeOutTime", 0));
			titlePart.setServerWide(ParserUtil.getBoolean(element, "isServerWide", false));
			titlePart.setRange(ParserUtil.getInt(element, "range", 16));

			titlePart.setDelay(ParserUtil.getInt(element, "delay", titlePart.getDelay()));

			titles.add(titlePart);
		}
		rewards.add(new TitleRewardType(titles.toArray(new TitlePart[0])));
		return rewards;
	}

	public static List<IRewardType> loadAreaReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<BlockAreaPart> titles = new ArrayList<>();
		for(JsonElement elementElem : rawReward)
		{
			JsonObject element = elementElem.getAsJsonObject();
			IntVar xSize = ParserUtil.getInt(element, "xSize", 1);
			IntVar ySize = ParserUtil.getInt(element, "ySize", 1);
			IntVar zSize = ParserUtil.getInt(element, "zSize", 1);
			StringVar block = ParserUtil.getString(element, "block", "minecraft:dirt");
			BlockAreaPart areaPart = new BlockAreaPart(xSize, ySize, zSize, block);

			areaPart.setxOff(ParserUtil.getInt(element, "xOff", 0));
			areaPart.setyOff(ParserUtil.getInt(element, "yOff", 0));
			areaPart.setzOff(ParserUtil.getInt(element, "zOff", 0));
			areaPart.setFalling(ParserUtil.getBoolean(element, "falling", false));
			areaPart.setCausesUpdate(ParserUtil.getBoolean(element, "causesUpdate", false));
			areaPart.setRelativeToPlayer(ParserUtil.getBoolean(element, "relativeToPlayer", false));
			areaPart.setDelay(ParserUtil.getInt(element, "delay", areaPart.getDelay()));

			titles.add(areaPart);
		}
		rewards.add(new BlockAreaRewardType(titles.toArray(new BlockAreaPart[0])));
		return rewards;
	}
}

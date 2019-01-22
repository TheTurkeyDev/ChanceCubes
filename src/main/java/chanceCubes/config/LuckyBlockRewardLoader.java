package chanceCubes.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.defaultRewards.BasicReward;
import chanceCubes.rewards.rewardparts.BasePart;
import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.rewards.rewardparts.EntityPart;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.MessagePart;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.rewardparts.ParticlePart;
import chanceCubes.rewards.rewardparts.PotionPart;
import chanceCubes.rewards.rewardparts.SoundPart;
import chanceCubes.rewards.type.BlockRewardType;
import chanceCubes.rewards.type.CommandRewardType;
import chanceCubes.rewards.type.EntityRewardType;
import chanceCubes.rewards.type.IRewardType;
import chanceCubes.rewards.type.ItemRewardType;
import chanceCubes.rewards.type.MessageRewardType;
import chanceCubes.rewards.type.ParticleEffectRewardType;
import chanceCubes.rewards.type.PotionRewardType;
import chanceCubes.rewards.type.SoundRewardType;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.SchematicUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import scala.actors.threadpool.Arrays;

public class LuckyBlockRewardLoader
{
	public static LuckyBlockRewardLoader instance;
	private File lbFolder;

	public LuckyBlockRewardLoader(File folder)
	{
		instance = this;
		lbFolder = folder;
	}

	public void parseLuckyBlockRewards()
	{
		for(File f : lbFolder.listFiles())
		{
			if(f.getName().contains(".zip"))
			{
				CCubesCore.logger.log(Level.INFO, "Loading Lucky Blocks rewards file " + f.getName());
				String fileName = f.getName().substring(0, f.getName().indexOf("."));
				try
				{
					ZipFile zipFile = new ZipFile(f);

					Enumeration<? extends ZipEntry> entries = zipFile.entries();

					while(entries.hasMoreElements())
					{
						ZipEntry entry = entries.nextElement();
						InputStream stream = zipFile.getInputStream(entry);
						if(entry.getName().equalsIgnoreCase("drops.txt"))
						{
							parseDropsFile(fileName, stream);
						}

						stream.close();
					}

					zipFile.close();
				} catch(Exception e)
				{
					e.printStackTrace();
				}
				CCubesCore.logger.log(Level.INFO, "Loaded Lucky Blocks rewards file " + f.getName());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void parseDropsFile(String fileName, InputStream stream) throws IOException
	{
		int rewardNumber = 0;
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder multiLine = new StringBuilder();
		String line;
		while((line = reader.readLine()) != null)
		{
			line = line.trim();
			if(line.isEmpty() || line.startsWith("/"))
			{
				continue;
			}
			else if(line.endsWith("\\"))
			{
				line = line.substring(0, line.length() - 1);
				multiLine.append(line);
				continue;
			}

			if(multiLine.length() > 0)
			{
				multiLine.append(line);
				line = multiLine.toString();
				multiLine.setLength(0);
			}

			Map<String, List<IRewardType>> rewards = new HashMap<String, List<IRewardType>>();
			try
			{
				if(line.startsWith("group"))
					parseGroup(line, rewards);
				else
					parseItem(line, rewards);
			} catch(Exception e)
			{
				e.printStackTrace();
				continue;
			}

			List<IRewardType> rewardTypes = new ArrayList<IRewardType>();
			for(String key : rewards.keySet())
			{
				switch(key)
				{
					case "item":
					{
						List<BasePart> parts = new ArrayList<BasePart>();
						for(IRewardType type : rewards.get(key))
							parts.addAll(Arrays.asList(((ItemRewardType) type).getRewardParts()));
						rewardTypes.add(new ItemRewardType(parts.toArray(new ItemPart[0])));
						break;
					}
					case "block":
					{
						List<BasePart> parts = new ArrayList<BasePart>();
						for(IRewardType type : rewards.get(key))
							parts.addAll(Arrays.asList(((BlockRewardType) type).getRewardParts()));
						rewardTypes.add(new BlockRewardType(parts.toArray(new OffsetBlock[0])));
						break;
					}
					case "entity":
					{
						List<BasePart> parts = new ArrayList<BasePart>();
						for(IRewardType type : rewards.get(key))
							parts.addAll(Arrays.asList(((EntityRewardType) type).getRewardParts()));
						rewardTypes.add(new EntityRewardType(parts.toArray(new EntityPart[0])));
						break;
					}
					case "command":
					{
						List<BasePart> parts = new ArrayList<BasePart>();
						for(IRewardType type : rewards.get(key))
							parts.addAll(Arrays.asList(((CommandRewardType) type).getRewardParts()));
						rewardTypes.add(new CommandRewardType(parts.toArray(new CommandPart[0])));
						break;
					}
					case "effect":
					{
						List<BasePart> parts = new ArrayList<BasePart>();
						for(IRewardType type : rewards.get(key))
							parts.addAll(Arrays.asList(((PotionRewardType) type).getRewardParts()));
						rewardTypes.add(new PotionRewardType(parts.toArray(new PotionPart[0])));
						break;
					}
					case "message":
					{
						List<BasePart> parts = new ArrayList<BasePart>();
						for(IRewardType type : rewards.get(key))
							parts.addAll(Arrays.asList(((MessageRewardType) type).getRewardParts()));
						rewardTypes.add(new MessageRewardType(parts.toArray(new MessagePart[0])));
						break;
					}
					case "particle":
					{
						List<BasePart> parts = new ArrayList<BasePart>();
						for(IRewardType type : rewards.get(key))
							parts.addAll(Arrays.asList(((ParticleEffectRewardType) type).getRewardParts()));
						rewardTypes.add(new ParticleEffectRewardType(parts.toArray(new ParticlePart[0])));
						break;
					}
					case "sound":
					{
						List<BasePart> parts = new ArrayList<BasePart>();
						for(IRewardType type : rewards.get(key))
							parts.addAll(Arrays.asList(((SoundRewardType) type).getRewardParts()));
						rewardTypes.add(new SoundRewardType(parts.toArray(new SoundPart[0])));
						break;
					}
				}
			}

			int chance = 0;
			if(line.indexOf("@luck") > -1)
				chance = Integer.parseInt(line.substring(line.indexOf("@luck") + 6));
			BasicReward reward = new BasicReward(fileName + "_" + rewardNumber, chance, rewardTypes.toArray(new IRewardType[rewards.size()]));
			System.out.println(reward.getName() + " @ " + reward.getChanceValue());
			ChanceCubeRegistry.INSTANCE.registerReward(reward);
			rewardNumber++;
		}
	}

	public void parseGroup(String in, Map<String, List<IRewardType>> rewards)
	{
		if(in.contains("group:"))
			in = in.replaceFirst(":.*:", "");
		String[] items = in.substring(in.indexOf("(") + 1, in.lastIndexOf(")")).split(";");
		for(String item : items)
		{
			if(item.startsWith("group"))
				parseGroup(item, rewards);
			else
				parseItem(item, rewards);
		}
	}

	public void parseItem(String in, Map<String, List<IRewardType>> rewards)
	{
		Map<String, String> typeMap = new HashMap<String, String>();
		do
		{
			String key = parseNextKey(in);
			in = in.substring(Math.min(key.length() + 1, in.length()));
			String value = parseKeyValue(key, in);
			in = in.substring(Math.min(value.length() + 1, in.length()));
			typeMap.put(key, value);
		} while(!in.isEmpty());

		String type = "item";
		if(typeMap.containsKey("type"))
			type = typeMap.get("type");

		switch(type)
		{
			case "item":
			{
				String item = typeMap.get("ID");
				NBTTagCompound itemNBT = new NBTTagCompound();
				itemNBT.setString("id", item);

				if(typeMap.containsKey("damage"))
					itemNBT.setString("damage", typeMap.get("damage"));

				ItemStack stack = new ItemStack(itemNBT);

				if(typeMap.containsKey("amount"))
					stack.setCount(Integer.parseInt(typeMap.get("amount")));

				try
				{
					if(typeMap.containsKey("NBTTag"))
						stack.setTagCompound(JsonToNBT.getTagFromJson(convertLBNBTToMCNBT(typeMap.get("NBTTag"))));
				} catch(NBTException e)
				{
					CCubesCore.logger.log(Level.ERROR, "Chance Cubes failed to parse the given NBTData: " + convertLBNBTToMCNBT(typeMap.get("NBTTag")));
					e.printStackTrace();
				}

				ItemPart itemPart = new ItemPart(stack);

				if(typeMap.containsKey("delay"))
					itemPart.setDelay(Integer.parseInt(typeMap.get("delay")));

				List<IRewardType> itemTypes = rewards.get("item");
				if(itemTypes == null)
				{
					itemTypes = new ArrayList<IRewardType>();
					rewards.put("item", itemTypes);
				}

				itemTypes.add(new ItemRewardType(itemPart));
				break;
			}
			case "block":
			{
				int x = 0, y = 0, z = 0;

				//TODO: parse out the x, y ans y of this
				if(typeMap.containsKey("posX"))
					x = x;

				if(typeMap.containsKey("posOffsetX"))
					x = Integer.parseInt(typeMap.get("posOffsetX"));
				if(typeMap.containsKey("posOffsetY"))
					y = Integer.parseInt(typeMap.get("posOffsetY"));
				if(typeMap.containsKey("posOffsetY"))
					z = Integer.parseInt(typeMap.get("posOffsetZ"));

				Block block;
				String[] blockDataParts = typeMap.get("posOffsetZ").split(":");
				if(blockDataParts.length == 2)
					block = RewardsUtil.getBlock(blockDataParts[0], blockDataParts[1]);
				else
					block = RewardsUtil.getBlock("minecraft", blockDataParts[0]);

				OffsetBlock osb = new OffsetBlock(x, y, z, block, false);
				if(typeMap.containsKey("meta"))
					osb.setBlockState(RewardsUtil.getBlockStateFromBlockMeta(block, Integer.parseInt(typeMap.get("meta"))));
				if(typeMap.containsKey("damage"))
					osb.setBlockState(RewardsUtil.getBlockStateFromBlockMeta(block, Integer.parseInt(typeMap.get("damage"))));
				if(typeMap.containsKey("state"))
					osb.setBlockState(RewardsUtil.getBlockStateFromBlockMeta(block, Integer.parseInt(typeMap.get("state"))));

				if(typeMap.containsKey("blockUpdate"))
					osb.setCausesBlockUpdate(Boolean.parseBoolean(typeMap.get("blockUpdate")));

				if(typeMap.containsKey("delay"))
					osb.setDelay(Integer.parseInt(typeMap.get("delay")));

				try
				{
					if(typeMap.containsKey("tileEntity"))
						osb = SchematicUtil.OffsetBlockToTileEntity(osb, JsonToNBT.getTagFromJson(convertLBNBTToMCNBT(typeMap.get("tileEntity"))));
					if(typeMap.containsKey("NBTTag"))
						osb = SchematicUtil.OffsetBlockToTileEntity(osb, JsonToNBT.getTagFromJson(convertLBNBTToMCNBT(typeMap.get("NBTTag"))));
				} catch(NBTException e)
				{
					e.printStackTrace();
				}

				List<IRewardType> itemTypes = rewards.get("block");
				if(itemTypes == null)
				{
					itemTypes = new ArrayList<IRewardType>();
					rewards.put("block", itemTypes);
				}

				itemTypes.add(new BlockRewardType(osb));
				break;
			}
			case "entity":
			{
				NBTTagCompound nbt = new NBTTagCompound();

				try
				{
					if(typeMap.containsKey("NBTTag"))
						nbt = JsonToNBT.getTagFromJson(convertLBNBTToMCNBT(typeMap.get("NBTTag")));
				} catch(NBTException e)
				{
					e.printStackTrace();
				}

				nbt.setString("id", typeMap.get("ID"));

				EntityPart entPart = new EntityPart(nbt);

				if(typeMap.containsKey("delay"))
					entPart.setDelay(Integer.parseInt(typeMap.get("delay")));

				List<IRewardType> itemTypes = rewards.get("entity");
				if(itemTypes == null)
				{
					itemTypes = new ArrayList<IRewardType>();
					rewards.put("entity", itemTypes);
				}

				itemTypes.add(new EntityRewardType(entPart));
				break;
			}
			case "structure":
			{
				//TODO
				List<IRewardType> itemTypes = rewards.get("schematic");
				if(itemTypes == null)
				{
					itemTypes = new ArrayList<IRewardType>();
					rewards.put("structure", itemTypes);
				}

				itemTypes.add(new MessageRewardType(new MessagePart("Not Implemented Yet!")));
				break;
			}
			case "command":
			{
				String commandString = typeMap.get("ID").trim();
				CommandPart command = new CommandPart(commandString.substring(1, commandString.length() - 2));

				if(typeMap.containsKey("delay"))
					command.setDelay(Integer.parseInt(typeMap.get("delay")));

				List<IRewardType> itemTypes = rewards.get("command");
				if(itemTypes == null)
				{
					itemTypes = new ArrayList<IRewardType>();
					rewards.put("command", itemTypes);
				}

				itemTypes.add(new CommandRewardType(command));
				break;
			}
			case "effect":
			{
				//TODO Create an effect reward type
				int duration = 30;
				if(typeMap.containsKey("duration"))
					duration = Integer.parseInt(typeMap.get("duration"));
				int amplifier = 0;
				if(typeMap.containsKey("amplifier"))
					duration = Integer.parseInt(typeMap.get("amplifier"));

				PotionPart potPart = new PotionPart(Potion.getPotionFromResourceLocation(typeMap.get("ID")), duration, amplifier);

				if(typeMap.containsKey("delay"))
					potPart.setDelay(Integer.parseInt(typeMap.get("delay")));

				List<IRewardType> itemTypes = rewards.get("effect");
				if(itemTypes == null)
				{
					itemTypes = new ArrayList<IRewardType>();
					rewards.put("effect", itemTypes);
				}

				itemTypes.add(new PotionRewardType(potPart));
				break;
			}
			case "difficulty":
			{
				//TODO
				List<IRewardType> itemTypes = rewards.get("difficulty");
				if(itemTypes == null)
				{
					itemTypes = new ArrayList<IRewardType>();
					rewards.put("difficulty", itemTypes);
				}

				itemTypes.add(new MessageRewardType(new MessagePart("Not Implemented Yet!")));
				break;
			}
			case "explosion":
			{
				//TODO
				List<IRewardType> itemTypes = rewards.get("explosion");
				if(itemTypes == null)
				{
					itemTypes = new ArrayList<IRewardType>();
					rewards.put("explosion", itemTypes);
				}

				itemTypes.add(new MessageRewardType(new MessagePart("Not Implemented Yet!")));
				break;
			}
			case "fill":
			{
				//TODO
				List<IRewardType> itemTypes = rewards.get("fill");
				if(itemTypes == null)
				{
					itemTypes = new ArrayList<IRewardType>();
					rewards.put("fill", itemTypes);
				}

				itemTypes.add(new MessageRewardType(new MessagePart("Not Implemented Yet!")));
				break;
			}
			case "message":
			{
				MessagePart message = new MessagePart(typeMap.get("ID"));

				if(typeMap.containsKey("delay"))
					message.setDelay(Integer.parseInt(typeMap.get("delay")));

				List<IRewardType> itemTypes = rewards.get("message");
				if(itemTypes == null)
				{
					itemTypes = new ArrayList<IRewardType>();
					rewards.put("message", itemTypes);
				}

				itemTypes.add(new MessageRewardType(message));
				break;
			}
			case "particle":
			{
				ParticlePart particle = new ParticlePart(typeMap.get("ID"));

				if(typeMap.containsKey("delay"))
					particle.setDelay(Integer.parseInt(typeMap.get("delay")));

				List<IRewardType> itemTypes = rewards.get("particle");
				if(itemTypes == null)
				{
					itemTypes = new ArrayList<IRewardType>();
					rewards.put("particle", itemTypes);
				}

				itemTypes.add(new ParticleEffectRewardType(particle));
				break;
			}
			case "sound":
			{
				SoundPart sound = new SoundPart(SoundEvent.REGISTRY.getObject(new ResourceLocation(typeMap.get("ID"))));

				if(typeMap.containsKey("volume"))
					sound.setVolume(Float.parseFloat(typeMap.get("volume")));
				if(typeMap.containsKey("pitch"))
					sound.setPitch(Float.parseFloat(typeMap.get("pitch")));

				if(typeMap.containsKey("delay"))
					sound.setDelay(Integer.parseInt(typeMap.get("delay")));

				List<IRewardType> itemTypes = rewards.get("sound");
				if(itemTypes == null)
				{
					itemTypes = new ArrayList<IRewardType>();
					rewards.put("sound", itemTypes);
				}

				itemTypes.add(new SoundRewardType(sound));
				break;
			}
			case "time":
			{
				//TODO
				List<IRewardType> itemTypes = rewards.get("time");
				if(itemTypes == null)
				{
					itemTypes = new ArrayList<IRewardType>();
					rewards.put("time", itemTypes);
				}

				itemTypes.add(new MessageRewardType(new MessagePart("Not Implemented Yet!")));
				break;
			}
			case "nothing":
			{
				List<IRewardType> itemTypes = rewards.get("nothing");
				if(itemTypes == null)
				{
					itemTypes = new ArrayList<IRewardType>();
					rewards.put("nothing", itemTypes);
				}

				itemTypes.add(new MessageRewardType(new MessagePart("Nothing!")));
				break;
			}
		}
	}

	public String parseNextKey(String s)
	{
		int index = 0;
		while(s.charAt(index) != '=')
			index++;
		return s.substring(0, index);
	}

	public String parseKeyValue(String key, String s)
	{
		Map<Character, Character> matches = new HashMap<>();
		matches.put(')', '(');
		matches.put(']', '[');
		matches.put('}', '{');
		int escaped = 0;
		List<Character> stack = new ArrayList<Character>();
		int index = 0;
		char currentChar = s.charAt(index);
		do
		{
			if(matches.values().contains(currentChar))
			{
				stack.add(currentChar);
			}
			else if(matches.keySet().contains(currentChar))
			{
				if(stack.get(stack.size() - 1) == matches.get(currentChar))
				{
					stack.remove(stack.size() - 1);
				}
				else
				{
					System.out.println("Error at index: " + index + "! Closing \"" + currentChar + "\", but not next in stack!");
					// ERROR!
				}
			}
			else if(currentChar == '"')
			{
				if(escaped == 1)
				{
					escaped--;
					continue;
				}
				if(stack.size() > 0 && stack.get(stack.size() - 1) == '"')
					stack.remove(stack.size() - 1);
				else
					stack.add(currentChar);
			}
			else if(currentChar == '\\')
			{
				if(escaped == 0)
					escaped = 2;
			}

			index++;
			if(index == s.length())
				break;
			currentChar = s.charAt(index);
			if(escaped > 0)
				escaped--;
		} while(stack.size() > 0 || (currentChar != ',' && currentChar != '@'));
		return s.substring(0, index);
	}

	public String convertLBNBTToMCNBT(String in)
	{
		return in.replace("=", ":").replace("(", "{").replace(")", "}");
	}
}

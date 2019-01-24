package chanceCubes.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
import chanceCubes.rewards.variableTypes.FloatVar;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.StringVar;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.SchematicUtil;
import net.minecraft.block.Block;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class LuckyBlockRewardLoader extends BaseLoader
{
	public static LuckyBlockRewardLoader instance;
	private static Map<String, String> staticHashVars = new HashMap<>();

	static
	{
		staticHashVars.put("luckyHelmetEnchantments", "[{id:0,lvl:4},{id:1,lvl:4},{id:3,lvl:4},{id:4,lvl:4},{id:5,lvl:3},{id:6,lvl:1},{id:34,lvl:3}]");
		staticHashVars.put("luckyChestplateEnchantments", "[{id:0,lvl:4},{id:1,lvl:4},{id:3,lvl:4},{id:4,lvl:4},{id:7,lvl:3},{id:34,lvl:3}]");
		staticHashVars.put("luckyLeggingsEnchantments", "[{id:0,lvl:4},{id:1,lvl:4},{id:3,lvl:4},{id:4,lvl:4},{id:7,lvl:3},{id:34,lvl:3}]");
		staticHashVars.put("luckyBootsEnchantments", "[{id:0,lvl:4},{id:1,lvl:4},{id:2,lvl:4},{id:3,lvl:4},{id:4,lvl:4},{id:7,lvl:3},{id:34,lvl:3}]");
		staticHashVars.put("luckySwordEnchantments", "[{id:16,lvl:5},{id:17,lvl:5},{id:18,lvl:5},{id:19,lvl:2},{id:20,lvl:2},{id:21,lvl:3},{id:34,lvl:3}]");
		staticHashVars.put("luckyAxeEnchantments", "[{id:16,lvl:5},{id:17,lvl:5},{id:18,lvl:5},{id:32,lvl:5},{id:34,lvl:3},{id:35,lvl:3}]");
		staticHashVars.put("luckyBowEnchantments", "[{id:34,lvl:3},{id:48,lvl:5},{id:49,lvl:2},{id:50,lvl:1},{id:51,lvl:1}]");
	}

	private File lbFolder;

	public LuckyBlockRewardLoader(File folder)
	{
		instance = this;
		lbFolder = folder;
	}

	public void parseLuckyBlockRewards()
	{
		System.out.println(lbFolder);
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

	public void parseDropsFile(String fileName, InputStream stream) throws IOException
	{
		int rewardNumber = 0;
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder multiLine = new StringBuilder();
		String line;
		while((line = reader.readLine()) != null)
		{
			currentParsingReward = fileName + "_" + rewardNumber;
			lineNumber++;
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

			String chance = getAtValue(line, "chance");
			String luck = getAtValue(line, "luck");
			if(luck.isEmpty())
				luck = "0";

			int endindex = 0;

			if(line.lastIndexOf("@luck") > 0)
				endindex = line.lastIndexOf("@luck");

			if(line.lastIndexOf("@chance") > 0)
				endindex = Math.min(endindex, line.lastIndexOf("@chance"));

			line = line.substring(0, endindex);

			line = replaceStaticPlaceHolders(line);

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
				currentParsingPart = key;
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

			BasicReward reward = new BasicReward(fileName + "_" + rewardNumber, Integer.parseInt(luck) * 50, rewardTypes.toArray(new IRewardType[rewards.size()]));
			System.out.println(reward.getName() + " @ " + reward.getChanceValue());
			ChanceCubeRegistry.INSTANCE.registerReward(reward);
			rewardNumber++;
		}
	}

	public String getAtValue(String in, String key)
	{
		int index = in.lastIndexOf("@" + key);
		if(index != -1)
		{
			int indexNextAt = in.indexOf("@", index + 1);
			if(indexNextAt != -1)
				return in.substring(index + 6, indexNextAt);
			else
				return in.substring(index + 6);
		}
		return "";
	}

	private String replaceStaticPlaceHolders(String line)
	{
		int index = line.indexOf("#");
		String s;
		while(index != -1)
		{
			s = line.substring(index + 1);
			s = this.parseStringPart(s, true, Arrays.asList(':', ','));
			if(staticHashVars.keySet().contains(s))
			{
				line = line.replace("#" + s, staticHashVars.get(s));
			}
			else
			{
				if(s.startsWith("randList("))
				{
					s = s.substring(s.indexOf("("));
					s = this.parseStringPart(s, true, new ArrayList<Character>());
					line = line.substring(0, (index + s.length() + 7)) + "]";
					line = line.replace("#randList(", "[");
				}
				else if(s.startsWith("rand("))
				{
					line = line.replace("#rand", "RND");
				}
			}
			index = line.indexOf("#", index + 1);
		}

		return line;
	}

	public void parseGroup(String in, Map<String, List<IRewardType>> rewards)
	{
		if(in.contains("group:"))
			in = in.replaceFirst(":.*:", "");
		in = in.substring(6).trim();
		String item;
		while(!in.isEmpty())
		{
			item = this.parseStringPart(in, true, Arrays.asList(';'));
			if(item.startsWith("group"))
				parseGroup(item, rewards);
			else
				parseItem(item, rewards);
			in = in.substring(Math.min(item.length() + 1, in.length()));
		}

	}

	public void parseItem(String in, Map<String, List<IRewardType>> rewards)
	{
		StringBuilder builder = new StringBuilder();
		Map<String, String> typeMap = new HashMap<String, String>();
		while(!in.isEmpty())
		{
			String key = parseNextKey(in);
			in = in.substring(Math.min(key.length() + 1, in.length()));
			String value = parseStringPart(in, false, Arrays.asList(','));
			in = in.substring(Math.min(value.length() + 1, in.length())).trim();
			typeMap.put(key, value);
		}

		String type = "item";
		if(typeMap.containsKey("type"))
			type = typeMap.get("type");

		switch(type)
		{
			case "item":
			{
				String item = typeMap.get("ID");

				builder.setLength(0);
				builder.append("{");

				if(typeMap.containsKey("damage"))
				{
					builder.append("damage:");
					builder.append(typeMap.get("damage"));
					builder.append(",");
				}

				if(typeMap.containsKey("amount"))
				{
					builder.append("Count:");
					builder.append(typeMap.get("amount"));
					builder.append(",");
				}

				if(typeMap.containsKey("NBTTag"))
				{
					builder.append("tag:");
					builder.append(convertLBNBTToMCNBT(typeMap.get("NBTTag")));
					builder.append(",");
				}

				if(builder.charAt(builder.length() - 1) != '{')
					builder.setLength(builder.length() - 1);

				ItemPart itemPart = new ItemPart(item, builder.toString());

				if(typeMap.containsKey("delay"))
					itemPart.setDelay(super.getInt(parseLBDelay(typeMap.get("delay")), 0));

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
				String[] blockDataParts = typeMap.get("ID").split(":");
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
					osb.setCausesBlockUpdate(super.getBoolean(typeMap.get("blockUpdate"), false));

				if(typeMap.containsKey("delay"))
					osb.setDelay(super.getInt(parseLBDelay(typeMap.get("delay")), 0));

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
				String nbt = "";

				if(typeMap.containsKey("NBTTag"))
					nbt = convertLBNBTToMCNBT(typeMap.get("NBTTag"));

				if(nbt.isEmpty())
					nbt = "{}";

				builder.setLength(0);
				builder.append(nbt.substring(0, 1));
				builder.append("id:\"");
				builder.append(typeMap.get("ID"));
				builder.append("\"");
				if(nbt.charAt(1) != '}')
					builder.append(",");
				builder.append(nbt.substring(1));

				EntityPart entPart = new EntityPart(super.getNBT(builder.toString()));

				if(typeMap.containsKey("delay"))
					entPart.setDelay(super.getInt(parseLBDelay(typeMap.get("delay")), 0));

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
				StringVar commandString = super.getString(typeMap.get("ID"), "/help");
				CommandPart command = new CommandPart(commandString);

				if(typeMap.containsKey("delay"))
					command.setDelay(super.getInt(parseLBDelay(typeMap.get("delay")), 0));

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
				IntVar duration = new IntVar(30);
				if(typeMap.containsKey("duration"))
					duration = super.getInt(typeMap.get("duration"), 30);
				IntVar amplifier = new IntVar(0);
				if(typeMap.containsKey("amplifier"))
					duration = super.getInt(typeMap.get("amplifier"), 0);

				PotionPart potPart = new PotionPart(super.getInt(typeMap.get("ID"), 1), duration, amplifier);

				if(typeMap.containsKey("delay"))
					potPart.setDelay(super.getInt(parseLBDelay(typeMap.get("delay")), 0));

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
				MessagePart message = new MessagePart(super.getString(typeMap.get("ID"), "Hmmm Broken...."));

				if(typeMap.containsKey("delay"))
					message.setDelay(super.getInt(parseLBDelay(typeMap.get("delay")), 0));

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
				ParticlePart particle = new ParticlePart(this.getString(typeMap.get("ID"), "explode"));

				if(typeMap.containsKey("delay"))
					particle.setDelay(super.getInt(parseLBDelay(typeMap.get("delay")), 0));

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
					sound.setVolume(super.getFloat(typeMap.get("volume"), 1f));
				if(typeMap.containsKey("pitch"))
					sound.setPitch(super.getFloat(typeMap.get("pitch"), 1f));

				if(typeMap.containsKey("delay"))
					sound.setDelay(super.getInt(parseLBDelay(typeMap.get("delay")), 0));

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

	public String parseStringPart(String s, boolean ignoreStackOverflow, List<Character> endChars)
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
				if(stack.size() > 0 && stack.get(stack.size() - 1) == matches.get(currentChar))
				{
					stack.remove(stack.size() - 1);
				}
				else
				{
					if(!ignoreStackOverflow)
					{
						//CCubesCore.logger.log(Level.ERROR, "Error at: " + s.substring(Math.max(index - 20, 0), Math.min(index + 1, s.length())) + "<-[HERE]! Closing \"" + currentChar + "\", but not next in stack (Expecting " + (stack.size() > 0 ? stack.get(stack.size() - 1) : "Nothing") + ")!");
						System.out.println("Error on line " + lineNumber + ": " + s.substring(Math.max(index - 20, 0), Math.min(index + 1, s.length())) + "<-[HERE]! Closing \"" + currentChar + "\", but not next in stack (" + (stack.size() > 0 ? "Expecting " + stack.get(stack.size() - 1) : "Too many") + ")!");
						// ERROR!
					}
					break;
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
		} while(stack.size() > 0 || (endChars.size() > 0 && !endChars.contains(currentChar)));
		return s.substring(0, index);
	}

	public String convertLBNBTToMCNBT(String in)
	{
		return in.replace("=", ":").replaceAll("[^D]\\(", "{").replace(")", "}");
	}

	public String parseLBDelay(String delay)
	{
		if(FloatVar.isFloat(delay))
			return String.valueOf(Math.round(20 * Float.parseFloat(delay)));
		return delay;
	}
}

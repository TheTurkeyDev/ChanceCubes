package chanceCubes.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.defaultRewards.BasicReward;
import chanceCubes.rewards.rewardparts.BasePart;
import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.rewards.rewardparts.EffectPart;
import chanceCubes.rewards.rewardparts.EntityPart;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.MessagePart;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.rewardparts.ParticlePart;
import chanceCubes.rewards.rewardparts.SoundPart;
import chanceCubes.rewards.type.BlockRewardType;
import chanceCubes.rewards.type.CommandRewardType;
import chanceCubes.rewards.type.EffectRewardType;
import chanceCubes.rewards.type.EntityRewardType;
import chanceCubes.rewards.type.IRewardType;
import chanceCubes.rewards.type.ItemRewardType;
import chanceCubes.rewards.type.MessageRewardType;
import chanceCubes.rewards.type.ParticleEffectRewardType;
import chanceCubes.rewards.type.SoundRewardType;
import chanceCubes.rewards.variableTypes.FloatVar;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.StringVar;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.SchematicUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class LuckyBlockRewardLoader extends BaseLoader
{
	public static LuckyBlockRewardLoader instance;
	private static Map<String, String> staticHashVars = new HashMap<>();

	private static Map<String, InputStream> files = new HashMap<>();

	private static Map<String, LBStructure> structures = new HashMap<>();

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
		for(File f : lbFolder.listFiles())
		{
			if(f.getName().contains(".zip"))
			{
				CCubesCore.logger.log(Level.INFO, "Loading Lucky Blocks rewards file " + f.getName());
				String rewardPackName = f.getName().substring(0, f.getName().indexOf("."));
				try
				{
					ZipFile zipFile = new ZipFile(f);

					Enumeration<? extends ZipEntry> entries = zipFile.entries();

					while(entries.hasMoreElements())
					{
						ZipEntry entry = entries.nextElement();
						InputStream stream = zipFile.getInputStream(entry);
						files.put(entry.getName(), stream);
					}

					zipFile.close();

					InputStream stream;
					if(files.containsKey("structures.txt"))
					{
						stream = files.get("structures.txt");
						parseStructuresFile(rewardPackName, files.get("structures.txt"));
						stream.close();
					}

					stream = files.get("drops.txt");
					parseDropsFile(rewardPackName, files.get("drops.txt"));
					stream.close();

				} catch(Exception e)
				{
					e.printStackTrace();
				}

				CCubesCore.logger.log(Level.INFO, "Loaded Lucky Blocks rewards file " + f.getName());
			}
		}
	}

	public void parseStructuresFile(String rewardPackName, InputStream stream) throws IOException
	{
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

			LBStructure structure = new LBStructure();
			String[] lineArgs = line.split(",");
			for(String arg : lineArgs)
			{
				String[] parts = arg.split("=");
				if(parts[0].equalsIgnoreCase("ID"))
					structure.id = parts[1];
				else if(parts[0].equalsIgnoreCase("file"))
					structure.file = parts[1];
				else if(parts[0].equalsIgnoreCase("centerX"))
					structure.xOff = this.getInt(parts[1], 0).getIntValue();
				else if(parts[0].equalsIgnoreCase("centerY"))
					structure.yOff = this.getInt(parts[1], 0).getIntValue();
				else if(parts[0].equalsIgnoreCase("centerZ"))
					structure.zOff = this.getInt(parts[1], 0).getIntValue();
				else if(parts[0].equalsIgnoreCase("blockMode"))
					structure.mode = parts[1];
				else if(parts[0].equalsIgnoreCase("blockUpdate"))
					structure.blockUpdate = this.getBoolean(parts[1], false).getBoolValue();
				else if(parts[0].equalsIgnoreCase("overlayStruct"))
					structure.overlayStruct = parts[1];
			}

			structures.put(structure.id, structure);
		}
	}

	public void parseDropsFile(String rewardPackName, InputStream stream) throws IOException
	{
		int rewardNumber = 0;
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder multiLine = new StringBuilder();
		String line;
		while((line = reader.readLine()) != null)
		{
			currentParsingReward = rewardPackName + "_" + rewardNumber;
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

			line = replaceStaticPlaceHolders(line, true);

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
							parts.addAll(Arrays.asList(((EffectRewardType) type).getRewardParts()));
						rewardTypes.add(new EffectRewardType(parts.toArray(new EffectPart[0])));
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

			BasicReward reward = new BasicReward(rewardPackName + "_" + rewardNumber, Integer.parseInt(luck) * 50, rewardTypes.toArray(new IRewardType[rewards.size()]));
			System.out.println(reward.getName() + " @ " + reward.getChanceValue());
			ChanceCubeRegistry.INSTANCE.registerReward(reward);
			ChanceCubeRegistry.INSTANCE.addCustomReward(reward);
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

	private String replaceStaticPlaceHolders(String line, boolean topLevel)
	{
		int index = line.indexOf("#");
		String s;
		while(index != -1)
		{
			s = line.substring(index + 1);

			if(s.startsWith("randList("))
			{
				s = this.parseStringPart(s.substring(8), true, new ArrayList<Character>());
				s = "randList" + s;
				String inner = s.substring(9, s.length() - 1);
				inner = replaceStaticPlaceHolders(inner, false);
				String replace = "[" + inner + "]";
				if(topLevel)
					replace = "%%" + replace + "%%";
				line = line.replace("#" + s, replace);
			}
			else if(s.startsWith("rand("))
			{
				s = this.parseStringPart(s.substring(4), true, new ArrayList<Character>());
				s = "rand" + s;
				String inner = s.substring(5, s.length() - 1);
				inner = replaceStaticPlaceHolders(inner, false);
				String replace = "RND(" + inner + ")";
				if(topLevel)
					replace = "%%" + replace + "%%";
				line = line.replace("#" + s, replace);
			}
			else if(staticHashVars.keySet().contains(s))
			{
				s = this.parseStringPart(s, true, Arrays.asList(':', ','));
				line = line.replace("#" + s, staticHashVars.get(s));
			}
			index = line.indexOf("#", index + 1);
		}

		Pattern p = Pattern.compile("[^']\\$[^']");
		Matcher m = p.matcher(line);
		while(m.find())
		{
			String s1 = m.group(0);
			//TODO: Java not liking the § character?
			line = line.replace(s1, s1.replace("$", "§"));
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

				String count = "1";
				if(typeMap.containsKey("amount"))
					count = typeMap.get("amount");

				builder.append("Count:");
				builder.append(count);
				builder.append(",");

				if(typeMap.containsKey("NBTTag"))
				{
					builder.append("tag:");
					builder.append(convertLBNBTToMCNBT(typeMap.get("NBTTag")));
				}

				if(builder.charAt(builder.length() - 1) == ',')
					builder.setLength(builder.length() - 1);

				builder.append("}");

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
					osb.setBlockState(RewardsUtil.getBlockStateFromBlockMeta(block, this.getInt(typeMap.get("meta"), 0).getIntValue()));
				if(typeMap.containsKey("damage"))
					osb.setBlockState(RewardsUtil.getBlockStateFromBlockMeta(block, this.getInt(typeMap.get("damage"), 0).getIntValue()));
				if(typeMap.containsKey("state"))
					osb.setBlockState(RewardsUtil.getBlockStateFromBlockMeta(block, this.getInt(typeMap.get("state"), 0).getIntValue()));

				if(typeMap.containsKey("blockUpdate"))
					osb.setCausesBlockUpdate(super.getBoolean(typeMap.get("blockUpdate"), false));

				if(typeMap.containsKey("delay"))
					osb.setDelay(super.getInt(parseLBDelay(typeMap.get("delay")), 0));

				if(typeMap.containsKey("tileEntity"))
					osb = SchematicUtil.OffsetBlockToTileEntity(osb, this.getNBT(convertLBNBTToMCNBT(typeMap.get("tileEntity"))));
				if(typeMap.containsKey("NBTTag"))
					osb = SchematicUtil.OffsetBlockToTileEntity(osb, this.getNBT(convertLBNBTToMCNBT(typeMap.get("NBTTag"))));

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

				EntityPart entPart = new EntityPart(super.getNBT(builder.toString()), new IntVar(0));

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
				String id = typeMap.get("ID");
				LBStructure struct = structures.get(id);
				
				//TODO: Continue
				
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

				List<IRewardType> commandTypes = rewards.get("command");
				if(commandTypes == null)
				{
					commandTypes = new ArrayList<IRewardType>();
					rewards.put("command", commandTypes);
				}

				commandTypes.add(new CommandRewardType(command));
				break;
			}
			case "effect":
			{
				IntVar duration = new IntVar(30);
				if(typeMap.containsKey("duration"))
					duration = super.getInt(typeMap.get("duration"), 30);
				IntVar amplifier = new IntVar(0);
				if(typeMap.containsKey("amplifier"))
					duration = super.getInt(typeMap.get("amplifier"), 0);

				EffectPart effectPart = new EffectPart(super.getString(typeMap.get("ID"), "1"), duration, amplifier);

				if(typeMap.containsKey("delay"))
					effectPart.setDelay(super.getInt(parseLBDelay(typeMap.get("delay")), 0));

				List<IRewardType> effectTypes = rewards.get("effect");
				if(effectTypes == null)
				{
					effectTypes = new ArrayList<IRewardType>();
					rewards.put("effect", effectTypes);
				}

				effectTypes.add(new EffectRewardType(effectPart));
				break;
			}
			case "difficulty":
			{
				//TODO
				List<IRewardType> difficultyTypes = rewards.get("difficulty");
				if(difficultyTypes == null)
				{
					difficultyTypes = new ArrayList<IRewardType>();
					rewards.put("difficulty", difficultyTypes);
				}

				difficultyTypes.add(new MessageRewardType(new MessagePart("Not Implemented Yet!")));
				break;
			}
			case "explosion":
			{
				//TODO
				List<IRewardType> explosionTypes = rewards.get("explosion");
				if(explosionTypes == null)
				{
					explosionTypes = new ArrayList<IRewardType>();
					rewards.put("explosion", explosionTypes);
				}

				explosionTypes.add(new MessageRewardType(new MessagePart("Not Implemented Yet!")));
				break;
			}
			case "fill":
			{
				int meta = 0;
				if(typeMap.containsKey("meta"))
					meta = Integer.parseInt(typeMap.get("meta"));
				if(typeMap.containsKey("damage"))
					meta = Integer.parseInt(typeMap.get("meta"));
				if(typeMap.containsKey("state"))
					meta = Integer.parseInt(typeMap.get("meta"));

				IBlockState block = Blocks.AIR.getDefaultState();
				String blockID = typeMap.get("ID");
				if(IntVar.isInteger(blockID))
					block = Block.getBlockById(Integer.parseInt(blockID)).getStateFromMeta(meta);
				else
					block = Block.getBlockFromName(blockID).getStateFromMeta(meta);

				int length = 1;
				int width = 1;
				int height = 1;

				if(typeMap.containsKey("length"))
					length = this.getInt(typeMap.get("length"), 1).getIntValue();
				if(typeMap.containsKey("width"))
					width = this.getInt(typeMap.get("width"), 1).getIntValue();
				if(typeMap.containsKey("height"))
					height = this.getInt(typeMap.get("height"), 1).getIntValue();

				if(typeMap.containsKey("size"))
				{
					String[] line = typeMap.get("size").replace("(", "").replace(")", "").split(",");
					length = this.getInt(line[0], 1).getIntValue();
					width = this.getInt(line[1], 1).getIntValue();
					height = this.getInt(line[2], 1).getIntValue();
				}

				List<OffsetBlock> blocks = new ArrayList<>();

				for(int x = 0; x < length; x++)
				{
					for(int y = 0; y < height; y++)
					{
						for(int z = 0; z < width; z++)
						{
							blocks.add(new OffsetBlock(x, y, z, block, false));
						}
					}
				}

				//TODO

				List<IRewardType> fillTypes = rewards.get("fill");
				if(fillTypes == null)
				{
					fillTypes = new ArrayList<IRewardType>();
					rewards.put("fill", fillTypes);
				}

				fillTypes.add(new BlockRewardType(blocks.toArray(new OffsetBlock[0])));
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
		String newString = in.replaceAll("=\\(", ":{").replaceAll("\\[\\(", "[{").replace("=", ":").replace(")", "}");
		if(newString.startsWith("("))
			newString = "{" + newString.substring(1);
		return newString;
	}

	public String parseLBDelay(String delay)
	{
		if(FloatVar.isFloat(delay))
			return String.valueOf(Math.round(20 * Float.parseFloat(delay)));
		return delay;
	}

	private class LBStructure
	{
		public String id = "";
		public String file = "";
		public int xOff = 0, yOff = 0, zOff = 0;
		public String mode = "replace";
		public boolean blockUpdate = false;
		public String overlayStruct = "";
	}
}

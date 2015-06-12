package chanceCubes.config;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.BasicReward;
import chanceCubes.rewards.type.BlockRewardType;
import chanceCubes.rewards.type.CommandRewardType;
import chanceCubes.rewards.type.EntityRewardType;
import chanceCubes.rewards.type.ExperienceRewardType;
import chanceCubes.rewards.type.IRewardType;
import chanceCubes.rewards.type.ItemRewardType;
import chanceCubes.rewards.type.MessageRewardType;
import chanceCubes.rewards.type.PotionRewardType;
import chanceCubes.util.OffsetBlock;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cpw.mods.fml.common.registry.GameRegistry;

public class CustomRewardsLoader
{
	public static CustomRewardsLoader instance;
	private File folder;
	private static JsonParser json;

	public CustomRewardsLoader(File folder)
	{
		instance = this;
		this.folder = folder;
		json = new JsonParser();
	}

	public void loadCustomRewards()
	{
		for(File f: folder.listFiles())
		{
			if(f.getName().substring(f.getName().indexOf(".")).equalsIgnoreCase(".json"))
			{
				JsonElement  fileJson;
				try{
					CCubesCore.logger.log(Level.INFO,"Loading custom rewards file " + f.getName());
					fileJson = json.parse(new FileReader(f));
				}catch(Exception e){
					CCubesCore.logger.log(Level.ERROR, "Unable to parse the file " + f.getName() + ". Skipping file loading.");
					continue;
				}

				for(Entry<String, JsonElement> reward :fileJson.getAsJsonObject().entrySet())
				{
					List<IRewardType> rewards = new ArrayList<IRewardType>();
					JsonObject rewardElements = reward.getValue().getAsJsonObject();
					int chance = 0;
					for(Entry<String, JsonElement> rewardElement : rewardElements.entrySet())
					{
						if(rewardElement.getKey().equalsIgnoreCase("chance"))
						{
							chance = rewardElement.getValue().getAsInt();
							continue;
						}

						JsonArray rewardTypes = rewardElement.getValue().getAsJsonArray();
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
					}

					ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":" + reward.getKey(), chance, rewards.toArray(new IRewardType[rewards.size()])));
				}
				CCubesCore.logger.log(Level.INFO,"Loaded custom rewards file " + f.getName());
			}
		}
	}

	public List<IRewardType> loadItemReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		for(JsonElement element : rawReward)
		{
			String itemInfo = element.getAsJsonObject().get("id").getAsString();
			if(itemInfo.contains(":"))
			{
				String mod = itemInfo.substring(0, itemInfo.indexOf(":"));
				String name = itemInfo.substring(itemInfo.indexOf(":") + 1);
				int id = Item.getIdFromItem(GameRegistry.findItem(mod, name));
				element.getAsJsonObject().addProperty("id", id);
			}
			try
			{
				String jsonEdited = this.removedKeyQuotes(element.toString());
				NBTBase nbtbase = JsonToNBT.func_150315_a(jsonEdited);

				if (!(nbtbase instanceof NBTTagCompound))
				{
					CCubesCore.logger.log(Level.ERROR, "Failed to convert the JSON to NBT for: " + element.toString());
				}
				else
				{
					ItemStack stack = ItemStack.loadItemStackFromNBT((NBTTagCompound) nbtbase);
					if(stack == null)
						CCubesCore.logger.log(Level.ERROR, "Failed to create an itemstack from the JSON of: " + jsonEdited + " and the NBT of: " + ((NBTTagCompound) nbtbase).toString());
					else
						rewards.add(new ItemRewardType(stack));
				}
			} catch (NBTException e1)
			{
				CCubesCore.logger.log(Level.ERROR, e1.getMessage());
			}
		}
		return rewards;
	}
	
	public List<IRewardType> loadBlockReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<OffsetBlock> blocks = new ArrayList<OffsetBlock>();
		for(JsonElement element : rawReward)
		{
			int x = element.getAsJsonObject().get("XOffSet").getAsInt();
			int y = element.getAsJsonObject().get("YOffSet").getAsInt();
			int z = element.getAsJsonObject().get("ZOffSet").getAsInt();
			String mod = element.getAsJsonObject().get("Block").getAsString().substring(0, element.getAsJsonObject().get("Block").getAsString().indexOf(":"));
			String blockName = element.getAsJsonObject().get("Block").getAsString().substring(element.getAsJsonObject().get("Block").getAsString().indexOf(":")+1);
			Block block = GameRegistry.findBlock(mod, blockName);
			boolean falling = element.getAsJsonObject().get("Falling").getAsBoolean();
			blocks.add(new OffsetBlock(x,y,z,block,falling));
		}
		rewards.add(new BlockRewardType(blocks.toArray(new OffsetBlock[blocks.size()])));
		return rewards;
	}

	public List<IRewardType> loadMessageReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<String> msgs = new ArrayList<String>();
		for(JsonElement element : rawReward)
			msgs.add(element.getAsJsonObject().get("message").getAsString());
		rewards.add(new MessageRewardType(msgs.toArray(new String[msgs.size()])));
		return rewards;
	}

	public List<IRewardType> loadCommandReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<String> cmds = new ArrayList<String>();
		for(JsonElement element : rawReward)
			cmds.add(element.getAsJsonObject().get("command").getAsString());
		rewards.add(new CommandRewardType(cmds.toArray(new String[cmds.size()])));
		return rewards;
	}

	public List<IRewardType> loadEntityReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		for(JsonElement element : rawReward)
		{
			try
			{
				String jsonEdited = this.removedKeyQuotes(element.toString());
				NBTBase nbtbase = JsonToNBT.func_150315_a(jsonEdited);

				if (!(nbtbase instanceof NBTTagCompound))
				{
					CCubesCore.logger.log(Level.ERROR, "Failed to convert the JSON to NBT for: " + element.toString());
				}
				else
				{
					rewards.add(new EntityRewardType((NBTTagCompound)nbtbase));
				}
			} catch (NBTException e1)
			{
				CCubesCore.logger.log(Level.ERROR, e1.getMessage());
			}
		}
		return rewards;
	}

	public List<IRewardType> loadExperienceReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<Integer> exp = new ArrayList<Integer>();
		for(JsonElement element : rawReward)
			exp.add(element.getAsJsonObject().get("experienceAmount").getAsInt());
		rewards.add(new ExperienceRewardType(exp.toArray(new Integer[exp.size()])));
		return rewards;
	}

	public List<IRewardType> loadPotionReward(JsonArray rawReward, List<IRewardType> rewards)
	{
		List<PotionEffect> potions = new ArrayList<PotionEffect>();
		for(JsonElement element : rawReward)
			potions.add(new PotionEffect(element.getAsJsonObject().get("potionid").getAsInt(), element.getAsJsonObject().get("duration").getAsInt() * 20));
		rewards.add(new PotionRewardType(potions.toArray(new PotionEffect[potions.size()])));
		return rewards;
	}	





	public String removedKeyQuotes(String raw)
	{
		StringBuilder sb = new StringBuilder(raw.toString());
		int index = 0;
		while((index = sb.indexOf("\"", index)) != -1)
		{
			int secondQuote = sb.indexOf("\"", index+1);
			if(secondQuote == -1)
				break;
			if(sb.charAt(secondQuote+1) == ':')
			{
				sb.deleteCharAt(index);
				sb.delete(secondQuote-1, secondQuote);
				index = secondQuote;
			}
			else
				index++;
		}
		return sb.toString();
	}
}
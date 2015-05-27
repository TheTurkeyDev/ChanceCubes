package chanceCubes.config;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.BasicReward;
import chanceCubes.rewards.type.IRewardType;
import chanceCubes.rewards.type.ItemRewardType;

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
					for(Entry<String, JsonElement> rewardElement : rewardElements.entrySet())
					{
						JsonArray rewardTypes = rewardElement.getValue().getAsJsonArray();
						if(rewardElement.getKey().equalsIgnoreCase("Item"))
							this.loadItemReward(rewardTypes, rewards);
					}
					ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":" + reward.getKey(), 0, rewards.toArray(new IRewardType[rewards.size()])));
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
					System.out.println(((NBTTagCompound) nbtbase).hasKey("id"));
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
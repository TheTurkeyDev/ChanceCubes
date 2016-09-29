package chanceCubes.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import chanceCubes.config.ConfigLoader;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.rewardparts.OffsetTileEntity;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class SchematicUtil
{
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static Location3I[] selectionPoints = new Location3I[2];

	public static void createCustomSchematic(World world, Location3I loc1, Location3I loc2, String fileName)
	{
		List<Integer> blocks = new ArrayList<Integer>();
		List<CustomEntry<Integer, String>> blockDataIds = new ArrayList<CustomEntry<Integer, String>>();
		List<CustomEntry<String, List<Integer>>> tileEntityData = new ArrayList<CustomEntry<String, List<Integer>>>();
		int largeX = loc1.getX() > loc2.getX() ? loc1.getX() : loc2.getX();
		int smallX = loc1.getX() < loc2.getX() ? loc1.getX() : loc2.getX();
		int largeY = loc1.getY() > loc2.getY() ? loc1.getY() : loc2.getY();
		int smallY = loc1.getY() < loc2.getY() ? loc1.getY() : loc2.getY();
		int largeZ = loc1.getZ() > loc2.getZ() ? loc1.getZ() : loc2.getZ();
		int smallZ = loc1.getZ() < loc2.getZ() ? loc1.getZ() : loc2.getZ();
		for(int y = smallY; y < largeY; y++)
		{
			for(int x = smallX; x < largeX; x++)
			{
				for(int z = smallZ; z < largeZ; z++)
				{
					Block b = world.getBlock(x, y, z);
					String blockData = GameData.getBlockRegistry().getNameForObject(b);
					blockData += ":" + world.getBlockMetadata(x, y, z);
					int id = -1;
					for(CustomEntry<Integer, String> data : blockDataIds)
					{
						if(blockData.equalsIgnoreCase(data.getValue()))
						{
							id = data.getKey();
						}
					}
					if(id == -1)
					{
						id = blockDataIds.size();
						blockDataIds.add(new CustomEntry<Integer, String>(id, blockData));
					}
					blocks.add(id);

					if(world.getTileEntity(x, y, z) != null)
					{
						TileEntity te = world.getTileEntity(x, y, z);
						NBTTagCompound nbt = new NBTTagCompound();
						te.writeToNBT(nbt);
						for(CustomEntry<String, List<Integer>> data : tileEntityData)
						{
							if(nbt.toString().equalsIgnoreCase(data.getKey()))
							{
								data.getValue().add(blocks.size() - 1);
								break;
							}
						}
						List<Integer> list = new ArrayList<Integer>();
						list.add(blocks.size() - 1);
						tileEntityData.add(new CustomEntry<String, List<Integer>>(nbt.toString(), list));
					}
				}
			}
		}

		JsonObject json = new JsonObject();

		JsonArray blockArray = new JsonArray();

		for(int i : blocks)
		{
			blockArray.add(new JsonPrimitive(i));
		}

		json.add("Blocks", blockArray);

		JsonArray blockDataArray = new JsonArray();
		for(CustomEntry<Integer, String> i : blockDataIds)
		{
			JsonObject index = new JsonObject();
			index.addProperty(i.getValue(), i.getKey());
			blockDataArray.add(index);
		}
		json.add("Block Data", blockDataArray);

		JsonArray tileEntityDataArray = new JsonArray();
		for(CustomEntry<String, List<Integer>> i : tileEntityData)
		{
			JsonObject index = new JsonObject();
			JsonArray tileEntityBlockIds = new JsonArray();
			for(int id : i.getValue())
				tileEntityBlockIds.add(new JsonPrimitive(id));
			index.add(i.getKey(), tileEntityBlockIds);
			tileEntityDataArray.add(index);
		}
		json.add("TileEntities", tileEntityDataArray);

		JsonObject info = new JsonObject();
		info.addProperty("xSize", largeX - smallX);
		info.addProperty("ySize", largeY - smallY);
		info.addProperty("zSize", largeZ - smallZ);
		json.add("Schematic Data", info);

		FileUtil.writeToFile(ConfigLoader.folder.getAbsolutePath() + "/CustomRewards/Schematics/" + fileName, gson.toJson(json));
	}

	public static CustomSchematic loadCustomSchematic(String file, int xOffSet, int yOffSet, int zOffSet, float delay, boolean falling, boolean relativeToPlayer)
	{
		JsonElement elem = FileUtil.readJsonfromFile(ConfigLoader.folder.getAbsolutePath() + "/CustomRewards/Schematics/" + file);
		if(elem == null)
			return null;
		JsonObject json = elem.getAsJsonObject();
		List<OffsetBlock> offsetBlocks = new ArrayList<OffsetBlock>();
		JsonObject info = json.get("Schematic Data").getAsJsonObject();
		int xSize = info.get("xSize").getAsInt();
		int ySize = info.get("ySize").getAsInt();
		int zSize = info.get("zSize").getAsInt();
		List<CustomEntry<Integer, String>> blockDataIds = new ArrayList<CustomEntry<Integer, String>>();

		JsonArray blockDataArray = json.get("Block Data").getAsJsonArray();
		for(JsonElement i : blockDataArray)
		{
			JsonObject index = i.getAsJsonObject();
			for(Entry<String, JsonElement> obj : index.entrySet())
				blockDataIds.add(new CustomEntry<Integer, String>(obj.getValue().getAsInt(), obj.getKey()));
		}

		float delayTotal = 0;
		int index = 0;
		JsonArray blockArray = json.get("Blocks").getAsJsonArray();
		for(int yOff = 0; yOff < ySize; yOff++)
		{
			for(int xOff = (xSize / 2) - xSize; xOff < (xSize / 2); xOff++)
			{
				for(int zOff = (zSize / 2) - zSize; zOff < (zSize / 2); zOff++)
				{
					JsonElement element = blockArray.get(index);
					int id = element.getAsInt();
					String blockData = "";
					for(CustomEntry<Integer, String> entry : blockDataIds)
					{
						if(entry.getKey() == id)
						{
							blockData = entry.getValue();
							break;
						}
					}
					String[] dataParts = blockData.split(":");
					Block b = GameRegistry.findBlock(dataParts[0], dataParts[1]);
					OffsetBlock osb = new OffsetBlock(xOff + xOffSet, yOff + yOffSet, zOff + zOffSet, b, falling, (int) delayTotal);
					osb.setData(Byte.parseByte(dataParts[2]));
					offsetBlocks.add(osb);
					if(!b.equals(Blocks.air))
						delayTotal += delay;
					index++;
				}
			}
		}

		JsonArray teArray = json.get("TileEntities").getAsJsonArray();
		for(JsonElement i : teArray)
		{
			for(Entry<String, JsonElement> obj : i.getAsJsonObject().entrySet())
			{
				String teData = obj.getKey();
				for(JsonElement ids : obj.getValue().getAsJsonArray())
				{
					int id = ids.getAsInt();
					OffsetBlock osb = offsetBlocks.get(id);
					OffsetTileEntity oste = OffsetBlockToTileEntity(osb, teData);
					if(oste != null)
						offsetBlocks.set(id, oste);
				}
			}
		}

		for(int i = offsetBlocks.size() - 1; i >= 0; i--)
		{
			OffsetBlock osb = offsetBlocks.get(i);
			if(osb.getBlock().equals(Blocks.air))
				offsetBlocks.remove(i);
		}

		return new CustomSchematic(offsetBlocks, xSize, ySize, zSize, relativeToPlayer);
	}

	public static OffsetTileEntity OffsetBlockToTileEntity(OffsetBlock osb, String nbt)
	{
		try
		{
			return OffsetBlockToTileEntity(osb, (NBTTagCompound) JsonToNBT.func_150315_a(nbt));
		} catch(NBTException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static OffsetTileEntity OffsetBlockToTileEntity(OffsetBlock osb, NBTTagCompound nbt)
	{
		OffsetTileEntity oste = new OffsetTileEntity(osb.xOff, osb.yOff, osb.zOff, osb.getBlock(), nbt, osb.isFalling(), osb.getDelay());
		oste.setData(osb.getData());
		oste.setDelay(osb.getDelay());
		oste.setRelativeToPlayer(osb.isRelativeToPlayer());
		oste.seFalling(osb.isFalling());
		return oste;
	}
}

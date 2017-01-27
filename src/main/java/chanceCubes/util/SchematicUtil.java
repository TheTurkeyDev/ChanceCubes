package chanceCubes.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SchematicUtil
{
	private static Gson gson = new GsonBuilder().create();

	public static BlockPos[] selectionPoints = new BlockPos[2];

	public static void createCustomSchematic(World world, BlockPos loc1, BlockPos loc2, String fileName)
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
					BlockPos pos = new BlockPos(x, y, z);
					IBlockState blockState = world.getBlockState(pos);
					String blockData = blockState.getBlock().getRegistryName().toString();
					// TODO: Find better way?
					blockData += ":" + blockState.getBlock().getMetaFromState(blockState);
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

					if(world.getTileEntity(pos) != null)
					{
						TileEntity te = world.getTileEntity(pos);
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

		int row = 0;
		int last = -1;
		for(int i : blocks)
		{
			if(last == i)
			{
				row++;
			}
			else
			{
				if(row != 0)
				{
					String value = "" + last;
					if(row != 1)
						value += "x" + row;
					blockArray.add(new JsonPrimitive(value));
				}
				last = i;
				row = 1;
			}

		}

		String value = "" + last;
		if(row != 1)
			value += "x" + row;
		blockArray.add(new JsonPrimitive(value));

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

	public static CustomSchematic loadLegacySchematic(String fileName, int xoff, int yoff, int zoff, float delay, boolean falling, boolean relativeToPlayer, boolean includeAirBlocks)
	{
		File schematic = new File(ConfigLoader.folder.getAbsolutePath() + "/CustomRewards/Schematics/" + fileName);
		NBTTagCompound nbtdata;
		try
		{
			FileInputStream is = new FileInputStream(schematic);
			nbtdata = CompressedStreamTools.readCompressed(is);
			is.close();
		} catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}

		short width = nbtdata.getShort("Width");
		short height = nbtdata.getShort("Height");
		short length = nbtdata.getShort("Length");

		byte[] blocks = nbtdata.getByteArray("Blocks");
		byte[] data = nbtdata.getByteArray("Data");
		List<OffsetBlock> offsetBlocks = new ArrayList<OffsetBlock>();

		NBTTagList tileentities = nbtdata.getTagList("TileEntities", 10);

		int i = 0;
		short halfLength = (short) (length / 2);
		short halfWidth = (short) (width / 2);

		for(int yy = 0; yy < height; yy++)
		{
			for(int zz = 0; zz < length; zz++)
			{
				for(int xx = 0; xx < width; xx++)
				{
					int j = blocks[i];
					if(j < 0)
						j = 128 + (128 + j);

					Block b = Block.getBlockById(j);
					if(b != Blocks.AIR)
					{
						OffsetBlock block = new OffsetBlock(halfWidth - xx, yy, halfLength - zz, b, falling);
						block.setRelativeToPlayer(relativeToPlayer);
						block.setBlockState(b.getStateFromMeta(data[i]));
						offsetBlocks.add(block);
					}
					i++;
				}
			}
		}

		if(tileentities != null)
		{
			for(int i1 = 0; i1 < tileentities.tagCount(); ++i1)
			{
				NBTTagCompound nbttagcompound4 = tileentities.getCompoundTagAt(i1);
				TileEntity tileentity = TileEntity.func_190200_a(null, nbttagcompound4);

				if(tileentity != null)
				{
					Block b = null;
					for(OffsetBlock osb : offsetBlocks)
						if(osb.xOff == tileentity.getPos().getX() && osb.yOff == tileentity.getPos().getY() && osb.zOff == tileentity.getPos().getZ())
							b = osb.getBlock();
					if(b == null)
						b = Blocks.STONE;
					OffsetTileEntity block = new OffsetTileEntity(tileentity.getPos().getX(), tileentity.getPos().getY(), tileentity.getPos().getZ(), b, nbttagcompound4, falling);
					block.setRelativeToPlayer(relativeToPlayer);
					block.setBlockState(b.getStateFromMeta(data[i1]));
					offsetBlocks.add(block);
				}
			}
		}

		return new CustomSchematic(offsetBlocks, width, height, length, relativeToPlayer, includeAirBlocks, delay);
	}
	
	public static CustomSchematic loadCustomSchematic(String file, int xOffSet, int yOffSet, int zOffSet, float delay, boolean falling, boolean relativeToPlayer, boolean includeAirBlocks)
	{
		JsonElement elem = FileUtil.readJsonfromFile(ConfigLoader.folder.getAbsolutePath() + "/CustomRewards/Schematics/" + file);
		return SchematicUtil.loadCustomSchematic(elem, xOffSet, yOffSet, zOffSet, delay, falling, relativeToPlayer, includeAirBlocks);
	}

	public static CustomSchematic loadCustomSchematic(JsonElement elem, int xOffSet, int yOffSet, int zOffSet, float delay, boolean falling, boolean relativeToPlayer, boolean includeAirBlocks)
	{
		
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

		int index = 0;
		List<Integer> blockArray = new ArrayList<Integer>();
		for(JsonElement ids : json.get("Blocks").getAsJsonArray())
		{
			String entry = ids.getAsString();
			String[] parts = entry.split("x");
			int id = Integer.parseInt(parts[0]);
			int recurse = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;
			for(int i = 0; i < recurse; i++)
				blockArray.add(id);
		}

		for(int yOff = 0; yOff < ySize; yOff++)
		{
			for(int xOff = (xSize / 2) - xSize; xOff < (xSize / 2); xOff++)
			{
				for(int zOff = (zSize / 2) - zSize; zOff < (zSize / 2); zOff++)
				{
					int id = blockArray.get(index);
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
					Block b = Block.REGISTRY.getObject(new ResourceLocation(dataParts[0], dataParts[1]));
					OffsetBlock osb = new OffsetBlock(xOff + xOffSet, yOff + yOffSet, zOff + zOffSet, b, falling, 0);
					// TODO: Find better way?
					osb.setBlockState(b.getStateFromMeta(Integer.parseInt(dataParts[2])));
					osb.setRelativeToPlayer(relativeToPlayer);
					offsetBlocks.add(osb);
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
			if(osb.getBlock().equals(Blocks.AIR) && !includeAirBlocks)
				offsetBlocks.remove(i);
		}

		return new CustomSchematic(offsetBlocks, xSize, ySize, zSize, relativeToPlayer, includeAirBlocks, delay);
	}

	public static OffsetTileEntity OffsetBlockToTileEntity(OffsetBlock osb, String nbt)
	{
		try
		{
			return OffsetBlockToTileEntity(osb, (NBTTagCompound) JsonToNBT.getTagFromJson(nbt));
		} catch(NBTException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static OffsetTileEntity OffsetBlockToTileEntity(OffsetBlock osb, NBTTagCompound nbt)
	{
		OffsetTileEntity oste = new OffsetTileEntity(osb.xOff, osb.yOff, osb.zOff, osb.getBlock(), nbt, osb.isFalling(), osb.getDelay());
		oste.setBlockState(osb.getBlockState());
		oste.setDelay(osb.getDelay());
		oste.setRelativeToPlayer(osb.isRelativeToPlayer());
		oste.setFalling(osb.isFalling());
		return oste;
	}
}
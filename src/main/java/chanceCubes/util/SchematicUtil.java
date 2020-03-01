package chanceCubes.util;

import chanceCubes.config.ConfigLoader;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.rewardparts.OffsetTileEntity;
import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.FloatVar;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.NBTVar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class SchematicUtil
{
	private static Gson gson = new GsonBuilder().create();

	public static BlockPos[] selectionPoints = new BlockPos[2];

	public static void createCustomSchematic(World world, BlockPos loc1, BlockPos loc2, String fileName)
	{
		List<Integer> blocks = new ArrayList<>();
		List<CustomEntry<Integer, String>> blockDataIds = new ArrayList<>();
		List<CustomEntry<String, List<Integer>>> tileEntityData = new ArrayList<>();
		int largeX = Math.max(loc1.getX(), loc2.getX());
		int smallX = Math.min(loc1.getX(), loc2.getX());
		int largeY = Math.max(loc1.getY(), loc2.getY());
		int smallY = Math.min(loc1.getY(), loc2.getY());
		int largeZ = Math.max(loc1.getZ(), loc2.getZ());
		int smallZ = Math.min(loc1.getZ(), loc2.getZ());
		StringBuilder blockData = new StringBuilder();
		for(int y = smallY; y < largeY; y++)
		{
			for(int x = smallX; x < largeX; x++)
			{
				for(int z = smallZ; z < largeZ; z++)
				{
					BlockPos pos = new BlockPos(x, y, z);
					BlockState blockState = world.getBlockState(pos);
					blockData.append(blockState.getBlock().getRegistryName().toString());
					String blockString = blockData.toString();
					int id = -1;
					for(CustomEntry<Integer, String> data : blockDataIds)
					{
						if(blockString.equalsIgnoreCase(data.getValue()))
						{
							id = data.getKey();
						}
					}
					if(id == -1)
					{
						id = blockDataIds.size();
						blockDataIds.add(new CustomEntry<>(id, blockString));
					}
					blocks.add(id);

					if(world.getTileEntity(pos) != null)
					{
						TileEntity te = world.getTileEntity(pos);
						CompoundNBT nbt = new CompoundNBT();
						nbt = te.write(nbt);
						for(CustomEntry<String, List<Integer>> data : tileEntityData)
						{
							if(nbt.toString().equalsIgnoreCase(data.getKey()))
							{
								data.getValue().add(blocks.size() - 1);
								break;
							}
						}
						List<Integer> list = new ArrayList<>();
						list.add(blocks.size() - 1);
						tileEntityData.add(new CustomEntry<>(nbt.toString(), list));
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

	public static CustomSchematic loadLegacySchematic(String fileName, int xoff, int yoff, int zoff, FloatVar spacingDelay, BoolVar falling, BoolVar relativeToPlayer, BoolVar includeAirBlocks, BoolVar playSound, IntVar delay)
	{
		File schematic = new File(ConfigLoader.folder.getAbsolutePath() + "/CustomRewards/Schematics/" + fileName);
		CompoundNBT nbtdata;
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
		return loadLegacySchematic(nbtdata, xoff, yoff, zoff, spacingDelay, falling, relativeToPlayer, includeAirBlocks, playSound, delay);
	}

	public static CustomSchematic loadLegacySchematic(CompoundNBT nbtdata, int xoff, int yoff, int zoff, FloatVar spacingDelay, BoolVar falling, BoolVar relativeToPlayer, BoolVar includeAirBlocks, BoolVar playSound, IntVar delay)
	{
		short width = nbtdata.getShort("Width");
		short height = nbtdata.getShort("Height");
		short length = nbtdata.getShort("Length");

		byte[] blocks = nbtdata.getByteArray("Blocks");
		List<OffsetBlock> offsetBlocks = new ArrayList<>();

		ListNBT tileentities = nbtdata.getList("TileEntities", 10);

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

					//TODO: Support legacy still? uses ID's
					Block b = ForgeRegistries.BLOCKS.getValues().stream().skip(j).findFirst().get();
					if(b != Blocks.AIR)
					{
						OffsetBlock block = new OffsetBlock(halfWidth - xx, yy, halfLength - zz, b, falling);
						block.setRelativeToPlayer(relativeToPlayer);
						block.setPlaysSound(playSound);
						offsetBlocks.add(block);
					}
					i++;
				}
			}
		}

		for(int i1 = 0; i1 < tileentities.size(); ++i1)
		{
			CompoundNBT nbttagcompound4 = tileentities.getCompound(i1);
			TileEntity tileentity = TileEntity.create(nbttagcompound4);

			if(tileentity != null)
			{
				Block b = null;
				for(OffsetBlock osb : offsetBlocks)
					if(osb.xOff.getIntValue() == halfWidth - tileentity.getPos().getX() && osb.yOff.getIntValue() == tileentity.getPos().getY() && osb.zOff.getIntValue() == halfLength - tileentity.getPos().getZ())
						b = osb.getBlockState().getBlock();
				if(b == null)
					b = Blocks.STONE;
				OffsetTileEntity block = new OffsetTileEntity(halfWidth - tileentity.getPos().getX(), tileentity.getPos().getY(), halfLength - tileentity.getPos().getZ(), b.getDefaultState(), nbttagcompound4, falling);
				block.setRelativeToPlayer(relativeToPlayer);
				block.setPlaysSound(playSound);
				offsetBlocks.add(block);
			}
		}

		return new CustomSchematic(offsetBlocks, width, height, length, relativeToPlayer, includeAirBlocks, spacingDelay, delay);
	}

	public static CustomSchematic loadCustomSchematic(String file, int xOffSet, int yOffSet, int zOffSet, FloatVar spacingDelay, BoolVar falling, BoolVar relativeToPlayer, BoolVar includeAirBlocks, BoolVar playSound, IntVar delay)
	{
		JsonElement elem = FileUtil.readJsonfromFile(ConfigLoader.folder.getAbsolutePath() + "/CustomRewards/Schematics/" + file);
		return SchematicUtil.loadCustomSchematic(elem, xOffSet, yOffSet, zOffSet, spacingDelay, falling, relativeToPlayer, includeAirBlocks, playSound, delay);
	}

	public static CustomSchematic loadCustomSchematic(JsonElement elem, int xOffSet, int yOffSet, int zOffSet, FloatVar spacingDelay, BoolVar falling, BoolVar relativeToPlayer, BoolVar includeAirBlocks, BoolVar playSound, IntVar delay)
	{
		if(elem == null)
			return null;
		JsonObject json = elem.getAsJsonObject();
		List<OffsetBlock> offsetBlocks = new ArrayList<>();
		JsonObject info = json.get("Schematic Data").getAsJsonObject();
		int xSize = info.get("xSize").getAsInt();
		int ySize = info.get("ySize").getAsInt();
		int zSize = info.get("zSize").getAsInt();
		List<CustomEntry<Integer, String>> blockDataIds = new ArrayList<>();

		JsonArray blockDataArray = json.get("Block Data").getAsJsonArray();
		for(JsonElement i : blockDataArray)
		{
			JsonObject index = i.getAsJsonObject();
			for(Entry<String, JsonElement> obj : index.entrySet())
				blockDataIds.add(new CustomEntry<>(obj.getValue().getAsInt(), obj.getKey()));
		}

		int index = 0;
		List<Integer> blockArray = new ArrayList<>();
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
					Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockData));
					OffsetBlock osb = new OffsetBlock(xOff + xOffSet, yOff + yOffSet, zOff + zOffSet, b, falling, new IntVar(0));
					osb.setRelativeToPlayer(relativeToPlayer);
					osb.setPlaysSound(playSound);
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

		return new CustomSchematic(offsetBlocks, xSize, ySize, zSize, relativeToPlayer, includeAirBlocks, spacingDelay, delay);
	}

	public static OffsetTileEntity OffsetBlockToTileEntity(OffsetBlock osb, String nbt)
	{
		try
		{
			return OffsetBlockToTileEntity(osb, JsonToNBT.getTagFromJson(nbt));
		} catch(CommandSyntaxException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static OffsetTileEntity OffsetBlockToTileEntity(OffsetBlock osb, NBTVar nbt)
	{
		OffsetTileEntity oste = new OffsetTileEntity(osb.xOff, osb.yOff, osb.zOff, osb.getBlockState(), nbt, osb.isFallingVar(), osb.getDelayVar());
		oste.setBlockState(osb.getBlockState());
		oste.setDelay(osb.getDelay());
		oste.setRelativeToPlayer(osb.isRelativeToPlayer());
		oste.setFalling(osb.isFalling());
		return oste;
	}

	public static OffsetTileEntity OffsetBlockToTileEntity(OffsetBlock osb, CompoundNBT nbt)
	{
		OffsetTileEntity oste = new OffsetTileEntity(osb.xOff, osb.yOff, osb.zOff, osb.getBlockState(), new NBTVar(nbt), osb.isFallingVar(), osb.getDelayVar());
		oste.setBlockState(osb.getBlockState());
		oste.setDelay(osb.getDelay());
		oste.setRelativeToPlayer(osb.isRelativeToPlayer());
		oste.setFalling(osb.isFalling());
		return oste;
	}

	public static JsonElement getSchematicJson(String path)
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(SchematicUtil.class.getResourceAsStream(path)));
		StringBuilder builder = new StringBuilder();
		try
		{
			String line;
			while((line = in.readLine()) != null)
				builder.append(line);

			in.close();
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		return FileUtil.JSON_PARSER.parse(builder.toString());
	}
}
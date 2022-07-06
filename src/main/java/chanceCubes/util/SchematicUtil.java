package chanceCubes.util;

import chanceCubes.config.ConfigLoader;
import chanceCubes.mcwrapper.BlockWrapper;
import chanceCubes.mcwrapper.JsonWrapper;
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
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SchematicUtil
{
	private static final Gson gson = new GsonBuilder().create();

	public static final BlockPos[] selectionPoints = new BlockPos[2];

	public static void createCustomSchematic(Level level, BlockPos loc1, BlockPos loc2, String fileName)
	{
		//I hate myself for this, but we have to move this to the main thread and the task system does......
		Scheduler.scheduleTask(new Task("Schematic Creation", 1)
		{
			@Override
			public void callback()
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
				Vec3i small = new Vec3i(smallX, smallY, smallZ);
				Vec3i large = new Vec3i(largeX, largeY, largeZ);

				storeBlocksInfo(small, large, level, blocks, blockDataIds, tileEntityData);

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
				info.addProperty("xSize", large.getX() - small.getX());
				info.addProperty("ySize", large.getY() - small.getY());
				info.addProperty("zSize", large.getZ() - small.getZ());
				json.add("Schematic Data", info);

				FileUtil.writeToFile(ConfigLoader.schematicsFolder.getAbsolutePath() + "/" + fileName, gson.toJson(json));
			}
		});
	}

	private static void storeBlocksInfo(Vec3i small, Vec3i large, Level level, List<Integer> blocks, List<CustomEntry<Integer, String>> blockDataIds, List<CustomEntry<String, List<Integer>>> tileEntityData)
	{
		StringBuilder blockData = new StringBuilder();
		for(int y = small.getY(); y < large.getY(); y++)
		{
			for(int x = small.getX(); x < large.getX(); x++)
			{
				for(int z = small.getZ(); z < large.getZ(); z++)
				{
					blockData.setLength(0);
					BlockPos pos = new BlockPos(x, y, z);
					BlockState blockState = level.getBlockState(pos);
					blockData.append(BlockWrapper.getBlockId(blockState.getBlock()));
					String encoded = encodeBlockState(blockState);
					if(!encoded.isEmpty())
						blockData.append(":").append(encoded);

					String blockString = blockData.toString();

					int id = -1;
					for(CustomEntry<Integer, String> data : blockDataIds)
						if(blockString.equalsIgnoreCase(data.getValue()))
							id = data.getKey();

					if(id == -1)
					{
						id = blockDataIds.size();
						blockDataIds.add(new CustomEntry<>(id, blockString));
					}
					blocks.add(id);

					if(level.getBlockEntity(pos) != null)
					{
						BlockEntity te = level.getBlockEntity(pos);
						if(te == null)
							continue;
						CompoundTag nbt = te.saveWithFullMetadata();
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
	}

	public static CustomSchematic loadCustomSchematic(String file, int xOffSet, int yOffSet, int zOffSet, FloatVar spacingDelay, BoolVar falling, BoolVar relativeToPlayer, BoolVar includeAirBlocks, BoolVar playSound, IntVar delay)
	{
		JsonElement elem = FileUtil.readJsonFromFile(ConfigLoader.schematicsFolder.getAbsolutePath() + "/" + file);
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
					BlockState state;
					String[] parts = blockData.split(":");
					if(parts.length == 1)
						state = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[0])).defaultBlockState();
					else
						state = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[0], parts[1])).defaultBlockState();

					if(parts.length == 3)
						state = decodeBlockState(state, parts[2]);

					OffsetBlock osb = new OffsetBlock(xOff + xOffSet, yOff + yOffSet, zOff + zOffSet, state.getBlock(), falling, new IntVar(0));
					osb.setBlockState(state);
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
					offsetBlocks.set(id, oste);
				}
			}
		}

		return new CustomSchematic(offsetBlocks, xSize, ySize, zSize, relativeToPlayer, includeAirBlocks, spacingDelay, delay);
	}

	public static OffsetTileEntity OffsetBlockToTileEntity(OffsetBlock osb, String nbt)
	{
		return OffsetBlockToTileEntity(osb, JsonWrapper.getNBTFromJson(nbt));
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

	public static OffsetTileEntity OffsetBlockToTileEntity(OffsetBlock osb, CompoundTag nbt)
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
		return JsonParser.parseString(builder.toString());
	}

	public static String encodeBlockState(BlockState state)
	{
		StringBuilder builder = new StringBuilder("[");
		for(Property<?> prop : state.getProperties())
			if(state.hasProperty(prop))
				builder.append(prop.getName()).append("=").append(state.getValue(prop)).append(",");

		builder.deleteCharAt(builder.length() - 1);
		builder.append("]");
		return builder.toString();
	}

	//[key=value, key2=value2,....]
	public static BlockState decodeBlockState(BlockState defaultState, String stateString)
	{
		BlockState returnState = defaultState;
		if(!stateString.matches("\\[.*]"))
			return returnState;

		stateString = stateString.substring(1, stateString.length() - 1);
		Map<String, String> map = new HashMap<>();
		Arrays.stream(stateString.split(",")).forEach((s) ->
		{
			String[] entry = s.split("=");
			map.put(entry[0], entry[1]);
		});

		for(Property<?> prop : defaultState.getProperties())
			if(map.containsKey(prop.getName()))
				returnState = withString(returnState, prop, map.get(prop.getName()));
		return returnState;
	}

	public static <T extends Comparable<T>> BlockState withString(BlockState state, Property<T> prop, String propVal)
	{
		return prop.getValue(propVal).map(t -> state.setValue(prop, t)).orElse(state);
	}
}
package chanceCubes.util;

import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import chanceCubes.rewards.rewardparts.CommandPart;
import com.google.gson.JsonObject;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.Level;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RewardsUtil
{
	private static List<String> oredicts = new ArrayList<>();
	private static String[] possibleModOres = new String[]{"ores/aluminum", "ores/copper", "ores/mythril", "ores/lead", "ores/plutonium", "ores/quartz", "ores/ruby", "ores/salt", "ores/sapphire", "ores/silver", "ores/tin", "ores/uranium", "ores/zinc"};

	public static final Random rand = new Random();

	public static List<String> getOreDicts()
	{
		return oredicts;
	}


	public static void initData()
	{
		oredicts.add("ores/gold");
		oredicts.add("ores/iron");
		oredicts.add("ores/lapis");
		oredicts.add("ores/diamond");
		oredicts.add("ores/redstone");
		oredicts.add("ores/emerald");
		oredicts.add("ores/quartz");
		oredicts.add("ores/coal");

		for(String oreDict : possibleModOres)
			if(BlockTags.getCollection().get(new ResourceLocation("forge", oreDict)) != null)
				oredicts.add(oreDict);
	}

	public static CommandPart[] executeXCommands(String command, int amount)
	{
		CommandPart[] toReturn = new CommandPart[amount];
		for(int i = 0; i < amount; i++)
			toReturn[i] = new CommandPart(command);
		return toReturn;
	}

	public static CommandPart[] executeXCommands(String command, int amount, int delay)
	{
		CommandPart[] toReturn = new CommandPart[amount];
		for(int i = 0; i < amount; i++)
		{
			CommandPart part = new CommandPart(command);
			part.setDelay(delay);
			toReturn[i] = part;
		}
		return toReturn;
	}

	public static void sendMessageToNearPlayers(World world, BlockPos pos, int distance, String message)
	{
		for(int i = 0; i < world.getPlayers().size(); ++i)
		{
			PlayerEntity entityplayer = world.getPlayers().get(i);
			double dist = Math.sqrt(Math.pow(pos.getX() - entityplayer.getPosX(), 2) + Math.pow(pos.getY() - entityplayer.getPosY(), 2) + Math.pow(pos.getZ() - entityplayer.getPosZ(), 2));
			if(dist <= distance)
				entityplayer.sendMessage(new StringTextComponent(message), entityplayer.getUniqueID());
		}
	}

	public static void sendMessageToAllPlayers(World world, String message)
	{
		for(int i = 0; i < world.getPlayers().size(); ++i)
		{
			PlayerEntity entityplayer = world.getPlayers().get(i);
			entityplayer.sendMessage(new StringTextComponent(message), entityplayer.getUniqueID());
		}
	}

	public static ItemStack getItemStack(String mod, String itemName, int size)
	{
		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(mod, itemName));
		return item == null ? ItemStack.EMPTY : new ItemStack(item, size);
	}

	public static Block getBlock(String mod, String blockName)
	{
		return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(mod, blockName));
	}

	public static boolean placeBlock(BlockState b, World world, BlockPos pos)
	{
		return RewardsUtil.placeBlock(b, world, pos, 3, false);
	}

	public static boolean placeBlock(BlockState b, World world, BlockPos pos, boolean ignoreUnbreakable)
	{
		return RewardsUtil.placeBlock(b, world, pos, 3, ignoreUnbreakable);
	}

	public static boolean placeBlock(BlockState b, World world, BlockPos pos, int update, boolean ignoreUnbreakable)
	{
		if(!RewardsUtil.isBlockUnbreakable(world, pos) || ignoreUnbreakable)
		{
			world.setBlockState(pos, b, update);
			return true;
		}
		return false;
	}

	public static boolean isBlockUnbreakable(World world, BlockPos pos)
	{
		return world.getBlockState(pos).getBlockHardness(world, pos) == -1 || CCubesSettings.nonReplaceableBlocks.contains(world.getBlockState(pos));
	}

	public static Enchantment getEnchantSafe(ResourceLocation res)
	{
		return getRegistryEntrySafe(ForgeRegistries.ENCHANTMENTS, res, ForgeRegistries.ENCHANTMENTS.getDefaultKey());
	}

	public static Effect getPotionSafe(ResourceLocation res)
	{
		return getRegistryEntrySafe(ForgeRegistries.POTIONS, res, ForgeRegistries.POTIONS.getDefaultKey());
	}

	public static ParticleType<?> getParticleSafe(ResourceLocation res)
	{
		return getRegistryEntrySafe(ForgeRegistries.PARTICLE_TYPES, res, ForgeRegistries.PARTICLE_TYPES.getDefaultKey());
	}

	public static <T extends IForgeRegistryEntry<T>> T getRegistryEntrySafe(IForgeRegistry<T> registry, ResourceLocation key, ResourceLocation defaultReturn)
	{
		T val = registry.getValue(key);
		return val == null ? registry.getValue(defaultReturn) : val;
	}

	public static Block getRandomOre()
	{
		return getRandomOre(new ArrayList<>());
	}

	public static Block getRandomOre(List<String> blacklist)
	{
		return BlockTags.getCollection().get(new ResourceLocation("forge", RewardsUtil.getRandomOreDict(blacklist))).getRandomElement(RewardsUtil.rand);
	}

	public static Block getRandomOreFromOreDict(String oreDict)
	{
		return BlockTags.getCollection().get(new ResourceLocation("forge", oreDict)).getRandomElement(RewardsUtil.rand);
	}

	public static Block getRandomBlock()
	{
		return randomRegistryEntry(ForgeRegistries.BLOCKS, Blocks.COBBLESTONE);
	}

	public static Item getRandomItem()
	{
		Item item;
		do
			item = randomRegistryEntry(ForgeRegistries.ITEMS, Items.APPLE);
		while(item == null || item.getCreativeTabs().size() == 0);
		return item;
	}

	public static Enchantment randomEnchantment()
	{
		Collection<Enchantment> enchantments = ForgeRegistries.ENCHANTMENTS.getValues();
		return randomRegistryEntry(ForgeRegistries.ENCHANTMENTS, enchantments.stream().skip(rand.nextInt(enchantments.size())).findFirst().orElse(null));
	}

	public static CustomEntry<Enchantment, Integer> getRandomEnchantmentAndLevel()
	{
		Enchantment ench = randomEnchantment();
		int level = rand.nextInt(ench.getMaxLevel()) + ench.getMinLevel();
		return new CustomEntry<>(ench, level);
	}

	public static Effect getRandomPotionEffect()
	{
		return randomRegistryEntry(ForgeRegistries.POTIONS, Effects.GLOWING);
	}

	public static EffectInstance getRandomPotionEffectInstance()
	{
		Effect effect = RewardsUtil.getRandomPotionEffect();
		int duration = ((int) Math.round(Math.abs(rand.nextGaussian()) * 5) + 3) * 20;
		int amplifier = (int) Math.round(Math.abs(rand.nextGaussian() * 1.5));

		return new EffectInstance(effect, duration, amplifier);
	}

	public static Potion getRandomPotionType()
	{
		return randomRegistryEntry(ForgeRegistries.POTION_TYPES, Potions.EMPTY);
	}

	public static <T extends IForgeRegistryEntry<T>> T randomRegistryEntry(IForgeRegistry<T> registry, T defaultReturn)
	{
		Collection<T> entries = registry.getValues();
		T entry = entries.stream().skip(rand.nextInt(entries.size())).findFirst().orElse(null);
		int iteration = 0;
		while(entry == null)
		{
			iteration++;
			if(iteration > 100)
				return defaultReturn;
			entry = entries.stream().skip(rand.nextInt(entries.size())).findFirst().orElse(null);
		}
		return entry;
	}

	public static ItemStack getRandomFirework()
	{
		ItemStack stack = new ItemStack(Items.FIREWORK_ROCKET);
		CompoundNBT data = new CompoundNBT();
		data.putInt("Flight", rand.nextInt(3) + 1);

		ListNBT explosionList = new ListNBT();

		for(int i = 0; i <= rand.nextInt(2); i++)
		{
			CompoundNBT explosionData = new CompoundNBT();
			explosionData.putInt("Type", rand.nextInt(5));
			explosionData.putBoolean("Flicker", rand.nextBoolean());
			explosionData.putBoolean("Trail", rand.nextBoolean());
			int[] colors = new int[rand.nextInt(2) + 1];
			for(int j = 0; j < colors.length; j++)
			{
				colors[j] = getRandomColor();
			}
			explosionData.putIntArray("Colors", colors);
			int[] fadeColors = new int[rand.nextInt(2) + 1];
			for(int j = 0; j < fadeColors.length; j++)
			{
				fadeColors[j] = getRandomColor();
			}
			explosionData.putIntArray("FadeColors", fadeColors);
			explosionList.add(explosionData);
		}
		data.put("Explosions", explosionList);
		CompoundNBT nbt = new CompoundNBT();
		nbt.put("Fireworks", data);

		stack.setTag(nbt);

		return stack;
	}

	public static String getRandomOreDict()
	{
		return getRandomOreDict(new ArrayList<>());
	}

	public static String getRandomOreDict(List<String> blacklist)
	{
		List<String> oredicts = RewardsUtil.getOreDicts().stream().filter(line -> !blacklist.contains(line)).collect(Collectors.toList());
		return oredicts.size() > 0 ? oredicts.get(rand.nextInt(oredicts.size())) : "ores/coal";
	}

	public static Fluid getRandomFluid(boolean onlySources)
	{
		Fluid fluid;
		do
			fluid = randomRegistryEntry(ForgeRegistries.FLUIDS, Fluids.WATER);
		while(onlySources && !fluid.isSource(fluid.getDefaultState()));

		return fluid;
	}

	public static int getRandomColor()
	{
		return (new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256))).getRGB();
	}

	private static final Block[] wools = {Blocks.WHITE_WOOL, Blocks.ORANGE_WOOL, Blocks.MAGENTA_WOOL, Blocks.LIGHT_BLUE_WOOL, Blocks.LIME_WOOL, Blocks.PINK_WOOL, Blocks.GRAY_WOOL, Blocks.LIGHT_GRAY_WOOL, Blocks.CYAN_WOOL, Blocks.PURPLE_WOOL, Blocks.BLUE_WOOL, Blocks.BROWN_WOOL, Blocks.GREEN_WOOL, Blocks.RED_WOOL, Blocks.BLACK_WOOL};

	public static BlockState getRandomWool()
	{
		return wools[rand.nextInt(wools.length)].getDefaultState();
	}


	public static boolean isPlayerOnline(PlayerEntity player)
	{
		if(player == null)
			return false;

		for(ServerPlayerEntity playerMP : player.world.getServer().getPlayerList().getPlayers())
			if(playerMP.getUniqueID().equals(player.getUniqueID()))
				return true;

		return false;
	}

	public static void executeCommand(World world, PlayerEntity player, Vector3i pos, String command)
	{
		RewardsUtil.executeCommand(world, player, new Vector3d(pos.getX(), pos.getY(), pos.getZ()), command);
	}

	public static void executeCommand(World world, PlayerEntity player, Vector3d pos, String command)
	{
		MinecraftServer server = world.getServer();
		// TODO: 1.16: getOverworld
		ServerWorld worldServer = server.func_241755_D_();
		boolean rule = worldServer.getGameRules().getBoolean(GameRules.COMMAND_BLOCK_OUTPUT);
		worldServer.getGameRules().get(GameRules.COMMAND_BLOCK_OUTPUT).set(false, server);
		CommandSource cs = new CommandSource(player, pos, player.getPitchYaw(), worldServer, 2, player.getName().getString(), player.getDisplayName(), server, player);
		cs = cs.withFeedbackDisabled();
		server.getCommandManager().handleCommand(cs, command);
		worldServer.getGameRules().get(GameRules.COMMAND_BLOCK_OUTPUT).set(rule, server);
	}

	public static void setNearPlayersTitle(World world, BlockPos pos, int range, STitlePacket.Type type, ITextComponent message, int fadeInTime, int displayTime, int fadeOutTime)
	{
		for(int i = 0; i < world.getPlayers().size(); ++i)
		{
			PlayerEntity entityplayer = world.getPlayers().get(i);

			double dist = Math.sqrt(Math.pow(pos.getX() - entityplayer.getPosX(), 2) + Math.pow(pos.getY() - entityplayer.getPosY(), 2) + Math.pow(pos.getZ() - entityplayer.getPosZ(), 2));
			if(dist <= range)
				setPlayerTitle(entityplayer, type, message, fadeInTime, displayTime, fadeOutTime);
		}
	}

	public static void setAllPlayersTitle(World world, STitlePacket.Type type, ITextComponent message, int fadeInTime, int displayTime, int fadeOutTime)
	{
		for(int i = 0; i < world.getPlayers().size(); ++i)
			setPlayerTitle(world.getPlayers().get(i), type, message, fadeInTime, displayTime, fadeOutTime);
	}

	public static void setPlayerTitle(PlayerEntity player, STitlePacket.Type type, ITextComponent message, int fadeInTime, int displayTime, int fadeOutTime)
	{
		if(player instanceof ServerPlayerEntity)
		{
			STitlePacket titlePacket = new STitlePacket(type, message, fadeInTime, displayTime, fadeOutTime);
			STitlePacket timesPacket = new STitlePacket(STitlePacket.Type.TIMES, new StringTextComponent(""), fadeInTime, displayTime, fadeOutTime);
			((ServerPlayerEntity) player).connection.sendPacket(timesPacket);
			((ServerPlayerEntity) player).connection.sendPacket(titlePacket);
		}
	}

	public static String[] getHardcodedRewards()
	{
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(RewardsUtil.class.getResourceAsStream("/data/chancecubes/rewards/rewards.txt")));
			List<String> files = new ArrayList<>();
			String line;
			while((line = in.readLine()) != null)
				files.add(line);
			in.close();
			return files.toArray(new String[0]);
		} catch(Exception e)
		{
			CCubesCore.logger.log(Level.ERROR, "CHANCE CUBES WAS UNABLE TO LOAD IN ITS DEFAULT REWARDS!!!!");
			CCubesCore.logger.log(Level.ERROR, "REPORT TO MOD AUTHOR ASAP!!!");
			e.printStackTrace();
		}
		return new String[0];
	}

	public static JsonObject getRewardJson(String file)
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(RewardsUtil.class.getResourceAsStream("/data/chancecubes/rewards/" + file)));
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
		return FileUtil.JSON_PARSER.parse(builder.toString()).getAsJsonObject();
	}
}
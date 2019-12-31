package chanceCubes.util;

import chanceCubes.config.CCubesSettings;
import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.rewards.rewardparts.EntityPart;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.rewardparts.ParticlePart;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RewardsUtil
{
	private static List<String> oredicts = new ArrayList<>();
	private static String[] possibleModOres = new String[]{"ores/aluminum", "ores/copper", "ores/mythril", "ores/lead", "ores/plutonium", "ores/buby", "ores/salt", "ores/sapphire", "ores/silver", "ores/tin", "ores/uranium", "ores/zinc"};
	private static List<String> fluids = new ArrayList<>();

	public static final Random rand = new Random();

	public static List<String> getOreDicts()
	{
		return oredicts;
	}

	public static List<String> getFluids()
	{
		return fluids;
	}

	public static void initData()
	{
		oredicts.add("ores/coal");
		oredicts.add("ores/diamond");
		oredicts.add("ores/emerald");
		oredicts.add("ores/gold");
		oredicts.add("ores/iron");
		oredicts.add("ores/lapis");
		oredicts.add("ores/quartz");
		oredicts.add("ores/redstone");

		for(String oreDict : possibleModOres)
			if(BlockTags.getCollection().get(new ResourceLocation("forge", oreDict)) != null)
				oredicts.add(oreDict);

		//		for(String s : FluidRegistry.getRegisteredFluids().keySet())
		//			fluids.add(s);
	}

	public static OffsetBlock[] fillArea(int xSize, int ySize, int zSize, Block block, int xOff, int yOff, int zOff, boolean falling, int delay, boolean causesUpdate, boolean relativeToPlayer)
	{
		List<OffsetBlock> toReturn = new ArrayList<>();

		for(int y = 0; y < ySize; y++)
			for(int z = 0; z < zSize; z++)
				for(int x = 0; x < xSize; x++)
					toReturn.add(new OffsetBlock(x + xOff, y + yOff, z + zOff, block, falling, delay).setCausesBlockUpdate(causesUpdate).setRelativeToPlayer(relativeToPlayer));

		return toReturn.toArray(new OffsetBlock[0]);
	}

	public static OffsetBlock[] addBlocksLists(OffsetBlock[]... lists)
	{
		int size = 0;
		for(OffsetBlock[] list : lists)
			size += list.length;

		OffsetBlock[] toReturn = new OffsetBlock[size];

		int i = 0;
		for(OffsetBlock[] list : lists)
		{
			for(OffsetBlock osb : list)
			{
				toReturn[i] = osb;
				i++;
			}
		}

		return toReturn;
	}

	public static EntityPart[] spawnXEntities(CompoundNBT entityNbt, int amount)
	{
		return RewardsUtil.spawnXEntities(entityNbt, amount, true);
	}

	public static EntityPart[] spawnXEntities(CompoundNBT entityNbt, int amount, boolean shouldRemoveBlocks)
	{
		EntityPart[] toReturn = new EntityPart[amount];
		for(int i = 0; i < amount; i++)
			toReturn[i] = new EntityPart(entityNbt).setRemovedBlocks(shouldRemoveBlocks);
		return toReturn;
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

	public static ParticlePart[] spawnXParticles(String particle, int amount)
	{
		ParticlePart[] toReturn = new ParticlePart[amount];
		for(int i = 0; i < amount; i++)
			toReturn[i] = new ParticlePart(particle);
		return toReturn;
	}

	public static ItemPart[] generateItemParts(ItemStack... stacks)
	{
		ItemPart[] toReturn = new ItemPart[stacks.length];
		for(int i = 0; i < stacks.length; i++)
			toReturn[i] = new ItemPart(stacks[i]);
		return toReturn;
	}

	public static ItemPart[] generateItemParts(Item... items)
	{
		ItemPart[] toReturn = new ItemPart[items.length];
		for(int i = 0; i < items.length; i++)
			toReturn[i] = new ItemPart(new ItemStack(items[i]));
		return toReturn;
	}

	public static void sendMessageToNearPlayers(World world, BlockPos pos, int distance, String message)
	{
		for(int i = 0; i < world.getPlayers().size(); ++i)
		{
			PlayerEntity entityplayer = world.getPlayers().get(i);
			double dist = Math.sqrt(Math.pow(pos.getX() - entityplayer.posX, 2) + Math.pow(pos.getY() - entityplayer.posY, 2) + Math.pow(pos.getZ() - entityplayer.posZ, 2));
			if(dist <= distance)
				entityplayer.sendMessage(new StringTextComponent(message));
		}
	}

	public static void sendMessageToAllPlayers(World world, String message)
	{
		for(int i = 0; i < world.getPlayers().size(); ++i)
		{
			PlayerEntity entityplayer = world.getPlayers().get(i);
			entityplayer.sendMessage(new StringTextComponent(message));
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

	public static ParticleType getParticleSafe(ResourceLocation res)
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
		return BlockTags.getCollection().get(new ResourceLocation("forge", RewardsUtil.getRandomOreDict())).getRandomElement(RewardsUtil.rand);
	}

	public static Block getRandomBlock()
	{
		return randomRegistryEntry(ForgeRegistries.BLOCKS, Blocks.COBBLESTONE);
	}

	public static Item getRandomItem()
	{
		return randomRegistryEntry(ForgeRegistries.ITEMS, Items.APPLE);
	}

	public static Enchantment randomEnchantment()
	{
		return randomRegistryEntry(ForgeRegistries.ENCHANTMENTS, Registry.ENCHANTMENT.getRandom(rand));
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
		return RewardsUtil.getOreDicts().get(rand.nextInt(RewardsUtil.getOreDicts().size()));
	}

	public static Fluid getRandomFluid()
	{
		return randomRegistryEntry(ForgeRegistries.FLUIDS, Fluids.WATER);
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

	public static void executeCommand(World world, PlayerEntity player, Vec3d pos, String command)
	{
		MinecraftServer server = world.getServer();
		ServerWorld worldServer = server.getWorld(DimensionType.OVERWORLD);
		boolean rule = worldServer.getGameRules().getBoolean(GameRules.COMMAND_BLOCK_OUTPUT);
		worldServer.getGameRules().get(GameRules.COMMAND_BLOCK_OUTPUT).set(false, server);
		CommandSource cs = new CommandSource(player, pos, player.getPitchYaw(), worldServer, 2, player.getName().getString(), player.getDisplayName(), server, player);
		cs = cs.withFeedbackDisabled();
		server.getCommandManager().handleCommand(cs, command);
		worldServer.getGameRules().get(GameRules.COMMAND_BLOCK_OUTPUT).set(rule, server);
	}

	public static void setNearPlayersTitle(World world, STitlePacket spackettitle, BlockPos pos, int range)
	{
		for(int i = 0; i < world.getPlayers().size(); ++i)
		{
			PlayerEntity entityplayer = world.getPlayers().get(i);

			double dist = Math.sqrt(Math.pow(pos.getX() - entityplayer.posX, 2) + Math.pow(pos.getY() - entityplayer.posY, 2) + Math.pow(pos.getZ() - entityplayer.posZ, 2));
			if(dist <= range)
				setPlayerTitle(entityplayer, spackettitle);
		}
	}

	public static void setAllPlayersTitle(World world, STitlePacket spackettitle)
	{
		for(int i = 0; i < world.getPlayers().size(); ++i)
			setPlayerTitle(world.getPlayers().get(i), spackettitle);
	}

	public static void setPlayerTitle(PlayerEntity player, STitlePacket title)
	{
		if(player instanceof ServerPlayerEntity)
			((ServerPlayerEntity) player).connection.sendPacket(title);
	}
}
package chanceCubes.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import chanceCubes.config.CCubesSettings;
import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.rewards.rewardparts.EntityPart;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.rewardparts.ParticlePart;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RewardsUtil
{
	private static List<String> oredicts = new ArrayList<String>();
	private static String[] possibleModOres = new String[] { "ores/aluminum", "ores/copper", "ores/mythril", "ores/lead", "ores/plutonium", "ores/buby", "ores/salt", "ores/sapphire", "ores/silver", "ores/tin", "ores/uranium", "ores/zinc" };
	private static List<String> fluids = new ArrayList<String>();

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

		for(String s : FluidRegistry.getRegisteredFluids().keySet())
			fluids.add(s);
	}

	/**
	 * 
	 * @param xSize
	 * @param ySize
	 * @param zSize
	 * @param block
	 * @param xOff
	 * @param yOff
	 * @param zOff
	 * @param falling
	 * @param delay
	 * @param causeUpdate
	 * @param relativeToPlayer
	 * @return
	 */
	public static OffsetBlock[] fillArea(int xSize, int ySize, int zSize, Block block, int xOff, int yOff, int zOff, boolean falling, int delay, boolean causesUpdate, boolean relativeToPlayer)
	{
		List<OffsetBlock> toReturn = new ArrayList<OffsetBlock>();

		for(int y = 0; y < ySize; y++)
			for(int z = 0; z < zSize; z++)
				for(int x = 0; x < xSize; x++)
					toReturn.add(new OffsetBlock(x + xOff, y + yOff, z + zOff, block, falling, delay).setCausesBlockUpdate(causesUpdate).setRelativeToPlayer(relativeToPlayer));

		return toReturn.toArray(new OffsetBlock[toReturn.size()]);
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

	public static EntityPart[] spawnXEntities(NBTTagCompound entityNbt, int amount)
	{
		EntityPart[] toReturn = new EntityPart[amount];
		for(int i = 0; i < amount; i++)
			toReturn[i] = new EntityPart(entityNbt);
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

	public static ParticlePart[] spawnXParticles(int particle, int amount)
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
		for(int i = 0; i < world.playerEntities.size(); ++i)
		{
			EntityPlayer entityplayer = (EntityPlayer) world.playerEntities.get(i);
			double dist = Math.sqrt(Math.pow(pos.getX() - entityplayer.posX, 2) + Math.pow(pos.getY() - entityplayer.posY, 2) + Math.pow(pos.getZ() - entityplayer.posZ, 2));
			if(dist <= distance)
				entityplayer.sendMessage(new TextComponentString(message));
		}
	}

	public static void sendMessageToAllPlayers(World world, String message)
	{
		for(int i = 0; i < world.playerEntities.size(); ++i)
		{
			EntityPlayer entityplayer = (EntityPlayer) world.playerEntities.get(i);
			entityplayer.sendMessage(new TextComponentString(message));
		}
	}

	public static ItemStack getItemStack(String mod, String itemName, int size)
	{
		return getItemStack(mod, itemName, size, 0);
	}

	public static ItemStack getItemStack(String mod, String itemName, int size, int meta)
	{
		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(mod, itemName));
		return item == null ? ItemStack.EMPTY : new ItemStack(item, size);
	}

	public static Block getBlock(String mod, String blockName)
	{
		return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(mod, blockName));
	}

	public static boolean placeBlock(IBlockState b, World world, BlockPos pos)
	{
		return RewardsUtil.placeBlock(b, world, pos, 3, false);
	}

	public static boolean placeBlock(IBlockState b, World world, BlockPos pos, boolean ignoreUnbreakable)
	{
		return RewardsUtil.placeBlock(b, world, pos, 3, ignoreUnbreakable);
	}

	public static boolean placeBlock(IBlockState b, World world, BlockPos pos, int update, boolean ignoreUnbreakable)
	{
		if((!RewardsUtil.isBlockUnbreakable(world, pos) || ignoreUnbreakable) && !CCubesSettings.nonReplaceableBlocks.contains(world.getBlockState(pos)))
		{
			world.setBlockState(pos, b, update);
			return true;
		}
		return false;
	}

	public static boolean isBlockUnbreakable(World world, BlockPos pos)
	{
		return world.getBlockState(pos).getBlockHardness(world, pos) == -1;
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
		return randomRegistryEntry(ForgeRegistries.ENCHANTMENTS, Enchantment.getEnchantmentByID(0));
	}

	public static PotionEffect getRandomPotionEffect()
	{
		Potion pot = randomRegistryEntry(ForgeRegistries.POTIONS, Potion.getPotionById(0));
		int duration = ((int) Math.round(Math.abs(rand.nextGaussian()) * 5) + 3) * 20;
		int amplifier = (int) Math.round(Math.abs(rand.nextGaussian() * 1.5));

		return new PotionEffect(pot, duration, amplifier);
	}

	public static PotionType getRandomPotionType()
	{
		return randomRegistryEntry(ForgeRegistries.POTION_TYPES, PotionTypes.EMPTY);
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
		NBTTagCompound data = new NBTTagCompound();
		data.setInt("Flight", rand.nextInt(3) + 1);

		NBTTagList explosionList = new NBTTagList();

		for(int i = 0; i <= rand.nextInt(2); i++)
		{
			NBTTagCompound explosionData = new NBTTagCompound();
			explosionData.setInt("Type", rand.nextInt(5));
			explosionData.setBoolean("Flicker", rand.nextBoolean());
			explosionData.setBoolean("Trail", rand.nextBoolean());
			int[] colors = new int[rand.nextInt(2) + 1];
			for(int j = 0; j < colors.length; j++)
			{
				colors[j] = getRandomColor();
			}
			explosionData.setIntArray("Colors", colors);
			int[] fadeColors = new int[rand.nextInt(2) + 1];
			for(int j = 0; j < fadeColors.length; j++)
			{
				fadeColors[j] = getRandomColor();
			}
			explosionData.setIntArray("FadeColors", fadeColors);
			explosionList.add(explosionData);
		}
		data.setTag("Explosions", explosionList);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("Fireworks", data);

		stack.setTag(nbt);

		return stack;
	}

	public static String getRandomOreDict()
	{
		return RewardsUtil.getOreDicts().get(rand.nextInt(RewardsUtil.getOreDicts().size()));
	}

	public static Fluid getRandomFluid()
	{
		Fluid f = FluidRegistry.getFluid(RewardsUtil.getFluids().get(rand.nextInt(RewardsUtil.getFluids().size())));
		while(f == null || f.getBlock() == null)
			f = FluidRegistry.getFluid(RewardsUtil.getFluids().get(rand.nextInt(RewardsUtil.getFluids().size())));
		return f;
	}

	public static int getRandomColor()
	{
		return (new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256))).getRGB();
	}

	private static final Block[] wools = { Blocks.WHITE_WOOL, Blocks.ORANGE_WOOL, Blocks.MAGENTA_WOOL, Blocks.LIGHT_BLUE_WOOL, Blocks.LIME_WOOL, Blocks.PINK_WOOL, Blocks.GRAY_WOOL, Blocks.LIGHT_GRAY_WOOL, Blocks.CYAN_WOOL, Blocks.PURPLE_WOOL, Blocks.BLUE_WOOL, Blocks.BROWN_WOOL, Blocks.GREEN_WOOL, Blocks.RED_WOOL, Blocks.BLACK_WOOL };

	public static IBlockState getRandomWool()
	{
		return wools[rand.nextInt(wools.length)].getDefaultState();
	}

	public static boolean isPlayerOnline(EntityPlayer player)
	{
		if(player == null)
			return false;

		for(EntityPlayerMP playerMP : player.world.getServer().getPlayerList().getPlayers())
			if(playerMP.getUniqueID().equals(player.getUniqueID()))
				return true;

		return false;
	}

	public static void executeCommand(World world, EntityPlayer player, String command)
	{
		MinecraftServer server = world.getServer();
		WorldServer worldServer = server.getWorld(DimensionType.OVERWORLD);
		Boolean rule = worldServer.getGameRules().getBoolean("commandBlockOutput");
		worldServer.getGameRules().setOrCreateGameRule("commandBlockOutput", "false", server);
		CommandSource cs = new CommandSource(player, player.getPositionVector(), player.getPitchYaw(), worldServer, 2, player.getName().getString(), player.getDisplayName(), server, player);
		server.getCommandManager().handleCommand(cs, command);
		worldServer.getGameRules().setOrCreateGameRule("commandBlockOutput", rule.toString(), server);
	}

	public static void setNearPlayersTitle(World world, SPacketTitle spackettitle, BlockPos pos, int range)
	{
		for(int i = 0; i < world.playerEntities.size(); ++i)
		{
			EntityPlayer entityplayer = world.playerEntities.get(i);

			double dist = Math.sqrt(Math.pow(pos.getX() - entityplayer.posX, 2) + Math.pow(pos.getY() - entityplayer.posY, 2) + Math.pow(pos.getZ() - entityplayer.posZ, 2));
			if(dist <= range)
				setPlayerTitle(entityplayer, spackettitle);
		}
	}

	public static void setAllPlayersTitle(World world, SPacketTitle spackettitle)
	{
		for(int i = 0; i < world.playerEntities.size(); ++i)
			setPlayerTitle(world.playerEntities.get(i), spackettitle);
	}

	public static void setPlayerTitle(EntityPlayer player, SPacketTitle title)
	{
		if(player instanceof EntityPlayerMP)
			((EntityPlayerMP) player).connection.sendPacket(title);
	}
}
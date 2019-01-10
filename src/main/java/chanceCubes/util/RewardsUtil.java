package chanceCubes.util;

import java.awt.Color;
import java.util.ArrayList;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.ForgeRegistries;

public class RewardsUtil
{
	private static List<String> oredicts = new ArrayList<String>();
	private static String[] possibleModOres = new String[] { "oreAluminum", "oreCopper", "oreMythril", "oreLead", "orePlutonium", "oreQuartz", "oreRuby", "oreSalt", "oreSapphire", "oreSilver", "oreTin", "oreUranium", "oreZinc" };
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
		oredicts.add("oreGold");
		oredicts.add("oreIron");
		oredicts.add("oreLapis");
		oredicts.add("oreDiamond");
		oredicts.add("oreRedstone");
		oredicts.add("oreEmerald");
		oredicts.add("oreQuartz");
		oredicts.add("oreCoal");

		for(String oreDict : possibleModOres)
			if(OreDictionary.doesOreNameExist(oreDict))
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
		Item item = Item.REGISTRY.get(new ResourceLocation(mod, itemName));
		return item == null ? ItemStack.EMPTY : new ItemStack(item, size);
	}

	public static Block getBlock(String mod, String blockName)
	{
		return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(mod, blockName));
	}

	@SuppressWarnings("deprecation")
	public static IBlockState getBlockStateFromBlockMeta(Block b, int meta)
	{
		return b.getStateFromMeta(meta);
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

	public static Block getRandomBlock()
	{
		int size = ForgeRegistries.BLOCKS.getValues().size();
		int randomblock = rand.nextInt(size);
		//TODO: PLZ IMPROVE
		Block b = ForgeRegistries.BLOCKS.getValues().toArray(new Block[0])[size];
		int iteration = 0;
		while(b == null)
		{
			iteration++;
			randomblock = rand.nextInt(size);
			if(iteration > 100)
				b = Blocks.COBBLESTONE;
			else
				b = ForgeRegistries.BLOCKS.getValues().toArray(new Block[0])[size];
			//TODO: PLZ IMPROVE
		}
		return b;
	}

	public static CustomEntry<Block, Integer> getRandomOre()
	{
		List<ItemStack> ores = OreDictionary.getOres(RewardsUtil.getRandomOreDict());
		Block ore = null;
		int meta = 0;

		int iteration = 0;
		while(ore == null)
		{
			iteration++;
			if(iteration > 100 || ores.size() == 0)
			{
				ore = Blocks.COAL_ORE;
			}
			else
			{
				ItemStack stack = ores.get(rand.nextInt(ores.size()));
				ore = Block.getBlockFromItem(stack.getItem());
				meta = stack.getDamage();
			}
		}

		return new CustomEntry<Block, Integer>(ore, meta);
	}

	public static Item getRandomItem()
	{
		Item item = Item.getItemById(256 + rand.nextInt(166));
		while(item == null)
			item = Item.getItemById(256 + rand.nextInt(166));
		return item;
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

	public static PotionEffect getRandomPotionEffect()
	{
		Potion potion = null;
		int tries = 0;
		do
		{
			if(tries > 10)
			{
				return new PotionEffect(MobEffects.WITHER, 5, 1);
			}
			potion = (Potion) Potion.REGISTRY.getObjectById(rand.nextInt(Potion.REGISTRY.getKeys().size()));
			tries++;
		} while(potion == null);
		int duration = ((int) Math.round(Math.abs(rand.nextGaussian()) * 5) + 3) * 20;
		int amplifier = (int) Math.round(Math.abs(rand.nextGaussian() * 1.5));

		return new PotionEffect(potion, duration, amplifier);
	}

	public static int getRandomColor()
	{
		return (new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256))).getRGB();
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
		Boolean rule = server.worlds[0].getGameRules().getBoolean("commandBlockOutput");
		server.worlds[0].getGameRules().setOrCreateGameRule("commandBlockOutput", "false");
		server.getCommandManager().handleCommand(new CCubesCommandSender(player, player.getPosition()), command);
		server.worlds[0].getGameRules().setOrCreateGameRule("commandBlockOutput", rule.toString());
	}
}
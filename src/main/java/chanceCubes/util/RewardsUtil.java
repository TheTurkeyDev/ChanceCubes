package chanceCubes.util;

import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.rewards.rewardparts.EntityPart;
import chanceCubes.rewards.rewardparts.ItemPart;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketTitle;
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
import org.apache.logging.log4j.Level;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class RewardsUtil
{
	private static List<String> oredicts = new ArrayList<>();
	private static String[] possibleModOres = new String[]{"oreAluminum", "oreCopper", "oreMythril", "oreLead", "orePlutonium", "oreQuartz", "oreRuby", "oreSalt", "oreSapphire", "oreSilver", "oreTin", "oreUranium", "oreZinc"};
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

		fluids.addAll(FluidRegistry.getRegisteredFluids().keySet());
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
			EntityPlayer entityplayer = world.playerEntities.get(i);
			double dist = Math.sqrt(Math.pow(pos.getX() - entityplayer.posX, 2) + Math.pow(pos.getY() - entityplayer.posY, 2) + Math.pow(pos.getZ() - entityplayer.posZ, 2));
			if(dist <= distance)
				entityplayer.sendMessage(new TextComponentString(message));
		}
	}

	public static void sendMessageToAllPlayers(World world, String message)
	{
		for(int i = 0; i < world.playerEntities.size(); ++i)
		{
			EntityPlayer entityplayer = world.playerEntities.get(i);
			entityplayer.sendMessage(new TextComponentString(message));
		}
	}

	public static ItemStack getItemStack(String mod, String itemName, int size)
	{
		return getItemStack(mod, itemName, size, 0);
	}

	public static ItemStack getItemStack(String mod, String itemName, int size, int meta)
	{
		Item item = Item.REGISTRY.getObject(new ResourceLocation(mod, itemName));
		return item == null ? ItemStack.EMPTY : new ItemStack(item, size, meta);
	}

	public static Block getBlock(String mod, String blockName)
	{
		return Block.REGISTRY.getObject(new ResourceLocation(mod, blockName));
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

	public static Enchantment getEnchantSafe(String res)
	{
		return RewardsUtil.getEnchantSafe(new ResourceLocation(res));
	}

	public static Enchantment getEnchantSafe(ResourceLocation res)
	{
		Enchantment ench = Enchantment.REGISTRY.getObject(res);
		if(ench == null)
			return Enchantments.AQUA_AFFINITY;
		return ench;
	}

	public static ItemStack getSpawnEggForEntity(ResourceLocation entityId)
	{
		ItemStack stack = new ItemStack(Items.SPAWN_EGG);
		NBTTagCompound nbttagcompound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
		nbttagcompound1.setString("id", entityId.toString());
		nbttagcompound.setTag("EntityTag", nbttagcompound1);
		stack.setTagCompound(nbttagcompound);
		return stack;
	}

	public static Block getRandomBlock()
	{
		return Block.REGISTRY.getObjectById(rand.nextInt(Block.REGISTRY.getKeys().size()));
	}

	public static CustomEntry<Block, Integer> getRandomOre()
	{
		return getRandomOre(new ArrayList<>());
	}

	public static CustomEntry<Block, Integer> getRandomOre(List<String> blacklist)
	{
		return RewardsUtil.getRandomOreFromOreDict(RewardsUtil.getRandomOreDict(blacklist));
	}

	public static CustomEntry<Block, Integer> getRandomOreFromOreDict(String oreDict)
	{
		List<ItemStack> ores = OreDictionary.getOres(oreDict);
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
				meta = stack.getItemDamage();
			}
		}

		return new CustomEntry<>(ore, meta);
	}

	public static Item getRandomItem()
	{
		return Item.getItemById(256 + rand.nextInt(166));
	}

	public static ItemStack getRandomFirework()
	{
		ItemStack stack = new ItemStack(Items.FIREWORKS);
		NBTTagCompound data = new NBTTagCompound();
		data.setInteger("Flight", rand.nextInt(3) + 1);

		NBTTagList explosionList = new NBTTagList();

		for(int i = 0; i <= rand.nextInt(2); i++)
		{
			NBTTagCompound explosionData = new NBTTagCompound();
			explosionData.setInteger("Type", rand.nextInt(5));
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
			explosionList.appendTag(explosionData);
		}
		data.setTag("Explosions", explosionList);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("Fireworks", data);

		stack.setTagCompound(nbt);

		return stack;
	}

	public static String getRandomOreDict()
	{
		return getRandomOreDict(new ArrayList<>());
	}

	public static String getRandomOreDict(List<String> blacklist)
	{
		Stream<String> oredicts = RewardsUtil.getOreDicts().stream().filter(line -> !blacklist.contains(line));
		return oredicts.skip(rand.nextInt((int) oredicts.count())).findFirst().orElse("oreCoal");
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
		Potion potion;
		int tries = 0;
		do
		{
			if(tries > 10)
			{
				return new PotionEffect(MobEffects.WITHER, 5, 1);
			}
			potion = Potion.REGISTRY.getObjectById(rand.nextInt(Potion.REGISTRY.getKeys().size()));
			tries++;
		} while(potion == null);
		int duration = ((int) Math.round(Math.abs(rand.nextGaussian()) * 5) + 3) * 20;
		int amplifier = (int) Math.round(Math.abs(rand.nextGaussian() * 1.5));

		return new PotionEffect(potion, duration, amplifier);
	}

	public static Enchantment getRandomEnchantment()
	{
		Enchantment ench = Enchantment.getEnchantmentByID(RewardsUtil.rand.nextInt(Enchantment.REGISTRY.getKeys().size()));
		while(ench == null)
			ench = Enchantment.getEnchantmentByID(RewardsUtil.rand.nextInt(Enchantment.REGISTRY.getKeys().size()));
		return ench;
	}

	public static CustomEntry<Enchantment, Integer> getRandomEnchantmentAndLevel()
	{
		Enchantment ench = Enchantment.getEnchantmentByID(RewardsUtil.rand.nextInt(Enchantment.REGISTRY.getKeys().size()));
		while(ench == null)
			ench = Enchantment.getEnchantmentByID(RewardsUtil.rand.nextInt(Enchantment.REGISTRY.getKeys().size()));
		int level = rand.nextInt(ench.getMaxLevel()) + ench.getMinLevel();
		return new CustomEntry<>(ench, level);
	}

	public static int getRandomColor()
	{
		return (new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256))).getRGB();
	}

	public static boolean isPlayerOnline(EntityPlayer player)
	{
		if(player == null)
			return false;

		for(EntityPlayerMP playerMP : player.world.getMinecraftServer().getPlayerList().getPlayers())
			if(playerMP.getUniqueID().equals(player.getUniqueID()))
				return true;

		return false;
	}

	public static void executeCommand(World world, EntityPlayer player, String command)
	{
		executeCommand(world, player, player.getPosition(), command);
	}

	public static void executeCommand(World world, EntityPlayer player, BlockPos pos, String command)
	{
		MinecraftServer server = world.getMinecraftServer();
		if(server != null)
		{
			boolean rule = server.worlds[0].getGameRules().getBoolean("commandBlockOutput");
			server.worlds[0].getGameRules().setOrCreateGameRule("commandBlockOutput", "false");
			server.getCommandManager().executeCommand(new CCubesCommandSender(player, pos), command);
			server.worlds[0].getGameRules().setOrCreateGameRule("commandBlockOutput", String.valueOf(rule));
		}
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
		{
			// Update the title times to be what the title packet defines because times are updated separately of the title message....
			SPacketTitle titlePacket = new SPacketTitle(SPacketTitle.Type.TIMES, new TextComponentString(""), title.getFadeInTime(), title.getDisplayTime(), title.getFadeOutTime());
			((EntityPlayerMP) player).connection.sendPacket(titlePacket);
			((EntityPlayerMP) player).connection.sendPacket(title);
		}
	}

	public static void setPlayerTitleReset(EntityPlayer player)
	{
		if(player instanceof EntityPlayerMP)
		{
			SPacketTitle resetPacket = new SPacketTitle(SPacketTitle.Type.RESET, new TextComponentString(""), 0, 0, 0);
			((EntityPlayerMP) player).connection.sendPacket(resetPacket);
			resetPacket = new SPacketTitle(SPacketTitle.Type.CLEAR, new TextComponentString(""), 0, 0, 0);
			((EntityPlayerMP) player).connection.sendPacket(resetPacket);
		}
	}

	public static String[] getHardcodedRewards()
	{
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(RewardsUtil.class.getResourceAsStream("/assets/chancecubes/rewards/rewards.txt")));
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
		BufferedReader in = new BufferedReader(new InputStreamReader(RewardsUtil.class.getResourceAsStream("/assets/chancecubes/rewards/" + file)));
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
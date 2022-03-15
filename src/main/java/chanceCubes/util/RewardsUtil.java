package chanceCubes.util;

import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import chanceCubes.rewards.rewardparts.CommandPart;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.StreamSupport;

public class RewardsUtil
{
	private static final List<String> oredicts = new ArrayList<>();
	private static final String[] possibleModOres = new String[]{"ores/aluminum", "ores/copper", "ores/mythril", "ores/lead", "ores/plutonium", "ores/quartz", "ores/ruby", "ores/salt", "ores/sapphire", "ores/silver", "ores/tin", "ores/uranium", "ores/zinc"};

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
			if(Registry.BLOCK.getTagOrEmpty(getTagKey(new ResourceLocation("forge", oreDict))).iterator().hasNext())
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

	public static void sendMessageToNearPlayers(Level level, BlockPos pos, int distance, String message)
	{
		for(int i = 0; i < level.players().size(); ++i)
		{
			Player entityplayer = level.players().get(i);
			double dist = Math.sqrt(Math.pow(pos.getX() - entityplayer.getX(), 2) + Math.pow(pos.getY() - entityplayer.getY(), 2) + Math.pow(pos.getZ() - entityplayer.getZ(), 2));
			if(dist <= distance)
				sendMessageToPlayer(entityplayer, message);
		}
	}

	public static void sendMessageToAllPlayers(Level level, String message)
	{
		for(int i = 0; i < level.players().size(); ++i)
		{
			Player entityplayer = level.players().get(i);
			sendMessageToPlayer(entityplayer, message);
		}
	}

	public static void sendMessageToPlayer(Player player, String message)
	{
		if(player != null)
			sendMessageToPlayer(player, new TextComponent(message));
	}

	public static void sendMessageToPlayer(Player player, Component message)
	{
		if(player != null)
			player.sendMessage(message, Util.NIL_UUID);
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

	public static boolean placeBlock(BlockState b, Level level, BlockPos pos)
	{
		return RewardsUtil.placeBlock(b, level, pos, 3, false);
	}

	public static boolean placeBlock(BlockState b, Level level, BlockPos pos, boolean ignoreUnbreakable)
	{
		return RewardsUtil.placeBlock(b, level, pos, 3, ignoreUnbreakable);
	}

	public static boolean placeBlock(BlockState b, Level level, BlockPos pos, int update, boolean ignoreUnbreakable)
	{
		if(!RewardsUtil.isBlockUnbreakable(level, pos) || ignoreUnbreakable)
		{
			level.setBlock(pos, b, update);
			return true;
		}
		return false;
	}

	public static boolean isBlockUnbreakable(Level level, BlockPos pos)
	{
		return level.getBlockState(pos).getDestroySpeed(level, pos) == -1 || CCubesSettings.nonReplaceableBlocks.contains(level.getBlockState(pos));
	}

	public static Enchantment getEnchantSafe(ResourceLocation res)
	{
		return getRegistryEntrySafe(ForgeRegistries.ENCHANTMENTS, res, ForgeRegistries.ENCHANTMENTS.getDefaultKey());
	}

	public static MobEffect getPotionSafe(ResourceLocation res)
	{
		return getRegistryEntrySafe(ForgeRegistries.MOB_EFFECTS, res, ForgeRegistries.MOB_EFFECTS.getDefaultKey());
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
		return getRandomOreFromOreDict(RewardsUtil.getRandomOreDict(blacklist));
	}

	public static Block getRandomOreFromOreDict(String oreDict)
	{
		return getRandomElement(Registry.BLOCK.getTagOrEmpty(getTagKey(new ResourceLocation("forge", oreDict))));
	}

	private static Block getRandomElement(Iterable<Holder<Block>> it)
	{
		List<Holder<Block>> result = StreamSupport.stream(it.spliterator(), false).toList();
		return result.get(rand.nextInt(result.size())).value();
	}

	private static TagKey<Block> getTagKey(ResourceLocation res)
	{
		return TagKey.create(Registry.BLOCK_REGISTRY, res);
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
		return randomRegistryEntry(ForgeRegistries.ENCHANTMENTS, Enchantments.SHARPNESS);
	}

	public static CustomEntry<Enchantment, Integer> getRandomEnchantmentAndLevel()
	{
		Enchantment ench = randomEnchantment();
		int level = rand.nextInt(ench.getMaxLevel()) + ench.getMinLevel();
		return new CustomEntry<>(ench, level);
	}

	public static MobEffect getRandomPotionEffect()
	{
		return randomRegistryEntry(ForgeRegistries.MOB_EFFECTS, MobEffects.GLOWING);
	}

	public static MobEffectInstance getRandomPotionEffectInstance()
	{
		MobEffect effect = RewardsUtil.getRandomPotionEffect();
		int duration = ((int) Math.round(Math.abs(rand.nextGaussian()) * 5) + 3) * 20;
		int amplifier = (int) Math.round(Math.abs(rand.nextGaussian() * 1.5));

		return new MobEffectInstance(effect, duration, amplifier);
	}

	public static Potion getRandomPotionType()
	{
		return randomRegistryEntry(ForgeRegistries.POTIONS, Potions.EMPTY);
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
		CompoundTag data = new CompoundTag();
		data.putInt("Flight", rand.nextInt(3) + 1);

		ListTag explosionList = new ListTag();

		for(int i = 0; i <= rand.nextInt(2); i++)
		{
			CompoundTag explosionData = new CompoundTag();
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
		CompoundTag nbt = new CompoundTag();
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
		List<String> oredicts = RewardsUtil.getOreDicts().stream().filter(line -> !blacklist.contains(line)).toList();
		return oredicts.size() > 0 ? oredicts.get(rand.nextInt(oredicts.size())) : "ores/coal";
	}

	public static Fluid getRandomFluid(boolean onlySources)
	{
		Fluid fluid;
		do
			fluid = randomRegistryEntry(ForgeRegistries.FLUIDS, Fluids.WATER);
		while(onlySources && !fluid.isSource(fluid.defaultFluidState()));

		return fluid;
	}

	public static int getRandomColor()
	{
		return (new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256))).getRGB();
	}

	private static final Block[] wools = {Blocks.WHITE_WOOL, Blocks.ORANGE_WOOL, Blocks.MAGENTA_WOOL, Blocks.LIGHT_BLUE_WOOL, Blocks.LIME_WOOL, Blocks.PINK_WOOL, Blocks.GRAY_WOOL, Blocks.LIGHT_GRAY_WOOL, Blocks.CYAN_WOOL, Blocks.PURPLE_WOOL, Blocks.BLUE_WOOL, Blocks.BROWN_WOOL, Blocks.GREEN_WOOL, Blocks.RED_WOOL, Blocks.BLACK_WOOL};

	public static BlockState getRandomWool()
	{
		return wools[rand.nextInt(wools.length)].defaultBlockState();
	}


	public static boolean isPlayerOnline(Player player)
	{
		if(player == null)
			return false;

		for(ServerPlayer playerMP : player.level.getServer().getPlayerList().getPlayers())
			if(playerMP.getUUID().equals(player.getUUID()))
				return true;

		return false;
	}

	public static void executeCommand(ServerLevel level, Player player, Vec3i pos, String command)
	{
		RewardsUtil.executeCommand(level, player, new Vec3(pos.getX(), pos.getY(), pos.getZ()), command);
	}

	public static void executeCommand(ServerLevel level, Player player, Vec3 pos, String command)
	{
		MinecraftServer server = level.getServer();
		boolean rule = level.getGameRules().getBoolean(GameRules.RULE_COMMANDBLOCKOUTPUT);
		level.getGameRules().getRule(GameRules.RULE_COMMANDBLOCKOUTPUT).set(false, server);
		CommandSourceStack cs = new CommandSourceStack(player, pos, player.getRotationVector(), level, 2, player.getName().getString(), player.getDisplayName(), server, player);
		cs = cs.withSuppressedOutput();
		server.getCommands().performCommand(cs, command);
		level.getGameRules().getRule(GameRules.RULE_COMMANDBLOCKOUTPUT).set(rule, server);
	}

	public static void setNearPlayersTitle(Level level, BlockPos pos, int range, GuiTextLocation type, Component message, int fadeInTime, int displayTime, int fadeOutTime)
	{
		for(int i = 0; i < level.players().size(); ++i)
		{
			Player entityplayer = level.players().get(i);

			double dist = Math.sqrt(Math.pow(pos.getX() - entityplayer.getX(), 2) + Math.pow(pos.getY() - entityplayer.getY(), 2) + Math.pow(pos.getZ() - entityplayer.getZ(), 2));
			if(dist <= range)
				setPlayerTitle(entityplayer, type, message, fadeInTime, displayTime, fadeOutTime);
		}
	}

	public static void setAllPlayersTitle(Level level, GuiTextLocation type, Component message, int fadeInTime, int displayTime, int fadeOutTime)
	{
		for(int i = 0; i < level.players().size(); ++i)
			setPlayerTitle(level.players().get(i), type, message, fadeInTime, displayTime, fadeOutTime);
	}

	public static void setPlayerTitle(Player player, GuiTextLocation type, Component message, int fadeInTime, int displayTime, int fadeOutTime)
	{
		if(player instanceof ServerPlayer)
		{
			Packet<?> titlePacket;
			switch(type)
			{
				case TITLE -> titlePacket = new ClientboundSetTitleTextPacket(message);
				case SUBTITLE -> titlePacket = new ClientboundSetSubtitleTextPacket(message);
				default -> titlePacket = new ClientboundSetActionBarTextPacket(message);
			}
			ClientboundSetTitlesAnimationPacket timesPacket = new ClientboundSetTitlesAnimationPacket(fadeInTime, displayTime, fadeOutTime);
			((ServerPlayer) player).connection.send(timesPacket);
			((ServerPlayer) player).connection.send(titlePacket);
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
			CCubesCore.logger.error("CHANCE CUBES WAS UNABLE TO LOAD IN ITS DEFAULT REWARDS!!!!");
			CCubesCore.logger.error("REPORT TO MOD AUTHOR ASAP!!!");
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
		return JsonParser.parseString(builder.toString()).getAsJsonObject();
	}
}
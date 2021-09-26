package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class LootBoxReward extends BaseCustomReward
{
	private static final String COMMON_LORE = "{\"text\":\"Common\",\"color\":\"white\",\"bold\":true}";
	private static final String UNCOMMON_LORE = "{\"text\":\"Uncommon\",\"color\":\"green\",\"bold\":true}";
	private static final String RARE_LORE = "{\"text\":\"Rare\",\"color\":\"blue\",\"bold\":true}";
	private static final String LEGENDARY_LORE = "{\"text\":\"Legendary\",\"color\":\"red\",\"bold\":true}";
	private static final String EPIC_LORE = "{\"text\":\"Epic\",\"color\":\"dark_purple\",\"bold\":true}";
	private static final String SHINY_LORE = "{\"text\":\"SHINY!\",\"color\":\"aqua\",\"italic\":true}";
	public static final List<ItemStack> COMMON = new ArrayList<>();        //50%
	public static final List<ItemStack> UNCOMMON = new ArrayList<>();    //30%
	public static final List<ItemStack> RARE = new ArrayList<>();        //13%
	public static final List<ItemStack> LEGENDARY = new ArrayList<>();    //6%
	public static final List<ItemStack> EPIC = new ArrayList<>();        //1%

	static
	{
		addItem(COMMON, Items.LEATHER_CHESTPLATE, COMMON_LORE);
		addItem(COMMON, Items.COAL, COMMON_LORE);
		addItem(COMMON, Items.STICK, COMMON_LORE);
		addItem(COMMON, Items.BRICK, COMMON_LORE);
		addItem(COMMON, Items.FEATHER, COMMON_LORE);
		addItem(UNCOMMON, Items.IRON_SWORD, UNCOMMON_LORE);
		addItem(UNCOMMON, Items.APPLE, UNCOMMON_LORE);
		addItem(UNCOMMON, Items.FLINT_AND_STEEL, UNCOMMON_LORE);
		addItem(UNCOMMON, Items.ARROW, UNCOMMON_LORE);
		addItem(UNCOMMON, Items.STRING, UNCOMMON_LORE);
		addItem(UNCOMMON, Items.WHEAT, UNCOMMON_LORE);
		addItem(UNCOMMON, Items.BONE, UNCOMMON_LORE);
		addItem(RARE, Items.DIAMOND, RARE_LORE);
		addItem(RARE, Items.CHAINMAIL_HELMET, RARE_LORE);
		addItem(RARE, Items.CHAINMAIL_BOOTS, RARE_LORE);
		addItem(RARE, Items.CLOCK, RARE_LORE);
		addItem(RARE, Items.IRON_NUGGET, RARE_LORE);
		addItem(LEGENDARY, Items.TURTLE_HELMET, LEGENDARY_LORE);
		addItem(LEGENDARY, Items.NETHERITE_LEGGINGS, LEGENDARY_LORE);
		addItem(LEGENDARY, Items.DIAMOND_CHESTPLATE, LEGENDARY_LORE);
		addItem(LEGENDARY, Items.GHAST_TEAR, LEGENDARY_LORE);
		addItem(EPIC, Items.SPECTRAL_ARROW, EPIC_LORE);
		addItem(EPIC, Items.CLAY_BALL, EPIC_LORE);
	}

	public LootBoxReward()
	{
		super(CCubesCore.MODID + ":loot_box", 35);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		level.setBlockAndUpdate(pos, Blocks.BEDROCK.defaultBlockState());
		Scheduler.scheduleTask(new Task("CC_Loot_Box_Animation", -1, 1)
		{
			int tick = 0;
			double y = 0;
			double inc = 0.023;

			@Override
			public void callback()
			{
				RewardsUtil.sendMessageToNearPlayers(level, pos, 25, "Open the Chance Cubes Loot Box to get some collectibles!");
				level.setBlockAndUpdate(pos, Blocks.CHEST.defaultBlockState());
				BlockEntity te = level.getBlockEntity(pos);
				if(!(te instanceof ChestBlockEntity chest))
					return;

				for(int i = 0; i < 10; i++)
					chest.setItem(i, getCollectible());
			}

			@Override
			public void update()
			{
				if(tick < 250)
				{
					y += inc;

					if(y > 1 || y < 0)
						inc *= -1;

					double xOff = Math.cos(tick / 3f);
					double yOff = Math.sin(tick / 3f);
					level.addParticle(ParticleTypes.DRIPPING_LAVA, pos.getX() + xOff + 0.5, pos.getY() + y, pos.getZ() + yOff + 0.5, 3, 0, 0, 0, 1);
					level.addParticle(ParticleTypes.DRIPPING_LAVA, pos.getX() - xOff + 0.5, pos.getY() + y, pos.getZ() - yOff + 0.5, 3, 0, 0, 0, 1);
				}
				if(tick == 250)
				{
					for(int i = 0; i < 100; i++)
					{
						level.addParticle(ParticleTypes.DRAGON_BREATH, pos.getX() + 0.5, pos.getY() + 0.95, pos.getZ() + 0.5, 3, 0, 0, 0, 1);
					}
				}
				if(tick > 250)
				{
					callback();
					Scheduler.removeTask(this);
				}
				tick++;
			}
		});
	}

	public ItemStack getCollectible()
	{
		ItemStack stack;
		int rarity = RewardsUtil.rand.nextInt(100);
		if(rarity < 50)
			stack = COMMON.get(RewardsUtil.rand.nextInt(COMMON.size())).copy();
		else if(rarity < 80)
			stack = UNCOMMON.get(RewardsUtil.rand.nextInt(UNCOMMON.size())).copy();
		else if(rarity < 93)
			stack = RARE.get(RewardsUtil.rand.nextInt(RARE.size())).copy();
		else if(rarity < 99)
			stack = LEGENDARY.get(RewardsUtil.rand.nextInt(LEGENDARY.size())).copy();
		else
			stack = EPIC.get(RewardsUtil.rand.nextInt(EPIC.size())).copy();

		if(RewardsUtil.rand.nextInt(100) == 42)
		{
			CompoundTag nbt = stack.getTag();
			if(nbt == null)
			{
				nbt = new CompoundTag();
				stack.setTag(nbt);
			}
			ListTag enchantList = new ListTag();
			nbt.put("Enchantments", enchantList);
			enchantList.add(new CompoundTag());
			CompoundTag display = (CompoundTag) nbt.get("display");
			if(display == null)
			{
				display = new CompoundTag();
				nbt.put("display", display);
			}
			ListTag loreList = (ListTag) display.get("Lore");
			if(loreList == null)
			{
				loreList = new ListTag();
				display.put("Lore", loreList);
			}

			loreList.add(StringTag.valueOf(SHINY_LORE));
		}

		return stack;
	}


	private static ItemStack addItem(List<ItemStack> list, Item item, String lore)
	{
		ItemStack stack = new ItemStack(item, 1);
		stack.setTag(getLoreNBT(lore));
		list.add(stack);
		return stack;
	}

	public static CompoundTag getLoreNBT(String... lore)
	{
		CompoundTag nbt = new CompoundTag();

		CompoundTag display = new CompoundTag();
		nbt.put("display", display);

		ListTag loreList = new ListTag();
		display.put("Lore", loreList);

		for(String l : lore)
			loreList.add(StringTag.valueOf(l));

		return nbt;
	}
}

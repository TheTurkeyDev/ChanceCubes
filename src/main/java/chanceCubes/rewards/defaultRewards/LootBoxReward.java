package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

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
	public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, JsonObject settings)
	{
		world.setBlockState(pos, Blocks.BEDROCK.getDefaultState());
		Scheduler.scheduleTask(new Task("CC_Loot_Box_Animation", -1, 1)
		{
			int tick = 0;
			double y = 0;
			double inc = 0.023;

			@Override
			public void callback()
			{
				RewardsUtil.sendMessageToNearPlayers(world, pos, 25, "Open the Chance Cubes Loot Box to get some collectibles!");
				world.setBlockState(pos, Blocks.CHEST.getDefaultState());
				TileEntity te = world.getTileEntity(pos);
				if(!(te instanceof ChestTileEntity))
					return;
				ChestTileEntity chest = (ChestTileEntity) te;
				for(int i = 0; i < 10; i++)
					chest.setInventorySlotContents(i, getCollectible());
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
					world.spawnParticle(ParticleTypes.DRIPPING_LAVA, pos.getX() + xOff + 0.5, pos.getY() + y, pos.getZ() + yOff + 0.5, 3, 0, 0, 0, 1);
					world.spawnParticle(ParticleTypes.DRIPPING_LAVA, pos.getX() - xOff + 0.5, pos.getY() + y, pos.getZ() - yOff + 0.5, 3, 0, 0, 0, 1);
				}
				if(tick == 250)
				{
					for(int i = 0; i < 100; i++)
					{
						world.spawnParticle(ParticleTypes.DRAGON_BREATH, pos.getX() + 0.5, pos.getY() + 0.95, pos.getZ() + 0.5, 3, 0, 0, 0, 1);
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
			CompoundNBT nbt = stack.getTag();
			if(nbt == null)
			{
				nbt = new CompoundNBT();
				stack.setTag(nbt);
			}
			ListNBT enchantList = new ListNBT();
			nbt.put("Enchantments", enchantList);
			enchantList.add(new CompoundNBT());
			CompoundNBT display = (CompoundNBT)nbt.get("display");
			if(display == null)
			{
				display = new CompoundNBT();
				nbt.put("display", display);
			}
			ListNBT loreList = (ListNBT) display.get("Lore");
			if(loreList == null)
			{
				loreList = new ListNBT();
				display.put("Lore", loreList);
			}

			loreList.add(StringNBT.valueOf(SHINY_LORE));
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

	public static CompoundNBT getLoreNBT(String... lore)
	{
		CompoundNBT nbt = new CompoundNBT();

		CompoundNBT display = new CompoundNBT();
		nbt.put("display", display);

		ListNBT loreList = new ListNBT();
		display.put("Lore", loreList);

		for(String l : lore)
			loreList.add(StringNBT.valueOf(l));

		return nbt;
	}
}

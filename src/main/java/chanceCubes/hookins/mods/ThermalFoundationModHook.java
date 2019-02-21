package chanceCubes.hookins.mods;

import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.defaultRewards.BasicReward;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.MessagePart;
import chanceCubes.rewards.type.ItemRewardType;
import chanceCubes.rewards.type.MessageRewardType;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ThermalFoundationModHook extends BaseModHook
{
	private int[] gears = { 22, 23, 24, 25, 26, 27, 256, 257, 258, 259, 260, 261, 262, 263, 264, 288, 289, 290, 291, 292, 293, 294, 295 };
	private int[] upgrades = { 0, 1, 2, 3 };
	private int[] phytoGro = { 0, 1, 2 };
	private int[] hardenedGlass = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
	private int[] hardenedAlloyGlass = { 0, 1, 2, 3, 4, 5, 6, 7 };

	public ThermalFoundationModHook()
	{
		super("thermalfoundation");
		loadRewards();
	}

	@Override
	public void loadRewards()
	{
		ItemStack stack;

		stack = RewardsUtil.getItemStack(super.modId, "material", 1);
		if(!stack.isEmpty())
		{
			ItemStack stack1 = stack;
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Grind_Gears", 60, new MessageRewardType(new MessagePart("You really grind my gears ;)")), new ItemRewardType(new ItemPart(stack1))
			{
				@Override
				public void trigger(ItemPart s, World world, int x, int y, int z, EntityPlayer player)
				{
					for (int i = 0; i < 4; i++)
					{
						stack1.setItemDamage(gears[RewardsUtil.rand.nextInt(gears.length)]);
						EntityItem itemEnt = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, stack1);
						itemEnt.setPickupDelay(10);
						world.spawnEntity(itemEnt);
					}
				}
			}));
		}

		stack = RewardsUtil.getItemStack(super.modId, "wrench", 1);
		if(!stack.isEmpty())
		{
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Crescent_Hammer", 60, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "meter", 1);
		if(!stack.isEmpty())
		{
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Multimeter", 60, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "upgrade", 1);
		if(!stack.isEmpty())
		{
			ItemStack stack1 = stack;
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Upgrade", 80, new ItemRewardType(new ItemPart(stack1))
			{
				@Override
				public void trigger(ItemPart s, World world, int x, int y, int z, EntityPlayer player)
				{
					stack1.setItemDamage(upgrades[RewardsUtil.rand.nextInt(upgrades.length)]);
					EntityItem itemEnt = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, stack1);
					itemEnt.setPickupDelay(10);
					world.spawnEntity(itemEnt);
				}
			}));
		}

		stack = RewardsUtil.getItemStack(super.modId, "security", 1);
		if(!stack.isEmpty())
		{
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Signalum_Security_Lock", 70, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "fertilizer", 8);
		if(!stack.isEmpty())
		{
			ItemStack stack1 = stack;
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Phyto_Gro", 75, new ItemRewardType(new ItemPart(stack1))
			{
				@Override
				public void trigger(ItemPart s, World world, int x, int y, int z, EntityPlayer player)
				{
					stack1.setItemDamage(phytoGro[RewardsUtil.rand.nextInt(phytoGro.length)]);
					EntityItem itemEnt = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, stack1);
					itemEnt.setPickupDelay(10);
					world.spawnEntity(itemEnt);
				}
			}));
		}

		stack = RewardsUtil.getItemStack(super.modId, "glass", 8);
		if(!stack.isEmpty())
		{
			ItemStack stack1 = stack;
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Hardened_Glass", 75, new ItemRewardType(new ItemPart(stack1))
			{
				@Override
				public void trigger(ItemPart s, World world, int x, int y, int z, EntityPlayer player)
				{
					stack1.setItemDamage(hardenedGlass[RewardsUtil.rand.nextInt(hardenedGlass.length)]);
					EntityItem itemEnt = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, stack1);
					itemEnt.setPickupDelay(10);
					world.spawnEntity(itemEnt);
				}
			}));
		}

		stack = RewardsUtil.getItemStack(super.modId, "glass_alloy", 8);
		if(!stack.isEmpty())
		{
			ItemStack stack1 = stack;
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Hardened_Alloy_Glass", 80, new ItemRewardType(new ItemPart(stack1))
			{
				@Override
				public void trigger(ItemPart s, World world, int x, int y, int z, EntityPlayer player)
				{
					stack1.setItemDamage(hardenedAlloyGlass[RewardsUtil.rand.nextInt(hardenedAlloyGlass.length)]);
					EntityItem itemEnt = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, stack1);
					itemEnt.setPickupDelay(10);
					world.spawnEntity(itemEnt);
				}
			}));
		}
	}
}

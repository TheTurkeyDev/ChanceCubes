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

	public ThermalFoundationModHook()
	{
		super("thermalfoundation");
		loadRewards();
	}

	@Override
	public void loadRewards()
	{
		ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Grind_Gears", 60, new MessageRewardType(new MessagePart("You really grind my gears ;)")), new ItemRewardType(new ItemPart(RewardsUtil.getItemStack(ThermalFoundationModHook.super.modId, "material", 1)))
		{
			ItemStack stack;

			@Override
			public void trigger(ItemPart s, World world, int x, int y, int z, EntityPlayer player)
			{
				for(int i = 0; i < 4; i++)
				{
					stack.setItemDamage(gears[RewardsUtil.rand.nextInt(gears.length)]);
					EntityItem itemEnt = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, s.getItemStack().copy());
					itemEnt.setPickupDelay(10);
					world.spawnEntity(itemEnt);
				}
			}
		}));

		/*
		 * List<ItemPart> avialable = new ArrayList<ItemPart>(); for(String s : this.rodTypes)
		 * if(GameRegistry.findItem(super.modId, "tool.fishingRod" + s) != null) avialable.add(new
		 * ItemPart(GameRegistry.findItemStack(super.modId, "tool.fishingRod" + s, 1)));
		 * ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":fishing", 5,
		 * new MessageRewardType(new MessagePart("Hey! Let's open a fishing store!")), new
		 * ItemRewardType(avialable.toArray(new ItemPart[avialable.size()]))));
		 */
	}
}

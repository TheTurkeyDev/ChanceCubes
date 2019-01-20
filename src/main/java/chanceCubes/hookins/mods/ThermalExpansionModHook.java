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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ThermalExpansionModHook extends BaseModHook
{

	public ThermalExpansionModHook()
	{
		super("thermalexpansion");
		loadRewards();
	}

	@Override
	public void loadRewards()
	{
		ItemStack stack;

		stack = RewardsUtil.getItemStack(super.modId, "florb", 1);
		if(!stack.isEmpty())
		{
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":florbs", 60, new MessageRewardType(new MessagePart("Florbs!!")), new ItemRewardType(new ItemPart(stack))
			{
				@Override
				public void trigger(ItemPart s, World world, int x, int y, int z, EntityPlayer player)
				{
					NBTTagCompound nbt;
					for(int i = 0; i < 5; i++)
					{
						nbt = new NBTTagCompound();
						nbt.setString("Fluid", RewardsUtil.getRandomFluid().getName());
						s.getItemStack().setTagCompound(nbt);
						EntityItem itemEnt = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, s.getItemStack().copy());
						itemEnt.setPickupDelay(10);
						world.spawnEntity(itemEnt);
					}
				}
			}));
		}
	}
}

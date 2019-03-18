package chanceCubes.hookins.mods;

import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.defaultRewards.BasicReward;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.MessagePart;
import chanceCubes.rewards.rewardtype.ItemRewardType;
import chanceCubes.rewards.rewardtype.MessageRewardType;
import chanceCubes.util.RewardsUtil;
import net.minecraft.enchantment.Enchantment;
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
			ItemStack stack1 = stack;
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Florbs", 60, new MessageRewardType(new MessagePart("Florbs!!")), new ItemRewardType(new ItemPart(stack1))
			{
				@Override
				public void trigger(ItemPart s, World world, int x, int y, int z, EntityPlayer player)
				{
					NBTTagCompound nbt;
					for(int i = 0; i < 5; i++)
					{
						nbt = new NBTTagCompound();
						nbt.setString("Fluid", RewardsUtil.getRandomFluid().getName());
						stack1.setTagCompound(nbt);
						EntityItem itemEnt = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, stack1.copy());
						itemEnt.setPickupDelay(10);
						world.spawnEntity(itemEnt);
					}
				}
			}));
		}

		stack = RewardsUtil.getItemStack(super.modId, "reservoir", 1, 4);
		if(!stack.isEmpty())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("Active", false);
			NBTTagCompound nbtFluid = new NBTTagCompound();
			nbtFluid.setString("FluidName", "water");
			nbtFluid.setInteger("Amount", 750000);
			nbt.setTag("Fluid", nbtFluid);
			stack.setTagCompound(nbt);
			stack.addEnchantment(Enchantment.getEnchantmentByLocation("cofhcore:holding"), 4);
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Stay_Hydrated", 10, new MessageRewardType(new MessagePart("Remember to stay hydrated!")), new ItemRewardType(new ItemPart(stack))));
		}
	}
}

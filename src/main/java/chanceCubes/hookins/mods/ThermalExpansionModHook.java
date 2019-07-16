package chanceCubes.hookins.mods;

import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.defaultRewards.BasicReward;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.MessagePart;
import chanceCubes.rewards.rewardtype.ItemRewardType;
import chanceCubes.rewards.rewardtype.MessageRewardType;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

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
				public void trigger(ItemPart s, World world, int x, int y, int z, PlayerEntity player)
				{
					CompoundNBT nbt;
					for(int i = 0; i < 5; i++)
					{
						nbt = new CompoundNBT();
						nbt.putString("Fluid", RewardsUtil.getRandomFluid().getTranslationKey());
						stack1.setTag(nbt);
						ItemEntity itemEnt = new ItemEntity(world, x + 0.5, y + 0.5, z + 0.5, stack1.copy());
						itemEnt.setPickupDelay(10);
						world.addEntity(itemEnt);
					}
				}
			}));
		}

		stack = RewardsUtil.getItemStack(super.modId, "reservoir", 1, 4);
		if(!stack.isEmpty())
		{
			CompoundNBT nbt = new CompoundNBT();
			nbt.putBoolean("Active", false);
			CompoundNBT nbtFluid = new CompoundNBT();
			nbtFluid.putString("FluidName", "water");
			nbtFluid.putInt("Amount", 750000);
			nbt.put("Fluid", nbtFluid);
			stack.setTag(nbt);
			stack.addEnchantment(ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("cofhcore:holding")), 4);
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Stay_Hydrated", 10, new MessageRewardType(new MessagePart("Remember to stay hydrated!")), new ItemRewardType(new ItemPart(stack))));
		}
	}
}

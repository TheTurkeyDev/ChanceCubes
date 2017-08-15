package chanceCubes.hookins.mods;

import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.defaultRewards.BasicReward;
import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.type.BlockRewardType;
import chanceCubes.rewards.type.CommandRewardType;
import chanceCubes.rewards.type.ItemRewardType;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ExtraUtilsModHook extends BaseModHook
{

	public ExtraUtilsModHook()
	{
		super("extrautils2");
		loadRewards();
	}

	@Override
	public void loadRewards()
	{
		ItemStack stack;
		Block block;

		// stack = RewardsUtil.getItemStack(super.modId, "machine", 1);
		// if(stack != null)
		// {
		// stack.setStackDisplayName("Useless Generator");
		// stack.setItemDamage(9);
		// ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Pink_Generator", 80, new ItemRewardType(new ItemPart(stack))));
		// }

		stack = RewardsUtil.getItemStack(super.modId, "wateringcan", 1);
		if(stack != null)
		{
			stack.setItemDamage(0);
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Watering_Can", 30, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "drum", 1);
		if(stack != null)
		{
			stack.setItemDamage(3);
			NBTTagCompound nbt = new NBTTagCompound();
			new FluidStack(FluidRegistry.WATER, 65536).writeToNBT(nbt);
			stack.setTagInfo("Fluid", nbt);
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Water_Drum", 80, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "bagofholding", 1);
		if(stack != null)
		{
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Golden_Bag", 90, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "plant/enderlilly", 3);
		if(stack != null)
		{
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Ender_Lilly", 65, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "compressedcobblestone", 4);
		if(stack != null)
		{
			stack.setItemDamage(5);
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Compressed_Cobble", 45, new ItemRewardType(new ItemPart(stack))));
		}

		block = RewardsUtil.getBlock(super.modId, "spike_iron");
		if(block != null)
		{
			OffsetBlock[] spikes = new OffsetBlock[34];
			int index = 0;
			for(int x = 0; x < 5; x++)
			{
				for(int z = 0; z < 5; z++)
				{
					if(x == 0 || x == 4 || z == 0 | z == 4)
					{
						spikes[index] = new OffsetBlock(x - 2, 0, z - 2, block, false).setRelativeToPlayer(true);
						spikes[index].setBlockState(RewardsUtil.getBlockStateFromBlockMeta(block, x == 0 ? 4 : x == 4 ? 5 : z == 0 ? 2 : 3));
						index++;
					}
					else
					{
						spikes[index] = new OffsetBlock(x - 2, -1, z - 2, block, false).setRelativeToPlayer(true);
						index++;
						spikes[index] = new OffsetBlock(x - 2, 2, z - 2, block, false).setRelativeToPlayer(true);
						spikes[index].setBlockState(RewardsUtil.getBlockStateFromBlockMeta(block, 1));
						index++;
					}

				}
			}
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Spikes", -40, new BlockRewardType(spikes)));
		}

		block = RewardsUtil.getBlock(super.modId, "cursedearthside");
		if(block != null)
		{
			OffsetBlock[] cursedEarth = new OffsetBlock[49];
			int index = 0;
			for(int x = 0; x < 7; x++)
			{
				for(int z = 0; z < 7; z++)
				{
					cursedEarth[index] = new OffsetBlock(x - 3, 0, z - 3, block, false).setRelativeToPlayer(true);
					index++;
				}
			}
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Cursed", -60, new BlockRewardType(cursedEarth), new CommandRewardType(new CommandPart("/time set 15000"))));
		}
	}
}

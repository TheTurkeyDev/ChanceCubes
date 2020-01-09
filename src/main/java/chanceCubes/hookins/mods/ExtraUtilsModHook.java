package chanceCubes.hookins.mods;

import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.rewards.defaultRewards.BasicReward;
import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.rewardtype.BlockRewardType;
import chanceCubes.rewards.rewardtype.CommandRewardType;
import chanceCubes.rewards.rewardtype.ItemRewardType;
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

		stack = RewardsUtil.getItemStack(super.modId, "machine", 1);
		if(!stack.isEmpty())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("Type", "extrautils2:generator_pink");
			stack.setTagCompound(nbt);
			stack.setStackDisplayName("Useless Generator");
			GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(this.modId + ":pink_generator", 80, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "wateringcan", 1);
		if(!stack.isEmpty())
		{
			stack.setItemDamage(0);
			GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(this.modId + ":watering_can", 30, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "drum", 1);
		if(!stack.isEmpty())
		{
			stack.setItemDamage(3);
			NBTTagCompound nbt = new NBTTagCompound();
			new FluidStack(FluidRegistry.WATER, 65536000).writeToNBT(nbt);
			stack.setTagInfo("Fluid", nbt);
			GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(this.modId + ":water_drum", 80, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "biomemarker", 1);
		if(!stack.isEmpty())
		{
			GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(this.modId + ":biome_marker", 75, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "snowglobe", 1);
		if(!stack.isEmpty())
		{
			GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(this.modId + ":snow_globe", 90, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "suncrystal", 1);
		if(!stack.isEmpty())
		{
			stack.setItemDamage(250);
			GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(this.modId + ":sun_crystal", 85, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "interactionproxy", 1);
		if(!stack.isEmpty())
		{
			GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(this.modId + ":ender_porcupine", 90, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "klein", 1);
		if(!stack.isEmpty())
		{
			GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(this.modId + ":klein_bottle", 85, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "minichest", 1);
		if(!stack.isEmpty())
		{
			stack.setStackDisplayName("World's Smallest Chest");
			GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(this.modId + ":mini_chest", 30, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "bagofholding", 1);
		if(!stack.isEmpty())
		{
			GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(this.modId + ":golden_bag", 90, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "enderlilly", 3);
		if(!stack.isEmpty())
		{
			GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(this.modId + ":ender_lilly", 65, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "compressedcobblestone", 4);
		if(!stack.isEmpty())
		{
			stack.setItemDamage(5);
			GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(this.modId + ":compressed_cobble", 45, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "compressednetherrack", 4);
		if(!stack.isEmpty())
		{
			stack.setItemDamage(5);
			GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(this.modId + ":compressed_netherrack", 65, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "compresseddirt", 4);
		if(!stack.isEmpty())
		{
			stack.setItemDamage(3);
			GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(this.modId + ":compressed_dirt", 50, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "compressedgravel", 4);
		if(!stack.isEmpty())
		{
			stack.setItemDamage(1);
			GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(this.modId + ":compressed_gravel", 45, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "compressedsand", 4);
		if(!stack.isEmpty())
		{
			stack.setItemDamage(1);
			GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(this.modId + ":compressed_sand", 45, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "decorativesolid", 1);
		if(!stack.isEmpty())
		{
			stack.setItemDamage(8);
			GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(this.modId + ":rainbow_stone", 85, new ItemRewardType(new ItemPart(stack))));
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
			GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(this.modId + ":spikes", -40, new BlockRewardType(spikes)));
		}

		block = RewardsUtil.getBlock(super.modId, "cursedearth");
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
			GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(this.modId + ":cursed", -60, new BlockRewardType(cursedEarth), new CommandRewardType(new CommandPart("/time set 15000"))));
		}
	}
}

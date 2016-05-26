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
import net.minecraft.block.BlockWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class ExtraUtilsModHook extends BaseModHook
{

	public ExtraUtilsModHook()
	{
		super("ExtraUtilities");
		loadRewards();
	}

	@Override
	public void loadRewards()
	{
		ItemStack stack;
		Block block;

		stack = RewardsUtil.getItemStack(super.modId, "unstableingot", 1);
		if(stack != null)
		{
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":unstableingot", -100, new ItemRewardType(new ItemPart(stack))
			{
				@Override
				public void trigger(ItemPart s, World world, int x, int y, int z, EntityPlayer player)
				{
					ItemStack stack1 = s.getItemStack().copy();
					NBTTagCompound ts = new NBTTagCompound();
					if(ts.hasKey("crafting"))
						ts.removeTag("crafting");

					if(stack1.getItemDamage() > 0)
						return;

					ts.setInteger("dimension", world.provider.getDimension());
					ts.setLong("time", world.getTotalWorldTime());
					stack1.setTagCompound(ts);

					world.setBlockState(new BlockPos(player.posX, player.posY, player.posZ), Blocks.CRAFTING_TABLE.getDefaultState());
					player.displayGui(new BlockWorkbench.InterfaceCraftingTable(world, new BlockPos(player.posX, player.posY, player.posZ)));
					player.inventory.setInventorySlotContents(player.inventory.currentItem, stack1);
					// if((player instanceof EntityPlayerMP))
					// ((EntityPlayerMP) player).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP) player);
				}
			}));
		}

		stack = RewardsUtil.getItemStack(super.modId, "generator.64", 1);
		if(stack != null)
		{
			stack.setStackDisplayName("Useless Generator");
			stack.setItemDamage(9);
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Pink_Generator", 80, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "watering_can", 1);
		if(stack != null)
		{
			stack.setItemDamage(2);
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Broken_Watering_Can", 30, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "drum", 1);
		if(stack != null)
		{
			stack.setItemDamage(1);
			IFluidContainerItem cont;
			try
			{
				cont = (IFluidContainerItem) stack.getItem();
				cont.fill(stack, new FluidStack(FluidRegistry.WATER, cont.getCapacity(stack)), true);
			} catch(Exception e)
			{
				e.printStackTrace();
			}
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Bedrockium_Drum", 80, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "golden_bag", 1);
		if(stack != null)
		{
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Golden_Bag", 90, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "plant/ender_lilly", 3);
		if(stack != null)
		{
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Ender_Lilly", 65, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "plant/ender_lilly", 10);
		if(stack != null)
		{
			stack.setItemDamage(2);
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Ender_Flux_Crystal", 80, new ItemRewardType(new ItemPart(stack))));
		}

		stack = RewardsUtil.getItemStack(super.modId, "cobblestone_compressed", 4);
		if(stack != null)
		{
			stack.setItemDamage(5);
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Compressed_Cobble", 45, new ItemRewardType(new ItemPart(stack))));
		}

		block = RewardsUtil.getBlock(super.modId, "spike_base");
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
						spikes[index].setData((byte) (x == 0 ? 5 : x == 4 ? 4 : z == 0 ? 3 : 2));
						index++;
					}
					else
					{
						spikes[index] = new OffsetBlock(x - 2, -1, z - 2, block, false).setRelativeToPlayer(true);
						spikes[index].setData((byte) 1);
						index++;
						spikes[index] = new OffsetBlock(x - 2, 2, z - 2, block, false).setRelativeToPlayer(true);
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

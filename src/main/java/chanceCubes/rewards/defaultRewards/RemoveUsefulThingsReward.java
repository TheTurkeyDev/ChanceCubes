package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.CCubesCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class RemoveUsefulThingsReward implements IChanceCubeReward
{
	List<Block> removables = new ArrayList<Block>();

	public RemoveUsefulThingsReward()
	{
		removables.add(Blocks.torch);
		removables.add(Blocks.stone_slab);
		removables.add(Blocks.furnace);
		removables.add(Blocks.glowstone);
		removables.add(Blocks.chest);
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		int removed = 0;
		for(int yy = -5; yy <= 5; yy++)
		{
			for(int xx = -5; xx <= 5; xx++)
			{
				for(int zz = -5; zz <= 5; zz++)
				{
					if(removables.contains(world.getBlockState(pos.add(xx, yy, zz)).getBlock()))
					{
						world.setBlockToAir(pos.add(xx, yy, zz));
						removed++;
					}
				}
			}
		}
		if(removed > 3)
		{
			player.addChatMessage(new TextComponentString("Look at all these useful things! #RIP"));
		}
		else
		{
			player.addChatMessage(new TextComponentString("Wow, only " + removed + " useful things around?"));
			player.addChatMessage(new TextComponentString("Here, let me give you a helping hand!"));

			for(int yy = -2; yy <= 2; yy++)
			{
				for(int xx = -5; xx <= 5; xx++)
				{
					for(int zz = -5; zz <= 5; zz++)
					{
						IBlockState blockState = world.getBlockState(pos.add(xx, yy, zz));
						if(blockState.getBlock().isOpaqueCube(blockState) && blockState.getBlock().equals(Blocks.air))
							world.setBlockState(pos.add(xx, yy, zz), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.UP));
					}
				}
			}
		}
	}

	@Override
	public int getChanceValue()
	{
		return -55;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Remove_Useful_Stuff";
	}

}
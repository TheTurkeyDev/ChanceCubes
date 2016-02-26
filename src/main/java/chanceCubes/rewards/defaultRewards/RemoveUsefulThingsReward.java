package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.CCubesCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
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
			player.addChatMessage(new ChatComponentText("Look at all these useful things! #RIP"));
		}
		else
		{
			player.addChatMessage(new ChatComponentText("Wow, only " + removed + " useful things around?"));
			player.addChatMessage(new ChatComponentText("Here, let me give you a helping hand!"));

			for(int yy = -2; yy <= 2; yy++)
				for(int xx = -5; xx <= 5; xx++)
					for(int zz = -5; zz <= 5; zz++)
						if(world.getBlockState(pos.add(xx, yy, zz)).getBlock().isOpaqueCube() && world.getBlockState(pos.add(xx, yy, zz)).getBlock().equals(Blocks.air))
							world.setBlockState(pos.add(xx, yy, zz), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.UP));
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
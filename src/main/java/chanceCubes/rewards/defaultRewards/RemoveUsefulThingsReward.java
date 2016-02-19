package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.CCubesCore;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
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
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		int removed = 0;
		for(int yy = -5; yy <= 5; yy++)
		{
			for(int xx = -5; xx <= 5; xx++)
			{
				for(int zz = -5; zz <= 5; zz++)
				{
					if(removables.contains(world.getBlock(x + xx, y + yy, z + zz)))
					{
						world.setBlock(x + xx, y + yy, z + zz, Blocks.air);
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
						if(world.getBlock(x + xx, y + yy, z + zz).isOpaqueCube() && world.getBlock(x + xx, y + yy + 1, z + zz).equals(Blocks.air))
							world.setBlock(x + xx, y + yy, z + zz, Blocks.torch);
		}

	}

	@Override
	public int getChanceValue()
	{
		return -20;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Remove_Useful_Stuff";
	}

}

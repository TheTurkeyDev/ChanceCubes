package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class TableFlipReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		player.addChatMessage(new ChatComponentText("(╯°□°）╯︵ ┻━┻)"));
		this.nextStep(0, world, x, y, z);
	}

	public void nextStep(final int stage, final World world, final int x, final int y, final int z)
	{
		Task task = new Task("Table_Flip", 10)
		{
			@Override
			public void callback()
			{
				switch(stage)
				{
					case 0:
					{
						world.setBlock(x, y, z, Blocks.wooden_slab);
						world.setBlockMetadataWithNotify(x, y, z, 8, 3);
						world.setBlock(x + 1, y, z, Blocks.oak_stairs);
						world.setBlockMetadataWithNotify(x + 1, y, z, 5, 3);
						world.setBlock(x - 1, y, z, Blocks.oak_stairs);
						world.setBlockMetadataWithNotify(x - 1, y, z, 12, 3);
						break;
					}
					case 1:
					{
						world.setBlock(x, y, z, Blocks.air);
						world.setBlock(x + 1, y, z, Blocks.air);
						world.setBlock(x - 1, y, z, Blocks.air);
						world.setBlock(x, y + 1, z, Blocks.wooden_slab);
						world.setBlockMetadataWithNotify(x, y + 1, z, 8, 3);
						world.setBlock(x + 1, y + 1, z, Blocks.oak_stairs);
						world.setBlockMetadataWithNotify(x + 1, y + 1, z, 5, 3);
						world.setBlock(x - 1, y + 1, z, Blocks.oak_stairs);
						world.setBlockMetadataWithNotify(x - 1, y + 1, z, 12, 3);
						break;
					}
					case 2:
					{
						world.setBlock(x, y + 1, z, Blocks.air);
						world.setBlock(x + 1, y + 1, z, Blocks.air);
						world.setBlock(x - 1, y + 1, z, Blocks.air);
						world.setBlock(x, y + 2, z + 1, Blocks.wooden_slab);
						world.setBlockMetadataWithNotify(x, y + 2, z + 1, 8, 3);
						world.setBlock(x + 1, y + 2, z + 1, Blocks.oak_stairs);
						world.setBlockMetadataWithNotify(x + 1, y + 2, z + 1, 5, 3);
						world.setBlock(x - 1, y + 2, z + 1, Blocks.oak_stairs);
						world.setBlockMetadataWithNotify(x - 1, y + 2, z + 1, 12, 3);
						break;
					}
					case 3:
					{
						world.setBlock(x, y + 2, z + 1, Blocks.air);
						world.setBlock(x + 1, y + 2, z + 1, Blocks.air);
						world.setBlock(x - 1, y + 2, z + 1, Blocks.air);
						world.setBlock(x, y + 1, z + 2, Blocks.wooden_slab);
						world.setBlock(x + 1, y + 1, z + 2, Blocks.oak_stairs);
						world.setBlockMetadataWithNotify(x + 1, y + 1, z + 2, 1, 3);
						world.setBlock(x - 1, y + 1, z + 2, Blocks.oak_stairs);
						break;
					}
					case 4:
					{
						world.setBlock(x, y + 1, z + 2, Blocks.air);
						world.setBlock(x + 1, y + 1, z + 2, Blocks.air);
						world.setBlock(x - 1, y + 1, z + 2, Blocks.air);
						world.setBlock(x, y, z + 2, Blocks.wooden_slab);
						world.setBlock(x + 1, y, z + 2, Blocks.oak_stairs);
						world.setBlockMetadataWithNotify(x + 1, y, z + 2, 1, 3);
						world.setBlock(x - 1, y, z + 2, Blocks.oak_stairs);
						break;
					}
				}
				
				if(stage < 4)
					nextStep(stage + 1, world, x, y, z);
			}

		};

		Scheduler.scheduleTask(task);
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Table_Flip";
	}

}

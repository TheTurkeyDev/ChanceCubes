package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
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
						RewardsUtil.placeBlock(Blocks.wooden_slab, world, x, y, z);
						world.setBlockMetadataWithNotify(x, y, z, 8, 3);
						RewardsUtil.placeBlock(Blocks.oak_stairs, world, x + 1, y, z);
						world.setBlockMetadataWithNotify(x + 1, y, z, 5, 3);
						RewardsUtil.placeBlock(Blocks.oak_stairs, world, x - 1, y, z);
						world.setBlockMetadataWithNotify(x + 1, y, z, 12, 3);
						break;
					}
					case 1:
					{
						RewardsUtil.placeBlock(Blocks.air, world, x, y, z);
						RewardsUtil.placeBlock(Blocks.air, world, x + 1, y, z);
						RewardsUtil.placeBlock(Blocks.air, world, x - 1, y, z);
						RewardsUtil.placeBlock(Blocks.wooden_slab, world, x, y + 1, z);
						world.setBlockMetadataWithNotify(x, y + 1, z, 8, 3);
						RewardsUtil.placeBlock(Blocks.oak_stairs, world, x + 1, y + 1, z);
						world.setBlockMetadataWithNotify(x + 1, y + 1, z, 5, 3);

						world.setBlockMetadataWithNotify(x - 1, y + 1, z, 12, 3);
						break;
					}
					case 2:
					{
						RewardsUtil.placeBlock(Blocks.air, world, x, y + 1, z);
						RewardsUtil.placeBlock(Blocks.air, world, x + 1, y + 1, z);
						RewardsUtil.placeBlock(Blocks.air, world, x - 1, y + 1, z);
						RewardsUtil.placeBlock(Blocks.wooden_slab, world, x, y + 2, z + 1);
						world.setBlockMetadataWithNotify(x, y + 2, z + 1, 8, 3);
						RewardsUtil.placeBlock(Blocks.oak_stairs, world, x + 1, y + 2, z + 1);
						world.setBlockMetadataWithNotify(x + 1, y + 2, z + 1, 5, 3);
						RewardsUtil.placeBlock(Blocks.oak_stairs, world, x - 1, y + 2, z + 1);
						world.setBlockMetadataWithNotify(x - 1, y + 2, z + 1, 12, 3);
						break;
					}
					case 3:
					{
						RewardsUtil.placeBlock(Blocks.air, world, x, y + 2, z + 1);
						RewardsUtil.placeBlock(Blocks.air, world, x + 1, y + 2, z + 1);
						RewardsUtil.placeBlock(Blocks.air, world, x - 1, y + 2, z + 1);
						RewardsUtil.placeBlock(Blocks.wooden_slab, world, x, y + 1, z + 2);
						RewardsUtil.placeBlock(Blocks.oak_stairs, world, x + 1, y + 1, z + 2);
						world.setBlockMetadataWithNotify(x + 1, y + 1, z + 2, 1, 3);
						RewardsUtil.placeBlock(Blocks.oak_stairs, world, x - 1, y + 1, z + 2);
						break;
					}
					case 4:
					{
						RewardsUtil.placeBlock(Blocks.air, world, x, y + 1, z + 2);
						RewardsUtil.placeBlock(Blocks.air, world, x + 1, y + 1, z + 2);
						RewardsUtil.placeBlock(Blocks.air, world, x - 1, y + 1, z + 2);
						RewardsUtil.placeBlock(Blocks.wooden_slab, world, x, y, z + 2);
						RewardsUtil.placeBlock(Blocks.oak_stairs, world, x + 1, y, z + 2);
						world.setBlockMetadataWithNotify(x + 1, y, z + 2, 1, 3);
						RewardsUtil.placeBlock(Blocks.oak_stairs, world, x - 1, y, z + 2);
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

package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SphereSnakeReward implements IChanceCubeReward
{

	// @formatter:off
	private IBlockState[] whitelist = { Blocks.OBSIDIAN.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.STONE.getDefaultState(), 
			Blocks.MELON_BLOCK.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), Blocks.CLAY.getDefaultState(),
			Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.byMetadata(RewardsUtil.rand.nextInt(16))),
			Blocks.BRICK_BLOCK.getDefaultState(), Blocks.WEB.getDefaultState(), Blocks.GLOWSTONE.getDefaultState(),
			Blocks.NETHERRACK.getDefaultState()};
	// @formatter:on

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		IBlockState state = whitelist[RewardsUtil.rand.nextInt(whitelist.length)];
		int[] posChange = { 0, 0, 0 };
		int[] currentDir = { 0, 1, 0 };

		Scheduler.scheduleTask(new Task("Sphere_Snake_Spawn_Delay", 1000, 10)
		{
			@Override
			public void callback()
			{

			}

			public void update()
			{
				boolean valid = false;
				int xChange;
				int yChange;
				int zChange;
				do
				{
					xChange = RewardsUtil.rand.nextInt(3) - 1;
					yChange = RewardsUtil.rand.nextInt(3) - 1;
					zChange = RewardsUtil.rand.nextInt(3) - 1;

					int dotProduct = xChange * currentDir[0] + yChange * currentDir[1] + zChange * currentDir[2];
					double changeLength = Math.sqrt((xChange * xChange) + (yChange * yChange) + (zChange * zChange));
					double currentLength = Math.sqrt((currentDir[0] * currentDir[0]) + (currentDir[1] * currentDir[1]) + (currentDir[2] * currentDir[2]));

					double angle = Math.acos(dotProduct / (changeLength * currentLength));
					valid = angle < 1.5708;
				} while(!valid);

				currentDir[0] = xChange;
				currentDir[1] = yChange;
				currentDir[2] = zChange;
				posChange[0] += xChange;
				posChange[1] += yChange;
				posChange[2] += zChange;
				BlockPos currentpos = new BlockPos(pos);
				currentpos = currentpos.add(posChange[0], posChange[1], posChange[2]);

				for(int yy = -3; yy < 4; yy++)
				{
					for(int zz = -3; zz < 4; zz++)
					{
						for(int xx = -3; xx < 4; xx++)
						{
							BlockPos loc = new BlockPos(xx, yy, zz);
							double dist = Math.abs(loc.getDistance(0, 0, 0));
							if(dist <= 3 && dist > 3 - 1)
							{
								world.setBlockState(currentpos.add(loc), state);
							}
						}
					}
				}
			}
		});
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Sphere_Snake";
	}

}

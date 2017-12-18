package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SquidChestReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		world.setBlockState(pos, Blocks.CHEST.getDefaultState());
		TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);
		Scheduler.scheduleTask(new Task("Squid_Chest_Init_Delay", 60)
		{

			@Override
			public void callback()
			{
				spawnSquids(world, pos, chest);
				chest.numPlayersUsing++;
				world.addBlockEvent(pos, chest.getBlockType(), 1, chest.numPlayersUsing);
				world.notifyNeighborsOfStateChange(pos, chest.getBlockType());
				world.notifyNeighborsOfStateChange(pos.down(), chest.getBlockType());
			}
		});
	}

	public void spawnSquids(World world, BlockPos pos, TileEntityChest chest)
	{
		Scheduler.scheduleTask(new Task("Squid_Chest_Squids", 250, 5)
		{
			@Override
			public void callback()
			{
				world.setBlockToAir(pos);
			}

			@Override
			public void update()
			{
				chest.numPlayersUsing++;
				EntitySquid squid = new EntitySquid(world);
				squid.setPositionAndRotation(pos.getX() + 0.5, pos.getY(), pos.getZ(), 90, 90);
				world.spawnEntityInWorld(squid);
				squid.motionX = 0;
				squid.motionY = 1.5;
				squid.motionZ = -50;
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
		return CCubesCore.MODID + ":Squid_Chest";
	}

}

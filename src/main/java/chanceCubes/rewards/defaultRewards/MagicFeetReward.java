package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.CustomEntry;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class MagicFeetReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		player.sendMessage(new TextComponentString("<Dovah_Jun> You've got magic feet!!!"));
		Scheduler.scheduleTask(new Task("Megic_Feet_Reward_Delay", 300, 2)
		{
			BlockPos last = pos;

			@Override
			public void callback()
			{
				player.sendMessage(new TextComponentString("<Dovah_Jun> You've used up all the magic in your feet!"));
			}

			@Override
			public void update()
			{
				BlockPos beneth = player.getPosition().add(0, -1, 0);
				if(!world.isAirBlock(beneth) && world.getTileEntity(beneth) == null && !last.equals(beneth))
				{
					CustomEntry<Block, Integer> block = RewardsUtil.getRandomOre();
					RewardsUtil.placeBlock(RewardsUtil.getBlockStateFromBlockMeta(block.getKey(), block.getValue()), world, beneth);
					last = beneth;
				}
			}
		});
	}

	@Override
	public int getChanceValue()
	{
		return 70;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Magic_Feet";
	}

}
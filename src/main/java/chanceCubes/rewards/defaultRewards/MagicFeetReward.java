package chanceCubes.rewards.defaultRewards;

import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.CustomEntry;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketTitle.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class MagicFeetReward extends BaseCustomReward
{
	public MagicFeetReward()
	{
		super(CCubesCore.MODID + ":magic_feet", 85);
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
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

				if(this.delayLeft % 20 == 0)
					this.showTimeLeft(player, Type.ACTIONBAR);
			}
		});
	}
}
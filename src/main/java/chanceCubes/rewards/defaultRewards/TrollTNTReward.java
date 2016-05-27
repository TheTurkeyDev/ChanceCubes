package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class TrollTNTReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, BlockPos pos, final EntityPlayer player)
	{
		for(int x = -1; x < 2; x++)
		{
			for(int z = -1; z < 2; z++)
			{
				RewardsUtil.placeBlock(Blocks.WEB.getDefaultState(), world, new BlockPos(player.posX + x, player.posY, player.posZ + z));
			}
		}

		final EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, player.posX + 1D, player.posY + 1D, player.posZ, player);
		world.spawnEntityInWorld(entitytntprimed);
		world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);

		if(world.rand.nextInt(5) != 1)
		{
			Task task = new Task("TrollTNT", 80)
			{
				@Override
				public void callback()
				{
					timeUp(entitytntprimed, player);
				}

			};

			Scheduler.scheduleTask(task);
		}
	}

	private void timeUp(Entity ent, EntityPlayer player)
	{
		player.addChatMessage(new TextComponentString("BOOM"));
		ent.setDead();
	}

	@Override
	public int getChanceValue()
	{
		return -5;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":TrollTNT";
	}
}
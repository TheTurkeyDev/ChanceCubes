package chanceCubes.rewards;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class TrollTNTReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, BlockPos pos, final EntityPlayer player)
	{
		for(int x = -1; x < 2; x++)
		{
			for(int z = -1; z < 2; z++)
			{
				world.setBlockState(new BlockPos(player.posX + x, player.posY, player.posZ + z), Blocks.web.getDefaultState());
			}
		}

		final EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, player.posX + 1D, player.posY + 1D, player.posZ, player);
		world.spawnEntityInWorld(entitytntprimed);
		world.playSoundAtEntity(entitytntprimed, "game.tnt.primed", 1.0F, 1.0F);

		if(world.rand.nextInt(25) != 1)
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
		player.addChatMessage(new ChatComponentText("BOOM"));
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
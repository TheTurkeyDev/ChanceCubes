package chanceCubes.rewards;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class WitherReward implements IChanceCubeReward
{
	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		final EntityWither wither = new EntityWither(world);
		wither.setLocationAndAngles((double)x + 0.5D, (double)y + 1D, (double)z + 1.5D, 90.0F, 0.0F);
		wither.renderYawOffset = 90.0F;
		wither.func_82206_m();
		wither.setCustomNameTag("Kiwi");
		world.spawnEntityInWorld(wither);
		
		player.addChatMessage(new ChatComponentText("\"You've got to ask yourself one question: 'Do I feel lucky?' Well, do ya, punk?\""));

		Task task = new Task("Wither Reward", 390)
		{
			@Override
			public void callback()
			{
				removeEnts(wither);
			}
		};

		Scheduler.scheduleTask(task);
	}

	private void removeEnts(Entity ent)
	{
		if(ent.worldObj.rand.nextInt(25) != 1)
			ent.setDead();
	}

	@Override
	public int getChanceValue()
	{
		return -100;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Wither";
	}

}

package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CookieMonsterReward implements IChanceCubeReward
{

	@Override
	public void trigger(final World world, final BlockPos pos, final EntityPlayer player)
	{
		RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "Here have some cookies!");
		Entity itemEnt = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.COOKIE, 8));
		world.spawnEntity(itemEnt);

		Scheduler.scheduleTask(new Task("Cookie Monster", 30)
		{
			@Override
			public void callback()
			{
				EntityZombie cm = new EntityZombie(world);
				cm.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
				cm.setChild(true);
				cm.setCustomNameTag("Cookie Monster");
				RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "[Cookie Monster] Hey! Those are mine!");
				world.spawnEntity(cm);
			}

		});
	}

	@Override
	public int getChanceValue()
	{
		return -5;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Cookie_Monster";
	}
}
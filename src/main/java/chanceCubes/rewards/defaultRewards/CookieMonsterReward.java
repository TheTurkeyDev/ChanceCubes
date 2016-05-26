package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class CookieMonsterReward implements IChanceCubeReward
{

	@Override
	public void trigger(final World world, final BlockPos pos, final EntityPlayer player)
	{
		if (!world.isRemote)
		{
			player.addChatMessage(new TextComponentString("Here have some cookies!"));
			Entity itemEnt = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.COOKIE, 8));
			world.spawnEntityInWorld(itemEnt);

			Task task = new Task("Cookie Monster", 30)
			{
				@Override
				public void callback()
				{
					SpawnCM();
				}

				private void SpawnCM()
				{
					EntityZombie cm = new EntityZombie(world);
					cm.setPosition(pos.getX(), pos.getY(), pos.getZ());
					cm.setChild(true);
					cm.setCustomNameTag("Cookie Monster");
					player.addChatMessage(new TextComponentString("[Cookie Monster] Hey! Those are mine!"));
					world.spawnEntityInWorld(cm);
				}

			};

			Scheduler.scheduleTask(task);
		}

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
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
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class CookieMonsterReward implements IChanceCubeReward
{

	@Override
	public void trigger(final World world, final int x, final int y, final int z, final EntityPlayer player)
	{
		if (!world.isRemote)
		{
			player.addChatMessage(new ChatComponentText("Here have some cookies!"));
			Entity itemEnt = new EntityItem(world, x, y, z, new ItemStack(Items.cookie, 8));
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
					cm.setPosition(x, y, z);
					cm.setChild(true);
					cm.setCustomNameTag("Cookie Monster");
					player.addChatMessage(new ChatComponentText("[Cookie Monster] Hey! Those are mine!"));
					world.spawnEntityInWorld(cm);
				}

			};

			Scheduler.scheduleTask(task);
		}

	}

	@Override
	public int getChanceValue()
	{
		return -50;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Cookie_Monster";
	}
}
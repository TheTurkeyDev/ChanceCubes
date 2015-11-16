package chanceCubes.rewards;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class CookieMonsterReward implements IChanceCubeReward
{

	@Override
	public void trigger(final World world, final BlockPos pos, final EntityPlayer player)
	{
		if (!world.isRemote)
		{
			player.addChatMessage(new ChatComponentText("Here have some cookies!"));
			Entity itemEnt = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.cookie, 8));
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
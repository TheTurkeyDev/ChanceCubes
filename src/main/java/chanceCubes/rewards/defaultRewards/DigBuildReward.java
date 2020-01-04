package chanceCubes.rewards.defaultRewards;

import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesDamageSource;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketTitle.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class DigBuildReward extends BaseCustomReward
{

	public DigBuildReward()
	{
		super(CCubesCore.MODID + ":dig_build", -5);
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		int min = super.getSettingAsInt(settings, "min", 5, 0, 100);
		int max = super.getSettingAsInt(settings, "max", 25, 0, 100);

		//Because someone will do it...
		if(min > max)
		{
			int swap = min;
			min = max;
			max = swap;
		}

		int initalY = player.getPosition().getY();
		int distance = RewardsUtil.rand.nextInt(max - min) + min;
		boolean up = (initalY + distance <= 150) && ((initalY - distance < 2) || RewardsUtil.rand.nextBoolean());

		player.sendMessage(new TextComponentString("Quick! Go " + (up ? "up " : "down ") + distance + " blocks!"));
		player.sendMessage(new TextComponentString("You have " + (distance + 3) + " seconds!"));

		Scheduler.scheduleTask(new Task("Dig_Build_Reward_Delay", (distance + 3) * 20, 5)
		{
			@Override
			public void callback()
			{
				player.world.createExplosion(player, player.posX, player.posY, player.posZ, 1.0F, false);
				player.attackEntityFrom(CCubesDamageSource.DIG_BUILD_FAIL, Float.MAX_VALUE);
			}

			@Override
			public void update()
			{
				if(up && player.getPosition().getY() >= initalY + distance)
				{
					player.sendMessage(new TextComponentString("Good Job!"));
					player.sendMessage(new TextComponentString("Here, have a item!"));
					player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, new ItemStack(RewardsUtil.getRandomItem(), 1)));
					Scheduler.removeTask(this);
				}
				else if(!up && player.getPosition().getY() <= initalY - distance)
				{
					player.sendMessage(new TextComponentString("Good Job!"));
					player.sendMessage(new TextComponentString("Here, have a item!"));
					player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, new ItemStack(RewardsUtil.getRandomItem(), 1)));
					Scheduler.removeTask(this);
				}

				if(this.delayLeft % 20 == 0)
					this.showTimeLeft(player, Type.ACTIONBAR);
			}
		});
	}
}
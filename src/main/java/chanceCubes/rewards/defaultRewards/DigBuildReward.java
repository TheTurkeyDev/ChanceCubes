package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.CCubesDamageSource;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class DigBuildReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		int initalY = player.getPosition().getY();
		int distance = RewardsUtil.rand.nextInt(20) + 5;
		boolean up = (initalY + distance > 150) ? false : ((initalY - distance < 2) ? true : RewardsUtil.rand.nextBoolean());

		player.addChatMessage(new TextComponentString("Quick! Go " + (up ? "up " : "down ") + distance + " blocks!"));
		player.addChatMessage(new TextComponentString("You have " + (distance + 3) + " seconds!"));

		Scheduler.scheduleTask(new Task("Dig_Build_Reward_Delay", (distance + 3) * 20, 5)
		{
			@Override
			public void callback()
			{
				player.worldObj.createExplosion(player, player.posX, player.posY, player.posZ, 1.0F, false);
				player.attackEntityFrom(CCubesDamageSource.DIG_BUILD_FAIL, Float.MAX_VALUE);
			}

			@Override
			public void update()
			{
				if(up && player.getPosition().getY() >= initalY + distance)
				{
					player.addChatMessage(new TextComponentString("Good Job!"));
					player.addChatMessage(new TextComponentString("Here, have a item!"));
					player.worldObj.spawnEntityInWorld(new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, new ItemStack(RewardsUtil.getRandomItem(), 1)));
					Scheduler.removeTask(this);
				}
				else if(!up && player.getPosition().getY() <= initalY - distance)
				{
					player.addChatMessage(new TextComponentString("Good Job!"));
					player.addChatMessage(new TextComponentString("Here, have a item!"));
					player.worldObj.spawnEntityInWorld(new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, new ItemStack(RewardsUtil.getRandomItem(), 1)));
					Scheduler.removeTask(this);
				}
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
		return CCubesCore.MODID + ":Dig_Build_Reward";
	}

}

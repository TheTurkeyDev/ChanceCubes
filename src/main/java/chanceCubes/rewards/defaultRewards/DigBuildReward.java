package chanceCubes.rewards.defaultRewards;

import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesDamageSource;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.STitlePacket.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.World;

public class DigBuildReward extends BaseCustomReward
{

	public DigBuildReward()
	{
		super(CCubesCore.MODID + ":Dig_Build_Reward", -5);
	}

	@Override
	public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		int initalY = player.getPosition().getY();
		int distance = RewardsUtil.rand.nextInt(20) + 5;
		boolean up = (initalY + distance <= 150) && ((initalY - distance < 2) || RewardsUtil.rand.nextBoolean());

		player.sendMessage(new StringTextComponent("Quick! Go " + (up ? "up " : "down ") + distance + " blocks!"));
		player.sendMessage(new StringTextComponent("You have " + (distance + 3) + " seconds!"));

		Scheduler.scheduleTask(new Task("Dig_Build_Reward_Delay", (distance + 3) * 20, 5)
		{
			@Override
			public void callback()
			{
				player.world.createExplosion(player, player.posX, player.posY, player.posZ, 1.0F, Mode.BREAK);
				player.attackEntityFrom(CCubesDamageSource.DIG_BUILD_FAIL, Float.MAX_VALUE);
			}

			@Override
			public void update()
			{
				if(up && player.getPosition().getY() >= initalY + distance)
				{
					player.sendMessage(new StringTextComponent("Good Job!"));
					player.sendMessage(new StringTextComponent("Here, have a item!"));
					player.world.addEntity(new ItemEntity(player.world, player.posX, player.posY, player.posZ, new ItemStack(RewardsUtil.getRandomItem(), 1)));
					Scheduler.removeTask(this);
				}
				else if(!up && player.getPosition().getY() <= initalY - distance)
				{
					player.sendMessage(new StringTextComponent("Good Job!"));
					player.sendMessage(new StringTextComponent("Here, have a item!"));
					player.world.addEntity(new ItemEntity(player.world, player.posX, player.posY, player.posZ, new ItemStack(RewardsUtil.getRandomItem(), 1)));
					Scheduler.removeTask(this);
				}

				if(this.delayLeft % 20 == 0)
					this.showTimeLeft(player, Type.ACTIONBAR);
			}
		});
	}
}
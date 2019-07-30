package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class WolvesToCreepersReward extends BaseCustomReward
{
	public WolvesToCreepersReward()
	{
		super(CCubesCore.MODID + ":Wolves_To_Creepers", -20);
	}

	@Override
	public void trigger(final World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		final List<Entity> wolves = new ArrayList<>();
		for(int i = 0; i < 10; i++)
		{
			for(int yy = 0; yy < 4; yy++)
				for(int xx = -1; xx < 2; xx++)
					for(int zz = -1; zz < 2; zz++)
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(xx, yy, zz));

			WolfEntity wolf = EntityType.WOLF.create(world);
			wolf.setPosition(pos.getX(), pos.getY(), pos.getZ());
			wolf.setTamed(true);
			wolf.setOwnerId(player.getUniqueID());
			wolf.setCustomName(new StringTextComponent("Kehaan"));
			wolves.add(wolf);
			world.addEntity(wolf);
		}

		RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "Do they look weird to you?");

		Scheduler.scheduleTask(new Task("Mob_Switch", 200)
		{
			@Override
			public void callback()
			{
				for(Entity wolf : wolves)
				{
					wolf.remove();
					CreeperEntity creeper = EntityType.CREEPER.create(world);
					creeper.setPositionAndRotation(wolf.posX, wolf.posY, wolf.posZ, wolf.rotationYaw, wolf.rotationPitch);
					creeper.setCustomName(new StringTextComponent("Jacky"));
					world.addEntity(creeper);
				}
			}
		});
	}
}

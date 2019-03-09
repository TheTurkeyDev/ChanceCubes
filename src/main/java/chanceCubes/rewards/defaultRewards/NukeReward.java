package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NukeReward extends BaseCustomReward
{
	public NukeReward()
	{
		super(CCubesCore.MODID + ":Nuke", -75);
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "May death rain upon them");
		world.spawnEntity(new EntityTNTPrimed(world, pos.getX() - 6, pos.getY() + 65, pos.getZ() - 6, null));
		world.spawnEntity(new EntityTNTPrimed(world, pos.getX() - 2, pos.getY() + 65, pos.getZ() - 6, null));
		world.spawnEntity(new EntityTNTPrimed(world, pos.getX() + 2, pos.getY() + 65, pos.getZ() - 6, null));
		world.spawnEntity(new EntityTNTPrimed(world, pos.getX() + 6, pos.getY() + 65, pos.getZ() - 6, null));
		world.spawnEntity(new EntityTNTPrimed(world, pos.getX() - 6, pos.getY() + 65, pos.getZ() - 2, null));
		world.spawnEntity(new EntityTNTPrimed(world, pos.getX() - 2, pos.getY() + 65, pos.getZ() - 2, null));
		world.spawnEntity(new EntityTNTPrimed(world, pos.getX() + 2, pos.getY() + 65, pos.getZ() - 2, null));
		world.spawnEntity(new EntityTNTPrimed(world, pos.getX() + 6, pos.getY() + 65, pos.getZ() - 2, null));
		world.spawnEntity(new EntityTNTPrimed(world, pos.getX() - 6, pos.getY() + 65, pos.getZ() + 2, null));
		world.spawnEntity(new EntityTNTPrimed(world, pos.getX() - 2, pos.getY() + 65, pos.getZ() + 2, null));
		world.spawnEntity(new EntityTNTPrimed(world, pos.getX() + 2, pos.getY() + 65, pos.getZ() + 2, null));
		world.spawnEntity(new EntityTNTPrimed(world, pos.getX() + 6, pos.getY() + 65, pos.getZ() + 2, null));
		world.spawnEntity(new EntityTNTPrimed(world, pos.getX() - 6, pos.getY() + 65, pos.getZ() + 6, null));
		world.spawnEntity(new EntityTNTPrimed(world, pos.getX() - 2, pos.getY() + 65, pos.getZ() + 6, null));
		world.spawnEntity(new EntityTNTPrimed(world, pos.getX() + 2, pos.getY() + 65, pos.getZ() + 6, null));
		world.spawnEntity(new EntityTNTPrimed(world, pos.getX() + 6, pos.getY() + 65, pos.getZ() + 6, null));
	}
}

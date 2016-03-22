package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class NukeReward implements IChanceCubeReward
{
	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		player.addChatMessage(new TextComponentString("May death rain upon them"));
		world.spawnEntityInWorld(new EntityTNTPrimed(world, pos.getX() - 6, pos.getY() + 65, pos.getZ() - 6, null));
		world.spawnEntityInWorld(new EntityTNTPrimed(world, pos.getX() - 2, pos.getY() + 65, pos.getZ() - 6, null));
		world.spawnEntityInWorld(new EntityTNTPrimed(world, pos.getX() + 2, pos.getY() + 65, pos.getZ() - 6, null));
		world.spawnEntityInWorld(new EntityTNTPrimed(world, pos.getX() + 6, pos.getY() + 65, pos.getZ() - 6, null));
		world.spawnEntityInWorld(new EntityTNTPrimed(world, pos.getX() - 6, pos.getY() + 65, pos.getZ() - 2, null));
		world.spawnEntityInWorld(new EntityTNTPrimed(world, pos.getX() - 2, pos.getY() + 65, pos.getZ() - 2, null));
		world.spawnEntityInWorld(new EntityTNTPrimed(world, pos.getX() + 2, pos.getY() + 65, pos.getZ() - 2, null));
		world.spawnEntityInWorld(new EntityTNTPrimed(world, pos.getX() + 6, pos.getY() + 65, pos.getZ() - 2, null));
		world.spawnEntityInWorld(new EntityTNTPrimed(world, pos.getX() - 6, pos.getY() + 65, pos.getZ() + 2, null));
		world.spawnEntityInWorld(new EntityTNTPrimed(world, pos.getX() - 2, pos.getY() + 65, pos.getZ() + 2, null));
		world.spawnEntityInWorld(new EntityTNTPrimed(world, pos.getX() + 2, pos.getY() + 65, pos.getZ() + 2, null));
		world.spawnEntityInWorld(new EntityTNTPrimed(world, pos.getX() + 6, pos.getY() + 65, pos.getZ() + 2, null));
		world.spawnEntityInWorld(new EntityTNTPrimed(world, pos.getX() - 6, pos.getY() + 65, pos.getZ() + 6, null));
		world.spawnEntityInWorld(new EntityTNTPrimed(world, pos.getX() - 2, pos.getY() + 65, pos.getZ() + 6, null));
		world.spawnEntityInWorld(new EntityTNTPrimed(world, pos.getX() + 2, pos.getY() + 65, pos.getZ() + 6, null));
		world.spawnEntityInWorld(new EntityTNTPrimed(world, pos.getX() + 6, pos.getY() + 65, pos.getZ() + 6, null));
	}

	@Override
	public int getChanceValue()
	{
		return -75;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Nuke";
	}

}

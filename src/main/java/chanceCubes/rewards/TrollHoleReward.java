package chanceCubes.rewards;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.util.Location3I;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class TrollHoleReward implements IChanceCubeReward
{

	@Override
	public void trigger(final World world, int x, int y, int z, final EntityPlayer player)
	{

		final Map<Location3I, Block> storedBlocks = new HashMap<Location3I, Block>();
		final int px = (int) Math.floor(player.posX);
		final int py = (int) Math.floor(player.posY) - 1;
		final int pz = (int) Math.floor(player.posZ);

		for(int yy = 0; yy < 75; yy++)
		{
			for(int xx = -2; xx < 3; xx++)
			{
				for(int zz = -2; zz < 3; zz++)
				{
					storedBlocks.put(new Location3I(xx, yy, zz), world.getBlock(px + xx, py - yy, pz + zz));
					world.setBlockToAir(px + xx, py - yy, pz + zz);
				}
			}
		}

		Task task = new Task("TrollHole", 70)
		{
			@Override
			public void callback()
			{
				fillHole(world, player, px, py, pz, storedBlocks);
			}

		};

		Scheduler.scheduleTask(task);
	}

	public void fillHole(World world, EntityPlayer player, int x, int y, int z, Map<Location3I, Block> storedBlocks)
	{
		for(Location3I loc : storedBlocks.keySet())
			world.setBlock(x + loc.getX(), y - loc.getY(), z + loc.getZ(), storedBlocks.get(loc));

		player.setPositionAndUpdate(x, y + 1, z);
		player.motionY = 0;
		player.fallDistance = 0;
	}

	@Override
	public int getChanceValue()
	{
		return -20;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":TrollHole";
	}
}
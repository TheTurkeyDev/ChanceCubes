package chanceCubes.rewards.defaultRewards;

import java.util.Random;

import chanceCubes.CCubesCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class AnvilRain implements IChanceCubeReward
{
	private Random rand = new Random();

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		int x1 = x + (rand.nextInt(9) - 4);
		int z1 = z + (rand.nextInt(9) - 4);

		int x2 = x + (rand.nextInt(9) - 4);
		int z2 = z + (rand.nextInt(9) - 4);

		int x3 = x + (rand.nextInt(9) - 4);
		int z3 = z + (rand.nextInt(9) - 4);

		int x4 = x + (rand.nextInt(9) - 4);
		int z4 = z + (rand.nextInt(9) - 4);

		int x5 = x + (rand.nextInt(9) - 4);
		int z5 = z + (rand.nextInt(9) - 4);

		int yy = 0;
		for(yy = 0; yy < 25; yy++)
			world.setBlockToAir(x, y + yy, z);
		for(yy = 0; yy < 25; yy++)
			world.setBlockToAir(x1, y + yy, z1);
		for(yy = 0; yy < 25; yy++)
			world.setBlockToAir(x2, y + yy, z2);
		for(yy = 0; yy < 25; yy++)
			world.setBlockToAir(x3, y + yy, z3);
		for(yy = 0; yy < 25; yy++)
			world.setBlockToAir(x4, y + yy, z4);
		for(yy = 0; yy < 25; yy++)
			world.setBlockToAir(x5, y + yy, z5);
		for(yy = 0; yy < 25; yy++)
			world.setBlockToAir((int) player.posX, y + yy, (int) player.posZ);

		world.setBlock(x, y + 25, z, Blocks.anvil);
		world.setBlock(x1, y + 25, z1, Blocks.anvil);
		world.setBlock(x2, y + 25, z2, Blocks.anvil);
		world.setBlock(x3, y + 25, z3, Blocks.anvil);
		world.setBlock(x4, y + 25, z4, Blocks.anvil);
		world.setBlock(x5, y + 25, z5, Blocks.anvil);
		world.setBlock((int) player.posX, y + 25, (int) player.posZ, Blocks.anvil);

		for(int xx = 0; xx < 2; xx++)
		{
			int xxx = xx == 1 ? x + 5 : x - 5;
			for(int zz = -5; zz < 6; zz++)
			{
				for(int yyy = 0; yyy < 3; yyy++)
				{
					world.setBlock(xxx, yyy + y, zz + z, Blocks.cobblestone);
				}
			}
		}

		for(int xx = -5; xx < 6; xx++)
		{
			for(int zz = 0; zz < 2; zz++)
			{
				int zzz = zz == 1 ? z + 5 : z - 5;
				for(int yyy = 0; yyy < 3; yyy++)
				{
					world.setBlock(xx + x, yyy + y, zzz, Blocks.cobblestone);
				}
			}
		}
	}

	@Override
	public int getChanceValue()
	{
		return -45;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":AnvilRain";
	}
}
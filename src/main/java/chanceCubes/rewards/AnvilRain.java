package chanceCubes.rewards;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;

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

		for(int yy = 0; yy < 25; yy++)
			world.setBlockToAir(x, y+yy, z);
		for(int yy = 0; yy < 25; yy++)
			world.setBlockToAir(x1, y+yy, z1);
		for(int yy = 0; yy < 25; yy++)
			world.setBlockToAir(x2, y+yy, z2);
		for(int yy = 0; yy < 25; yy++)
			world.setBlockToAir(x3, y+yy, z3);
		for(int yy = 0; yy < 25; yy++)
			world.setBlockToAir(x4, y+yy, z4);
		for(int yy = 0; yy < 25; yy++)
			world.setBlockToAir(x5, y+yy, z5);
		
		world.setBlock(x, y+25, z, Blocks.anvil);
		world.setBlock(x1, y+25, z1, Blocks.anvil);
		world.setBlock(x2, y+25, z2, Blocks.anvil);
		world.setBlock(x3, y+25, z3, Blocks.anvil);
		world.setBlock(x4, y+25, z4, Blocks.anvil);
		world.setBlock(x5, y+25, z5, Blocks.anvil);
	}

	@Override
	public int getChanceValue()
	{
		return -90;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID+":AnvilRain";
	}

}
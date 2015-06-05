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

		int x1 = rand.nextInt(12);
		int z1 = rand.nextInt(12);

		int x2 = rand.nextInt(12);
		int z2 = rand.nextInt(12);

		int x3 = rand.nextInt(12);
		int z3 = rand.nextInt(12);

		int x4 = rand.nextInt(12);
		int z4 = rand.nextInt(12);

		int x5 = rand.nextInt(12);
		int z5 = rand.nextInt(12);

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
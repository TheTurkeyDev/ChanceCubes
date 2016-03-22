package chanceCubes.rewards.defaultRewards;

import java.util.Random;

import chanceCubes.CCubesCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AnvilRain implements IChanceCubeReward
{
	private Random rand = new Random();

	@Override
	public void trigger(World world, BlockPos position, EntityPlayer player)
	{
		int x = position.getX();
		int y = position.getY();
		int z = position.getZ();
		
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
			world.setBlockToAir(new BlockPos(x, y + yy, z));
		for(yy = 0; yy < 25; yy++)
			world.setBlockToAir(new BlockPos(x1, y + yy, z1));
		for(yy = 0; yy < 25; yy++)
			world.setBlockToAir(new BlockPos(x2, y + yy, z2));
		for(yy = 0; yy < 25; yy++)
			world.setBlockToAir(new BlockPos(x3, y + yy, z3));
		for(yy = 0; yy < 25; yy++)
			world.setBlockToAir(new BlockPos(x4, y + yy, z4));
		for(yy = 0; yy < 25; yy++)
			world.setBlockToAir(new BlockPos(x5, y + yy, z5));
		for(yy = 0; yy < 25; yy++)
			world.setBlockToAir(new BlockPos((int) player.posX, y + yy, (int) player.posZ));

		world.setBlockState(new BlockPos(x, y + 25, z), Blocks.anvil.getDefaultState());
		world.setBlockState(new BlockPos(x1, y + 25, z1), Blocks.anvil.getDefaultState());
		world.setBlockState(new BlockPos(x2, y + 25, z2), Blocks.anvil.getDefaultState());
		world.setBlockState(new BlockPos(x3, y + 25, z3), Blocks.anvil.getDefaultState());
		world.setBlockState(new BlockPos(x4, y + 25, z4), Blocks.anvil.getDefaultState());
		world.setBlockState(new BlockPos(x5, y + 25, z5), Blocks.anvil.getDefaultState());
		world.setBlockState(new BlockPos((int) player.posX, y + 25, (int) player.posZ), Blocks.anvil.getDefaultState());
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
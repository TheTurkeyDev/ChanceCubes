package chanceCubes.rewards.defaultRewards;

import java.util.Random;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.util.RewardsUtil;

public class DiscoReward implements IChanceCubeReward
{
	private Random rand = new Random();

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		for(int xx = -4; xx < 5; xx++)
			for(int zz = -4; zz < 5; zz++)
				RewardsUtil.placeBlock(Blocks.wool, world, x + xx, y - 1, z + zz, rand.nextInt(16));

		for(int i = 0; i < 10; i++)
		{
			EntitySheep sheep = new EntitySheep(world);
			sheep.setCustomNameTag("jeb_");
			sheep.setLocationAndAngles(x, y + 1, z, 0, 0);
			world.spawnEntityInWorld(sheep);
		}

		RewardsUtil.placeBlock(CCubesBlocks.chanceIcosahedron, world, x, y + 3, z);

		RewardsUtil.sendMessageToNearPlayers(world, x, y, z, 32, "Disco Party!!!!");
	}

	@Override
	public int getChanceValue()
	{
		return 40;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Disco";
	}

}

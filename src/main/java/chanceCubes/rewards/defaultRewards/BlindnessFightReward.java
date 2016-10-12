package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlindnessFightReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, BlockPos position, EntityPlayer player)
	{
		int x = position.getX();
		int y = position.getY();
		int z = position.getZ();

		player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 2));
		player.addChatMessage(new TextComponentString("Fight!!!"));

		for(int xx = -2; xx < 3; xx++)
		{
			for(int zz = -2; zz < 3; zz++)
			{
				RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, new BlockPos(x + xx, y, z + zz));
				RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, new BlockPos(x + xx, y + 1, z + zz));
			}
		}

		for(int i = 0; i < 6; i++)
		{
			EntitySkeleton skele = new EntitySkeleton(world);
			skele.onInitialSpawn(world.getDifficultyForLocation(skele.getPosition()), null);
			skele.setPosition(x + 0.5, y, z + 0.5);
			world.spawnEntityInWorld(skele);
		}
	}

	@Override
	public int getChanceValue()
	{
		return -65;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Blindness_Fight";
	}
}
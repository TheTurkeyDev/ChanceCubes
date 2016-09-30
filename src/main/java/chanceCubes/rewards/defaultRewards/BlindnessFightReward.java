package chanceCubes.rewards.defaultRewards;

import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;

public class BlindnessFightReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		player.addPotionEffect(new PotionEffect(Potion.blindness.id, 100, 2));
		player.addChatMessage(new ChatComponentText("Fight!!!"));

		for(int xx = -2; xx < 3; xx++)
		{
			for(int zz = -2; zz < 3; zz++)
			{
				RewardsUtil.placeBlock(Blocks.air, world, x + xx, y, z + zz);
				RewardsUtil.placeBlock(Blocks.air, world, x + xx, y + 1, z + zz);
			}
		}

		for(int i = 0; i < 6; i++)
		{
			EntitySkeleton skele = new EntitySkeleton(world);
			skele.onSpawnWithEgg(null);
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
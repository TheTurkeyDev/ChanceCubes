package chanceCubes.rewards;

import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;

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
				world.setBlockToAir(x+xx, y, z+zz);
				world.setBlockToAir(x+xx, y + 1, z+zz);
			}
		}
		
		EntitySkeleton skele = new EntitySkeleton(world);
		skele.setPosition(x, y, z);
		world.spawnEntityInWorld(skele);
		
		skele = new EntitySkeleton(world);
		skele.setPosition(x, y, z);
		world.spawnEntityInWorld(skele);
		
		skele = new EntitySkeleton(world);
		skele.setPosition(x, y, z);
		world.spawnEntityInWorld(skele);
		
		skele = new EntitySkeleton(world);
		skele.setPosition(x, y, z);
		world.spawnEntityInWorld(skele);
		
		skele = new EntitySkeleton(world);
		skele.setPosition(x, y, z);
		world.spawnEntityInWorld(skele);
		
		skele = new EntitySkeleton(world);
		skele.setPosition(x, y, z);
		world.spawnEntityInWorld(skele);
	}

	@Override
	public int getChanceValue()
	{
		return -10;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Blindness_Fight";
	}

}

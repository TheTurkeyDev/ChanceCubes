package chanceCubes.rewards.type;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;

public class PotionRewardType implements IRewardType {

	private ArrayList<Integer> potion_ids = new ArrayList<Integer>();
	
	public static enum PotionType
	{
		REGENERATION(16385), REGENERATION_II(16417),
		SWIFTNESS(16386), SWIFTNESS_II(16418),
		FIRE_RESIST(16387),
		POISON(16388), POISON_II(16420),
		HEALTH(16389), HEALTH_II(16421),
		NIGHT_VISION(16390),
		WEAKNESS(16392),
		STRENGTH(16393), STRENGTH_II(16425),
		SLOWNESS(16394), SLOWNESS_II(16426),
		HARMING(16396), HARMING_II(16428),
		WATER_BREATHING(16397),
		INVISIBILITY(16398);
		
		public int id;
		PotionType(int id)
		{
			this.id = id;
		}
	}
	
	public PotionRewardType(Potion... potions)
	{
		for (Potion pot : potions)
			potion_ids.add(pot.id);
	}
	
	public PotionRewardType(PotionType... potions)
	{
		for (PotionType pot : potions)
			potion_ids.add(pot.id);
	}
	
	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player) 
	{
		for (Integer i : potion_ids)
		{
			EntityPotion entity = new EntityPotion(world, player, i);
			entity.posX = player.posX;
			entity.posY = player.posY + 2;
			entity.posZ = player.posZ;
			entity.motionY = -1;
			
			world.spawnEntityInWorld(entity);
		}
		
	}

}

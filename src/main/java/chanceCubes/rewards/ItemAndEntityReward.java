package chanceCubes.rewards;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemAndEntityReward implements IChanceCubeReward {

	private String name;
	private int luck;
	private ItemStack[] items;
	private String[] entities;
	
	public ItemAndEntityReward(String name, int luck, ItemStack[] items, String[] entities)
	{
		this.name = name;
		this.luck = luck;
		this.items = items;
		this.entities = entities;
	}
	
	@Override
	public void trigger(World world, int x, int y, int z) {
		if(!world.isRemote)
		{
			if(items != null)
				for(ItemStack stack: items)
				{
					Entity itemEnt = new EntityItem(world, x, y, z, stack.copy());
					world.spawnEntityInWorld(itemEnt);
				}
			if(entities != null)
				for(String entName: entities)
				{
					
					Entity newEnt = EntityList.createEntityByName(entName, world);
					if(newEnt == null)
					{
						CCubesCore.logger.log(Level.ERROR, "Failed to spawn an Entity with the name of: " + entName);
						return;
					}
					newEnt.setWorld(world);
					newEnt.setPosition(x, y, z);
					world.spawnEntityInWorld(newEnt);
				}
		}
		
	}

	@Override
	public int getLuckValue() {
		return this.luck;
	}

	@Override
	public String getName() {
		return this.name;
	}

}

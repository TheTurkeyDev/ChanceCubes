package chanceCubes.rewards;

import java.util.List;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ChanceCubeReward
{
	private String name;
	private int luckValue;
	private List<ItemStack> drops;
	private List<String> ents;

	public ChanceCubeReward(String name, List<ItemStack> drops, List<String> ents)
	{
		this.name = name;
		this.drops = drops;
		this.ents = ents;
	}

	/**
	 * Triggers the reward with the give reward at the given location
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	public void trigger(World world, float x, float y, float z)
	{
		if(!world.isRemote)
		{
			if(drops != null)
				for(ItemStack stack: drops)
				{
					Entity itemEnt = new EntityItem(world, x, y, z, stack.copy());
					world.spawnEntityInWorld(itemEnt);
				}
			if(ents != null)
				for(String entName: ents)
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

	public String getName()
	{
		return this.name;
	}

	public int getLuckValue()
	{
		return this.luckValue;
	}
	
	public String toString()
	{
		String toReturn = "Drops for \"" + name + "\": ";
		if(drops != null)
		{
			toReturn += "ItemStacks: {"; 
			for(ItemStack stack: drops)
			{
				toReturn += stack.toString() + ", ";
			}
			toReturn += "} ";
		}
		if(ents != null)
		{
			toReturn += "Entities: {"; 
			for(String entNum: ents)
			{
				toReturn += entNum + ", ";
			}
			toReturn += "} ";
		}
		return toReturn;
	}
}

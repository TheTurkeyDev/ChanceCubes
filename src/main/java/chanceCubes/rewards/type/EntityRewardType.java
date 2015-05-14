package chanceCubes.rewards.type;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.world.World;

public class EntityRewardType implements IRewardType
{

	private String[] entities;

	public EntityRewardType(String... entities)
	{
		this.entities = entities;
	}

	@Override
	public void trigger(World world, int x, int y, int z)
	{
		if(!world.isRemote)
		{
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

}

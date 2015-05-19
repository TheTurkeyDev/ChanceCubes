package chanceCubes.rewards.type;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;

public class EntityRewardType extends BaseRewardType<String>
{
    public EntityRewardType(String... entities)
    {
        super(entities);
    }

    @Override
    public void trigger(String entName, World world, int x, int y, int z, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            Entity newEnt = EntityList.createEntityByName(entName, world);
            if (newEnt == null)
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

package chanceCubes.rewards.type;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityRewardType extends BaseRewardType<NBTTagCompound>
{
    public EntityRewardType(NBTTagCompound... entities)
    {
        super(entities);
    }

    @Override
    public void trigger(NBTTagCompound entData, World world, int x, int y, int z, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            Entity newEnt = EntityList.createEntityFromNBT(entData, world);
            newEnt.setWorld(world);
            newEnt.setPosition(x, y, z);
            world.spawnEntityInWorld(newEnt);
        }
    }
    
    public static NBTTagCompound getBasicNBTForEntity(String entity)
    {
    	String json = "{id:" + entity + "}";
    	NBTTagCompound nbt = null;
    	try
		{
    		nbt = (NBTTagCompound) JsonToNBT.func_150315_a(json);
		} catch (NBTException e)
		{
			CCubesCore.logger.log(Level.ERROR, "Failed to create a simple NBTTagCompound from " + entity);
			return null;
		}
    	return nbt;
    }
}
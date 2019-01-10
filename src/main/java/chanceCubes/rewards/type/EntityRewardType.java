package chanceCubes.rewards.type;

import org.apache.logging.log4j.Level;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.rewardparts.EntityPart;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityRewardType extends BaseRewardType<EntityPart>
{
	public EntityRewardType(EntityPart... entities)
	{
		super(entities);
	}

	@Override
	public void trigger(final EntityPart part, final World world, final int x, final int y, final int z, final EntityPlayer player)
	{
		Scheduler.scheduleTask(new Task("Entity Reward Delay", part.getDelay())
		{
			@Override
			public void callback()
			{
				if(part.shouldRemovedBlocks())
					for(int yy = 0; yy < 4; yy++)
						for(int xx = -1; xx < 2; xx++)
							for(int zz = -1; zz < 2; zz++)
								RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, new BlockPos(x + xx, y + yy, z + zz));

				Entity newEnt = EntityType.create(part.getNBT(), world);
				newEnt.setPosition(x + 0.5, y, z + 0.5);
				world.spawnEntity(newEnt);
			}
		});
	}

	public static NBTTagCompound getBasicNBTForEntity(String entity)
	{
		String json = "{id:" + entity + "}";
		NBTTagCompound nbt = null;
		try
		{
			nbt = (NBTTagCompound) JsonToNBT.getTagFromJson(json);
		} catch(CommandSyntaxException e)
		{
			CCubesCore.logger.log(Level.ERROR, "Failed to create a simple NBTTagCompound from " + entity);
			return null;
		}
		return nbt;
	}
}
package chanceCubes.rewards.rewardtype;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.rewardparts.EntityPart;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.Level;

import java.util.Optional;

public class EntityRewardType extends BaseRewardType<EntityPart>
{
	public EntityRewardType(EntityPart... entities)
	{
		super(entities);
	}

	public EntityRewardType(String... entities)
	{
		super(convertToEntityParts(entities));
	}

	private static EntityPart[] convertToEntityParts(String... entities)
	{
		EntityPart[] toReturn = new EntityPart[entities.length];
		for(int i = 0; i < entities.length; i++)
			toReturn[i] = new EntityPart(EntityRewardType.getBasicNBTForEntity(entities[i]));
		return toReturn;
	}

	@Override
	public void trigger(final EntityPart part, final ServerWorld world, final int x, final int y, final int z, final PlayerEntity player)
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

				int copies = part.getCopies().getIntValue() + 1;
				for(int i = 0; i < copies; i++)
				{
					Optional<Entity> opt = EntityType.loadEntityUnchecked(part.getNBT(), world);
					if(!opt.isPresent())
					{
						CCubesCore.logger.log(Level.ERROR, "Invalid entity NBT! " + part.getNBT().toString());
						return;
					}
					Entity newEnt = opt.get();
					newEnt.setPosition(x + 0.5, y, z + 0.5);
					world.addEntity(newEnt);
				}
			}
		});
	}

	public static CompoundNBT getBasicNBTForEntity(String entity)
	{
		String json = "{id:" + entity + "}";
		CompoundNBT nbt;
		try
		{
			nbt = JsonToNBT.getTagFromJson(json);
		} catch(CommandSyntaxException e)
		{
			CCubesCore.logger.log(Level.ERROR, "Failed to create a simple NBTTagCompound from " + entity);
			return null;
		}
		return nbt;
	}
}
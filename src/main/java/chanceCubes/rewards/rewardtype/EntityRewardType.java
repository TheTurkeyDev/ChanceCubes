package chanceCubes.rewards.rewardtype;

import java.util.Optional;

import org.apache.logging.log4j.Level;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.rewardparts.EntityPart;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityRewardType extends BaseRewardType<EntityPart>
{
	public EntityRewardType(EntityPart... entities)
	{
		super(entities);
	}

	@Override
	public void trigger(final EntityPart part, final World world, final int x, final int y, final int z, final PlayerEntity player)
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
		});
	}

	public static CompoundNBT getBasicNBTForEntity(String entity)
	{
		String json = "{id:\"" + entity + "\"}";
		CompoundNBT nbt = null;
		try
		{
			nbt = (CompoundNBT) JsonToNBT.getTagFromJson(json);
		} catch(CommandSyntaxException e)
		{
			CCubesCore.logger.log(Level.ERROR, "Failed to create a simple NBTTagCompound from " + entity);
			e.printStackTrace();
			return null;
		}

		return nbt;
	}

	public static CompoundNBT getBasicNBTForEntity(String entity, String displayName, String extraNBT)
	{
		CompoundNBT nbt = getBasicNBTForEntity(entity);

		if(nbt != null)
		{
			nbt.putString("CustomName", "\"" + displayName + "\"");
			nbt.putInt("CustomNameVisible", 1);
			if(extraNBT != null && !extraNBT.isEmpty())
			{
				try
				{
					CompoundNBT exNBT = (CompoundNBT) JsonToNBT.getTagFromJson(extraNBT);
					nbt = nbt.merge(exNBT);
				} catch(CommandSyntaxException e)
				{
					CCubesCore.logger.log(Level.ERROR, "Failed to read NBT " + extraNBT);
				}
			}
		}

		return nbt;
	}
}
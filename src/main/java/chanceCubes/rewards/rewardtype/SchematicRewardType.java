package chanceCubes.rewards.rewardtype;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.rewardparts.SchematicPart;
import chanceCubes.util.CustomSchematic;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public class SchematicRewardType implements IRewardType
{
	private SchematicPart part;

	public SchematicRewardType(SchematicPart part)
	{
		this.part = part;
	}

	@Override
	public void trigger(World world, int x, int y, int z, PlayerEntity player)
	{
		CustomSchematic schematic = part.getSchematic();
		if(schematic == null)
		{
			CCubesCore.logger.log(Level.ERROR, "Failed to load a schematic reward with the file name " + part.getFileName());
			return;
		}
		List<OffsetBlock> stack = new ArrayList<>();
		for(OffsetBlock osb : schematic.getBlocks())
			if(schematic.includeAirBlocks() || !osb.getBlockState().getBlock().equals(Blocks.AIR))
				stack.add(osb);

		Scheduler.scheduleTask(new Task("Schematic_Spawn_Delay", schematic.getDelay())
		{
			@Override
			public void callback()
			{
				double playerX = player.posX;
				double playerY = player.posY;
				double playerZ = player.posZ;
				Scheduler.scheduleTask(new Task("Schematic_Reward_Block_Spawn", -1, schematic.getSpacingDelay() < 1 ? 1 : (int) schematic.getSpacingDelay())
				{
					@Override
					public void callback()
					{
					}

					@Override
					public void update()
					{
						float lessThan1 = 0;
						while(lessThan1 < 1 && !stack.isEmpty())
						{
							OffsetBlock osb = stack.remove(0);
							if(schematic.isRelativeToPlayer())
							{
								BlockPos pos = new BlockPos((int) Math.floor(playerX) + osb.xOff.getIntValue(), (int) Math.floor(playerY) + osb.yOff.getIntValue(), (int) Math.floor(playerZ) + osb.zOff.getIntValue());
								if(world.getBlockState(pos).getBlock().isAir(world.getBlockState(pos), world, pos) && osb.getBlockState().getBlock() instanceof AirBlock)
									continue;
								osb.spawnInWorld(world, (int) Math.floor(playerX), (int) Math.floor(playerY), (int) Math.floor(playerZ));
							}
							else
							{
								BlockPos pos = new BlockPos(x + osb.xOff.getIntValue(), y + osb.yOff.getIntValue(), z + osb.zOff.getIntValue());
								if(world.getBlockState(pos).getBlock().isAir(world.getBlockState(pos), world, pos) && osb.getBlockState().getBlock() instanceof AirBlock)
								{
									continue;
								}
								osb.spawnInWorld(world, x, y, z);
							}

							lessThan1 += schematic.getSpacingDelay();
							if(stack.size() == 0)
								lessThan1 = 1;
						}

						if(stack.size() == 0)
							Scheduler.removeTask(this);
					}
				});
			}
		});
	}
}
package chanceCubes.rewards.rewardtype;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.rewardparts.SchematicPart;
import chanceCubes.util.CustomSchematic;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

public class SchematicRewardType implements IRewardType
{
	private SchematicPart part;

	public SchematicRewardType(SchematicPart part)
	{
		this.part = part;
	}

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
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
								BlockPos pos = new BlockPos((int) Math.floor(player.posX) + osb.xOff.getIntValue(), (int) Math.floor(player.posY) + osb.yOff.getIntValue(), (int) Math.floor(player.posZ) + osb.zOff.getIntValue());
								if(world.getBlockState(pos).getBlock().isAir(world.getBlockState(pos), world, pos) && osb.getBlockState().getBlock() instanceof BlockAir)
								{
									continue;
								}
								osb.spawnInWorld(world, (int) Math.floor(player.posX), (int) Math.floor(player.posY), (int) Math.floor(player.posZ));
							}
							else
							{
								BlockPos pos = new BlockPos(x + osb.xOff.getIntValue(), y + osb.yOff.getIntValue(), z + osb.zOff.getIntValue());
								if(world.getBlockState(pos).getBlock().isAir(world.getBlockState(pos), world, pos) && osb.getBlockState().getBlock() instanceof BlockAir)
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
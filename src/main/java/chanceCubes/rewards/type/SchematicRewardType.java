package chanceCubes.rewards.type;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.CustomSchematic;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SchematicRewardType implements IRewardType
{
	private CustomSchematic schematic;

	public SchematicRewardType(CustomSchematic schematic)
	{
		this.schematic = schematic;
	}

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		List<OffsetBlock> stack = new ArrayList<OffsetBlock>();
		for(OffsetBlock osb : schematic.getBlocks())
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
								BlockPos pos = new BlockPos((int) Math.floor(player.posX) + osb.xOff, (int) Math.floor(player.posY) + osb.yOff, (int) Math.floor(player.posZ) + osb.zOff);
								if(world.getBlockState(pos).getBlock().isAir(world.getBlockState(pos), world, pos) && osb.getBlockState().getBlock() instanceof BlockAir)
								{
									continue;
								}
								osb.spawnInWorld(world, (int) Math.floor(player.posX), (int) Math.floor(player.posY), (int) Math.floor(player.posZ));
							}
							else
							{
								BlockPos pos = new BlockPos(x + osb.xOff, y + osb.yOff, z + osb.zOff);
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
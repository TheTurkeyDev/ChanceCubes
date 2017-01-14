package chanceCubes.rewards.type;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.CustomSchematic;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class SchematicRewardType implements IRewardType
{
	private CustomSchematic schematic;

	private Queue<OffsetBlock> stack = new LinkedList<OffsetBlock>();

	public SchematicRewardType(CustomSchematic schematic)
	{
		this.schematic = schematic;
	}

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		// ArrayLists are faster for this kind of accessing IIRC
		List<OffsetBlock> temp = new ArrayList<OffsetBlock>(schematic.getBlocks());

		int blocksPerTick = Math.max((int) (1 / schematic.getdelay()), 1);

		if(blocksPerTick > 1)
		{
			for(int start = 0; start < temp.size(); start += 2 * blocksPerTick)
			{
				int end = Math.min(start + 2 * blocksPerTick, temp.size());

				for(int i = start; i < end; i += 2)
				{
					stack.add(temp.get(i));
				}

				for(int i = start + 1; i < end; i += 2)
				{
					stack.add(temp.get(i));
				}
			}
		}
		else
		{
			stack.addAll(temp);
		}

		this.spawnInBlock(stack, schematic, world, x, y, z);
	}

	public void spawnInBlock(final Queue<OffsetBlock> stack, final CustomSchematic schem, World world, final int x, final int y, final int z)
	{
		Scheduler.scheduleTask(new Task("Schematic_Reward_Block_Spawn", schem.getdelay() < 1 ? 1 : (int) schem.getdelay())
		{
			@Override
			public void callback()
			{
				// Get # of blocks to spawn this tick, no less than 1
				int blocksToSpawn = Math.max((int) (1 / schem.getdelay()), 1);
				// Iterate that many times, as long as the stack isn't empty
				for(int i = 0; i < blocksToSpawn && !stack.isEmpty(); i++)
				{
					OffsetBlock osb = stack.remove();
					osb.spawnInWorld(world, x, y, z);
				}

				if(!stack.isEmpty())
					spawnInBlock(stack, schem, world, x, y, z);
			}
		});
	}

}

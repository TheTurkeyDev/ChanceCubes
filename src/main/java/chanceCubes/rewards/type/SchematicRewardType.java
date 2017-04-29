package chanceCubes.rewards.type;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.CustomSchematic;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
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

		this.spawnInBlock(stack, schematic, world, x, y, z);
	}

	public void spawnInBlock(final List<OffsetBlock> stack, final CustomSchematic schem, final World world, final int x, final int y, final int z)
	{
		Scheduler.scheduleTask(new Task("Schematic_Reward_Block_Spawn", schem.getdelay() < 1 ? 1 : (int) schem.getdelay())
		{
			@Override
			public void callback()
			{
				float lessThan1 = 0;
				while(lessThan1 < 1 && !stack.isEmpty())
				{
					OffsetBlock osb = stack.remove(0);
					osb.spawnInWorld(world, x, y, z);
					lessThan1 += schem.getdelay();
					if(stack.size() == 0)
						lessThan1 = 1;
				}
				if(stack.size() != 0)
					spawnInBlock(stack, schem, world, x, y, z);
			}
		});
	}

}
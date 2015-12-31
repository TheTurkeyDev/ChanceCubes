package chanceCubes.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.rewards.rewardparts.EntityPart;
import chanceCubes.rewards.rewardparts.OffsetBlock;

public class RewardsUtil
{
	/**
	 * 
	 * @param xSize
	 * @param ySize
	 * @param zSize
	 * @param block
	 * @param xOff
	 * @param yOff
	 * @param zOff
	 * @param falling
	 * @param delay
	 * @param causeUpdate
	 * @param relativeToPlayer
	 * @return
	 */
	public static OffsetBlock[] fillArea(int xSize, int ySize, int zSize, Block block, int xOff, int yOff, int zOff, boolean falling, int delay, boolean causeUpdate, boolean relativeToPlayer)
	{
		List<OffsetBlock> toReturn = new ArrayList<OffsetBlock>();

		for(int y = 0; y < ySize; y++)
			for(int z = 0; z < zSize; z++)
				for(int x = 0; x < xSize; x++)
					toReturn.add(new OffsetBlock(x + xOff, y + yOff, z + zOff, block, falling, delay).setCausesBlockUpdate(causeUpdate).setRelativeToPlayer(relativeToPlayer));

		return toReturn.toArray(new OffsetBlock[toReturn.size()]);
	}

	public static OffsetBlock[] addBlocksLists(OffsetBlock[] l1, OffsetBlock[] l2)
	{
		OffsetBlock[] toReturn = new OffsetBlock[l1.length + l2.length];

		int i = 0;
		for(OffsetBlock osb : l1)
		{
			toReturn[i] = osb;
			i++;
		}
		for(OffsetBlock osb : l2)
		{
			toReturn[i] = osb;
			i++;
		}

		return toReturn;
	}

	public static EntityPart[] spawnXEntities(NBTTagCompound entityNbt, int amount)
	{
		EntityPart[] toReturn = new EntityPart[amount];
		for(int i = 0; i < amount; i++)
			toReturn[i] = new EntityPart(entityNbt);
		return toReturn;
	}

	public static CommandPart[] executenXCommands(String command, int amount)
	{
		CommandPart[] toReturn = new CommandPart[amount];
		for(int i = 0; i < amount; i++)
			toReturn[i] = new CommandPart(command);
		return toReturn;
	}
}

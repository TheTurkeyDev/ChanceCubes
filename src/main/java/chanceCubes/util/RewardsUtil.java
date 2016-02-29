package chanceCubes.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.rewards.rewardparts.EntityPart;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.rewardparts.ParticlePart;

public class RewardsUtil
{
	/**
	 * *
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

	public static OffsetBlock[] addBlocksLists(OffsetBlock[]... lists)
	{
		int size = 0;
		for(OffsetBlock[] list : lists)
			size += list.length;
		
		OffsetBlock[] toReturn = new OffsetBlock[size];

		int i = 0;
		for(OffsetBlock[] list : lists)
		{
			for(OffsetBlock osb: list)
			{
				toReturn[i] = osb;
				i++;
			}
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

	public static CommandPart[] executeXCommands(String command, int amount)
	{
		CommandPart[] toReturn = new CommandPart[amount];
		for(int i = 0; i < amount; i++)
			toReturn[i] = new CommandPart(command);
		return toReturn;
	}

	public static ParticlePart[] spawnXParticles(String particle, int amount)
	{
		ParticlePart[] toReturn = new ParticlePart[amount];
		for(int i = 0; i < amount; i++)
			toReturn[i] = new ParticlePart(particle);
		return toReturn;
	}

	public static void sendMessageToNearPlayers(World world, int x, int y, int z, int distance, String message)
	{
		for(int i = 0; i < world.playerEntities.size(); ++i)
		{
			EntityPlayer entityplayer = (EntityPlayer) world.playerEntities.get(i);
			double dist = Math.sqrt(Math.pow(x - entityplayer.posX, 2) + Math.pow(y - entityplayer.posY, 2) + Math.pow(z - entityplayer.posZ, 2));
			if(dist <= distance)
				entityplayer.addChatMessage(new ChatComponentText(message));
		}
	}

	public static void sendMessageToAllPlayers(World world, String message)
	{
		for(int i = 0; i < world.playerEntities.size(); ++i)
		{
			EntityPlayer entityplayer = (EntityPlayer) world.playerEntities.get(i);
			entityplayer.addChatMessage(new ChatComponentText(message));
		}
	}
}

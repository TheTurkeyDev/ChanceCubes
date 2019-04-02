package chanceCubes.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RewardBlockCache
{
	protected Map<BlockPos, IBlockState> storedBlocks = new HashMap<BlockPos, IBlockState>();
	protected Map<BlockPos, NBTTagCompound> storedTE = new HashMap<BlockPos, NBTTagCompound>();

	private BlockPos origin;
	private BlockPos playerloc;
	private World world;
	private boolean force = true;

	public RewardBlockCache(World world, BlockPos pos, BlockPos playerloc)
	{
		this.world = world;
		this.origin = pos;
		this.playerloc = playerloc;
	}

	public void setForce(boolean force)
	{
		this.force = force;
	}

	public void cacheBlock(BlockPos offset, IBlockState newState)
	{
		BlockPos adjPos = origin.add(offset);
		IBlockState oldState = world.getBlockState(adjPos);
		NBTTagCompound oldNBT = null;
		TileEntity te = world.getTileEntity(adjPos);
		if(te != null)
		{
			oldNBT = te.serializeNBT();
			if(te instanceof IInventory)
				((IInventory) te).clear();

		}

		if(RewardsUtil.placeBlock(newState, world, adjPos, force))
		{
			if(!storedBlocks.containsKey(offset))
			{
				storedBlocks.put(offset, oldState);
				if(oldNBT != null)
					storedTE.put(offset, oldNBT);
			}
		}
	}

	public void restoreBlocks(Entity player)
	{
		for(BlockPos loc : storedBlocks.keySet())
		{
			RewardsUtil.placeBlock(storedBlocks.get(loc), world, origin.add(loc), true);
			if(storedTE.containsKey(loc))
				world.getTileEntity(origin.add(loc)).deserializeNBT(storedTE.get(loc));
		}
		player.setPositionAndUpdate(playerloc.getX() + 0.5, playerloc.getY(), playerloc.getZ() + 0.5);
	}
}
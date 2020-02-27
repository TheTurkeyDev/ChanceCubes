package chanceCubes.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class RewardBlockCache
{
	protected Map<BlockPos, BlockState> storedBlocks = new HashMap<>();
	protected Map<BlockPos, CompoundNBT> storedTE = new HashMap<>();

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

	public void cacheBlock(BlockPos offset, BlockState newState)
	{
		cacheBlock(offset, newState, 3);
	}

	public void cacheBlock(BlockPos offset, BlockState newState, int update)
	{
		BlockPos adjPos = origin.add(offset);
		BlockState oldState = world.getBlockState(adjPos);
		CompoundNBT oldNBT = null;
		TileEntity te = world.getTileEntity(adjPos);
		if(te != null)
		{
			oldNBT = te.serializeNBT();
			if(te instanceof IInventory)
				((IInventory) te).clear();

		}

		if(RewardsUtil.placeBlock(newState, world, adjPos, update, force))
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

		if(player != null)
			player.setPositionAndUpdate(playerloc.getX() + 0.5, playerloc.getY(), playerloc.getZ() + 0.5);
	}
}

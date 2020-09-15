package chanceCubes.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardBlockCache
{
	protected List<Tuple<BlockPos, BlockState>> storedBlocks = new ArrayList<>();
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
			if(storedBlocks.stream().noneMatch(t -> t.getA().equals(offset)))
			{
				storedBlocks.add(new Tuple<>(offset, oldState));
				if(oldNBT != null)
					storedTE.put(offset, oldNBT);
			}
		}
	}

	public void restoreBlocks(Entity player)
	{
		for(Tuple<BlockPos, BlockState> tuple : storedBlocks)
		{
			BlockPos worldPos = origin.add(tuple.getA());
			RewardsUtil.placeBlock(tuple.getB(), world, worldPos, true);
			TileEntity tile = world.getTileEntity(worldPos);
			if(storedTE.containsKey(tuple.getA()) && tile != null)
				tile.deserializeNBT(storedTE.get(tuple.getA()));
		}

		if(player != null)
			player.setPositionAndUpdate(playerloc.getX() + 0.5, playerloc.getY(), playerloc.getZ() + 0.5);
	}
}

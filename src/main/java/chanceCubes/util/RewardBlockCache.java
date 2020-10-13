package chanceCubes.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardBlockCache
{
	protected List<StoredBlockData> storedBlocks = new ArrayList<>();
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
			if(storedBlocks.stream().noneMatch(t -> t.pos.equals(offset)))
			{
				storedBlocks.add(new StoredBlockData(offset, oldState, newState));
				if(oldNBT != null)
					storedTE.put(offset, oldNBT);
			}
		}
	}

	public void restoreBlocks(Entity player)
	{
		for(StoredBlockData storedBlock : storedBlocks)
		{
			BlockPos worldPos = origin.add(storedBlock.pos);
			if(world.getBlockState(worldPos).getBlock().equals(storedBlock.placedState.getBlock()) || world.getBlockState(worldPos).getBlock().equals(Blocks.AIR))
			{
				RewardsUtil.placeBlock(storedBlock.oldState, world, worldPos, true);
				TileEntity tile = world.getTileEntity(worldPos);
				if(storedTE.containsKey(storedBlock.pos) && tile != null)
					tile.deserializeNBT(storedTE.get(storedBlock.pos));
			}
		}

		if(player != null)
			player.setPositionAndUpdate(playerloc.getX() + 0.5, playerloc.getY(), playerloc.getZ() + 0.5);
	}

	private static class StoredBlockData
	{
		public BlockPos pos;
		public BlockState oldState;
		public BlockState placedState;

		public StoredBlockData(BlockPos pos, BlockState oldState, BlockState placedState)
		{
			this.pos = pos;
			this.oldState = oldState;
			this.placedState = placedState;
		}
	}
}

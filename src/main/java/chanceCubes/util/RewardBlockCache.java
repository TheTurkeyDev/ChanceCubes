package chanceCubes.util;

import chanceCubes.config.CCubesSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardBlockCache
{
	protected final List<StoredBlockData> storedBlocks = new ArrayList<>();
	protected final Map<BlockPos, CompoundTag> storedTE = new HashMap<>();

	private final BlockPos origin;
	private final BlockPos playerLoc;
	private final Level level;
	private boolean force = true;

	public RewardBlockCache(Level level, BlockPos pos, BlockPos playerLoc)
	{
		this.level = level;
		this.origin = pos;
		this.playerLoc = playerLoc;
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
		BlockPos adjPos = origin.offset(offset);
		BlockState oldState = level.getBlockState(adjPos);
		CompoundTag oldNBT = null;
		BlockEntity te = level.getBlockEntity(adjPos);
		if(te != null)
		{
			oldNBT = te.serializeNBT();
			if(te instanceof Container)
				((Container) te).clearContent();

		}

		if(RewardsUtil.placeBlock(newState, level, adjPos, update, force))
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
		List<? extends String> blockedRestoreBlocks = CCubesSettings.blockRestoreBlacklist.get();
		for(StoredBlockData storedBlock : storedBlocks)
		{
			BlockPos worldPos = origin.offset(storedBlock.pos);
			ResourceLocation res = level.getBlockState(worldPos).getBlock().getRegistryName();
			if(res == null || !blockedRestoreBlocks.contains(res.toString()))
			{
				RewardsUtil.placeBlock(storedBlock.oldState, level, worldPos, true);
				BlockEntity tile = level.getBlockEntity(worldPos);
				if(storedTE.containsKey(storedBlock.pos) && tile != null)
					tile.deserializeNBT(storedTE.get(storedBlock.pos));
			}
		}

		if(player != null)
			player.moveTo(playerLoc.getX() + 0.5, playerLoc.getY() + 1, playerLoc.getZ() + 0.5);
	}

	private static class StoredBlockData
	{
		public final BlockPos pos;
		public final BlockState oldState;
		public final BlockState placedState;

		public StoredBlockData(BlockPos pos, BlockState oldState, BlockState placedState)
		{
			this.pos = pos;
			this.oldState = oldState;
			this.placedState = placedState;
		}
	}
}

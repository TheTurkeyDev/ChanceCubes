package chanceCubes.util;

import java.util.List;

import chanceCubes.rewards.rewardparts.OffsetBlock;

public class CustomSchematic
{
	private List<OffsetBlock> blocks;
	private int xSize;
	private int ySize;
	private int zSize;
	private boolean relativeToPlayer;
	private boolean includeAirBlocks;

	public CustomSchematic(List<OffsetBlock> blocks, int xSize, int ySize, int zSize, boolean relativeToPlayer, boolean includeAirBlocks)
	{
		this.blocks = blocks;
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
		this.relativeToPlayer = relativeToPlayer;
		this.includeAirBlocks = includeAirBlocks;
	}

	public List<OffsetBlock> getBlocks()
	{
		return this.blocks;
	}

	public int getXSize()
	{
		return this.xSize;
	}

	public int getYSize()
	{
		return this.ySize;
	}

	public int getZSize()
	{
		return this.zSize;
	}

	public boolean isRelativeToPlayer()
	{
		return relativeToPlayer;
	}

	public boolean includeAirBlocks()
	{
		return this.includeAirBlocks;
	}
}
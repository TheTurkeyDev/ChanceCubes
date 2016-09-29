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

	public CustomSchematic(List<OffsetBlock> blocks, int xSize, int ySize, int zSize, boolean relativeToPlayer)
	{
		this.blocks = blocks;
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
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
}

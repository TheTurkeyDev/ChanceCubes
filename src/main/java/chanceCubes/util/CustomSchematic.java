package chanceCubes.util;

import java.util.List;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.FloatVar;
import chanceCubes.rewards.variableTypes.IntVar;

public class CustomSchematic
{
	private List<OffsetBlock> blocks;
	private int xSize;
	private int ySize;
	private int zSize;
	private BoolVar relativeToPlayer;
	private BoolVar includeAirBlocks;
	private FloatVar spacingDelay;
	private IntVar delay;

	public CustomSchematic(List<OffsetBlock> blocks, int xSize, int ySize, int zSize, boolean relativeToPlayer, boolean includeAirBlocks, float spacingDelay, int delay)
	{
		this(blocks, xSize, ySize, zSize, new BoolVar(relativeToPlayer), new BoolVar(includeAirBlocks), new FloatVar(spacingDelay), new IntVar(delay));

	}

	public CustomSchematic(List<OffsetBlock> blocks, int xSize, int ySize, int zSize, BoolVar relativeToPlayer, BoolVar includeAirBlocks, FloatVar spacingDelay, IntVar delay)
	{
		this.blocks = blocks;
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
		this.relativeToPlayer = relativeToPlayer;
		this.includeAirBlocks = includeAirBlocks;
		this.spacingDelay = spacingDelay;
		this.delay = delay;
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
		return relativeToPlayer.getBoolValue();
	}

	public boolean includeAirBlocks()
	{
		return this.includeAirBlocks.getBoolValue();
	}

	public float getSpacingDelay()
	{
		return this.spacingDelay.getFloatValue();
	}

	public int getDelay()
	{
		return delay.getIntValue();
	}
}
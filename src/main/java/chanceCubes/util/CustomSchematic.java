package chanceCubes.util;

import java.util.List;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.FloatVar;
import chanceCubes.rewards.variableTypes.IntVar;

//TODO: Record?
public class CustomSchematic
{
	private final List<OffsetBlock> blocks;
	private final int xSize;
	private final int ySize;
	private final int zSize;
	private final BoolVar relativeToPlayer;
	private final BoolVar includeAirBlocks;
	private final FloatVar spacingDelay;
	private final IntVar delay;

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
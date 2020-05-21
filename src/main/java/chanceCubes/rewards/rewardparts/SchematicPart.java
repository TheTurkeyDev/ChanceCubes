package chanceCubes.rewards.rewardparts;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.FloatVar;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.util.CustomSchematic;
import chanceCubes.util.SchematicUtil;

public class SchematicPart extends BasePart
{

	private String fileName;
	private boolean hardcoded;
	private IntVar xOff = new IntVar(0);
	private IntVar yOff = new IntVar(-1);
	private IntVar zOff = new IntVar(0);
	private FloatVar spacingDelay = new FloatVar(0.1f);

	private BoolVar falling = new BoolVar(false);
	private BoolVar relativeToPlayer = new BoolVar(false);
	private BoolVar placeAirBlocks = new BoolVar(false);
	private BoolVar playSound = new BoolVar(true);

	public SchematicPart(String fileName, boolean hardcoded)
	{
		this.fileName = fileName;
		this.hardcoded = hardcoded;
	}

	public SchematicPart(String fileName, boolean hardcoded, IntVar xOff, IntVar yOff, IntVar zOff, FloatVar spacingDelay, BoolVar falling, BoolVar relativeToPlayer, BoolVar includeAirBlocks, BoolVar playSound, IntVar delay)
	{
		this(fileName, hardcoded);
		this.xOff = xOff;
		this.yOff = yOff;
		this.zOff = zOff;
		this.spacingDelay = spacingDelay;

		this.falling = falling;
		this.relativeToPlayer = relativeToPlayer;
		this.placeAirBlocks = includeAirBlocks;
		this.playSound = playSound;
		this.delay = delay;
	}

	public String getFileName()
	{
		return this.fileName;
	}

	public CustomSchematic getSchematic()
	{
		if(hardcoded)
		{
			return SchematicUtil.loadCustomSchematic(SchematicUtil.getSchematicJson(fileName), xOff.getIntValue(), yOff.getIntValue(), zOff.getIntValue(), spacingDelay, falling, relativeToPlayer, placeAirBlocks, playSound, delay);
		}
		else
		{
			if(fileName.endsWith(".ccs"))
			{
				return SchematicUtil.loadCustomSchematic(fileName, xOff.getIntValue(), yOff.getIntValue(), zOff.getIntValue(), spacingDelay, falling, relativeToPlayer, placeAirBlocks, playSound, delay);
			}
			else
			{
				if(fileName.endsWith(".schematic"))
					CCubesCore.logger.error("Legacy Schematic files no longer work in this version of Minecraft!");
				else
					CCubesCore.logger.error("Chance Cubes does not support the file and given extension!: " + fileName);
				return null;
			}
		}
	}


	public SchematicPart setSpacingdelay(float spacingDelay)
	{
		this.spacingDelay = new FloatVar(spacingDelay);
		return this;
	}

	public SchematicPart shouldPlaceAitBlocks(boolean placeAirBlocks)
	{
		this.placeAirBlocks = new BoolVar(placeAirBlocks);
		return this;
	}

	public SchematicPart setBlocksFalling(boolean falling)
	{
		this.falling = new BoolVar(falling);
		return this;
	}
}

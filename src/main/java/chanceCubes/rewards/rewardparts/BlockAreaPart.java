package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.StringVar;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockAreaPart extends BasePart
{
	private StringVar block;
	private IntVar xSize;
	private IntVar ySize;
	private IntVar zSize;
	private IntVar xOff = new IntVar(0);
	private IntVar yOff = new IntVar(0);
	private IntVar zOff = new IntVar(0);
	private BoolVar falling = new BoolVar(false);
	private BoolVar causesUpdate = new BoolVar(false);
	private BoolVar relativeToPlayer = new BoolVar(false);

	public BlockAreaPart(IntVar xSize, IntVar ySize, IntVar zSize, StringVar block)
	{
		this.block = block;
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
	}

	public BlockAreaPart(IntVar xSize, IntVar ySize, IntVar zSize, StringVar block, IntVar xOff, IntVar yOff, IntVar zOff, BoolVar falling, IntVar delay, BoolVar causesUpdate, BoolVar relativeToPlayer)
	{
		this(xSize, ySize, zSize, block);
		this.xOff = xOff;
		this.yOff = yOff;
		this.zOff = zOff;
		this.falling = falling;
		this.causesUpdate = causesUpdate;
		this.relativeToPlayer = relativeToPlayer;
		this.setDelay(delay);
	}

	public BlockAreaPart(int xSize, int ySize, int zSize, StringVar block, int xOff, int yOff, int zOff, boolean falling, int delay, boolean causesUpdate, boolean relativeToPlayer)
	{
		this(new IntVar(xSize), new IntVar(ySize), new IntVar(zSize), block, new IntVar(xOff), new IntVar(yOff), new IntVar(zOff), new BoolVar(falling), new IntVar(delay), new BoolVar(causesUpdate), new BoolVar(relativeToPlayer));
	}

	public void placeBlocks(World world, PlayerEntity player, int worldX, int worldY, int worldZ)
	{
		String[] blockDataParts = block.getValue().split(":");
		Block osbBlock;
		if(blockDataParts.length < 2)
			osbBlock = RewardsUtil.getBlock("minecraft", blockDataParts[0]);
		else
			osbBlock = RewardsUtil.getBlock(blockDataParts[0], blockDataParts[1]);

		if(osbBlock == null)
			return;

		int xSizeCalc = xSize.getIntValue();
		int ySizeCalc = ySize.getIntValue();
		int zSizeCalc = zSize.getIntValue();

		int xOffCalc = xOff.getIntValue();
		int yOffCalc = yOff.getIntValue();
		int zOffCalc = zOff.getIntValue();

		for(int y = 0; y < ySizeCalc; y++)
		{
			for(int z = 0; z < zSizeCalc; z++)
			{
				for(int x = 0; x < xSizeCalc; x++)
				{
					OffsetBlock osb = new OffsetBlock(x + xOffCalc, y + yOffCalc, z + zOffCalc, osbBlock, falling, delay).setCausesBlockUpdate(causesUpdate).setRelativeToPlayer(relativeToPlayer);

					if(osb.isRelativeToPlayer() && !RewardsUtil.isBlockUnbreakable(world, new BlockPos((int) Math.floor(player.getPosX()), (int) Math.floor(player.getPosY()), (int) Math.floor(player.getPosZ()))))
						osb.spawnInWorld(world, (int) Math.floor(player.getPosX()), (int) Math.floor(player.getPosY()), (int) Math.floor(player.getPosZ()));
					else if(!RewardsUtil.isBlockUnbreakable(world, new BlockPos(worldX, worldY + 3, worldZ)))
						osb.spawnInWorld(world, worldX, worldY, worldZ);
				}
			}
		}
	}

	public void setxOff(IntVar xOff)
	{
		this.xOff = xOff;
	}

	public void setyOff(IntVar yOff)
	{
		this.yOff = yOff;
	}

	public void setzOff(IntVar zOff)
	{
		this.zOff = zOff;
	}

	public void setFalling(BoolVar falling)
	{
		this.falling = falling;
	}

	public void setCausesUpdate(BoolVar causesUpdate)
	{
		this.causesUpdate = causesUpdate;
	}

	public void setRelativeToPlayer(BoolVar relativeToPlayer)
	{
		this.relativeToPlayer = relativeToPlayer;
	}
}

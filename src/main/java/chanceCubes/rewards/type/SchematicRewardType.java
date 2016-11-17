package chanceCubes.rewards.type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.config.CustomRewardsLoader.Schematic;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.rewardparts.OffsetTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class SchematicRewardType extends BaseRewardType<OffsetBlock>
{
	private String schemName;
	private Schematic schem;
	private int delay;
	private boolean falling;
	private boolean relativeToPlayer;

	/**
	 * 
	 * @param schematic
	 * @param delay
	 * @param falling
	 * @param relative
	 *            to the player
	 */
	public SchematicRewardType(String schemName, int delay, boolean falling, boolean rtp)
	{
		this.schemName = schemName;
		try
		{
			this.schem = CustomRewardsLoader.instance.parseSchematic(schemName, true);
		} catch(IOException e)
		{
			CCubesCore.logger.log(Level.ERROR, "Failed to load a hardcoded schematic called " + this.schemName + ". It seems to be dead :(");
			return;
		}
		this.delay = delay;
		this.falling = falling;
		this.relativeToPlayer = rtp;
		List<OffsetBlock> blocks = this.loadSchematicReward();
		if(blocks == null)
			return;
		super.rewards = blocks.toArray(new OffsetBlock[blocks.size()]);
	}

	@Override
	protected void trigger(OffsetBlock block, World world, int x, int y, int z, EntityPlayer player)
	{
		if(rewards == null)
			return;
		if(block.isRelativeToPlayer())
			block.spawnInWorld(world, (int) player.posX, (int) player.posY, (int) player.posZ);
		else
			block.spawnInWorld(world, x, y, z);
	}

	public List<OffsetBlock> loadSchematicReward()
	{
		List<OffsetBlock> blocks = new ArrayList<OffsetBlock>();

		if(schem == null)
		{
			CCubesCore.logger.log(Level.ERROR, "Failed to load a hardcoded schematic called " + this.schemName + ". It seems to be dead :(");
			return null;
		}

		int i = 0;
		short halfLength = (short) (schem.length / 2);
		short halfWidth = (short) (schem.width / 2);

		for(int yy = 0; yy < schem.height; yy++)
		{
			for(int zz = 0; zz < schem.length; zz++)
			{
				for(int xx = 0; xx < schem.width; xx++)
				{

					int j = schem.blocks[i];
					if(j < 0)
					{
						j = 128 + (128 + j);
					}

					Block b = Block.getBlockById(j);

					if(schem.tileentities != null)
					{
						for(int i1 = 0; i1 < schem.tileentities.tagCount(); ++i1)
						{
							NBTTagCompound nbttagcompound4 = schem.tileentities.getCompoundTagAt(i1);
							if(nbttagcompound4.getInteger("x") == xx && nbttagcompound4.getInteger("y") == yy && nbttagcompound4.getInteger("z") == zz)
							{
								TileEntity tileentity = TileEntity.func_190200_a(null, nbttagcompound4);
								if(tileentity != null)
								{
									OffsetTileEntity block = new OffsetTileEntity(tileentity.getPos().getX(), tileentity.getPos().getY(), tileentity.getPos().getZ(), b, nbttagcompound4, falling);
									block.setRelativeToPlayer(this.relativeToPlayer);
									block.setDelay(i * delay);
									block.setBlockState(b.getStateFromMeta(schem.data[i]));
									blocks.add(block);
									continue;
								}
							}
						}
					}

					if(b != Blocks.AIR)
					{
						OffsetBlock block = new OffsetBlock(halfWidth - xx, yy, halfLength - zz, b, falling);
						block.setRelativeToPlayer(this.relativeToPlayer);
						block.setDelay(i * delay);
						block.setBlockState(b.getStateFromMeta(schem.data[i]));
						blocks.add(block);
					}

					i++;
				}
			}
		}
		return blocks;
	}
}
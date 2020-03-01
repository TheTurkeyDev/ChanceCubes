package chanceCubes.blocks;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class BlockFallingCustom extends FallingBlockEntity
{
	private int normY;
	private OffsetBlock osb;
	private BlockState fallTile;

	public BlockFallingCustom(World world, double x, double y, double z, BlockState state, int normY, OffsetBlock osb)
	{
		super(world, x, y, z, state);
		fallTile = state;
		this.normY = normY;
		this.osb = osb;
	}

	@Override
	public void tick()
	{
		if(this.fallTile.getBlockState().isAir(world, this.getPosition()))
		{
			this.remove();
		}
		else
		{
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			Block block = this.fallTile.getBlock();
			if(this.fallTime++ == 0)
			{
				BlockPos blockpos = new BlockPos(this);
				if(this.world.getBlockState(blockpos).getBlock() == block)
				{
					this.world.removeBlock(blockpos, false);
				}
				else if(this.world.isRemote)
				{
					this.remove();
					return;
				}
			}

			this.setMotion(0, this.getMotion().y - 0.04, 0);
			this.move(MoverType.SELF, this.getMotion());
			if(!this.world.isRemote)
			{
				BlockPos blockpos1 = new BlockPos(this);
				if(this.onGround)
				{
					BlockState iblockstate = this.world.getBlockState(blockpos1);
					this.setMotion(this.getMotion().mul(0.7D, -0.5D, 0.7D));

					if(iblockstate.getBlock() != Blocks.PISTON_HEAD)
					{
						this.remove();
						if(block instanceof FallingBlock)
							osb.placeInWorld(world, blockpos1, false, null);

						if(this.tileEntityData != null && block.hasTileEntity(fallTile))
						{
							TileEntity tileentity = this.world.getTileEntity(blockpos1);

							if(tileentity != null)
							{
								CompoundNBT nbttagcompound = new CompoundNBT();
								tileentity.write(nbttagcompound);

								for(String s : this.tileEntityData.keySet())
								{
									INBT nbtbase = this.tileEntityData.get(s);

									if(!s.equals("x") && !s.equals("y") && !s.equals("z"))
										nbttagcompound.put(s, nbtbase.copy());
								}

								tileentity.read(nbttagcompound);
								tileentity.markDirty();
							}
						}

					}
				}
				else if(this.fallTime > 100 && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.fallTime > 600)
				{
					if(this.shouldDropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))
						this.entityDropItem(new ItemStack(block, 1), 0.0F);

					this.remove();
				}
				else if(normY == blockpos1.getY() || this.getMotion().y == 0)
				{
					this.remove();
					osb.placeInWorld(world, blockpos1, false, null);
				}
			}
		}
	}
}
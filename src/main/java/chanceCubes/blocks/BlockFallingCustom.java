package chanceCubes.blocks;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFallingCustom extends EntityFallingBlock
{
	private int normY;
	private OffsetBlock osb;
	private IBlockState fallTile;

	public BlockFallingCustom(World world, double x, double y, double z, IBlockState state, int normY, OffsetBlock osb)
	{
		super(world, x, y, z, state);
		fallTile = state;
		this.normY = normY;
		this.osb = osb;
	}

	public void onUpdate()
	{
		Block block = this.fallTile.getBlock();

		if(this.fallTile.getMaterial() == Material.AIR)
		{
			this.setDead();
		}
		else
		{
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;

			if(this.fallTime++ == 0)
			{
				BlockPos blockpos = new BlockPos(this);

				if(this.worldObj.getBlockState(blockpos).getBlock() == block)
				{
					this.worldObj.setBlockToAir(blockpos);
				}
				else if(this.worldObj.isRemote)
				{
					this.setDead();
					return;
				}
			}

			this.motionY -= 0.04D;
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.98D;
			this.motionY *= 0.98D;
			this.motionZ *= 0.98D;

			if(!this.worldObj.isRemote)
			{
				BlockPos blockpos1 = new BlockPos(this);

				if(this.onGround)
				{
					IBlockState iblockstate = this.worldObj.getBlockState(blockpos1);

					this.motionX *= 0.7D;
					this.motionZ *= 0.7D;
					this.motionY *= -0.5D;

					if(iblockstate.getBlock() != Blocks.PISTON_EXTENSION)
					{
						this.setDead();
						if(this.worldObj.canBlockBePlaced(block, blockpos1, true, EnumFacing.UP, (Entity) null, (ItemStack) null) && !BlockFalling.canFallThrough(this.worldObj.getBlockState(blockpos1.down())) && this.worldObj.setBlockState(blockpos1, this.fallTile, 3))
						{
							if(block instanceof BlockFalling)
								osb.placeInWorld(worldObj, blockpos1, false);

							if(this.tileEntityData != null && block instanceof ITileEntityProvider)
							{
								TileEntity tileentity = this.worldObj.getTileEntity(blockpos1);

								if(tileentity != null)
								{
									NBTTagCompound nbttagcompound = new NBTTagCompound();
									tileentity.writeToNBT(nbttagcompound);

									for(String s : this.tileEntityData.getKeySet())
									{
										NBTBase nbtbase = this.tileEntityData.getTag(s);

										if(!s.equals("x") && !s.equals("y") && !s.equals("z"))
											nbttagcompound.setTag(s, nbtbase.copy());
									}

									tileentity.readFromNBT(nbttagcompound);
									tileentity.markDirty();
								}
							}
						}
						else if(this.shouldDropItem && this.worldObj.getGameRules().getBoolean("doEntityDrops"))
						{
							this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0F);
						}
					}
				}
				else if(this.fallTime > 100 && !this.worldObj.isRemote && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.fallTime > 600)
				{
					if(this.shouldDropItem && this.worldObj.getGameRules().getBoolean("doEntityDrops"))
						this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0F);

					this.setDead();
				}
				else if(normY == blockpos1.getY() || this.motionY == 0)
				{
					this.setDead();
					osb.placeInWorld(worldObj, blockpos1, false);
				}
			}
		}
	}
}

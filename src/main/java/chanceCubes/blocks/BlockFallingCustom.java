package chanceCubes.blocks;

import java.util.Iterator;

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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import chanceCubes.rewards.rewardparts.OffsetBlock;

public class BlockFallingCustom extends EntityFallingBlock
{
	private int normY;
	private OffsetBlock osb;
	private IBlockState fallTile;

	public BlockFallingCustom(World world, double x, double y, double z, IBlockState state, int data, int normY, OffsetBlock osb)
	{
		super(world, x, y, z, state);
		fallTile = state;
		this.normY = normY;
		this.osb = osb;
	}

	public void onUpdate()
	{
		Block block = this.fallTile.getBlock();

		if(block.getMaterial() == Material.air)
		{
			this.setDead();
		}
		else
		{
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			BlockPos blockpos;

			if(this.fallTime++ == 0)
			{
				blockpos = new BlockPos(this);

				if(this.worldObj.getBlockState(blockpos).getBlock() == block)
				{
					this.worldObj.setBlockToAir(blockpos);
				}
				else if(!this.worldObj.isRemote)
				{
					this.setDead();
					return;
				}
			}

			this.motionY -= 0.03999999910593033D;
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.9800000190734863D;
			this.motionY *= 0.9800000190734863D;
			this.motionZ *= 0.9800000190734863D;

			if(!this.worldObj.isRemote)
			{
				blockpos = new BlockPos(this);

				if(this.onGround)
				{
					this.motionX *= 0.699999988079071D;
					this.motionZ *= 0.699999988079071D;
					this.motionY *= -0.5D;

					if(this.worldObj.getBlockState(blockpos).getBlock() != Blocks.piston_extension)
					{
						this.setDead();

						if(this.worldObj.canBlockBePlaced(block, blockpos, true, EnumFacing.UP, (Entity) null, (ItemStack) null) && !BlockFalling.canFallInto(this.worldObj, blockpos.down()) && this.worldObj.setBlockState(blockpos, this.fallTile, 3))
						{
							if(block instanceof BlockFalling)
							{
								osb.placeInWorld(worldObj, blockpos, false);
							}

							if(this.tileEntityData != null && block instanceof ITileEntityProvider)
							{
								TileEntity tileentity = this.worldObj.getTileEntity(blockpos);

								if(tileentity != null)
								{
									NBTTagCompound nbttagcompound = new NBTTagCompound();
									tileentity.writeToNBT(nbttagcompound);
									Iterator<?> iterator = this.tileEntityData.getKeySet().iterator();

									while(iterator.hasNext())
									{
										String s = (String) iterator.next();
										NBTBase nbtbase = this.tileEntityData.getTag(s);

										if(!s.equals("x") && !s.equals("y") && !s.equals("z"))
										{
											nbttagcompound.setTag(s, nbtbase.copy());
										}
									}

									tileentity.readFromNBT(nbttagcompound);
									tileentity.markDirty();
								}
							}
						}
						else if(this.shouldDropItem && this.worldObj.getGameRules().getBoolean("doTileDrops"))
						{
							this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0F);
						}
					}
				}
				else if(this.fallTime > 100 && !this.worldObj.isRemote && (blockpos.getY() < 1 || blockpos.getY() > 256) || this.fallTime > 600)
				{
					if(this.shouldDropItem && this.worldObj.getGameRules().getBoolean("doTileDrops"))
					{
						this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0F);
					}

					this.setDead();
				}
				else if(normY == blockpos.getY())
				{
					this.setDead();
					osb.placeInWorld(worldObj, blockpos, false);
				}
			}
		}
	}
}

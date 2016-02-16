package chanceCubes.blocks;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockFallingCustom extends EntityFallingBlock
{
	private int normY;
	private OffsetBlock osb;

	public BlockFallingCustom(World world, double x, double y, double z, Block b, int data, int normY, OffsetBlock osb)
	{
		super(world, x, y, z, b, data);
		this.normY = normY;
		this.osb = osb;
	}

	public void onUpdate()
	{
		if(super.func_145805_f() == null || super.func_145805_f().getMaterial() == Material.air)
		{
			this.setDead();
		}
		else
		{
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			++this.field_145812_b;
			this.motionY -= 0.03999999910593033D;
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.9800000190734863D;
			this.motionY *= 0.9800000190734863D;
			this.motionZ *= 0.9800000190734863D;

			if(!this.worldObj.isRemote)
			{
				int i = MathHelper.floor_double(this.posX);
				int j = MathHelper.floor_double(this.posY - .5);
				int k = MathHelper.floor_double(this.posZ);

				if(this.onGround)
				{
					this.motionX *= 0.699999988079071D;
					this.motionZ *= 0.699999988079071D;
					this.motionY *= -0.5D;

					if(this.worldObj.getBlock(i, j, k) != Blocks.piston_extension)
					{
						this.setDead();
						if(this.worldObj.canPlaceEntityOnSide(super.func_145805_f(), i, j, k, true, 1, (Entity) null, (ItemStack) null) && !BlockFalling.func_149831_e(this.worldObj, i, j - 1, k) && this.worldObj.setBlock(i, j, k, super.func_145805_f(), this.field_145814_a, 3))
							osb.placeInWorld(worldObj, i, j, k, false);
						else if(this.field_145813_c)
							this.entityDropItem(new ItemStack(super.func_145805_f(), 1, super.func_145805_f().damageDropped(this.field_145814_a)), 0.0F);
					}
				}
				else if(normY == j)
				{
					this.setDead();
					osb.placeInWorld(worldObj, i, j, k, false);
				}
				else if(this.field_145812_b > 100 && !this.worldObj.isRemote && (j < 1 || j > 256) || this.field_145812_b > 600)
				{
					if(this.field_145813_c)
						this.entityDropItem(new ItemStack(super.func_145805_f(), 1, super.func_145805_f().damageDropped(this.field_145814_a)), 0.0F);
					this.setDead();
				}
			}
		}
	}
}

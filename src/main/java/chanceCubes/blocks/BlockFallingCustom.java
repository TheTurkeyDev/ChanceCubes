package chanceCubes.blocks;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BlockFallingCustom extends FallingBlockEntity
{
	private final int normY;
	private final OffsetBlock osb;

	public BlockFallingCustom(Level level, double x, double y, double z, BlockState state, int normY, OffsetBlock osb)
	{
		super(EntityType.FALLING_BLOCK, level);
		//this.blockState = p_31957_;
		this.blocksBuilding = true;
		this.setPos(x, y, z);
		this.setDeltaMovement(Vec3.ZERO);
		this.xo = x;
		this.yo = y;
		this.zo = z;
		this.setStartPos(this.blockPosition());
		this.normY = normY;
		this.osb = osb;
	}

	@Override
	public void tick()
	{
		if(this.getBlockState().isAir())
		{
			this.discard();
		}
		else
		{
			Block block = this.getBlockState().getBlock();
			if(this.time++ == 0)
			{
				BlockPos blockpos = this.blockPosition();
				if(this.level().getBlockState(blockpos).getBlock() == block)
				{
					this.level().removeBlock(blockpos, false);
				}
				else if(this.level().isClientSide())
				{
					this.discard();
					return;
				}
			}

			if(!this.isNoGravity())
				this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));

			this.move(MoverType.SELF, this.getDeltaMovement());
			if(!this.level().isClientSide())
			{
				BlockPos blockpos1 = new BlockPos(this.getOnPos());
				if(this.onGround())
				{
					BlockState iblockstate = this.level().getBlockState(blockpos1);
					this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));

					if(iblockstate.getBlock() != Blocks.PISTON_HEAD)
					{
						this.discard();
						if(block instanceof FallingBlock)
							osb.placeInWorld(this.level(), blockpos1, false, null);

						if(this.blockData != null && this.getBlockState().hasBlockEntity())
						{
							BlockEntity blockentity = this.level().getBlockEntity(blockpos1);
							if(blockentity != null)
							{
								CompoundTag compoundtag = blockentity.saveWithFullMetadata();

								for(String s : this.blockData.getAllKeys())
								{
									Tag tag = this.blockData.get(s);
									if(tag != null && !"x".equals(s) && !"y".equals(s) && !"z".equals(s))
										compoundtag.put(s, tag.copy());
								}

								try
								{
									blockentity.load(compoundtag);
								} catch(Exception exception)
								{
									CCubesCore.logger.error("Failed to load block entity from falling block", exception);
								}

								blockentity.setChanged();
							}
						}

					}
				}
				else if(this.time > 100 && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.time > 600)
				{
					if(this.dropItem && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS))
					{
						this.callOnBrokenAfterFall(block, blockpos1);
						this.spawnAtLocation(block);
					}

					this.discard();
				}
				else if((normY >= (getY() + this.getDeltaMovement().y) && this.getDeltaMovement().y <= 0) || this.getDeltaMovement().y == 0)
				{
					this.discard();
					osb.placeInWorld(this.level(), blockpos1, false, null);
				}
			}
		}
	}
}
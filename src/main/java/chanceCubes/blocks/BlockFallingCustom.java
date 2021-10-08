package chanceCubes.blocks;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BlockFallingCustom extends FallingBlockEntity
{
	private final int normY;
	private final OffsetBlock osb;
	private BlockState fallTile;

	public BlockFallingCustom(Level level, double x, double y, double z, BlockState state, int normY, OffsetBlock osb)
	{
		super(level, x, y, z, state);
		fallTile = state;
		this.normY = normY;
		this.osb = osb;
	}


	public void tick() {
		if (this.fallTile.isAir()) {
			this.discard();
		} else {
			Block block = this.fallTile.getBlock();
			if (this.time++ == 0) {
				BlockPos blockpos = this.blockPosition();
				if (this.level.getBlockState(blockpos).is(block)) {
					this.level.removeBlock(blockpos, false);
				} else if (!this.level.isClientSide) {
					this.discard();
					return;
				}
			}

			if (!this.isNoGravity()) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
			}

			this.move(MoverType.SELF, this.getDeltaMovement());
			if (!this.level.isClientSide) {
				BlockPos blockpos1 = this.blockPosition();
				boolean flag = this.fallTile.getBlock() instanceof ConcretePowderBlock;
				boolean flag1 = flag && this.level.getFluidState(blockpos1).is(FluidTags.WATER);
				double d0 = this.getDeltaMovement().lengthSqr();
				if (flag && d0 > 1.0D) {
					BlockHitResult blockhitresult = this.level.clip(new ClipContext(new Vec3(this.xo, this.yo, this.zo), this.position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, this));
					if (blockhitresult.getType() != HitResult.Type.MISS && this.level.getFluidState(blockhitresult.getBlockPos()).is(FluidTags.WATER)) {
						blockpos1 = blockhitresult.getBlockPos();
						flag1 = true;
					}
				}

				if (!this.onGround && !flag1) {
					if (!this.level.isClientSide && (this.time > 100 && (blockpos1.getY() <= this.level.getMinBuildHeight() || blockpos1.getY() > this.level.getMaxBuildHeight()) || this.time > 600)) {
						if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
							this.spawnAtLocation(block);
						}

						this.discard();
					}
				} else {
					BlockState blockstate = this.level.getBlockState(blockpos1);
					this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
					if (!blockstate.is(Blocks.MOVING_PISTON)) {
						if (!this.cancelDrop) {
							boolean flag2 = blockstate.canBeReplaced(new DirectionalPlaceContext(this.level, blockpos1, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
							boolean flag3 = FallingBlock.isFree(this.level.getBlockState(blockpos1.below())) && (!flag || !flag1);
							boolean flag4 = this.fallTile.canSurvive(this.level, blockpos1) && !flag3;
							if (flag2 && flag4) {
								if (this.fallTile.hasProperty(BlockStateProperties.WATERLOGGED) && this.level.getFluidState(blockpos1).getType() == Fluids.WATER) {
									this.fallTile = this.fallTile.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true));
								}

								if (this.level.setBlock(blockpos1, this.fallTile, 3)) {
									((ServerLevel)this.level).getChunkSource().chunkMap.broadcast(this, new ClientboundBlockUpdatePacket(blockpos1, this.level.getBlockState(blockpos1)));
									this.discard();
									if (block instanceof Fallable) {
										((Fallable)block).onLand(this.level, blockpos1, this.fallTile, blockstate, this);
									}

									if (this.blockData != null && this.fallTile.hasBlockEntity()) {
										BlockEntity blockentity = this.level.getBlockEntity(blockpos1);
										if (blockentity != null) {
											CompoundTag compoundtag = blockentity.save(new CompoundTag());

											for(String s : this.blockData.getAllKeys()) {
												Tag tag = this.blockData.get(s);
												if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) {
													compoundtag.put(s, tag.copy());
												}
											}

											try {
												blockentity.load(compoundtag);
											} catch (Exception exception) {
												LOGGER.error("Failed to load block entity from falling block", (Throwable)exception);
											}

											blockentity.setChanged();
										}
									}
								} else if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
									this.discard();
									this.callOnBrokenAfterFall(block, blockpos1);
									this.spawnAtLocation(block);
								}
							} else {
								this.discard();
								if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
									this.callOnBrokenAfterFall(block, blockpos1);
									this.spawnAtLocation(block);
								}
							}
						} else {
							this.discard();
							this.callOnBrokenAfterFall(block, blockpos1);
						}
					}
				}
			}
			this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
		}
	}
}
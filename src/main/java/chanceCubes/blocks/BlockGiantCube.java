package chanceCubes.blocks;

import chanceCubes.items.CCubesItems;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.tileentities.TileGiantCube;
import chanceCubes.util.GiantCubeUtil;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.StatsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;

public class BlockGiantCube extends BaseChanceBlock implements EntityBlock
{
	public BlockGiantCube()
	{
		super(getBuilder());
	}

	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state)
	{
		return new TileGiantCube(pos, state);
	}

	@Override
	public @NotNull VoxelShape getShape(@NotNull BlockState state, BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context)
	{
		BlockEntity te = level.getBlockEntity(pos);
		if(!(te instanceof TileGiantCube gc))
			return Shapes.block();

		BlockPos diff = gc.getMasterOffset();
		return Shapes.box(diff.getX() - 1, diff.getY() - 1, diff.getZ() - 1, 2+diff.getX(), 2+diff.getY(), 2+diff.getZ());
	}

	@Override
	public BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player)
	{
		BlockEntity be = level.getBlockEntity(pos);
		if(!level.isClientSide() && !(player instanceof FakePlayer) && be instanceof TileGiantCube gcte)
		{
			if(!player.getInventory().getSelected().isEmpty() && player.getInventory().getSelected().getItem().equals(CCubesItems.SILK_PENDANT.get()))
			{
				popResource(level, pos, new ItemStack(CCubesBlocks.COMPACT_GIANT_CUBE.get()));
				GiantCubeUtil.removeStructure(gcte.getMasterPostion(), level);
				return super.playerWillDestroy(level, pos, state, player);
			}

			if(!gcte.hasMaster() || !gcte.checkForMaster())
				level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

			ServerLevel serverWorld = (ServerLevel) level;
			RewardsUtil.executeCommand(serverWorld, player, player.getOnPos(), "/advancement grant @p only chancecubes:giant_chance_cube");
			player.awardStat(StatsRegistry.OPENED_GIANT_CHANCE_CUBE.get());
			GlobalCCRewardRegistry.GIANT.triggerRandomReward(serverWorld, gcte.getMasterPostion(), player, 0);
			GiantCubeUtil.removeStructure(gcte.getMasterPostion(), level);
		}
		return super.playerWillDestroy(level, pos, state, player);
	}
}
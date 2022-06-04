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
import net.minecraftforge.common.util.FakePlayer;

public class BlockGiantCube extends BaseChanceBlock implements EntityBlock
{
	public BlockGiantCube()
	{
		super(getBuilder(), "giant_chance_cube");
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileGiantCube(pos, state);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		BlockEntity te = level.getBlockEntity(pos);
		if(!(te instanceof TileGiantCube gc))
			return Shapes.block();

		BlockPos diff = pos.subtract(gc.getMasterPostion());
		return Shapes.box(-1 - diff.getX(), -1 - diff.getY(), -1 - diff.getZ(), 2 - diff.getX(), 2 - diff.getY(), 2 - diff.getZ());
	}

	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
	{
		super.playerWillDestroy(level, pos, state, player);
		BlockEntity be = level.getBlockEntity(pos);
		if(!level.isClientSide() && !(player instanceof FakePlayer) && be instanceof TileGiantCube gcte)
		{
			if(!player.getInventory().getSelected().isEmpty() && player.getInventory().getSelected().getItem().equals(CCubesItems.silkPendant))
			{
				popResource(level, pos, new ItemStack(CCubesBlocks.COMPACT_GIANT_CUBE));
				GiantCubeUtil.removeStructure(gcte.getMasterPostion(), level);
				return;
			}

			if(!gcte.hasMaster() || !gcte.checkForMaster())
				level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

			ServerLevel serverWorld = (ServerLevel) level;
			RewardsUtil.executeCommand(serverWorld, player, player.getOnPos(), "/advancement grant @p only chancecubes:giant_chance_cube");
			player.awardStat(StatsRegistry.OPENED_GIANT_CHANCE_CUBE);
			GlobalCCRewardRegistry.GIANT.triggerRandomReward(serverWorld, gcte.getMasterPostion(), player, 0);
			GiantCubeUtil.removeStructure(gcte.getMasterPostion(), level);
		}
	}
}
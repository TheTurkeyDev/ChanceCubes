//package chanceCubes.blocks;
//
//import chanceCubes.items.CCubesItems;
//import chanceCubes.items.ItemChanceCube;
//import chanceCubes.network.CCubesPacketHandler;
//import chanceCubes.network.PacketTriggerD20;
//import chanceCubes.util.RewardsUtil;
//import chanceCubes.util.StatsRegistry;
//import net.minecraft.core.BlockPos;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.EntityBlock;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.BlockHitResult;
//import net.minecraft.world.phys.shapes.CollisionContext;
//import net.minecraft.world.phys.shapes.VoxelShape;
//import net.minecraftforge.common.util.FakePlayer;
//import net.minecraftforge.network.PacketDistributor;
//
//public class BlockChanceD20 extends BaseChanceBlock implements EntityBlock
//{
//	private static final VoxelShape SHAPE = Block.box(0.1, 0.1, 0.1, 15.9, 15.9, 15.9);
//
//	public BlockChanceD20()
//	{
//		super(getBuilder().strength(-1f, Integer.MAX_VALUE).lightLevel(state -> 7));
//	}
//
//	@Override
//	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
//	{
//		return new TileChanceD20(pos, state);
//	}
//
//	@Override
//	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_)
//	{
//		return SHAPE;
//	}
//
//	@Override
//	public void attack(BlockState state, Level level, BlockPos pos, Player player)
//	{
//		this.startd20(level, pos, player);
//	}
//
////	@Override
////	public BlockRenderType getRenderType(BlockState state)
////	{
////		return BlockRenderType.ENTITYBLOCK_ANIMATED;
////	}
//
//	@Override
//	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
//	{
//		return this.startd20(level, pos, player) ? InteractionResult.PASS : InteractionResult.FAIL;
//	}
//
//	public boolean startd20(Level level, BlockPos pos, Player player)
//	{
//		if(level.isClientSide() || player == null || player instanceof FakePlayer)
//			return true;
//
//		if(level.getBlockEntity(pos) instanceof TileChanceD20 te)
//		{
//			if(!player.getInventory().getSelected().isEmpty() && player.getInventory().getSelected().getItem().equals(CCubesItems.SILK_PENDANT.get()))
//			{
//				ItemStack stack = new ItemStack(CCubesItems.CHANCE_ICOSAHEDRON.get(), 1);
//				((ItemChanceCube) stack.getItem()).setChance(stack, te.isScanned() ? te.getChance() : -101);
//				popResource(level, pos, stack);
//				level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
//				level.removeBlockEntity(pos);
//				return false;
//			}
//
//			RewardsUtil.executeCommand((ServerLevel) level, player, player.getOnPos(), "/advancement grant @p only chancecubes:chance_icosahedron");
//			player.awardStat(StatsRegistry.OPENED_D20.get());
//			te.startBreaking(player);
//			CCubesPacketHandler.CHANNEL.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 50, level.dimension())), new PacketTriggerD20(pos));
//		}
//		return false;
//	}
//}
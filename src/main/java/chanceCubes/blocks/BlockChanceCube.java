package chanceCubes.blocks;

import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.util.GiantCubeUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.StatsRegistry;
import chanceCubes.util.Task;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;

public class BlockChanceCube extends BaseChanceBlock implements EntityBlock
{
	public static final EnumProperty<EnumTexture> TEXTURE = EnumProperty.create("texture", BlockChanceCube.EnumTexture.class);

	public static EnumTexture textureToSet = EnumTexture.DEFAULT;

	public BlockChanceCube()
	{
		super(getBuilder().lightLevel(state -> state.getValue(TEXTURE).equals(EnumTexture.HALLOWEEN) ? 2 : 0));
		this.registerDefaultState(this.getStateDefinition().any().setValue(TEXTURE, EnumTexture.DEFAULT));
	}


	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state)
	{
		return new TileChanceCube(pos, state);
	}

	@Override
	public BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player)
	{
		BlockEntity be = level.getBlockEntity(pos);
		if(!level.isClientSide() && !(player instanceof FakePlayer) && be instanceof TileChanceCube te)
		{
			if(!player.getInventory().getSelected().isEmpty() && player.getInventory().getSelected().getItem().equals(CCubesItems.SILK_PENDANT.get()))
			{
				ItemStack stackCube = new ItemStack(CCubesItems.CHANCE_CUBE.get(), 1);
				((ItemChanceCube) stackCube.getItem()).setChance(stackCube, te.isScanned() ? te.getChance() : -101);
				ItemEntity blockStack = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stackCube);
				level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				level.removeBlockEntity(pos);
				level.addFreshEntity(blockStack);
				return super.playerWillDestroy(level, pos, state, player);
			}

			level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			Scheduler.scheduleTask(new Task("reward_delay", 1)
			{
				@Override
				public void callback()
				{
					GlobalCCRewardRegistry.DEFAULT.triggerRandomReward((ServerLevel) level, pos, player, te.getChance());
				}
			});
			player.awardStat(StatsRegistry.OPENED_CHANCE_CUBE.get());
		}
		return super.playerWillDestroy(level, pos, state, player);
	}

	@Override
	public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state2, boolean p_60570_)
	{
		GiantCubeUtil.checkMultiBlockForm(pos, level, true);
		super.onPlace(state, level, pos, state2, p_60570_);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		BlockState iBlockState = super.getStateForPlacement(context);
		return iBlockState.setValue(TEXTURE, textureToSet);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(TEXTURE);
	}

	public enum EnumTexture implements StringRepresentable
	{
		// @formatter:off
		DEFAULT("default"), VALENTINES("valentines"), STPATRICKS("stpatricks"), 
		HALLOWEEN("halloween"), HOLIDAYS("holidays"), EASTER("easter");
		// @formatter:on

		private final String name;

		EnumTexture(String name)
		{
			this.name = name;
		}

		public String toString()
		{
			return this.name;
		}

		public String getName()
		{
			return this.name;
		}


		@Override
		public String getSerializedName()
		{
			return this.name;
		}
	}
}
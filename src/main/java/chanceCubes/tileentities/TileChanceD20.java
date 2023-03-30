package chanceCubes.tileentities;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.sounds.CCubesSounds;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import java.util.Random;

public class TileChanceD20 extends BlockEntity
{
	private static final Random random = new Random();

	private boolean breaking = false;
	private int stage = 0;
	public float rotation = 0, wave = 0;
	private Player player;

	private int chance;
	private boolean isScanned = false;

	public TileChanceD20(BlockPos pos, BlockState state)
	{
		super(CCubesBlocks.TILE_CHANCE_ICOSAHEDRON, pos, state);
		if(!CCubesSettings.d20UseNormalChances.get())
		{
			this.chance = random.nextBoolean() ? -100 : 100;
		}
		else
		{
			this.chance = Math.round((float) (random.nextGaussian() * 40));
			while(this.chance > 100 || this.chance < -100)
				this.chance = Math.round((float) (random.nextGaussian() * 40));
		}
	}

	public TileChanceD20(int initialChance, BlockPos pos, BlockState state)
	{
		super(CCubesBlocks.TILE_CHANCE_ICOSAHEDRON, pos, state);
		this.chance = initialChance;
	}


	public void setChance(int newChance)
	{
		this.chance = newChance;
	}

	public int getChance()
	{
		return this.chance;
	}


	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.putInt("chance", this.getChance());
	}

	@Override
	public CompoundTag getUpdateTag()
	{
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("chance", this.getChance());
		return nbt;
	}


	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		this.chance = nbt.getInt("chance");
	}

	//TODO
	public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t)
	{
		TileChanceD20 d20 = (TileChanceD20) t;
		if(d20.breaking && d20.stage < 200) {
			d20.stage++;
		}
		if(d20.stage >= 200)
		{
			d20.breaking = false;
			d20.stage = 0;
			if(level != null && !level.isClientSide())
			{
				level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
				level.removeBlockEntity(blockPos);
				GlobalCCRewardRegistry.DEFAULT.triggerRandomReward((ServerLevel) level, blockPos, d20.player, d20.getChance());
			}
		}
		else if(level != null && level.isClientSide())
		{
			Quaternion yaw = new Quaternion(0, 1, 0, (float) (Math.toRadians((level.getGameTime() % 10000F) / 10000F * 360F) + (0.4 + Math.pow(1.02, d20.getStage() + 1))));
			Quaternion pitch = new Quaternion(1, 0, 0, 0F);

			level.setBlockAndUpdate(blockPos, blockState);
		}
	}

	public void startBreaking(Player player)
	{
		if(!breaking)
		{
			if(!player.level.isClientSide())
			{
				player.level.playSound(null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), CCubesSounds.D20_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
				this.player = player;
			}
			breaking = true;
			stage = 0;
		}
	}

	public int getStage()
	{
		return this.stage;
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket pkt)
	{
		load(pkt.getTag());
	}

	public boolean isScanned()
	{
		return isScanned;
	}

	public void setScanned(boolean isScanned)
	{
		this.isScanned = isScanned;
	}

	@Nonnull
	@Override
	public IModelData getModelData()
	{
		IModelData modelData = super.getModelData();
		return modelData;
	}

	public static class D20AnimationWrapper
	{
		public Vector3f transform;
		public Quaternion rot;
	}
}

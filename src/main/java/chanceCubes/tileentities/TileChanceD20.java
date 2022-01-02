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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import java.util.Random;

public class TileChanceD20 extends BlockEntity
{
	public static final ModelProperty<D20AnimationWrapper> D20AnimationProperty = new ModelProperty<>();

	private static final Random random = new Random();

	private boolean breaking = false;
	private int stage = 0;
	public float rotation = 0, wave = 0;
	private Player player;

	private int chance;
	private boolean isScanned = false;

	private final D20AnimationWrapper animationWrapper = new D20AnimationWrapper();

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
		nbt = super.save(nbt);
		return nbt;
	}


	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		this.chance = nbt.getInt("chance");
	}

	//TODO
	public void tick()
	{
		if(breaking)
			stage++;
		if(stage > 200)
		{
			breaking = false;
			if(this.level != null && !this.level.isClientSide())
			{
				this.level.setBlockAndUpdate(this.getBlockPos(), Blocks.AIR.defaultBlockState());
				this.level.removeBlockEntity(this.getBlockPos());
				GlobalCCRewardRegistry.DEFAULT.triggerRandomReward((ServerLevel) this.level, this.getBlockPos(), player, this.getChance());
			}
		}
		else if(level != null && level.isClientSide())
		{
			Quaternion yaw = new Quaternion(0, 1, 0, (float) (Math.toRadians((level.getGameTime() % 10000F) / 10000F * 360F) + (0.4 + Math.pow(1.02, getStage() + 1))));
			Quaternion pitch = new Quaternion(1, 0, 0, 0F);

			animationWrapper.transform = new Vector3f(0.5F, 0.5F + wave * 0.15f, 0.5F);
			animationWrapper.rot = yaw;

			//if(breaking)
			//{
//				Quaternion rot = new Quaternion(0, 0, 0, 1);
//				rot.multiply(yaw);
//				rot.multiply(pitch);
//				rotationMat = new TransformationMatrix(new Matrix4f(rot));
			//}

			this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState());
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
		//modelData.setData(Properties.AnimationProperty, animationWrapper);
		return modelData;
	}

	public static class D20AnimationWrapper
	{
		public Vector3f transform;
		public Quaternion rot;
	}
}

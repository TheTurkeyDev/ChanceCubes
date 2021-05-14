package chanceCubes.tileentities;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.sounds.CCubesSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import java.util.Random;

public class TileChanceD20 extends TileEntity implements ITickableTileEntity
{
	public static final ModelProperty<D20AnimationWrapper> D20AnimationProperty = new ModelProperty<>();

	private static final Random random = new Random();

	private boolean breaking = false;
	private int stage = 0;
	public float rotation = 0, wave = 0;
	private PlayerEntity player;

	private int chance;
	private boolean isScanned = false;

	private D20AnimationWrapper animationWrapper = new D20AnimationWrapper();

	public TileChanceD20()
	{
		super(CCubesBlocks.TILE_CHANCE_ICOSAHEDRON);
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

	public TileChanceD20(int initialChance)
	{
		super(CCubesBlocks.TILE_CHANCE_ICOSAHEDRON);
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
	public CompoundNBT write(CompoundNBT nbt)
	{
		nbt.putInt("chance", this.getChance());
		nbt = super.write(nbt);
		return nbt;
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt)
	{
		super.read(state, nbt);
		this.chance = nbt.getInt("chance");
	}

	@Override
	public void tick()
	{
		if(breaking)
			stage++;
		if(stage > 200)
		{
			breaking = false;
			if(this.world != null && !this.world.isRemote)
			{
				this.world.setBlockState(this.pos, Blocks.AIR.getDefaultState());
				this.world.removeTileEntity(this.pos);
				GlobalCCRewardRegistry.DEFAULT.triggerRandomReward((ServerWorld) this.world, this.pos, player, this.getChance());
			}
		}
		else if(world != null && world.isRemote)
		{
			Quaternion yaw = new Quaternion(0, 1, 0, (float) (Math.toRadians((world.getGameTime() % 10000F) / 10000F * 360F) + (0.4 + Math.pow(1.02, getStage() + 1))));
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

			this.world.markBlockRangeForRenderUpdate(this.pos, this.getBlockState(), this.getBlockState());
		}
	}

	public void startBreaking(PlayerEntity player)
	{
		if(!breaking)
		{
			if(!player.world.isRemote)
			{
				player.world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), CCubesSounds.D20_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
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
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT syncData = new CompoundNBT();
		syncData = this.write(syncData);
		return new SUpdateTileEntityPacket(this.pos, 1, syncData);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		read(this.getBlockState(), pkt.getNbtCompound());
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

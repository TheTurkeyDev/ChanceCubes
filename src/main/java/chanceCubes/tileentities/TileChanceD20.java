package chanceCubes.tileentities;

import java.util.Random;

import chanceCubes.config.CCubesSettings;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.util.RenderUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class TileChanceD20 extends TileEntity implements ITickable
{
	private static final float BASE_SPIN_SPEED = 0.5F;
	private static final float HOVER_SPEED = 12F;

	private static final Random random = new Random();

	private boolean breaking = false;
	private int stage = 0;
	public float rotation = 0, rotationDelta = 0, rotationInc = 0, wave = 0;
	private EntityPlayer player;

	private int chance;

	public TileChanceD20()
	{
		if(!CCubesSettings.d20UseNormalChances)
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
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("chance", this.chance);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.chance = nbt.getInteger("chance");
	}

	public void update()
	{
		if(!breaking)
			return;
		stage++;
		if(stage > 200)
		{
			breaking = false;
			if(!this.worldObj.isRemote)
			{
				this.worldObj.setBlockToAir(this.pos);
				this.worldObj.removeTileEntity(this.pos);
				ChanceCubeRegistry.INSTANCE.triggerRandomReward(this.worldObj, this.pos, player, this.getChance());
			}
		}
		else if(worldObj.isRemote)
		{
			rotationDelta = (float) (BASE_SPIN_SPEED + Math.pow(1.02, getStage() + 1));
			rotation += (float) (BASE_SPIN_SPEED + Math.pow(1.02, getStage()));
		}
	}

	public void renderUpdate(float partialTick, long worldTime, BlockPos pos)
	{
		random.setSeed(RenderUtil.getCoordinateRandom(pos.getX(), pos.getY(), pos.getY()));
		rotationInc = rotation + (rotationDelta * partialTick);
		wave = stage == 0 ? MathHelper.sin((((worldTime % (HOVER_SPEED * 1000F) + partialTick) / (HOVER_SPEED * 1000F)) + random.nextFloat()) * 360F) : ((stage + partialTick) / 10f);
	}

	public void startBreaking(EntityPlayer player)
	{
		if(!breaking)
		{
			if(!player.worldObj.isRemote)
			{
				//TODO: 
				//player.worldObj.playSoundEffect(this.pos.getX(), this.pos.getY(), this.pos.getZ(), CCubesCore.MODID + ":d20_Break", 1, 1);
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
	public SPacketUpdateTileEntity getDescriptionPacket()
	{
		NBTTagCompound syncData = new NBTTagCompound();
		this.writeSyncableDataToNBT(syncData);
		return new SPacketUpdateTileEntity(this.pos, 1, syncData);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readSyncableDataFromNBT(pkt.getNbtCompound());
	}

	private void writeSyncableDataToNBT(NBTTagCompound syncData)
	{
		syncData.setInteger("chance", this.getChance());

	}

	private void readSyncableDataFromNBT(NBTTagCompound nbt)
	{
		this.chance = nbt.getInteger("chance");
	}
}

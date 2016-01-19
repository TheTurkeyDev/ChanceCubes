package chanceCubes.tileentities;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import chanceCubes.CCubesCore;
import chanceCubes.registry.ChanceCubeRegistry;

public class TileChanceD20 extends TileEntity
{
	private boolean breaking = false;
	private int stage = 0;
	public float rotationStage = 0;
	private EntityPlayer player;

	private int chance;

	public TileChanceD20()
	{
		this(new Random().nextBoolean() ? -100 : 100);
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

	public void updateEntity()
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
	}

	public void startBreaking(EntityPlayer player)
	{
		System.out.println("here");
		if(!breaking)
		{
			System.out.println("here 2");
			if(!player.worldObj.isRemote)
			{
				System.out.println("here 3");
				player.worldObj.playSoundEffect(this.pos.getX(), this.pos.getY(), this.pos.getZ(), CCubesCore.MODID + ":d20_Break", 1, 1);
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
	public S35PacketUpdateTileEntity getDescriptionPacket()
	{
		NBTTagCompound syncData = new NBTTagCompound();
		this.writeSyncableDataToNBT(syncData);
		return new S35PacketUpdateTileEntity(this.pos, 1, syncData);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
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

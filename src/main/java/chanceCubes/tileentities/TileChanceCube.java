package chanceCubes.tileentities;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileChanceCube extends TileEntity
{

	private int chance;

	public TileChanceCube()
	{
		this(new Random().nextInt(201) - 100);
	}

	public TileChanceCube(int initialChance)
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

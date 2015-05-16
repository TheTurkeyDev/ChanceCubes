package chanceCubes.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import chanceCubes.CCubesCore;

public class TileChanceD20 extends TileEntity
{
	private boolean breaking = false;
	private int stage = 0;
	private EntityPlayer player;

	public TileChanceD20()
	{

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
				this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
				this.worldObj.removeTileEntity(this.xCoord, this.yCoord, this.zCoord);
				CCubesCore.cCubeRegistry.triggerRandomReward(this.worldObj, this.xCoord, this.yCoord, this.zCoord, player, 0);
			}
		}
	}

	public void startBreaking(EntityPlayer player)
	{
		if(!breaking)
		{
			breaking = true;
			stage = 0;
			this.player = player;
		}
	}

	public int getStage()
	{
		return this.stage;
	}
}

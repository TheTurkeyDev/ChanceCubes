package chanceCubes.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import chanceCubes.blocks.BlockChanceCube;
import chanceCubes.registry.ChanceCubeRegistry;

import com.enderio.core.common.util.BlockCoord;

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
        if (!breaking)
            return;
        stage++;
        if (stage > 200)
        {
            breaking = false;
            if (!this.worldObj.isRemote)
            {
                this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
                this.worldObj.removeTileEntity(this.xCoord, this.yCoord, this.zCoord);
                ChanceCubeRegistry.INSTANCE.triggerRandomReward(this.worldObj, new BlockCoord(this), player, 0, BlockChanceCube.luckBound);
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

package chanceCubes.util;

import chanceCubes.CCubesCore;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class CCubesCommandSender implements ICommandSender
{
	EntityPlayer harvester;
	ChunkCoordinates blockLoc;
    
    public CCubesCommandSender(EntityPlayer player, int x, int y, int z)
    {
    	blockLoc = new ChunkCoordinates(x, y, z);
    	harvester = player;
    }
    
    @Override
    public boolean canCommandSenderUseCommand(int level, String command)
    {
        return level <= 2;
    }
    
    @Override
    public String getCommandSenderName()
    {
        return CCubesCore.NAME;
    }
    
    @Override
    public IChatComponent func_145748_c_()
    {
        return new ChatComponentText(this.getCommandSenderName());
    }

    @Override
    public void addChatMessage(IChatComponent p_145747_1_)
    {
        if (this.getEntityWorld() != null && !this.getEntityWorld().isRemote)
        {
        	this.getEntityWorld().markBlockForUpdate(blockLoc.posX, blockLoc.posY, blockLoc.posZ);
        }
    }

	@Override
	public ChunkCoordinates getPlayerCoordinates()
	{
		return blockLoc;
	}

	@Override
	public World getEntityWorld()
	{
		return harvester != null? harvester.worldObj : null;
	}
}
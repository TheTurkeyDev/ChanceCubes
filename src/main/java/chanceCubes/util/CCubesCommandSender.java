package chanceCubes.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CCubesCommandSender extends CommandBlockLogic
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
			this.getEntityWorld().markBlockForUpdate(blockLoc.posX, blockLoc.posY, blockLoc.posZ);
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
	
    public void func_145756_e(){}

    @SideOnly(Side.CLIENT)
    public int func_145751_f()
    {
    	return 0; // Unknown purpose
    }

    @SideOnly(Side.CLIENT)
    public void func_145757_a(ByteBuf p_145757_1_){};

    public void func_145750_b(IChatComponent p_145750_1_){}
}
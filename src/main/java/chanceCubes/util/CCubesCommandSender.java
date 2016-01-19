package chanceCubes.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CCubesCommandSender extends CommandBlockLogic
{
	EntityPlayer harvester;
	BlockPos blockLoc;

	public CCubesCommandSender(EntityPlayer player, int x, int y, int z)
	{
		blockLoc = new BlockPos(x, y, z);
		harvester = player;
		super.setName("Chance Cube");
	}
	
	public CCubesCommandSender(EntityPlayer player, BlockPos pos)
	{
		blockLoc = pos;
		harvester = player;
		super.setName("Chance Cube");
	}

	@Override
	public void addChatMessage(IChatComponent p_145747_1_)
	{
		if (this.getEntityWorld() != null && !this.getEntityWorld().isRemote)
		{
			this.getEntityWorld().markBlockForUpdate(blockLoc);
		}
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

	@Override
	public BlockPos getPosition()
	{
		return blockLoc;
	}

	@Override
	public Vec3 getPositionVector()
	{
		return new Vec3(blockLoc.getX(), blockLoc.getY(), blockLoc.getZ());
	}

	@Override
	public Entity getCommandSenderEntity()
	{
		return harvester;
	}
	
	@Override
	public void updateCommand()
	{
		
	}
}
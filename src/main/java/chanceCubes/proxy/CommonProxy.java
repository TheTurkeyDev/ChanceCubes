package chanceCubes.proxy;

import net.minecraft.entity.player.EntityPlayer;


public class CommonProxy
{

	public boolean isClient()
	{
		return false;
	}

	public void registerGuis()
	{

	}

	public void registerRenderings()
	{

	}
	
	public void registerEvents()
	{
		
	}
	
	public EntityPlayer getClientPlayer()
	{
		return null;
	}
}

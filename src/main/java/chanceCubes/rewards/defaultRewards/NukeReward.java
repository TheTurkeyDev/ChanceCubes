package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class NukeReward implements IChanceCubeReward
{
	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		 player.addChatMessage(new ChatComponentText("May death rain upon them"));
		 world.spawnEntityInWorld(new EntityTNTPrimed(world, x-6, y+65, z-6, null));
		 world.spawnEntityInWorld(new EntityTNTPrimed(world, x-2, y+65, z-6, null));
		 world.spawnEntityInWorld(new EntityTNTPrimed(world, x+2, y+65, z-6, null));
		 world.spawnEntityInWorld(new EntityTNTPrimed(world, x+6, y+65, z-6, null));
		 world.spawnEntityInWorld(new EntityTNTPrimed(world, x-6, y+65, z-2, null));
		 world.spawnEntityInWorld(new EntityTNTPrimed(world, x-2, y+65, z-2, null));
		 world.spawnEntityInWorld(new EntityTNTPrimed(world, x+2, y+65, z-2, null));
		 world.spawnEntityInWorld(new EntityTNTPrimed(world, x+6, y+65, z-2, null));
		 world.spawnEntityInWorld(new EntityTNTPrimed(world, x-6, y+65, z+2, null));
		 world.spawnEntityInWorld(new EntityTNTPrimed(world, x-2, y+65, z+2, null));
		 world.spawnEntityInWorld(new EntityTNTPrimed(world, x+2, y+65, z+2, null));
		 world.spawnEntityInWorld(new EntityTNTPrimed(world, x+6, y+65, z+2, null));
		 world.spawnEntityInWorld(new EntityTNTPrimed(world, x-6, y+65, z+6, null));
		 world.spawnEntityInWorld(new EntityTNTPrimed(world, x-2, y+65, z+6, null));
		 world.spawnEntityInWorld(new EntityTNTPrimed(world, x+2, y+65, z+6, null));
		 world.spawnEntityInWorld(new EntityTNTPrimed(world, x+6, y+65, z+6, null));
	}

	@Override
	public int getChanceValue()
	{
		return -85;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID+":Nuke";
	}

}

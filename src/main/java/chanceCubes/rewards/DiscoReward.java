package chanceCubes.rewards;

import java.util.Random;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;

public class DiscoReward implements IChanceCubeReward
{
	private Random rand = new Random();
	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		for(int xx = -4; xx < 5; xx++)
		{
			for(int zz = -4; xx < 5; zz++)
			{
				world.setBlock(x+xx, y-1, z+zz, Blocks.wool, rand.nextInt(16), 3);
			}
		}
		
		for(int i = 0; i < 10; i++)
		{
			EntitySheep sheep = new EntitySheep(world);
			sheep.setCustomNameTag("Jeb_");
		}
		
		world.setBlock(x, y + 3, z, CCubesBlocks.chanceIcosahedron);
		
		player.addChatMessage(new ChatComponentText("Disco Party!!!!"));
	}

	@Override
	public int getChanceValue()
	{
		return 40;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Disco";
	}

}

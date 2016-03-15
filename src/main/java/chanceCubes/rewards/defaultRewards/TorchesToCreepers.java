package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class TorchesToCreepers implements IChanceCubeReward
{

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		for(int yy = -32; yy <= 32; yy++)
		{
			for(int xx = -32; xx <= 32; xx++)
			{
				for(int zz = -32; zz <= 32; zz++)
				{
					if(world.getBlock(x + xx, y + yy, z + zz).getLightValue() > 0)
					{
						world.setBlock(x + xx, y + yy, z + zz, Blocks.air);
						EntityCreeper creeper = new EntityCreeper(world);
						creeper.setLocationAndAngles(x + xx + 0.5, y + yy, z + zz + 0.5, 0, 0);
						world.spawnEntityInWorld(creeper);
					}
				}
			}
		}
		player.addChatMessage(new ChatComponentText("Those lights seem a little weird.... O.o"));
	}

	@Override
	public int getChanceValue()
	{
		return -40;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Torches_To_Creepers";
	}

}

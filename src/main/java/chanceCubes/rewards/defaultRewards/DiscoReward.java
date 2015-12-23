package chanceCubes.rewards.defaultRewards;

import java.util.Random;

import net.minecraft.block.BlockColored;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;

public class DiscoReward implements IChanceCubeReward
{
	private Random rand = new Random();

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		for(int xx = -4; xx < 5; xx++)
		{
			for(int zz = -4; zz < 5; zz++)
			{
				world.setBlockState(pos.add(xx, -1, zz), Blocks.wool.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.byMetadata(rand.nextInt(16))), 3);
			}
		}

		for(int i = 0; i < 10; i++)
		{
			EntitySheep sheep = new EntitySheep(world);
			sheep.setCustomNameTag("jeb_");
			sheep.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
			world.spawnEntityInWorld(sheep);
		}

		// world.setBlockState(pos.add(0, 3, 0), CCubesBlocks.chanceIcosahedron);

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
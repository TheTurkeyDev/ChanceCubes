package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class TorchesToCreepers extends BaseCustomReward
{
	public TorchesToCreepers()
	{
		this.setChanceValue(-40);
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		for(int yy = -32; yy <= 32; yy++)
		{
			for(int xx = -32; xx <= 32; xx++)
			{
				for(int zz = -32; zz <= 32; zz++)
				{
					IBlockState b = world.getBlockState(pos.add(xx, yy, zz));
					if(b.getLightValue(world, pos) > 0 && b.getBlock() != Blocks.LAVA && !b.getBlock().hasTileEntity(b))
					{
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(xx, yy, zz));
						EntityCreeper creeper = new EntityCreeper(world);
						creeper.setLocationAndAngles(pos.getX() + xx + 0.5, pos.getY() + yy, pos.getZ() + zz + 0.5, 0, 0);
						world.spawnEntity(creeper);
					}
				}
			}
		}
		player.sendMessage(new TextComponentString("Those lights seem a little weird.... O.o"));
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Torches_To_Creepers";
	}

}
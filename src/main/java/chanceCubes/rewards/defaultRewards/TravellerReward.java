package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TravellerReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		int x = RewardsUtil.rand.nextInt(1000) + 1000;
		int z = RewardsUtil.rand.nextInt(1000) + 1000;

		BlockPos newPos = pos.add(x, 0, z);
		RewardsUtil.placeBlock(Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.WEST), world, newPos);
		TileEntityChest chest = (TileEntityChest) world.getTileEntity(newPos);
		for(int i = 0; i < 10; i++)
			chest.setInventorySlotContents(i, new ItemStack(RewardsUtil.getRandomItem()));

		RewardsUtil.sendMessageToNearPlayers(world, pos, 25, "" + newPos.getX() + ", " + newPos.getY() + ", " + newPos.getZ());
	}

	@Override
	public int getChanceValue()
	{
		return 15;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Traveller";
	}

}

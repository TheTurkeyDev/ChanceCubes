package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class RottenFoodReward extends BaseCustomReward
{
	public RottenFoodReward()
	{
		super(CCubesCore.MODID + ":Rotten_Food", -30);
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		for(int i = 0; i < player.inventory.mainInventory.size(); i++)
		{
			ItemStack stack = player.inventory.mainInventory.get(i);
			if(!stack.isEmpty() && stack.getItem() instanceof ItemFood)
				player.inventory.mainInventory.set(i, new ItemStack(Items.ROTTEN_FLESH, stack.getCount()));
		}

		player.sendMessage(new TextComponentString("Ewwww it's all rotten"));

	}
}
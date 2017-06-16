package chanceCubes.rewards.defaultRewards;

import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemOfDestinyReward implements IChanceCubeReward
{
	private Random rand = new Random();

	@Override
	public void trigger(World world, BlockPos pos, final EntityPlayer player)
	{
		final EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(RewardsUtil.getRandomItem(), 1));
		item.setPickupDelay(100000);
		world.spawnEntity(item);
		player.sendMessage(new TextComponentString("Selecting random item"));
		Scheduler.scheduleTask(new Task("Item_Of_Destiny_Reward", -1, 5)
		{
			int iteration = 0;
			int enchants = 0;

			@Override
			public void callback()
			{

			}

			@Override
			public void update()
			{
				if(iteration < 17)
				{
					item.setEntityItemStack(new ItemStack(RewardsUtil.getRandomItem(), 1));
				}
				else if(iteration == 17)
				{
					player.sendMessage(new TextComponentString("Random item selected"));
					player.sendMessage(new TextComponentString("Selecting number of enchants to give item"));
				}
				else if(iteration == 27)
				{
					int i = RewardsUtil.rand.nextInt(9);
					enchants = i < 5 ? 1 : i < 8 ? 2 : 3;
					player.sendMessage(new TextComponentString(enchants + " random enchants will be added!"));
					player.sendMessage(new TextComponentString("Selecting random enchant to give to the item"));
				}
				else if(iteration > 27 && (iteration - 7) % 10 == 0)
				{
					if((iteration / 10) - 3 < enchants)
					{
						Enchantment ench = randomEnchantment();
						int level = ench.getMinLevel() + RewardsUtil.rand.nextInt(ench.getMaxLevel());
						item.getEntityItem().addEnchantment(ench, level);
						player.sendMessage(new TextComponentString(ench.getTranslatedName(level) + " Has been added to the item!"));
					}
					else
					{
						player.sendMessage(new TextComponentString("Your item of destiny is complete! Enjoy!"));
						item.setPickupDelay(0);
						Scheduler.removeTask(this);
					}
				}

				iteration++;
			}
		});
	}

	public Enchantment randomEnchantment()
	{

		Enchantment ench = Enchantment.getEnchantmentByID(rand.nextInt(Enchantment.REGISTRY.getKeys().size()));
		while(ench == null)
			ench = Enchantment.getEnchantmentByID(rand.nextInt(Enchantment.REGISTRY.getKeys().size()));
		return ench;
	}

	@Override
	public int getChanceValue()
	{
		return 40;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Item_Of_Destiny";
	}

}

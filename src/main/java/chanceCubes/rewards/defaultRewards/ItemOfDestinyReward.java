package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.CustomEntry;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraft.world.World;

import java.util.Map;

public class ItemOfDestinyReward extends BaseCustomReward
{
	public ItemOfDestinyReward()
	{
		super(CCubesCore.MODID + ":item_of_destiny", 40);
	}

	@Override
	public void trigger(World world, BlockPos pos, final PlayerEntity player, Map<String, Object> settings)
	{
		final ItemEntity item = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(RewardsUtil.getRandomItem(), 1));
		item.setPickupDelay(100000);
		world.addEntity(item);
		player.sendMessage(new StringTextComponent("Selecting random item"));
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
					item.setItem(new ItemStack(RewardsUtil.getRandomItem(), 1));
				}
				else if(iteration == 17)
				{
					player.sendMessage(new StringTextComponent("Random item selected"));
					player.sendMessage(new StringTextComponent("Selecting number of enchants to give item"));
				}
				else if(iteration == 27)
				{
					int i = RewardsUtil.rand.nextInt(9);
					enchants = i < 5 ? 1 : i < 8 ? 2 : 3;
					player.sendMessage(new StringTextComponent(enchants + " random enchants will be added!"));
					player.sendMessage(new StringTextComponent("Selecting random enchant to give to the item"));
				}
				else if(iteration > 27 && (iteration - 7) % 10 == 0)
				{
					if((iteration / 10) - 3 < enchants)
					{
						CustomEntry<Enchantment, Integer> ench = RewardsUtil.getRandomEnchantmentAndLevel();
						item.getItem().addEnchantment(ench.getKey(), ench.getValue());
						player.sendMessage(new StringTextComponent(LanguageMap.getInstance().translateKey(ench.getKey().getName()) + " Has been added to the item!"));
					}
					else
					{
						player.sendMessage(new StringTextComponent("Your item of destiny is complete! Enjoy!"));
						item.setPickupDelay(0);
						Scheduler.removeTask(this);
					}
				}

				iteration++;
			}
		});
	}
}

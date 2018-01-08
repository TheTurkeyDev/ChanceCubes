package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class CountDownReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		Scheduler.scheduleTask(new Task("Countdown_Reward_Delay", 80, 20)
		{
			int count = 0;

			@Override
			public void callback()
			{
				int thing = RewardsUtil.rand.nextInt(10);

				if(thing == 0)
				{
					world.setBlockState(pos, Blocks.DIAMOND_BLOCK.getDefaultState());
				}
				else if(thing == 1)
				{
					world.setBlockState(pos, Blocks.GLASS.getDefaultState());
				}
				else if(thing == 2)
				{
					world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
				}
				else if(thing == 3)
				{
					EntityCreeper creeper = new EntityCreeper(world);
					creeper.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
					world.spawnEntityInWorld(creeper);
				}
				else if(thing == 4)
				{
					EntityCow cow = new EntityCow(world);
					cow.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
					world.spawnEntityInWorld(cow);
				}
				else if(thing == 5)
				{
					EntityVillager villager = new EntityVillager(world);
					villager.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
					world.spawnEntityInWorld(villager);
				}
				else if(thing == 6)
				{
					EntityTNTPrimed tnt = new EntityTNTPrimed(world);
					tnt.setFuse(20);
					tnt.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
					world.spawnEntityInWorld(tnt);
				}
				else if(thing == 7)
				{
					EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(RewardsUtil.getRandomItem(), 1));
					world.spawnEntityInWorld(item);
				}
				else if(thing == 8)
				{
					PotionType potionType = PotionType.REGISTRY.getObjectById(RewardsUtil.rand.nextInt(PotionType.REGISTRY.getKeys().size()));
					EntityPotion pot = new EntityPotion(world, player, PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionType));
					pot.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					pot.motionY = -1;
					world.spawnEntityInWorld(pot);
				}
				else if(thing == 9)
				{
					world.setBlockState(pos, RewardsUtil.getRandomFluid().getBlock().getDefaultState());
				}
			}

			@Override
			public void update()
			{
				if(count == 0)
				{
					player.addChatMessage(new TextComponentString("3...."));
				}
				else if(count == 1)
				{
					player.addChatMessage(new TextComponentString("2...."));
				}
				else
				{
					player.addChatMessage(new TextComponentString("1...."));
				}
				count++;
			}
		});
	}

	@Override
	public int getChanceValue()
	{
		return 15;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Countdown";
	}

}

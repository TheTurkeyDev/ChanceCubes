package chanceCubes.rewards.defaultRewards;

import java.util.Map;

import chanceCubes.CCubesCore;
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
import net.minecraft.network.play.server.SPacketTitle.Type;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CountDownReward extends BaseCustomReward
{
	public CountDownReward()
	{
		super(CCubesCore.MODID + ":countdown", 15);
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		Scheduler.scheduleTask(new Task("Countdown_Reward_Delay", 80, 20)
		{
			@Override
			public void callback()
			{
				int thing = RewardsUtil.rand.nextInt(10);

				if(thing == 0)
				{
					RewardsUtil.placeBlock(Blocks.DIAMOND_BLOCK.getDefaultState(), world, pos);
				}
				else if(thing == 1)
				{
					RewardsUtil.placeBlock(Blocks.GLASS.getDefaultState(), world, pos);
				}
				else if(thing == 2)
				{
					RewardsUtil.placeBlock(Blocks.COBBLESTONE.getDefaultState(), world, pos);
				}
				else if(thing == 3)
				{
					EntityCreeper creeper = new EntityCreeper(world);
					creeper.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
					world.spawnEntity(creeper);
				}
				else if(thing == 4)
				{
					EntityCow cow = new EntityCow(world);
					cow.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
					world.spawnEntity(cow);
				}
				else if(thing == 5)
				{
					EntityVillager villager = new EntityVillager(world);
					villager.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
					world.spawnEntity(villager);
				}
				else if(thing == 6)
				{
					EntityTNTPrimed tnt = new EntityTNTPrimed(world);
					tnt.setFuse(20);
					tnt.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
					world.spawnEntity(tnt);
				}
				else if(thing == 7)
				{
					EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(RewardsUtil.getRandomItem(), 1));
					world.spawnEntity(item);
				}
				else if(thing == 8)
				{
					PotionType potionType = PotionType.REGISTRY.getObjectById(RewardsUtil.rand.nextInt(PotionType.REGISTRY.getKeys().size()));
					EntityPotion pot = new EntityPotion(world, player, PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionType));
					pot.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					pot.motionY = -1;
					world.spawnEntity(pot);
				}
				else if(thing == 9)
				{
					RewardsUtil.placeBlock(RewardsUtil.getRandomFluid().getBlock().getDefaultState(), world, pos);
				}
			}

			@Override
			public void update()
			{
				this.showTimeLeft(player, Type.TITLE);
			}
		});
	}
}
package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;

public class CountDownReward extends BaseCustomReward
{
	public CountDownReward()
	{
		super(CCubesCore.MODID + ":countdown", 15);
	}

	@Override
	public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
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
					CreeperEntity creeper = EntityType.CREEPER.create(world);
					creeper.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
					world.addEntity(creeper);
				}
				else if(thing == 4)
				{
					CowEntity cow = EntityType.COW.create(world);
					cow.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
					world.addEntity(cow);
				}
				else if(thing == 5)
				{
					VillagerEntity villager = EntityType.VILLAGER.create(world);
					villager.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
					world.addEntity(villager);
				}
				else if(thing == 6)
				{
					TNTEntity tnt = EntityType.TNT.create(world);
					tnt.setFuse(20);
					tnt.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
					world.addEntity(tnt);
				}
				else if(thing == 7)
				{
					ItemEntity item = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(RewardsUtil.getRandomItem(), 1));
					world.addEntity(item);
				}
				else if(thing == 8)
				{
					PotionEntity pot = new PotionEntity(world, player);
					pot.setItem(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), RewardsUtil.getRandomPotionType()));
					pot.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					pot.setMotion(0, -1, 0);
					world.addEntity(pot);
				}
				else if(thing == 9)
				{
					RewardsUtil.placeBlock(RewardsUtil.getRandomFluid(true).getDefaultState().getBlockState(), world, pos);
				}
			}

			@Override
			public void update()
			{
				this.showTimeLeft(player, STitlePacket.Type.TITLE);
			}
		});
	}
}
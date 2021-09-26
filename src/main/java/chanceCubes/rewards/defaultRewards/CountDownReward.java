package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
public class CountDownReward extends BaseCustomReward
{
	public CountDownReward()
	{
		super(CCubesCore.MODID + ":countdown", 15);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		Scheduler.scheduleTask(new Task("Countdown_Reward_Delay", 80, 20)
		{
			@Override
			public void callback()
			{
				int thing = RewardsUtil.rand.nextInt(10);

				if(thing == 0)
				{
					RewardsUtil.placeBlock(Blocks.DIAMOND_BLOCK.defaultBlockState(), level, pos);
				}
				else if(thing == 1)
				{
					RewardsUtil.placeBlock(Blocks.GLASS.defaultBlockState(), level, pos);
				}
				else if(thing == 2)
				{
					RewardsUtil.placeBlock(Blocks.COBBLESTONE.defaultBlockState(), level, pos);
				}
				else if(thing == 3)
				{
					Creeper creeper = EntityType.CREEPER.create(level);
					creeper.moveTo(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
					level.addFreshEntity(creeper);
				}
				else if(thing == 4)
				{
					Cow cow = EntityType.COW.create(level);
					cow.moveTo(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
					level.addFreshEntity(cow);
				}
				else if(thing == 5)
				{
					Villager villager = EntityType.VILLAGER.create(level);
					villager.moveTo(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
					level.addFreshEntity(villager);
				}
				else if(thing == 6)
				{
					PrimedTnt tnt = EntityType.TNT.create(level);
					tnt.setFuse(20);
					tnt.moveTo(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
					level.addFreshEntity(tnt);
				}
				else if(thing == 7)
				{
					ItemEntity item = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(RewardsUtil.getRandomItem(), 1));
					level.addFreshEntity(item);
				}
				else if(thing == 8)
				{
					PotionEntity pot = new PotionEntity(level, player);
					pot.setItem(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), RewardsUtil.getRandomPotionType()));
					pot.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					pot.setMotion(0, -1, 0);
					level.addFreshEntity(pot);
				}
				else if(thing == 9)
				{
					RewardsUtil.placeBlock(RewardsUtil.getRandomFluid(true).defaultFluidState().createLegacyBlock(), level, pos);
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
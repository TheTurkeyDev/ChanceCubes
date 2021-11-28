package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.config.ConfigLoader;
import chanceCubes.mcwrapper.BlockWrapper;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.rewardtype.BlockRewardType;
import chanceCubes.rewards.rewardtype.EntityRewardType;
import chanceCubes.rewards.rewardtype.ItemRewardType;
import chanceCubes.rewards.rewardtype.MessageRewardType;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Rotations;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.ArrayList;
import java.util.List;

public class WheelSpinReward extends BaseCustomReward
{
	private static final List<RewardTrigger> rewards = new ArrayList<>();

	static
	{
		rewards.add(new RewardTrigger(new BasicReward("diamond", 0, new ItemRewardType(new ItemPart(new ItemStack(Items.DIAMOND, 1)))), "", "1 Diamond"));
		rewards.add(new RewardTrigger(new BasicReward("creeper", 0, new EntityRewardType("creeper")), "", "Creeper"));
		rewards.add(new RewardTrigger(new BaseCustomReward("tnt", 0)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				for(int i = 0; i < 5; i++)
				{
					PrimedTnt entitytntprimed = new PrimedTnt(level, player.getX(), player.getY() + 1D, player.getZ(), player);
					level.addFreshEntity(entitytntprimed);
					level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
					entitytntprimed.setFuse(140);
				}
			}
		}, "", "TNT"));
		rewards.add(new RewardTrigger(new BasicReward("diamond_cooking", 0, new ItemRewardType(new ItemPart(new ItemStack(Items.COAL_BLOCK, 1))), new MessageRewardType("Sorry, your diamond wasn't done cooking! :(")), "", "1 Diamond?"));
		rewards.add(new RewardTrigger(new BaseCustomReward("random_reward_neutral", 0)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				GlobalCCRewardRegistry.DEFAULT.triggerRandomReward(level, pos, player, 0);
			}
		}, "Random", "chance Cube", "Reward", "Value: 0"));
		rewards.add(new RewardTrigger(new BaseCustomReward("random_reward_bad", 0)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				GlobalCCRewardRegistry.DEFAULT.triggerRandomReward(level, pos, player, -50);
			}
		}, "Random", "chance Cube", "Reward", "Value: -50"));
		rewards.add(new RewardTrigger(new BaseCustomReward("random_reward_good", 0)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				GlobalCCRewardRegistry.DEFAULT.triggerRandomReward(level, pos, player, 50);
			}
		}, "Random", "chance Cube", "Reward", "Value: 50"));
		rewards.add(new RewardTrigger(new BasicReward("emerald", 0, new ItemRewardType(new ItemPart(new ItemStack(Items.EMERALD, 1)))), "", "1 Emerald"));
		rewards.add(new RewardTrigger(new BasicReward("nothing", 0, new MessageRewardType("Congrats! You won absolutely nothing!")), "", "Nothing"));
		ItemStack car = new ItemStack(Items.MINECART);
		car.setHoverName(new TextComponent("New Car!"));
		car.enchant(Enchantments.UNBREAKING, 0);
		rewards.add(new RewardTrigger(new BasicReward("new_car", 0, new ItemRewardType(new ItemPart(car))), "", "A Brand", "New Car"));
		rewards.add(new RewardTrigger(new BasicReward("lava", 0, new BlockRewardType(new OffsetBlock(0, 0, 0, Blocks.LAVA, false))), "", "Hot Stuff"));
		rewards.add(new RewardTrigger(new BaseCustomReward("vacation", 0)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				int xChange = ((RewardsUtil.rand.nextInt(50) + 20) + pos.getX()) - 35;
				int zChange = ((RewardsUtil.rand.nextInt(50) + 20) + pos.getZ()) - 35;

				int yChange = -1;

				for(int yy = 0; yy <= level.getHeight(); yy++)
				{
					if(level.getBlockState(new BlockPos(xChange, yy, zChange)).isAir() && level.getBlockState(new BlockPos(xChange, yy + 1, zChange)).isAir())
					{
						yChange = yy;
						break;
					}
				}
				if(yChange == -1)
					return;

				player.moveTo(xChange, yChange, zChange);
			}
		}, "All", "Expenses", "Paid", "Vacation"));
		rewards.add(new RewardTrigger(new BasicReward("free_groceries", 0, new ItemRewardType(new ItemPart(new ItemStack(Items.BREAD, 64)), new ItemPart(new ItemStack(Items.COOKIE, 64)), new ItemPart(new ItemStack(Items.MILK_BUCKET, 10))), new MessageRewardType("There, that should last you a year!")), "", "Free Groceries", "For a YEAR!"));
	}

	private final RewardTrigger[] rewardsChosen = new RewardTrigger[4];

	public WheelSpinReward()
	{
		super(CCubesCore.MODID + ":wheel_spin", 10);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, final Player player, JsonObject settings)
	{
		ArmorStand armorStand = EntityType.ARMOR_STAND.create(level);
		armorStand.moveTo(pos.getX() + 0.8, pos.getY() + 1.15, pos.getZ() + 1.5, 0, 0);
		armorStand.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.ORANGE_BANNER));
		armorStand.setRightArmPose(new Rotations(90, 0, 0));
		armorStand.setInvisible(true);
		armorStand.setNoGravity(true);
		level.addFreshEntity(armorStand);

		rewardsChosen[0] = rewards.get(RewardsUtil.rand.nextInt(rewards.size()));
		rewardsChosen[1] = rewards.get(RewardsUtil.rand.nextInt(rewards.size()));
		rewardsChosen[2] = rewards.get(RewardsUtil.rand.nextInt(rewards.size()));
		rewardsChosen[3] = rewards.get(RewardsUtil.rand.nextInt(rewards.size()));

		RewardBlockCache cache = new RewardBlockCache(level, pos, player.getOnPos());
		BlockState bc = Blocks.BLACK_CONCRETE.defaultBlockState();
		BlockState wc = Blocks.WHITE_CONCRETE.defaultBlockState();
		BlockState lbc = Blocks.LIGHT_BLUE_CONCRETE.defaultBlockState();

		BlockState ss = Blocks.OAK_WALL_SIGN.defaultBlockState().setValue(WallSignBlock.FACING, Direction.SOUTH);
		BlockPos offset = new BlockPos(0, 0, 1);
		cache.cacheBlock(offset, ss);
		BlockEntity be = level.getBlockEntity(pos.offset(offset));
		BlockWrapper.setSignText(be, rewardsChosen[0].signText);

		offset = new BlockPos(-2, 2, 1);
		cache.cacheBlock(offset, ss);
		be = level.getBlockEntity(pos.offset(offset));
		BlockWrapper.setSignText(be, rewardsChosen[1].signText);

		offset = new BlockPos(2, 2, 1);
		cache.cacheBlock(offset, ss);
		be = level.getBlockEntity(pos.offset(offset));
		BlockWrapper.setSignText(be, rewardsChosen[3].signText);

		offset = new BlockPos(0, 4, 1);
		cache.cacheBlock(offset, ss);
		be = level.getBlockEntity(pos.offset(offset));
		BlockWrapper.setSignText(be, rewardsChosen[2].signText);

		offset = new BlockPos(4, 1, 1);
		cache.cacheBlock(offset, ss);
		be = level.getBlockEntity(pos.offset(offset));
		BlockWrapper.setSignText(be, new String[]{"Pull", "To Spin", "The Wheel!"});

		BlockPos leverOffset = new BlockPos(4, 0, 1);
		cache.cacheBlock(leverOffset, Blocks.LEVER.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
		cache.cacheBlock(new BlockPos(4, 1, 0), wc);
		cache.cacheBlock(new BlockPos(4, 0, 0), wc);

		cache.cacheBlock(new BlockPos(-1, 4, 0), bc);
		cache.cacheBlock(new BlockPos(0, 4, 0), bc);
		cache.cacheBlock(new BlockPos(1, 4, 0), bc);
		cache.cacheBlock(new BlockPos(-2, 3, 0), lbc);
		cache.cacheBlock(new BlockPos(-1, 3, 0), bc);
		cache.cacheBlock(new BlockPos(0, 3, 0), bc);
		cache.cacheBlock(new BlockPos(1, 3, 0), lbc);
		cache.cacheBlock(new BlockPos(2, 3, 0), lbc);
		cache.cacheBlock(new BlockPos(-2, 2, 0), lbc);
		cache.cacheBlock(new BlockPos(-1, 2, 0), lbc);
		cache.cacheBlock(new BlockPos(0, 2, 0), wc);
		cache.cacheBlock(new BlockPos(1, 2, 0), lbc);
		cache.cacheBlock(new BlockPos(2, 2, 0), lbc);
		cache.cacheBlock(new BlockPos(-2, 1, 0), lbc);
		cache.cacheBlock(new BlockPos(-1, 1, 0), lbc);
		cache.cacheBlock(new BlockPos(0, 1, 0), bc);
		cache.cacheBlock(new BlockPos(1, 1, 0), bc);
		cache.cacheBlock(new BlockPos(2, 1, 0), lbc);
		cache.cacheBlock(new BlockPos(-1, 0, 0), bc);
		cache.cacheBlock(new BlockPos(0, 0, 0), bc);
		cache.cacheBlock(new BlockPos(1, 0, 0), bc);

		Scheduler.scheduleTask(new Task("wheel_spin_lever_task", 600, 1)
		{
			final BlockPos leverPos = pos.offset(leverOffset);

			@Override
			public void callback()
			{
				cache.restoreBlocks(null);
				armorStand.remove(Entity.RemovalReason.DISCARDED);
			}

			@Override
			public void update()
			{
				BlockState state = level.getBlockState(leverPos);
				if(!state.getBlock().equals(Blocks.LEVER))
				{
					level.setBlockAndUpdate(leverPos, Blocks.LEVER.defaultBlockState());
				}
				else
				{
					if(state.getValue(LeverBlock.POWERED))
					{
						spinWheel(level, pos, armorStand, cache, player);
						Scheduler.removeTask(this);
					}
				}
			}
		});
	}

	public void spinWheel(ServerLevel level, BlockPos pos, ArmorStand armorStand, RewardBlockCache cache, Player player)
	{
		int rewardPicked = RewardsUtil.rand.nextInt(4);
		int delayBase = 194 + (rewardPicked * 6) - 1;
		Scheduler.scheduleTask(new Task("wheel_spin_task", delayBase, 1)
		{
			float rotIndex = 0;

			@Override
			public void callback()
			{
				endDelay(level, pos, armorStand, cache, player, rewardPicked);
			}

			@Override
			public void update()
			{
				rotIndex += 15;
				armorStand.setRightArmPose(new Rotations(90, 0, rotIndex));

				if(rotIndex % 30 == 0)
				{
					level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.BLOCKS, 1f, 1f);
				}
			}
		});
	}

	public void endDelay(ServerLevel level, BlockPos pos, ArmorStand armorStand, RewardBlockCache cache, Player player, int rewardPicked)
	{
		Scheduler.scheduleTask(new Task("wheel_spin_end_task", 60)
		{
			@Override
			public void callback()
			{
				armorStand.remove(Entity.RemovalReason.DISCARDED);
				cache.restoreBlocks(player);
				IChanceCubeReward reward = rewardsChosen[rewardPicked].reward;
				JsonObject settingsJson = ConfigLoader.getRewardSettings(reward.getName());
				reward.trigger(level, pos, player, settingsJson);
			}
		});
	}

	private static class RewardTrigger
	{
		public String[] signText;
		public BaseCustomReward reward;

		public RewardTrigger(BaseCustomReward reward, String... lines)
		{
			this.reward = reward;
			this.signText = lines;
		}
	}
}

package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				for(int i = 0; i < 5; i++)
				{
					TNTEntity entitytntprimed = new TNTEntity(world, player.getPosX(), player.getPosY() + 1D, player.getPosZ(), player);
					world.addEntity(entitytntprimed);
					world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
					entitytntprimed.setFuse(140);
				}
			}
		}, "", "TNT"));
		rewards.add(new RewardTrigger(new BasicReward("diamond_cooking", 0, new ItemRewardType(new ItemPart(new ItemStack(Items.COAL_BLOCK, 1))), new MessageRewardType("Sorry, your diamond wasn't done cooking! :(")), "", "1 Diamond?"));
		rewards.add(new RewardTrigger(new BaseCustomReward("random_reward_neutral", 0)
		{
			@Override
			public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				GlobalCCRewardRegistry.DEFAULT.triggerRandomReward(world, pos, player, 0);
			}
		}, "Random", "chance Cube", "Reward", "Value: 0"));
		rewards.add(new RewardTrigger(new BaseCustomReward("random_reward_bad", 0)
		{
			@Override
			public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				GlobalCCRewardRegistry.DEFAULT.triggerRandomReward(world, pos, player, -50);
			}
		}, "Random", "chance Cube", "Reward", "Value: -50"));
		rewards.add(new RewardTrigger(new BaseCustomReward("random_reward_good", 0)
		{
			@Override
			public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				GlobalCCRewardRegistry.DEFAULT.triggerRandomReward(world, pos, player, 50);
			}
		}, "Random", "chance Cube", "Reward", "Value: 50"));
		rewards.add(new RewardTrigger(new BasicReward("emerald", 0, new ItemRewardType(new ItemPart(new ItemStack(Items.EMERALD, 1)))), "", "1 Emerald"));
		rewards.add(new RewardTrigger(new BasicReward("nothing", 0, new MessageRewardType("Congrats! You won absolutely nothing!")), "", "Nothing"));
		ItemStack car = new ItemStack(Items.MINECART);
		car.setDisplayName(new StringTextComponent("New Car!"));
		car.addEnchantment(Enchantments.UNBREAKING, 0);
		rewards.add(new RewardTrigger(new BasicReward("new_car", 0, new ItemRewardType(new ItemPart(car))), "", "A Brand", "New Car"));
		rewards.add(new RewardTrigger(new BasicReward("lava", 0, new BlockRewardType(new OffsetBlock(0, 0, 0, Blocks.LAVA, false))), "", "Hot Stuff"));
		rewards.add(new RewardTrigger(new BaseCustomReward("vacation", 0)
		{
			@Override
			public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				int xChange = ((world.rand.nextInt(50) + 20) + pos.getX()) - 35;
				int zChange = ((world.rand.nextInt(50) + 20) + pos.getZ()) - 35;

				int yChange = -1;

				for(int yy = 0; yy <= world.getHeight(); yy++)
				{
					if(world.isAirBlock(new BlockPos(xChange, yy, zChange)) && world.isAirBlock(new BlockPos(xChange, yy + 1, zChange)))
					{
						yChange = yy;
						break;
					}
				}
				if(yChange == -1)
					return;

				player.setPositionAndUpdate(xChange, yChange, zChange);
			}
		}, "All", "Expenses", "Paid", "Vacation"));
		rewards.add(new RewardTrigger(new BasicReward("free_groceries", 0, new ItemRewardType(new ItemPart(new ItemStack(Items.BREAD, 64)), new ItemPart(new ItemStack(Items.COOKIE, 64)), new ItemPart(new ItemStack(Items.MILK_BUCKET, 10))), new MessageRewardType("There, that should last you a year!")), "", "Free Groceries", "For a YEAR!"));
	}

	private RewardTrigger[] rewardsChosen = new RewardTrigger[4];

	public WheelSpinReward()
	{
		super(CCubesCore.MODID + ":wheel_spin", 10);
	}

	@Override
	public void trigger(ServerWorld world, BlockPos pos, final PlayerEntity player, Map<String, Object> settings)
	{
		ArmorStandEntity armorStand = EntityType.ARMOR_STAND.create(world);
		armorStand.setPositionAndRotation(pos.getX() + 0.8, pos.getY() + 1.15, pos.getZ() + 1.5, 0, 0);
		armorStand.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.ORANGE_BANNER));
		armorStand.setRightArmRotation(new Rotations(90, 0, 0));
		armorStand.setInvisible(true);
		armorStand.setNoGravity(true);
		world.addEntity(armorStand);

		rewardsChosen[0] = rewards.get(RewardsUtil.rand.nextInt(rewards.size()));
		rewardsChosen[1] = rewards.get(RewardsUtil.rand.nextInt(rewards.size()));
		rewardsChosen[2] = rewards.get(RewardsUtil.rand.nextInt(rewards.size()));
		rewardsChosen[3] = rewards.get(RewardsUtil.rand.nextInt(rewards.size()));

		RewardBlockCache cache = new RewardBlockCache(world, pos, player.getPosition());
		BlockState bc = Blocks.BLACK_CONCRETE.getDefaultState();
		BlockState wc = Blocks.WHITE_CONCRETE.getDefaultState();
		BlockState lbc = Blocks.LIGHT_BLUE_CONCRETE.getDefaultState();

		BlockState ss = Blocks.OAK_WALL_SIGN.getDefaultState().with(WallSignBlock.FACING, Direction.SOUTH);
		BlockPos offset = new BlockPos(0, 0, 1);
		cache.cacheBlock(offset, ss);
		TileEntity tileEntity = world.getTileEntity(pos.add(offset));
		if(tileEntity instanceof SignTileEntity)
		{
			SignTileEntity sign = (SignTileEntity) tileEntity;
			String[] textToSet = rewardsChosen[0].signText;
			for(int i = 0; i < textToSet.length; i++)
				sign.setText(i, new StringTextComponent(textToSet[i]));
		}

		offset = new BlockPos(-2, 2, 1);
		cache.cacheBlock(offset, ss);
		tileEntity = world.getTileEntity(pos.add(offset));
		if(tileEntity instanceof SignTileEntity)
		{
			SignTileEntity sign = (SignTileEntity) tileEntity;
			String[] textToSet = rewardsChosen[1].signText;
			for(int i = 0; i < textToSet.length; i++)
				sign.setText(i, new StringTextComponent(textToSet[i]));
		}

		offset = new BlockPos(2, 2, 1);
		cache.cacheBlock(offset, ss);
		tileEntity = world.getTileEntity(pos.add(offset));
		if(tileEntity instanceof SignTileEntity)
		{
			SignTileEntity sign = (SignTileEntity) tileEntity;
			String[] textToSet = rewardsChosen[3].signText;
			for(int i = 0; i < textToSet.length; i++)
				sign.setText(i, new StringTextComponent(textToSet[i]));
		}

		offset = new BlockPos(0, 4, 1);
		cache.cacheBlock(offset, ss);
		tileEntity = world.getTileEntity(pos.add(offset));
		if(tileEntity instanceof SignTileEntity)
		{
			SignTileEntity sign = (SignTileEntity) tileEntity;
			String[] textToSet = rewardsChosen[2].signText;
			for(int i = 0; i < textToSet.length; i++)
				sign.setText(i, new StringTextComponent(textToSet[i]));
		}

		offset = new BlockPos(4, 1, 1);
		cache.cacheBlock(offset, ss);
		tileEntity = world.getTileEntity(pos.add(offset));
		if(tileEntity instanceof SignTileEntity)
		{
			SignTileEntity sign = (SignTileEntity) tileEntity;
			sign.setText(0, new StringTextComponent("Pull"));
			sign.setText(1, new StringTextComponent("To Spin"));
			sign.setText(2, new StringTextComponent("The Wheel!"));
		}
		BlockPos leverOffset = new BlockPos(4, 0, 1);
		cache.cacheBlock(leverOffset, Blocks.LEVER.getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, Direction.SOUTH));
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
			BlockPos leverPos = pos.add(leverOffset);

			@Override
			public void callback()
			{
				cache.restoreBlocks(null);
				armorStand.remove();
			}

			@Override
			public void update()
			{
				BlockState state = world.getBlockState(leverPos);
				if(!state.getBlock().equals(Blocks.LEVER))
				{
					world.setBlockState(leverPos, Blocks.LEVER.getDefaultState());
				}
				else
				{
					if(state.get(LeverBlock.POWERED))
					{
						spinWheel(world, pos, armorStand, cache, player);
						Scheduler.removeTask(this);
					}
				}
			}
		});
	}

	public void spinWheel(ServerWorld world, BlockPos pos, ArmorStandEntity armorStand, RewardBlockCache cache, PlayerEntity player)
	{
		int rewardPicked = RewardsUtil.rand.nextInt(4);
		int delayBase = 194 + (rewardPicked * 6) - 1;
		Scheduler.scheduleTask(new Task("wheel_spin_task", delayBase, 1)
		{
			float rotIndex = 0;

			@Override
			public void callback()
			{
				endDelay(world, pos, armorStand, cache, player, rewardPicked);
			}

			@Override
			public void update()
			{
				rotIndex += 15;
				armorStand.setRightArmRotation(new Rotations(90, 0, rotIndex));

				if(rotIndex % 30 == 0)
				{
					world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 1f, 1f);
				}
			}
		});
	}

	public void endDelay(ServerWorld world, BlockPos pos, ArmorStandEntity armorStand, RewardBlockCache cache, PlayerEntity player, int rewardPicked)
	{
		Scheduler.scheduleTask(new Task("wheel_spin_end_task", 60)
		{
			@Override
			public void callback()
			{
				armorStand.remove();
				cache.restoreBlocks(player);
				rewardsChosen[rewardPicked].reward.trigger(world, pos, player, new HashMap<>());
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

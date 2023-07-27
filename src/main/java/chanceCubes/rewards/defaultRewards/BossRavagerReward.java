package chanceCubes.rewards.defaultRewards;

import chanceCubes.blocks.BlockFallingCustom;
import chanceCubes.config.CCubesSettings;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BossRavagerReward extends BossBaseReward
{
	private ArmorStand armorStandEntity;
	public BossRavagerReward()
	{
		super("ravager");
	}

	@Override
	public LivingEntity initBoss(ServerLevel level, BlockPos pos, Player player, JsonObject settings, BattleWrapper battleWrapper)
	{
		Ravager ravager = EntityType.RAVAGER.create(level);

		armorStandEntity = EntityType.ARMOR_STAND.create(level);
		armorStandEntity.setInvulnerable(true);
		armorStandEntity.startRiding(ravager, true);
		level.addFreshEntity(armorStandEntity);


		ItemStack headStack = new ItemStack(Items.PLAYER_HEAD);
		CompoundTag nbt = headStack.getTag();
		if(nbt == null)
		{
			nbt = new CompoundTag();
			headStack.setTag(nbt);
		}
		nbt.putString("SkullOwner", player.getName().getString());

		armorStandEntity.setItemSlot(EquipmentSlot.HEAD, headStack);
		armorStandEntity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
		armorStandEntity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
		armorStandEntity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_HOE));
		armorStandEntity.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));

		Scheduler.scheduleTask(new Task("witch_abilities", -1, 20)
		{
			@Override
			public void callback()
			{
			}

			@Override
			public void update()
			{
				if(!ravager.isAlive())
				{
					Scheduler.removeTask(this);
					return;
				}

				if(RewardsUtil.rand.nextInt(20) == 4)
					groundPound(ravager.getOnPos(), level);
				if(RewardsUtil.rand.nextInt(10) == 4)
					charge(ravager, player);
			}
		});

		return ravager;
	}

	private void groundPound(BlockPos ravagerPos, ServerLevel level)
	{
		Scheduler.scheduleTask(new Task("ground_pound_ability", -1, 5)
		{
			int radius = 3;

			@Override
			public void callback()
			{
			}

			@Override
			public void update()
			{
				BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(0, 0, 0);
				for(int x = -radius; x <= radius; x++)
				{
					for(int z = -radius; z <= radius; z++)
					{
						pos.set(x, 0, z);
						if(withinDistance(pos, radius))
						{
							BlockPos newPos = ravagerPos.offset(pos);
							BlockState state = level.getBlockState(newPos);

							if(CCubesSettings.nonReplaceableBlocks.contains(state) || state.getBlock().equals(Blocks.AIR))
								state = Blocks.DIRT.defaultBlockState();
							else
								level.setBlockAndUpdate(newPos, Blocks.AIR.defaultBlockState());

							BlockFallingCustom block = new BlockFallingCustom(level, newPos.getX() + 0.5, newPos.getY(), newPos.getZ() + 0.5, state, newPos.getY(), new OffsetBlock(newPos.getX(), newPos.getY(), newPos.getZ(), state, false));
							block.fallDistance = 0;
							block.setDeltaMovement(0, 0.33f, 0);

							level.addFreshEntity(block);
						}
					}
				}

				radius++;
				if(radius == 9)
					Scheduler.removeTask(this);
			}
		});
	}

	private void charge(Ravager ravager, Player player)
	{
		BlockPos dist = player.getOnPos().subtract(ravager.getOnPos());
		double unit = Math.sqrt(dist.distSqr(BlockPos.ZERO));
		BlockPos move = BlockPos.containing((dist.getX() / unit) * 3, 0, (dist.getZ() / unit) * 3);
		ravager.setDeltaMovement(move.getX(), 0, move.getZ());
	}

	@Override
	public void onBossFightEnd(ServerLevel level, BlockPos pos, Player player)
	{
		armorStandEntity.remove(Entity.RemovalReason.DISCARDED);
	}

	public boolean withinDistance(BlockPos pos, double rad)
	{
		double dist = pos.distToLowCornerSqr(0, 0, 0);
		return dist < rad * rad && dist >= (rad - 1) * (rad - 1);
	}

}
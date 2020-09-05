package chanceCubes.rewards.defaultRewards;

import chanceCubes.blocks.BlockFallingCustom;
import chanceCubes.config.CCubesSettings;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;

public class BossRavagerReward extends BossBaseReward
{
	public BossRavagerReward()
	{
		super("ravager");
	}

	@Override
	public void spawnBoss(ServerWorld world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		RavagerEntity ravager = EntityType.RAVAGER.create(world);
		ravager.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
		ravager.getAttribute(Attributes.MAX_HEALTH).setBaseValue(getBossHealthDynamic(player, settings) * 2);
		ravager.setHealth(ravager.getMaxHealth());

		ArmorStandEntity armorStandEntity = EntityType.ARMOR_STAND.create(world);
		armorStandEntity.setInvulnerable(true);
		armorStandEntity.startRiding(ravager, true);


		ItemStack headStack = new ItemStack(Items.PLAYER_HEAD);
		CompoundNBT nbt = headStack.getTag();
		if(nbt == null)
		{
			nbt = new CompoundNBT();
			headStack.setTag(nbt);
		}
		nbt.putString("SkullOwner", player.getName().getString());

		armorStandEntity.setItemStackToSlot(EquipmentSlotType.HEAD, headStack);
		armorStandEntity.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
		armorStandEntity.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
		armorStandEntity.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.DIAMOND_HOE));
		armorStandEntity.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.SHIELD));

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
					groundPound(ravager.getPosition(), world);
				if(RewardsUtil.rand.nextInt(10) == 4)
					charge(ravager, player);
//				if(RewardsUtil.rand.nextInt(5) == 4)
//					throwPotion(witch, player.getPosition(), world);
			}
		});


		world.addEntity(ravager);
		//world.addEntity(armorStandEntity);
		super.trackEntities(ravager);
		super.trackedPlayers(player);
	}

	private void groundPound(BlockPos ravagerPos, ServerWorld world)
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
				BlockPos.Mutable pos = new BlockPos.Mutable(0, -1, 0);
				for(int x = -radius; x <= radius; x++)
				{
					for(int z = -radius; z <= radius; z++)
					{
						pos.setPos(x, -1, z);
						if(withinDistance(pos, radius))
						{
							BlockPos newPos = ravagerPos.add(pos);
							BlockState state = world.getBlockState(newPos);

							if(CCubesSettings.nonReplaceableBlocks.contains(state) || state.getBlock().equals(Blocks.AIR))
								state = Blocks.DIRT.getDefaultState();
							else
								world.setBlockState(newPos, Blocks.AIR.getDefaultState());

							BlockFallingCustom block = new BlockFallingCustom(world, newPos.getX() + 0.5, newPos.getY(), newPos.getZ() + 0.5, state, newPos.getY(), new OffsetBlock(newPos.getX(), newPos.getY(), newPos.getZ(), state, false));
							block.fallTime = 1;
							block.setMotion(0, 0.33f, 0);

							world.addEntity(block);
						}
					}
				}

				radius++;
				if(radius == 9)
					Scheduler.removeTask(this);
			}
		});
	}

	private void charge(RavagerEntity ravager, PlayerEntity player)
	{
		BlockPos dist = player.getPosition().subtract(ravager.getPosition());
		double unit = Math.sqrt(dist.distanceSq(BlockPos.ZERO));
		BlockPos move = new BlockPos((dist.getX() / unit) * 3, 0, (dist.getZ() / unit) * 3);
		ravager.setMotion(move.getX(), 0, move.getZ());
	}

	@Override
	public void onBossFightEnd(ServerWorld world, BlockPos pos, PlayerEntity player)
	{

	}

	public boolean withinDistance(BlockPos pos, double rad)
	{
		double dist = pos.distanceSq(0, 0, 0, false);
		return dist < rad * rad && dist >= (rad - 1) * (rad - 1);
	}

}
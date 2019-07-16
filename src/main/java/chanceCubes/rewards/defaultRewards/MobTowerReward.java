package chanceCubes.rewards.defaultRewards;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.monster.SilverfishEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class MobTowerReward extends BaseCustomReward
{
	//@formatter:off
	private List<Class<? extends Entity>> entities = Arrays.asList(CreeperEntity.class, SkeletonEntity.class, BlazeEntity.class,
			EndermanEntity.class,EndermiteEntity.class, ZombiePigmanEntity.class, SilverfishEntity.class, SlimeEntity.class,
			SnowGolemEntity.class, SpiderEntity.class, WitchEntity.class, ZombieEntity.class, BatEntity.class, ChickenEntity.class,
			CowEntity.class, OcelotEntity.class, ParrotEntity.class, PigEntity.class, RabbitEntity.class, SheepEntity.class,
			VillagerEntity.class, WolfEntity.class);
	//@formatter:on

	public MobTowerReward()
	{
		super(CCubesCore.MODID + ":Mob_Tower", 0);
	}

	@Override
	public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		player.sendMessage(new StringTextComponent("How did they end up like that? O.o"));
		int height = RewardsUtil.rand.nextInt(6) + 7;
		Entity last;
		try
		{
			last = entities.get(RewardsUtil.rand.nextInt(entities.size())).getConstructor(World.class).newInstance(world);
			last.setPosition(pos.getX(), pos.getY(), pos.getZ());
			world.addEntity(last);
			System.out.println(last);
		} catch(Exception e)
		{
			player.sendMessage(new StringTextComponent("Uh oh! Something went wrong and the reward could not be spawned! Please repot this to the mod dev!"));
			return;
		}

		for(int i = 0; i < height; i++)
		{
			try
			{
				Entity ent = entities.get(RewardsUtil.rand.nextInt(entities.size())).getConstructor(World.class).newInstance(world);
				ent.setPosition(pos.getX(), pos.getY(), pos.getZ());
				world.addEntity(ent);
				ent.startRiding(last, true);
				last = ent;
				System.out.println(ent);
			} catch(Exception e)
			{
				continue;
			}

		}
	}
}

package chanceCubes.rewards.defaultRewards;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class MobTowerReward extends BaseCustomReward
{
	//@formatter:off
	private List<EntityType<?>> entities = Arrays.asList(EntityType.CREEPER, EntityType.SKELETON, EntityType.BLAZE,
			EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.ZOMBIE_PIGMAN, EntityType.SILVERFISH, EntityType.SLIME,
			EntityType.SNOW_GOLEM, EntityType.SPIDER, EntityType.WITCH, EntityType.ZOMBIE, EntityType.BAT, EntityType.CHICKEN,
			EntityType.COW, EntityType.OCELOT, EntityType.PARROT, EntityType.PIG, EntityType.RABBIT, EntityType.SHEEP,
			EntityType.VILLAGER, EntityType.WOLF);
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
			last = entities.get(RewardsUtil.rand.nextInt(entities.size())).spawn(world, new CompoundNBT(), null, player, pos, SpawnReason.TRIGGERED, false, false);
			last.setPosition(pos.getX(), pos.getY(), pos.getZ());
			world.addEntity(last);
			System.out.println(last);
		} catch(Exception e)
		{
			player.sendMessage(new StringTextComponent("Uh oh! Something went wrong and the reward could not be spawned! Please repot this to the Chance Cubes mod dev!"));
			e.printStackTrace();
			return;
		}

		for(int i = 0; i < height; i++)
		{
			try
			{
				Entity ent = entities.get(RewardsUtil.rand.nextInt(entities.size())).spawn(world, new CompoundNBT(), null, player, pos, SpawnReason.JOCKEY, false, false);
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

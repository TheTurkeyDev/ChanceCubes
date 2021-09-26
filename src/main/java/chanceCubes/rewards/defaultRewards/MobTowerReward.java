package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;
import java.util.List;

public class MobTowerReward extends BaseCustomReward
{
	//@formatter:off
	private final List<EntityType<? extends Entity>> entities = Arrays.asList(EntityType.CREEPER, EntityType.SKELETON, EntityType.BLAZE,
			EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.ZOMBIFIED_PIGLIN, EntityType.SILVERFISH, EntityType.SLIME,
			EntityType.SNOW_GOLEM, EntityType.SPIDER, EntityType.WITCH, EntityType.ZOMBIE, EntityType.BAT, EntityType.CHICKEN,
			EntityType.COW, EntityType.OCELOT, EntityType.PARROT, EntityType.PIG, EntityType.RABBIT, EntityType.SHEEP,
			EntityType.VILLAGER, EntityType.WOLF, EntityType.PANDA, EntityType.PIGLIN, EntityType.BEE);
	//@formatter:on

	public MobTowerReward()
	{
		super(CCubesCore.MODID + ":mob_tower", 0);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		RewardsUtil.sendMessageToPlayer(player, "How did they end up like that? O.o");
		int minHeight = super.getSettingAsInt(settings, "minHeight", 7, 0, 20);
		int maxHeight = minHeight - super.getSettingAsInt(settings, "maxHeight", 13, 1, 50);
		if(maxHeight < 1)
			maxHeight = 1;

		//Because someone will do it...
		if(minHeight > maxHeight)
		{
			int swap = minHeight;
			minHeight = maxHeight;
			maxHeight = swap;
		}

		int height = RewardsUtil.rand.nextInt(maxHeight) + minHeight;
		Entity last;
		try
		{
			last = entities.get(RewardsUtil.rand.nextInt(entities.size())).create(level);
			last.moveTo(pos.getX(), pos.getY(), pos.getZ());
			level.addFreshEntity(last);
		} catch(Exception e)
		{
			RewardsUtil.sendMessageToPlayer(player, "Uh oh! Something went wrong and the reward could not be spawned! Please report this to the mod dev!");
			return;
		}

		for(int i = 0; i < height; i++)
		{
			try
			{
				Entity ent = entities.get(RewardsUtil.rand.nextInt(entities.size())).create(level);
				ent.moveTo(pos.getX(), pos.getY(), pos.getZ());
				level.addFreshEntity(ent);
				ent.startRiding(last, true);
				last = ent;
			} catch(Exception ignored)
			{
			}
		}
	}
}

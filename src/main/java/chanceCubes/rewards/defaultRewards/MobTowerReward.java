package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MobTowerReward extends BaseCustomReward
{
	//@formatter:off
	private List<EntityType<? extends Entity>> entities = Arrays.asList(EntityType.CREEPER, EntityType.SKELETON, EntityType.BLAZE,
			EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.ZOMBIE_PIGMAN, EntityType.SILVERFISH, EntityType.SLIME,
			EntityType.SNOW_GOLEM, EntityType.SPIDER, EntityType.WITCH, EntityType.ZOMBIE, EntityType.BAT, EntityType.CHICKEN,
			EntityType.COW, EntityType.OCELOT, EntityType.PARROT, EntityType.PIG, EntityType.RABBIT, EntityType.SHEEP,
			EntityType.VILLAGER, EntityType.WOLF);
	//@formatter:on

	public MobTowerReward()
	{
		super(CCubesCore.MODID + ":mob_tower", 0);
	}

	@Override
	public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		player.sendMessage(new StringTextComponent("How did they end up like that? O.o"));
		int minHeight = super.getSettingAsInt(settings, "min_height", 7, 0, 20);
		int maxHeight = minHeight - super.getSettingAsInt(settings, "max_height", 13, 1, 50);
		if(maxHeight < 1)
			maxHeight = 1;
		int height = RewardsUtil.rand.nextInt(maxHeight) + minHeight;
		Entity last;
		try
		{
			last = entities.get(RewardsUtil.rand.nextInt(entities.size())).create(world);
			last.setPosition(pos.getX(), pos.getY(), pos.getZ());
			world.addEntity(last);
		} catch(Exception e)
		{
			player.sendMessage(new StringTextComponent("Uh oh! Something went wrong and the reward could not be spawned! Please repot this to the mod dev!"));
			return;
		}

		for(int i = 0; i < height; i++)
		{
			try
			{
				Entity ent = entities.get(RewardsUtil.rand.nextInt(entities.size())).create(world);
				ent.setPosition(pos.getX(), pos.getY(), pos.getZ());
				world.addEntity(ent);
				ent.startRiding(last, true);
				last = ent;
				System.out.println(ent);
			} catch(Exception ignored)
			{
			}
		}
	}
}

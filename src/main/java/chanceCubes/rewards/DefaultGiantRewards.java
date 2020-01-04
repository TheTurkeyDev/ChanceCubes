package chanceCubes.rewards;

import chanceCubes.CCubesCore;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.rewards.defaultRewards.BasicReward;
import chanceCubes.rewards.giantRewards.*;
import chanceCubes.rewards.rewardparts.SchematicPart;
import chanceCubes.rewards.rewardtype.SchematicRewardType;

public class DefaultGiantRewards
{

	public static void loadDefaultRewards()
	{
		GlobalCCRewardRegistry.GIANT.registerReward(new BasicReward(CCubesCore.MODID + ":village", 0, new SchematicRewardType(new SchematicPart("/assets/chancecubes/schematics/village.ccs", true))));
		GlobalCCRewardRegistry.GIANT.registerReward(new BasicReward(CCubesCore.MODID + ":woodland_mansion", 0, new SchematicRewardType(new SchematicPart("/assets/chancecubes/schematics/mansion.ccs", true).setSpacingdelay(0.05f).shouldPlaceAitBlocks(true))));

		GlobalCCRewardRegistry.GIANT.registerReward(new BioDomeReward());
		GlobalCCRewardRegistry.GIANT.registerReward(new TNTSlingReward());
		GlobalCCRewardRegistry.GIANT.registerReward(new ThrowablesReward());
		GlobalCCRewardRegistry.GIANT.registerReward(new OrePillarReward());
		GlobalCCRewardRegistry.GIANT.registerReward(new ChunkReverserReward());
		GlobalCCRewardRegistry.GIANT.registerReward(new FloorIsLavaReward());
		GlobalCCRewardRegistry.GIANT.registerReward(new ChunkFlipReward());
		GlobalCCRewardRegistry.GIANT.registerReward(new OreSphereReward());
		GlobalCCRewardRegistry.GIANT.registerReward(new PotionsReward());
		GlobalCCRewardRegistry.GIANT.registerReward(new FluidSphereReward());
		GlobalCCRewardRegistry.GIANT.registerReward(new MixedFluidSphereReward());
		GlobalCCRewardRegistry.GIANT.registerReward(new FireworkShowReward());
		GlobalCCRewardRegistry.GIANT.registerReward(new SphereSnakeReward());
		GlobalCCRewardRegistry.GIANT.registerReward(new RandomExplosionReward());
		GlobalCCRewardRegistry.GIANT.registerReward(new BeaconArenaReward());
		GlobalCCRewardRegistry.GIANT.registerReward(new BlockInfectionReward());
		GlobalCCRewardRegistry.GIANT.registerReward(new BlockThrowerReward());
	}
}

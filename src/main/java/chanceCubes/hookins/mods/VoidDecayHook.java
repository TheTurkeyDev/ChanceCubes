package chanceCubes.hookins.mods;

import cpw.mods.fml.common.registry.GameRegistry;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.defaultRewards.BasicReward;
import chanceCubes.rewards.rewardparts.EntityPart;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.type.BlockRewardType;
import chanceCubes.rewards.type.EntityRewardType;
import chanceCubes.rewards.type.ItemRewardType;

public class VoidDecayHook extends BaseModHook
{
	public VoidDecayHook()
	{
		super("voiddecay");
		this.loadRewards();
	}

	@Override
	public void loadRewards()
	{
		ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Cleanse_TNT", 80, new ItemRewardType(new ItemPart(GameRegistry.findItemStack(super.modId, "cleanse_tnt", 5)))));
		ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Void_TNT", -90, new EntityRewardType(new EntityPart("{id:void_tnt, fuse:80}"))));
		ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Void_Block", -90, new BlockRewardType(new OffsetBlock(0, 0, 0, GameRegistry.findBlock(super.modId, "void_decay"), false))));
	}

}

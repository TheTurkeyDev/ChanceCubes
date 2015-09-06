package chanceCubes.hookins.mods;

import cpw.mods.fml.common.registry.GameRegistry;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.BasicReward;
import chanceCubes.rewards.type.BlockRewardType;
import chanceCubes.rewards.type.EntityRewardType;
import chanceCubes.rewards.type.ItemRewardType;
import chanceCubes.util.OffsetBlock;

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
		ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Cleanse_TNT", 80, new ItemRewardType(GameRegistry.findItemStack(super.modId, "cleanse_tnt", 5))));
		ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Void_TNT", -90, new EntityRewardType("{id:void_tnt, fuse:80}")));
		ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Void_Block", -90, new BlockRewardType(new OffsetBlock(0, 0, 0, GameRegistry.findBlock(super.modId, "void_decay"), false))));
	}

}

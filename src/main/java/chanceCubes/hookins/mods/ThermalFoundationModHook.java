package chanceCubes.hookins.mods;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.defaultRewards.BasicReward;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.MessagePart;
import chanceCubes.rewards.type.ItemRewardType;
import chanceCubes.rewards.type.MessageRewardType;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

public class ThermalFoundationModHook extends BaseModHook
{
	private String[] rodTypes = { "Platinum", "Electrum", "Invar", "Tin", "Lead", "Nickel", "Silver", "Bronze", "Copper" };

	public ThermalFoundationModHook()
	{
		super("ThermalFoundation");
		loadRewards();
	}

	@Override
	public void loadRewards()
	{
		ItemStack stack;

		if(GameRegistry.findItem(super.modId, "material") != null)
		{
			stack = GameRegistry.findItemStack(super.modId, "material", 4);
			stack.setItemDamage(12);
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Grind_Gears", 10, new MessageRewardType(new MessagePart("You really grind my gears ;)")), new ItemRewardType(new ItemPart(stack))));
		}

		List<ItemPart> avialable = new ArrayList<ItemPart>();
		for(String s : this.rodTypes)
			if(GameRegistry.findItem(super.modId, "tool.fishingRod" + s) != null)
				avialable.add(new ItemPart(GameRegistry.findItemStack(super.modId, "tool.fishingRod" + s, 1)));
		ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":fishing", 5, new MessageRewardType(new MessagePart("Hey! Let's open a fishing store!")), new ItemRewardType(avialable.toArray(new ItemPart[avialable.size()]))));
	}

}

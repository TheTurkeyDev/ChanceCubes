package chanceCubes.items;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class CCubesItems {
	
	public static Item chancePendantT1;
	public static Item chancePendantT2;
	public static Item chancePendantT3;
	public static Item chancePendantT4;
	
	public static Item silkPendant;
	public static Item creativePendant;
	public static Item rewardSelectorPendant;
	
	public static Item scanner;
	
	public static void loadItems()
	{	
		GameRegistry.registerItem(chancePendantT1 = new ItemChancePendant(1, 10), "chancePendantTier1");
		GameRegistry.registerItem(chancePendantT2 = new ItemChancePendant(2, 25), "chancePendantTier2");
		GameRegistry.registerItem(chancePendantT3 = new ItemChancePendant(3, 50), "chancePendantTier3");
		GameRegistry.registerItem(chancePendantT4 = new ItemChancePendant(4, 100), "chancePendantTier4");
		
		GameRegistry.registerItem(silkPendant = new ItemSilkTouchPendant(), "silkTouchPendant");
		GameRegistry.registerItem(creativePendant = new ItemCreativePendant(), "creativePendant");
		GameRegistry.registerItem(rewardSelectorPendant = new ItemRewardSelectorPendant(), "reward_Selector_Pendant");
		
		GameRegistry.registerItem(scanner = new ItemScanner(), "scanner");
	}

}

package chanceCubes.items;

import cpw.mods.fml.common.registry.GameRegistry;

public class CCubesItems {
	
	public static BaseChanceCubesItem chancePendantT1;
	public static BaseChanceCubesItem chancePendantT2;
	public static BaseChanceCubesItem chancePendantT3;
	public static BaseChanceCubesItem chancePendantT4;
	
	public static BaseChanceCubesItem silkPendant;
	public static BaseChanceCubesItem creativePendant;
	public static BaseChanceCubesItem rewardSelectorPendant;
	
	public static BaseChanceCubesItem scanner;
	
	public static void loadItems()
	{	
		GameRegistry.registerItem(chancePendantT1 = new ItemChancePendant(1, 10), chancePendantT1.getItemName());
		GameRegistry.registerItem(chancePendantT2 = new ItemChancePendant(2, 25), chancePendantT2.getItemName());
		GameRegistry.registerItem(chancePendantT3 = new ItemChancePendant(3, 50), chancePendantT3.getItemName());
		GameRegistry.registerItem(chancePendantT4 = new ItemChancePendant(4, 100), chancePendantT4.getItemName());
		
		GameRegistry.registerItem(silkPendant = new ItemSilkTouchPendant(), silkPendant.getItemName());
		GameRegistry.registerItem(creativePendant = new ItemCreativePendant(), creativePendant.getItemName());
		GameRegistry.registerItem(rewardSelectorPendant = new ItemRewardSelectorPendant(), rewardSelectorPendant.getItemName());
		
		GameRegistry.registerItem(scanner = new ItemScanner(), scanner.getItemName());
	}
}
package chanceCubes.items;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class CCubesItems {
	
	public static Item chancePendantT1;
	public static Item chancePendantT2;
	public static Item chancePendantT3;
	public static Item chancePendantT4;
	
	public static Item scanner;
	
	public static void loadItems()
	{
		chancePendantT1 = new ItemChancePendant(1, 10);
		chancePendantT2 = new ItemChancePendant(2, 25);
		chancePendantT3 = new ItemChancePendant(3, 50);
		chancePendantT4 = new ItemChancePendant(4, 100);
		
		scanner = new ItemScanner();
		
		GameRegistry.registerItem(chancePendantT1, "chancePendantTier1");
		GameRegistry.registerItem(chancePendantT2, "chancePendantTier2");
		GameRegistry.registerItem(chancePendantT3, "chancePendantTier3");
		GameRegistry.registerItem(chancePendantT4, "chancePendantTier4");
		
		GameRegistry.registerItem(scanner, "scanner");
	}

}

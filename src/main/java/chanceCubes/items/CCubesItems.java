package chanceCubes.items;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class CCubesItems {
	
	public static Item chancePendantT1;
	
	public static void loadItems()
	{
		chancePendantT1 = new ItemChancePendantT1();
		
		GameRegistry.registerItem(chancePendantT1, "chancePendantTier1");
	}

}

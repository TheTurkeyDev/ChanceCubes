package chanceCubes.items;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class CCubesItems {
	
	public static Item chancePendant;
	
	public static void loadItems()
	{
		chancePendant = new ItemChancePendant();
		
		GameRegistry.registerItem(chancePendant, "chancePendant");
	}

}

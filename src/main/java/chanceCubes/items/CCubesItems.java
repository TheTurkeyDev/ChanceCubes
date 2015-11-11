package chanceCubes.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import chanceCubes.CCubesCore;

public class CCubesItems
{

	public static ItemChancePendant chancePendantT1;
	public static ItemChancePendant chancePendantT2;
	public static ItemChancePendant chancePendantT3;
	public static ItemChancePendant chancePendantT4;

	public static ItemSilkTouchPendant silkPendant;
	public static ItemCreativePendant creativePendant;
	public static ItemRewardSelectorPendant rewardSelectorPendant;

	public static ItemScanner scanner;

	public static void loadItems()
	{
		chancePendantT1 = new ItemChancePendant(1, 10);
		chancePendantT2 = new ItemChancePendant(2, 25);
		chancePendantT3 = new ItemChancePendant(3, 50);
		chancePendantT4 = new ItemChancePendant(4, 100);
		GameRegistry.registerItem(chancePendantT1, chancePendantT1.itemNameID);
		GameRegistry.registerItem(chancePendantT2, chancePendantT2.itemNameID);
		GameRegistry.registerItem(chancePendantT3, chancePendantT3.itemNameID);
		GameRegistry.registerItem(chancePendantT4, chancePendantT4.itemNameID);

		GameRegistry.registerItem(silkPendant = new ItemSilkTouchPendant(), silkPendant.itemNameID);
		GameRegistry.registerItem(creativePendant = new ItemCreativePendant(), creativePendant.itemNameID);
		GameRegistry.registerItem(rewardSelectorPendant = new ItemRewardSelectorPendant(), rewardSelectorPendant.itemNameID);

		GameRegistry.registerItem(scanner = new ItemScanner(), "scanner");
	}

	public static void loadItemRenderes()
	{
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

		renderItem.getItemModelMesher().register(CCubesItems.silkPendant, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.silkPendant.itemNameID, "inventory"));
		renderItem.getItemModelMesher().register(CCubesItems.chancePendantT1, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.chancePendantT1.itemNameID, "inventory"));
		renderItem.getItemModelMesher().register(CCubesItems.chancePendantT2, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.chancePendantT2.itemNameID, "inventory"));
		renderItem.getItemModelMesher().register(CCubesItems.chancePendantT3, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.chancePendantT3.itemNameID, "inventory"));
		renderItem.getItemModelMesher().register(CCubesItems.chancePendantT4, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.chancePendantT4.itemNameID, "inventory"));
		renderItem.getItemModelMesher().register(CCubesItems.creativePendant, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.creativePendant.itemNameID, "inventory"));
		renderItem.getItemModelMesher().register(CCubesItems.rewardSelectorPendant, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.rewardSelectorPendant.itemNameID, "inventory"));
		renderItem.getItemModelMesher().register(CCubesItems.scanner, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.scanner.itemNameID, "inventory"));
	}

}

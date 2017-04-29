package chanceCubes.items;

import chanceCubes.CCubesCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CCubesItems
{

	public static BaseChanceCubesItem chancePendantT1;
	public static BaseChanceCubesItem chancePendantT2;
	public static BaseChanceCubesItem chancePendantT3;

	public static BaseChanceCubesItem silkPendant;
	public static BaseChanceCubesItem creativePendant;
	public static BaseChanceCubesItem rewardSelectorPendant;

	public static BaseChanceCubesItem scanner;

	public static void loadItems()
	{
		GameRegistry.register(chancePendantT1 = new ItemChancePendant(1, 10));
		GameRegistry.register(chancePendantT2 = new ItemChancePendant(2, 25));
		GameRegistry.register(chancePendantT3 = new ItemChancePendant(3, 50));

		GameRegistry.register(silkPendant = new ItemSilkTouchPendant());
		GameRegistry.register(creativePendant = new ItemCreativePendant());
		GameRegistry.register(rewardSelectorPendant = new ItemRewardSelectorPendant());

		GameRegistry.register(scanner = new ItemScanner());
	}

	public static void registerItems()
	{
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		mesher.register(CCubesItems.silkPendant, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.silkPendant.getItemName(), "inventory"));
		mesher.register(CCubesItems.chancePendantT1, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.chancePendantT1.getItemName(), "inventory"));
		mesher.register(CCubesItems.chancePendantT2, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.chancePendantT2.getItemName(), "inventory"));
		mesher.register(CCubesItems.chancePendantT3, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.chancePendantT3.getItemName(), "inventory"));
		mesher.register(CCubesItems.creativePendant, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.creativePendant.getItemName(), "inventory"));
		mesher.register(CCubesItems.rewardSelectorPendant, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.rewardSelectorPendant.getItemName(), "inventory"));
		mesher.register(CCubesItems.scanner, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.scanner.getItemName(), "inventory"));
	}

}

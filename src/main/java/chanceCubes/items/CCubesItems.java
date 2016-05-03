package chanceCubes.items;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.BlockChanceCube;
import chanceCubes.blocks.CCubesBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CCubesItems
{

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
		GameRegistry.register(chancePendantT1 = new ItemChancePendant(1, 10));
		GameRegistry.register(chancePendantT2 = new ItemChancePendant(2, 25));
		GameRegistry.register(chancePendantT3 = new ItemChancePendant(3, 50));
		GameRegistry.register(chancePendantT4 = new ItemChancePendant(4, 100));

		GameRegistry.register(silkPendant = new ItemSilkTouchPendant());
		GameRegistry.register(creativePendant = new ItemCreativePendant());
		GameRegistry.register(rewardSelectorPendant = new ItemRewardSelectorPendant());

		GameRegistry.register(scanner = new ItemScanner());
	}

	public static void registerItems()
	{
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

		renderItem.getItemModelMesher().register(Item.getItemFromBlock(CCubesBlocks.chanceCube), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + ((BlockChanceCube) CCubesBlocks.chanceCube).getBlockName(), "inventory"));

		renderItem.getItemModelMesher().register(CCubesItems.silkPendant, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.silkPendant.getItemName(), "inventory"));
		renderItem.getItemModelMesher().register(CCubesItems.chancePendantT1, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.chancePendantT1.getItemName(), "inventory"));
		renderItem.getItemModelMesher().register(CCubesItems.chancePendantT2, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.chancePendantT2.getItemName(), "inventory"));
		renderItem.getItemModelMesher().register(CCubesItems.chancePendantT3, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.chancePendantT3.getItemName(), "inventory"));
		renderItem.getItemModelMesher().register(CCubesItems.chancePendantT4, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.chancePendantT4.getItemName(), "inventory"));
		renderItem.getItemModelMesher().register(CCubesItems.creativePendant, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.creativePendant.getItemName(), "inventory"));
		renderItem.getItemModelMesher().register(CCubesItems.rewardSelectorPendant, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.rewardSelectorPendant.getItemName(), "inventory"));
		renderItem.getItemModelMesher().register(CCubesItems.scanner, 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.scanner.getItemName(), "inventory"));
	}

}

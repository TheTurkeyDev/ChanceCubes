package chanceCubes.items;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CCubesItems
{

	public static BaseChanceCubesItem chancePendantT1;
	public static BaseChanceCubesItem chancePendantT2;
	public static BaseChanceCubesItem chancePendantT3;

	public static BaseChanceCubesItem silkPendant;
	public static BaseChanceCubesItem creativePendant;
	public static BaseChanceCubesItem rewardSelectorPendant;
	public static BaseChanceCubesItem singleUseRewardSelectorPendant;

	public static BaseChanceCubesItem scanner;

	@SubscribeEvent
	public void onItemRegistry(RegistryEvent.Register<Item> e)
	{
		e.getRegistry().register(chancePendantT1 = new ItemChancePendant(1, 10));
		e.getRegistry().register(chancePendantT2 = new ItemChancePendant(2, 25));
		e.getRegistry().register(chancePendantT3 = new ItemChancePendant(3, 50));

		e.getRegistry().register(silkPendant = new ItemSilkTouchPendant());
		e.getRegistry().register(creativePendant = new ItemCreativePendant());
		e.getRegistry().register(rewardSelectorPendant = new ItemRewardSelectorPendant());
		e.getRegistry().register(singleUseRewardSelectorPendant = new ItemSingleUseRewardSelectorPendant());

		e.getRegistry().register(new ItemChanceCube(CCubesBlocks.CHANCE_CUBE).setRegistryName(CCubesBlocks.CHANCE_CUBE.getRegistryName()));
		e.getRegistry().register(new ItemChanceCube(CCubesBlocks.CHANCE_ICOSAHEDRON).setRegistryName(CCubesBlocks.CHANCE_ICOSAHEDRON.getRegistryName()));
		e.getRegistry().register(new ItemChanceCube(CCubesBlocks.GIANT_CUBE).setRegistryName(CCubesBlocks.GIANT_CUBE.getRegistryName()));
		e.getRegistry().register(new ItemChanceCube(CCubesBlocks.COMPACT_GIANT_CUBE).setRegistryName(CCubesBlocks.COMPACT_GIANT_CUBE.getRegistryName()));
		e.getRegistry().register(new ItemChanceCube(CCubesBlocks.CUBE_DISPENSER).setRegistryName(CCubesBlocks.CUBE_DISPENSER.getRegistryName()));

		e.getRegistry().register(scanner = new ItemScanner());
	}

	public static void registerItems()
	{
		ItemModelMesher mesher = Minecraft.getInstance().getItemRenderer().getItemModelMesher();

		mesher.register(CCubesItems.silkPendant, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.silkPendant.getItemName(), "inventory"));
		mesher.register(CCubesItems.chancePendantT1, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.chancePendantT1.getItemName(), "inventory"));
		mesher.register(CCubesItems.chancePendantT2, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.chancePendantT2.getItemName(), "inventory"));
		mesher.register(CCubesItems.chancePendantT3, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.chancePendantT3.getItemName(), "inventory"));
		mesher.register(CCubesItems.creativePendant, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.creativePendant.getItemName(), "inventory"));
		mesher.register(CCubesItems.rewardSelectorPendant, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.rewardSelectorPendant.getItemName(), "inventory"));
		mesher.register(CCubesItems.singleUseRewardSelectorPendant, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.singleUseRewardSelectorPendant.getItemName(), "inventory"));
		mesher.register(CCubesItems.scanner, new ModelResourceLocation(CCubesCore.MODID + ":" + CCubesItems.scanner.getItemName(), "inventory"));
	}

}

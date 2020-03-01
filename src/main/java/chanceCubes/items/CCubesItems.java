package chanceCubes.items;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.containers.CreativePendantContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CCubesCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
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

	public static ItemChanceCube CHANCE_CUBE;
	public static ItemChanceCube CHANCE_ICOSAHEDRON;
	public static ItemChanceCube GIANT_CUBE;
	public static ItemChanceCube COMPACT_GIANT_CUBE;
	public static ItemChanceCube CUBE_DISPENSER;

	public static ContainerType<CreativePendantContainer> CREATIVE_PENDANT_CONTAINER;

	@SubscribeEvent
	public static void onItemRegistry(RegistryEvent.Register<Item> e)
	{
		e.getRegistry().register(chancePendantT1 = new ItemChancePendant(1, 10));
		e.getRegistry().register(chancePendantT2 = new ItemChancePendant(2, 25));
		e.getRegistry().register(chancePendantT3 = new ItemChancePendant(3, 50));

		e.getRegistry().register(silkPendant = new ItemSilkTouchPendant());
		e.getRegistry().register(creativePendant = new ItemCreativePendant());
		e.getRegistry().register(rewardSelectorPendant = new ItemRewardSelectorPendant());
		e.getRegistry().register(singleUseRewardSelectorPendant = new ItemSingleUseRewardSelectorPendant());

		e.getRegistry().register(CHANCE_CUBE = new ItemChanceCube(CCubesBlocks.CHANCE_CUBE));
		e.getRegistry().register(CHANCE_ICOSAHEDRON = new ItemChanceCube(CCubesBlocks.CHANCE_ICOSAHEDRON));
		e.getRegistry().register(GIANT_CUBE = new ItemChanceCube(CCubesBlocks.GIANT_CUBE));
		e.getRegistry().register(COMPACT_GIANT_CUBE = new ItemChanceCube(CCubesBlocks.COMPACT_GIANT_CUBE));
		e.getRegistry().register(CUBE_DISPENSER = new ItemChanceCube(CCubesBlocks.CUBE_DISPENSER));

		e.getRegistry().register(scanner = new ItemScanner());
	}

	@SubscribeEvent
	public static void onContainerRegistry(RegistryEvent.Register<ContainerType<?>> event)
	{
		CREATIVE_PENDANT_CONTAINER = IForgeContainerType.create((windowId, inv, data) -> new CreativePendantContainer(windowId, inv));
		CREATIVE_PENDANT_CONTAINER.setRegistryName(CCubesCore.MODID, "creative_pendant_container");
		event.getRegistry().register(CREATIVE_PENDANT_CONTAINER);
	}

}

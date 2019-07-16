package chanceCubes.items;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import net.minecraft.item.Item;
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

		e.getRegistry().register(new ItemChanceCube(CCubesBlocks.CHANCE_CUBE).setRegistryName(CCubesBlocks.CHANCE_CUBE.getRegistryName()));
		e.getRegistry().register(new ItemChanceCube(CCubesBlocks.CHANCE_ICOSAHEDRON).setRegistryName(CCubesBlocks.CHANCE_ICOSAHEDRON.getRegistryName()));
		e.getRegistry().register(new ItemChanceCube(CCubesBlocks.GIANT_CUBE).setRegistryName(CCubesBlocks.GIANT_CUBE.getRegistryName()));
		e.getRegistry().register(new ItemChanceCube(CCubesBlocks.COMPACT_GIANT_CUBE).setRegistryName(CCubesBlocks.COMPACT_GIANT_CUBE.getRegistryName()));
		e.getRegistry().register(new ItemChanceCube(CCubesBlocks.CUBE_DISPENSER).setRegistryName(CCubesBlocks.CUBE_DISPENSER.getRegistryName()));

		e.getRegistry().register(scanner = new ItemScanner());
	}
}

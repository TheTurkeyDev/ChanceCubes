package chanceCubes.items;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class CCubesItems
{

	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CCubesCore.MODID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CCubesCore.MODID);

	public static DeferredItem<ItemChancePendant> CHANCE_PENDANT_T1 = ITEMS.register("chance_pendant_tier1", () -> new ItemChancePendant(10));
	public static DeferredItem<ItemChancePendant> CHANCE_PENDANT_T2 = ITEMS.register("chance_pendant_tier2", () -> new ItemChancePendant(25));
	public static DeferredItem<ItemChancePendant> CHANCE_PENDANT_T3 = ITEMS.register("chance_pendant_tier3", () -> new ItemChancePendant(50));

	public static DeferredItem<ItemSilkTouchPendant> SILK_PENDANT = ITEMS.register("silk_touch_pendant", ItemSilkTouchPendant::new);
	public static DeferredItem<BaseChanceCubesItem> CREATIVE_PENDANT = ITEMS.register("creative_pendant", ItemCreativePendant::new);
	public static DeferredItem<BaseChanceCubesItem> REWARD_SELECTOR_PENDANT = ITEMS.register("reward_selector_pendant", ItemRewardSelectorPendant::new);
	public static DeferredItem<BaseChanceCubesItem> SINGLE_USE_REWARD_SELECTOR_PENDANT = ITEMS.register("single_use_reward_selector_pendant", ItemSingleUseRewardSelectorPendant::new);

	public static DeferredItem<BaseChanceCubesItem> SCANNER = ITEMS.register("scanner", ItemScanner::new);

	public static DeferredItem<ItemChanceCube> CHANCE_CUBE = ITEMS.register("chance_cube", () -> new ItemChanceCube(CCubesBlocks.CHANCE_CUBE.get()));
	//public static RegistryObject<ItemChanceCube> CHANCE_ICOSAHEDRON = ITEMS.register("chance_icosahedron", () -> new ItemChanceCube(CCubesBlocks.CHANCE_ICOSAHEDRON.get()));
	public static DeferredItem<ItemChanceCube> GIANT_CUBE = ITEMS.register("giant_chance_cube", () -> new ItemChanceCube(CCubesBlocks.GIANT_CUBE.get()));
	public static DeferredItem<ItemChanceCube> COMPACT_GIANT_CUBE = ITEMS.register("compact_giant_chance_cube", () -> new ItemChanceCube(CCubesBlocks.COMPACT_GIANT_CUBE.get()));
	public static DeferredItem<ItemChanceCube> CUBE_DISPENSER = ITEMS.register("cube_dispenser", () -> new ItemChanceCube(CCubesBlocks.CUBE_DISPENSER.get()));

	public static final Supplier<CreativeModeTab> CHANCE_TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
			.icon(() -> new ItemStack(CCubesBlocks.CHANCE_CUBE.get()))
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.chancecubes"))
			.displayItems((parameters, output) -> {
				List<ItemStack> stacks = CCubesItems.ITEMS.getEntries().stream()
						.filter(registryObject -> registryObject.get() != CCubesBlocks.GIANT_CUBE.get().asItem())
						.map(reg -> new ItemStack(reg.get())).toList();
				output.acceptAll(stacks);
			}).build());

}

package chanceCubes.items;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.gui.CreativePendantScreen;
import chanceCubes.containers.CreativePendantContainer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = CCubesCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCubesItems
{

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CCubesCore.MODID);

	public static RegistryObject<ItemChancePendant> CHANCE_PENDANT_T1 = ITEMS.register("chance_pendant_tier1", () -> new ItemChancePendant(10));
	public static RegistryObject<ItemChancePendant> CHANCE_PENDANT_T2 = ITEMS.register("chance_pendant_tier2", () -> new ItemChancePendant(25));
	public static RegistryObject<ItemChancePendant> CHANCE_PENDANT_T3 = ITEMS.register("chance_pendant_tier3", () -> new ItemChancePendant(50));

	public static RegistryObject<ItemSilkTouchPendant> SILK_PENDANT = ITEMS.register("silk_touch_pendant", ItemSilkTouchPendant::new);
	public static RegistryObject<BaseChanceCubesItem> CREATIVE_PENDANT = ITEMS.register("creative_pendant", ItemCreativePendant::new);
	public static RegistryObject<BaseChanceCubesItem> REWARD_SELECTOR_PENDANT = ITEMS.register("reward_selector_pendant", ItemRewardSelectorPendant::new);
	public static RegistryObject<BaseChanceCubesItem> SINGLE_USE_REWARD_SELECTOR_PENDANT = ITEMS.register("single_use_reward_selector_pendant", ItemSingleUseRewardSelectorPendant::new);

	public static RegistryObject<BaseChanceCubesItem> SCANNER = ITEMS.register("scanner", ItemScanner::new);

	public static RegistryObject<ItemChanceCube> CHANCE_CUBE = ITEMS.register("chance_cube", () -> new ItemChanceCube(CCubesBlocks.CHANCE_CUBE.get()));
	public static RegistryObject<ItemChanceCube> CHANCE_ICOSAHEDRON = ITEMS.register("chance_icosahedron", () -> new ItemChanceCube(CCubesBlocks.CHANCE_ICOSAHEDRON.get()));
	public static RegistryObject<ItemChanceCube> GIANT_CUBE = ITEMS.register("giant_chance_cube", () -> new ItemChanceCube(CCubesBlocks.GIANT_CUBE.get()));
	public static RegistryObject<ItemChanceCube> COMPACT_GIANT_CUBE = ITEMS.register("compact_giant_chance_cube", () -> new ItemChanceCube(CCubesBlocks.COMPACT_GIANT_CUBE.get()));
	public static RegistryObject<ItemChanceCube> CUBE_DISPENSER = ITEMS.register("cube_dispenser", () -> new ItemChanceCube(CCubesBlocks.CUBE_DISPENSER.get()));


	public static MenuType<CreativePendantContainer> CREATIVE_PENDANT_CONTAINER;

	@SubscribeEvent
	public static void onContainerRegistry(RegisterEvent event)
	{
		event.register(ForgeRegistries.Keys.CONTAINER_TYPES,
				helper ->
				{
					CREATIVE_PENDANT_CONTAINER = IForgeMenuType.create((windowId, inv, data) -> new CreativePendantContainer(windowId, inv));
					helper.register("creative_pendant_container", CREATIVE_PENDANT_CONTAINER);

					DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
					{
						MenuScreens.register(CREATIVE_PENDANT_CONTAINER, CreativePendantScreen::new);
					});
				});
	}

}

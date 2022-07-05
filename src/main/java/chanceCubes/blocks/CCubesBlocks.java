package chanceCubes.blocks;

import chanceCubes.CCubesCore;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.tileentities.TileCubeDispenser;
import chanceCubes.tileentities.TileGiantCube;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = CCubesCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCubesBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CCubesCore.MODID);

	public static final RegistryObject<BaseChanceBlock> CHANCE_CUBE = BLOCKS.register("chance_cube", BlockChanceCube::new);
	public static final RegistryObject<BaseChanceBlock> CHANCE_ICOSAHEDRON = BLOCKS.register("chance_icosahedron", BlockChanceD20::new);
	public static final RegistryObject<BaseChanceBlock> GIANT_CUBE = BLOCKS.register("giant_chance_cube", BlockGiantCube::new);
	public static final RegistryObject<BaseChanceBlock> COMPACT_GIANT_CUBE = BLOCKS.register("compact_giant_chance_cube", BlockCompactGiantCube::new);
	public static final RegistryObject<BaseChanceBlock> CUBE_DISPENSER = BLOCKS.register("cube_dispenser", BlockCubeDispenser::new);

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, CCubesCore.MODID);

	public static RegistryObject<BlockEntityType<TileChanceCube>> TILE_CHANCE_CUBE = BLOCK_ENTITIES.register("tile_chance_cube", () -> BlockEntityType.Builder.of(TileChanceCube::new, CCubesBlocks.CHANCE_CUBE).build(null).setRegistryName(CCubesCore.MODID, "tile_chance_cube"));
	public static BlockEntityType<TileChanceD20> TILE_CHANCE_ICOSAHEDRON;
	public static BlockEntityType<TileGiantCube> TILE_CHANCE_GIANT;
	public static BlockEntityType<TileCubeDispenser> TILE_CUBE_DISPENSER;

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void onTileEntityRegistry(RegistryEvent.Register<BlockEntityType<?>> event)
	{
		event.getRegistry().register(TILE_CHANCE_CUBE =);
		event.getRegistry().register(TILE_CHANCE_ICOSAHEDRON = (BlockEntityType<TileChanceD20>) BlockEntityType.Builder.of(TileChanceD20::new, CCubesBlocks.CHANCE_ICOSAHEDRON).build(null).setRegistryName(CCubesCore.MODID, "tile_chance_icosahedron"));
		event.getRegistry().register(TILE_CHANCE_GIANT = (BlockEntityType<TileGiantCube>) BlockEntityType.Builder.of(TileGiantCube::new, CCubesBlocks.GIANT_CUBE).build(null).setRegistryName(CCubesCore.MODID, "tile_chance_giant"));
		event.getRegistry().register(TILE_CUBE_DISPENSER = (BlockEntityType<TileCubeDispenser>) BlockEntityType.Builder.of(TileCubeDispenser::new, CCubesBlocks.CUBE_DISPENSER).build(null).setRegistryName(CCubesCore.MODID, "tile_cube_dispenser"));
	}
}
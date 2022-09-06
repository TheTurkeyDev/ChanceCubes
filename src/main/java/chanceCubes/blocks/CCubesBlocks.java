package chanceCubes.blocks;

import chanceCubes.CCubesCore;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.tileentities.TileCubeDispenser;
import chanceCubes.tileentities.TileGiantCube;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CCubesCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCubesBlocks
{
	public static BaseChanceBlock CHANCE_CUBE;
	//public static BaseChanceBlock CHANCE_ICOSAHEDRON;
	public static BaseChanceBlock GIANT_CUBE;
	public static BaseChanceBlock COMPACT_GIANT_CUBE;
	public static BaseChanceBlock CUBE_DISPENSER;

	public static BlockEntityType<TileChanceCube> TILE_CHANCE_CUBE;
	public static BlockEntityType<TileChanceD20> TILE_CHANCE_ICOSAHEDRON;
	public static BlockEntityType<TileGiantCube> TILE_CHANCE_GIANT;
	public static BlockEntityType<TileCubeDispenser> TILE_CUBE_DISPENSER;

	@SubscribeEvent
	public static void onBlockRegistry(RegistryEvent.Register<Block> e)
	{
		e.getRegistry().register(CHANCE_CUBE = new BlockChanceCube());
		//e.getRegistry().register(CHANCE_ICOSAHEDRON = new BlockChanceD20());
		e.getRegistry().register(GIANT_CUBE = new BlockGiantCube());
		e.getRegistry().register(COMPACT_GIANT_CUBE = new BlockCompactGiantCube());
		e.getRegistry().register(CUBE_DISPENSER = new BlockCubeDispenser());
	}

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void onTileEntityRegistry(RegistryEvent.Register<BlockEntityType<?>> event)
	{
		event.getRegistry().register(TILE_CHANCE_CUBE = (BlockEntityType<TileChanceCube>) BlockEntityType.Builder.of(TileChanceCube::new, CCubesBlocks.CHANCE_CUBE).build(null).setRegistryName(CCubesCore.MODID, "tile_chance_cube"));
		//event.getRegistry().register(TILE_CHANCE_ICOSAHEDRON = (BlockEntityType<TileChanceD20>) BlockEntityType.Builder.of(TileChanceD20::new, CCubesBlocks.CHANCE_ICOSAHEDRON).build(null).setRegistryName(CCubesCore.MODID, "tile_chance_icosahedron"));
		event.getRegistry().register(TILE_CHANCE_GIANT = (BlockEntityType<TileGiantCube>) BlockEntityType.Builder.of(TileGiantCube::new, CCubesBlocks.GIANT_CUBE).build(null).setRegistryName(CCubesCore.MODID, "tile_chance_giant"));
		event.getRegistry().register(TILE_CUBE_DISPENSER = (BlockEntityType<TileCubeDispenser>) BlockEntityType.Builder.of(TileCubeDispenser::new, CCubesBlocks.CUBE_DISPENSER).build(null).setRegistryName(CCubesCore.MODID, "tile_cube_dispenser"));
	}
}
package chanceCubes.blocks;

import chanceCubes.CCubesCore;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileCubeDispenser;
import chanceCubes.tileentities.TileGiantCube;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
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

	public static TileEntityType<TileChanceCube> TILE_CHANCE_CUBE;
	//public static TileEntityType<TileChanceD20> TILE_CHANCE_ICOSAHEDRON;
	public static TileEntityType<TileGiantCube> TILE_CHANCE_GIANT;
	public static TileEntityType<TileCubeDispenser> TILE_CUBE_DISPENSER;

	@SubscribeEvent
	public static void onBlockRegistry(RegistryEvent.Register<Block> e)
	{
		e.getRegistry().register(CHANCE_CUBE = new BlockChanceCube());
		//e.getRegistry().register(CHANCE_ICOSAHEDRON = new BlockChanceD20());
		e.getRegistry().register(GIANT_CUBE = new BlockGiantCube());
		e.getRegistry().register(COMPACT_GIANT_CUBE = new BlockCompactGiantCube());
		e.getRegistry().register(CUBE_DISPENSER = new BlockCubeDispenser());

		TILE_CHANCE_CUBE = TileEntityType.register(CCubesCore.MODID + ":tile_chance_cube", TileEntityType.Builder.create(TileChanceCube::new));
		//TILE_CHANCE_ICOSAHEDRON = TileEntityType.register(CCubesCore.MODID + ":tile_chance_icosahedron", TileEntityType.Builder.create(TileChanceD20::new));
		TILE_CHANCE_GIANT = TileEntityType.register(CCubesCore.MODID + ":tile_chance_giant", TileEntityType.Builder.create(TileGiantCube::new));
		TILE_CUBE_DISPENSER = TileEntityType.register(CCubesCore.MODID + ":tile_cube_dispenser", TileEntityType.Builder.create(TileCubeDispenser::new));
	}
}
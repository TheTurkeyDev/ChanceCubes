package chanceCubes.blocks;

import chanceCubes.CCubesCore;
import chanceCubes.containers.CreativePendantContainer;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.tileentities.TileCubeDispenser;
import chanceCubes.tileentities.TileGiantCube;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Level;

@Mod.EventBusSubscriber(modid = CCubesCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCubesBlocks
{
	public static BlockChanceCube CHANCE_CUBE;
	public static BaseChanceBlock CHANCE_ICOSAHEDRON;
	public static BaseChanceBlock GIANT_CUBE;
	public static BaseChanceBlock COMPACT_GIANT_CUBE;
	public static BaseChanceBlock CUBE_DISPENSER;

	public static TileEntityType<?> TILE_CHANCE_CUBE;
	public static TileEntityType<?> TILE_CHANCE_ICOSAHEDRON;
	public static TileEntityType<?> TILE_CHANCE_GIANT;
	public static TileEntityType<?> TILE_CUBE_DISPENSER;

	@SubscribeEvent
	public static void onBlockRegistry(RegistryEvent.Register<Block> e)
	{
		e.getRegistry().register(CHANCE_CUBE = new BlockChanceCube());
		e.getRegistry().register(CHANCE_ICOSAHEDRON = new BlockChanceD20());
		e.getRegistry().register(GIANT_CUBE = new BlockGiantCube());
		e.getRegistry().register(COMPACT_GIANT_CUBE = new BlockCompactGiantCube());
		e.getRegistry().register(CUBE_DISPENSER = new BlockCubeDispenser());
	}

	@SubscribeEvent
	public static void onTileEntityRegistry(RegistryEvent.Register<TileEntityType<?>> event)
	{
		event.getRegistry().register(TILE_CHANCE_CUBE = TileEntityType.Builder.create(TileChanceCube::new, CCubesBlocks.CHANCE_CUBE).build(null).setRegistryName(CCubesCore.MODID, "tile_chance_cube"));
		event.getRegistry().register(TILE_CHANCE_ICOSAHEDRON = TileEntityType.Builder.create(TileChanceD20::new, CCubesBlocks.CHANCE_ICOSAHEDRON).build(null).setRegistryName(CCubesCore.MODID, "tile_chance_icosahedron"));
		event.getRegistry().register(TILE_CHANCE_GIANT = TileEntityType.Builder.create(TileGiantCube::new, CCubesBlocks.GIANT_CUBE).build(null).setRegistryName(CCubesCore.MODID, "tile_chance_giant"));
		event.getRegistry().register(TILE_CUBE_DISPENSER = TileEntityType.Builder.create(TileCubeDispenser::new, CCubesBlocks.CUBE_DISPENSER).build(null).setRegistryName(CCubesCore.MODID, "tile_cube_dispenser"));
	}
}
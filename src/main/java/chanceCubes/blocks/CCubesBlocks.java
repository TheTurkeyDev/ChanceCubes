package chanceCubes.blocks;

import chanceCubes.CCubesCore;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.tileentities.TileCubeDispenser;
import chanceCubes.tileentities.TileGiantCube;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CCubesCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCubesBlocks
{
	public static BaseChanceBlock CHANCE_CUBE;
	public static BaseChanceBlock CHANCE_ICOSAHEDRON;
	public static BaseChanceBlock GIANT_CUBE;
	public static BaseChanceBlock COMPACT_GIANT_CUBE;
	public static BaseChanceBlock CUBE_DISPENSER;

	public static TileEntityType<TileChanceCube> TILE_CHANCE_CUBE;
	public static TileEntityType<TileChanceD20> TILE_CHANCE_ICOSAHEDRON;
	public static TileEntityType<TileGiantCube> TILE_CHANCE_GIANT;
	public static TileEntityType<TileCubeDispenser> TILE_CUBE_DISPENSER;

	@SubscribeEvent
	public static void onBlockRegistry(RegistryEvent.Register<Block> e)
	{
		e.getRegistry().register(CHANCE_CUBE = new BlockChanceCube());
		e.getRegistry().register(CHANCE_ICOSAHEDRON = new BlockChanceD20());
		e.getRegistry().register(GIANT_CUBE = new BlockGiantCube());
		e.getRegistry().register(COMPACT_GIANT_CUBE = new BlockCompactGiantCube());
		e.getRegistry().register(CUBE_DISPENSER = new BlockCubeDispenser());

		TILE_CHANCE_CUBE = TileEntityType.register(CCubesCore.MODID + ":tile_chance_cube", TileEntityType.Builder.create(TileChanceCube::new));
		TILE_CHANCE_ICOSAHEDRON = TileEntityType.register(CCubesCore.MODID + ":tile_chance_icosahedron", TileEntityType.Builder.create(TileChanceD20::new));
		TILE_CHANCE_GIANT = TileEntityType.register(CCubesCore.MODID + ":tile_chance_giant", TileEntityType.Builder.create(TileGiantCube::new));
		TILE_CUBE_DISPENSER = TileEntityType.register(CCubesCore.MODID + ":tile_cube_dispenser", TileEntityType.Builder.create(TileCubeDispenser::new));
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		OBJLoader.INSTANCE.addDomain(CCubesCore.MODID);
		//https://gyazo.com/141bd0c62dab407ec4d278f2310783a0
		//ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(CHANCE_ICOSAHEDRON), new ModelResourceLocation(CHANCE_ICOSAHEDRON.getRegistryName(), "inventory"));
	}

	public static void registerBlocksItems()
	{
		ItemModelMesher mesher = Minecraft.getInstance().getItemRenderer().getItemModelMesher();

		mesher.register(Item.getItemFromBlock(CHANCE_CUBE), new ModelResourceLocation(CCubesCore.MODID + ":" + CHANCE_CUBE.getBlockName(), "inventory"));
		mesher.register(Item.getItemFromBlock(GIANT_CUBE), new ModelResourceLocation(CCubesCore.MODID + ":" + GIANT_CUBE.getBlockName(), "inventory"));
		mesher.register(Item.getItemFromBlock(COMPACT_GIANT_CUBE), new ModelResourceLocation(CCubesCore.MODID + ":" + COMPACT_GIANT_CUBE.getBlockName(), "inventory"));
		mesher.register(Item.getItemFromBlock(CUBE_DISPENSER), new ModelResourceLocation(CCubesCore.MODID + ":" + CUBE_DISPENSER.getBlockName(), "inventory"));
		mesher.register(Item.getItemFromBlock(CHANCE_ICOSAHEDRON), new ModelResourceLocation(CCubesCore.MODID + ":" + CHANCE_ICOSAHEDRON.getBlockName(), "inventory"));
	}
}
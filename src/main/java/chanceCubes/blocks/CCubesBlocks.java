package chanceCubes.blocks;

import chanceCubes.CCubesCore;
import chanceCubes.renderer.TileChanceD20ItemRenderer;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.tileentities.TileCubeDispenser;
import chanceCubes.tileentities.TileGiantCube;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = CCubesCore.MODID)
public class CCubesBlocks
{
	public static BaseChanceBlock CHANCE_CUBE;
	public static BaseChanceBlock CHANCE_ICOSAHEDRON;
	public static BaseChanceBlock GIANT_CUBE;
	public static BaseChanceBlock COMPACT_GIANT_CUBE;
	public static BaseChanceBlock CUBE_DISPENSER;

	@SubscribeEvent
	public void onBlockRegistry(RegistryEvent.Register<Block> e)
	{
		e.getRegistry().register(CHANCE_CUBE = new BlockChanceCube());
		e.getRegistry().register(CHANCE_ICOSAHEDRON = new BlockChanceD20());
		e.getRegistry().register(GIANT_CUBE = new BlockGiantCube());
		e.getRegistry().register(COMPACT_GIANT_CUBE = new BlockCompactGiantCube());
		e.getRegistry().register(CUBE_DISPENSER = new BlockCubeDispenser());

		GameRegistry.registerTileEntity(TileChanceCube.class, new ResourceLocation(CCubesCore.MODID, "tileChanceCube"));
		GameRegistry.registerTileEntity(TileChanceD20.class, new ResourceLocation(CCubesCore.MODID, "tileChanceIcosahedron"));
		GameRegistry.registerTileEntity(TileGiantCube.class, new ResourceLocation(CCubesCore.MODID, "tileChanceGiant"));
		GameRegistry.registerTileEntity(TileCubeDispenser.class, new ResourceLocation(CCubesCore.MODID, "tileCubeDispenser"));
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		OBJLoader.INSTANCE.addDomain(CCubesCore.MODID);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(CHANCE_ICOSAHEDRON), 0, new ModelResourceLocation(CHANCE_ICOSAHEDRON.getRegistryName(), "inventory"));
	}

	public static void registerBlocksItems()
	{
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		mesher.register(Item.getItemFromBlock(CHANCE_CUBE), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CHANCE_CUBE.getBlockName(), "inventory"));
		mesher.register(Item.getItemFromBlock(GIANT_CUBE), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + GIANT_CUBE.getBlockName(), "inventory"));
		mesher.register(Item.getItemFromBlock(COMPACT_GIANT_CUBE), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + COMPACT_GIANT_CUBE.getBlockName(), "inventory"));
		mesher.register(Item.getItemFromBlock(CUBE_DISPENSER), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CUBE_DISPENSER.getBlockName(), "inventory"));
		//mesher.register(Item.getItemFromBlock(CHANCE_ICOSAHEDRON), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CHANCE_ICOSAHEDRON.getBlockName(), "inventory"));
	}
}
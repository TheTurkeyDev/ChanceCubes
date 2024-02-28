package chanceCubes.blocks;

import chanceCubes.CCubesCore;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileCubeDispenser;
import chanceCubes.tileentities.TileGiantCube;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CCubesBlocks
{
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CCubesCore.MODID);

	public static final DeferredBlock<BaseChanceBlock> CHANCE_CUBE = BLOCKS.register("chance_cube", BlockChanceCube::new);
	//public static final DeferredBlock<BaseChanceBlock> CHANCE_ICOSAHEDRON = BLOCKS.register("chance_icosahedron", BlockChanceD20::new);
	public static final DeferredBlock<BaseChanceBlock> GIANT_CUBE = BLOCKS.register("giant_chance_cube", BlockGiantCube::new);
	public static final DeferredBlock<BaseChanceBlock> COMPACT_GIANT_CUBE = BLOCKS.register("compact_giant_chance_cube", BlockCompactGiantCube::new);
	public static final DeferredBlock<BaseChanceBlock> CUBE_DISPENSER = BLOCKS.register("cube_dispenser", BlockCubeDispenser::new);

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CCubesCore.MODID);

	public static Supplier<BlockEntityType<TileChanceCube>> TILE_CHANCE_CUBE = BLOCK_ENTITIES.register("tile_chance_cube", () -> BlockEntityType.Builder.of(TileChanceCube::new, CCubesBlocks.CHANCE_CUBE.get()).build(null));
	//public static Supplier<BlockEntityType<TileChanceD20>> TILE_CHANCE_ICOSAHEDRON = BLOCK_ENTITIES.register("tile_chance_icosahedron", () -> BlockEntityType.Builder.of(TileChanceD20::new, CCubesBlocks.CHANCE_ICOSAHEDRON.get()).build(null));
	public static Supplier<BlockEntityType<TileGiantCube>> TILE_CHANCE_GIANT = BLOCK_ENTITIES.register("tile_chance_giant", () -> BlockEntityType.Builder.of(TileGiantCube::new, CCubesBlocks.GIANT_CUBE.get()).build(null));
	public static Supplier<BlockEntityType<TileCubeDispenser>> TILE_CUBE_DISPENSER = BLOCK_ENTITIES.register("tile_cube_dispenser", () -> BlockEntityType.Builder.of(TileCubeDispenser::new, CCubesBlocks.CUBE_DISPENSER.get()).build(null));

}
package chanceCubes.datagen;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.items.CCubesItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.Tags;

public class CCubesRecipeProvider extends RecipeProvider
{
	public CCubesRecipeProvider(PackOutput packOutput)
	{
		super(packOutput);
	}

	@Override
	protected void buildRecipes(RecipeOutput output)
	{
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CCubesBlocks.CHANCE_CUBE.get())
				.pattern("LLL")
				.pattern("LBL")
				.pattern("LLL")
				.define('B', Tags.Items.STORAGE_BLOCKS_LAPIS)
				.define('L', Tags.Items.GEMS_LAPIS)
				.unlockedBy("has_lapis_block", has(Tags.Items.STORAGE_BLOCKS_LAPIS))
				.unlockedBy("has_lapis_lazuli", has(Tags.Items.GEMS_LAPIS))
				.save(output, modLoc("chance_cube_crafting"));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CCubesItems.SCANNER.get())
				.pattern("IGI")
				.pattern("GPG")
				.pattern("IGI")
				.define('G', Tags.Items.GLASS)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('P', CCubesItems.CHANCE_PENDANT_T1.get())
				.unlockedBy("has_glass", has(Tags.Items.GLASS))
				.unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
				.unlockedBy("has_pendant", has(CCubesItems.CHANCE_PENDANT_T1.get()))
				.save(output, modLoc("cube_scanner_crafting"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CCubesItems.SILK_PENDANT.get())
				.pattern("SBS")
				.pattern("SPS")
				.pattern("SBS")
				.define('B', Tags.Items.STORAGE_BLOCKS_LAPIS)
				.define('P', CCubesItems.CHANCE_PENDANT_T1.get())
				.define('S', Tags.Items.STRING)
				.unlockedBy("has_lapis_block", has(Tags.Items.STORAGE_BLOCKS_LAPIS))
				.unlockedBy("has_pendant", has(CCubesItems.CHANCE_PENDANT_T1.get()))
				.unlockedBy("has_string", has(Tags.Items.STRING))
				.save(output, modLoc("silk_pendant_crafting"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CCubesItems.CHANCE_PENDANT_T1.get())
				.pattern("GBG")
				.pattern("BDB")
				.pattern("GBG")
				.define('B', Tags.Items.STORAGE_BLOCKS_LAPIS)
				.define('D', Tags.Items.STORAGE_BLOCKS_DIAMOND)
				.define('G', Tags.Items.STORAGE_BLOCKS_GOLD)
				.unlockedBy("has_lapis_block", has(Tags.Items.STORAGE_BLOCKS_LAPIS))
				.unlockedBy("has_diamond_block", has(Tags.Items.STORAGE_BLOCKS_DIAMOND))
				.unlockedBy("has_GOLD_block", has(Tags.Items.STORAGE_BLOCKS_GOLD))
				.save(output, modLoc("tier_1_pendant_crafting"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CCubesItems.CHANCE_PENDANT_T2.get())
				.pattern("GBG")
				.pattern("BPB")
				.pattern("GBG")
				.define('B', Tags.Items.STORAGE_BLOCKS_LAPIS)
				.define('P', CCubesItems.CHANCE_PENDANT_T1.get())
				.define('G', Tags.Items.STORAGE_BLOCKS_GOLD)
				.unlockedBy("has_lapis_block", has(Tags.Items.STORAGE_BLOCKS_LAPIS))
				.unlockedBy("has_pendant", has(CCubesItems.CHANCE_PENDANT_T1.get()))
				.unlockedBy("has_GOLD_block", has(Tags.Items.STORAGE_BLOCKS_GOLD))
				.save(output, modLoc("tier_2_pendant_crafting"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CCubesItems.CHANCE_PENDANT_T3.get())
				.pattern("GBG")
				.pattern("BPB")
				.pattern("GBG")
				.define('B', Tags.Items.STORAGE_BLOCKS_LAPIS)
				.define('P', CCubesItems.CHANCE_PENDANT_T2.get())
				.define('G', Tags.Items.STORAGE_BLOCKS_GOLD)
				.unlockedBy("has_lapis_block", has(Tags.Items.STORAGE_BLOCKS_LAPIS))
				.unlockedBy("has_pendant", has(CCubesItems.CHANCE_PENDANT_T2.get()))
				.unlockedBy("has_GOLD_block", has(Tags.Items.STORAGE_BLOCKS_GOLD))
				.save(output, modLoc("tier_3_pendant_crafting"));

	}

	private ResourceLocation modLoc(String path)
	{
		return new ResourceLocation(CCubesCore.MODID, path);
	}
}

package chanceCubes.util;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import chanceCubes.items.CCubesItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CCubesRecipies
{

	public static void loadRecipies()
	{
		ResourceLocation group = new ResourceLocation("Chance Cubes");

		if(CCubesSettings.craftingRecipie)
			GameRegistry.addShapedRecipe(new ResourceLocation(CCubesCore.MODID, "Chance Cube"), group, new ItemStack(CCubesBlocks.CHANCE_CUBE, 1), "LLL", "LBL", "LLL", 'B', new ItemStack(Blocks.LAPIS_BLOCK), 'L', new ItemStack(Items.DYE, 1, 4));

		GameRegistry.addShapedRecipe(new ResourceLocation(CCubesCore.MODID, "Chance Pendant 1"), group, new ItemStack(CCubesItems.chancePendantT1, 1), "GBG", "BDB", "GBG", 'G', new ItemStack(Blocks.GOLD_BLOCK), 'B', new ItemStack(Blocks.LAPIS_BLOCK), 'D', new ItemStack(Blocks.DIAMOND_BLOCK));
		GameRegistry.addShapedRecipe(new ResourceLocation(CCubesCore.MODID, "Chance Pendant 2"), group, new ItemStack(CCubesItems.chancePendantT2, 1), "GBG", "BPB", "GBG", 'P', new ItemStack(CCubesItems.chancePendantT1), 'B', new ItemStack(Blocks.LAPIS_BLOCK), 'G', new ItemStack(Blocks.GOLD_BLOCK));
		GameRegistry.addShapedRecipe(new ResourceLocation(CCubesCore.MODID, "Chance Pendant 3"), group, new ItemStack(CCubesItems.chancePendantT3, 1), "GBG", "BPB", "GBG", 'P', new ItemStack(CCubesItems.chancePendantT2), 'B', new ItemStack(Blocks.LAPIS_BLOCK), 'G', new ItemStack(Blocks.GOLD_BLOCK));

		GameRegistry.addShapedRecipe(new ResourceLocation(CCubesCore.MODID, "Silk Pendant"), group, new ItemStack(CCubesItems.silkPendant, 1), "SBS", "SPS", "SBS", 'P', new ItemStack(CCubesItems.chancePendantT1), 'B', new ItemStack(Blocks.LAPIS_BLOCK), 'S', new ItemStack(Items.STRING));

		GameRegistry.addShapedRecipe(new ResourceLocation(CCubesCore.MODID, "Scanner"), group, new ItemStack(CCubesItems.scanner, 1), "IGI", "GPG", "IGI", 'P', new ItemStack(CCubesItems.chancePendantT1), 'G', new ItemStack(Blocks.GLASS), 'I', new ItemStack(Items.IRON_INGOT));
	}
}

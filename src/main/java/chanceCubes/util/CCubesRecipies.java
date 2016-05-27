package chanceCubes.util;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import chanceCubes.items.CCubesItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CCubesRecipies
{

	public static void loadRecipies()
	{
		if(CCubesSettings.craftingRecipie)
			GameRegistry.addShapedRecipe(new ItemStack(CCubesBlocks.CHANCE_CUBE, 1), "LLL", "LBL", "LLL", 'B', new ItemStack(Blocks.LAPIS_BLOCK), 'L', new ItemStack(Items.DYE, 1, 4));

		GameRegistry.addShapedRecipe(new ItemStack(CCubesItems.chancePendantT1, 1), "LBL", "BGB", "LBL", 'G', new ItemStack(Blocks.GOLD_BLOCK), 'B', new ItemStack(Blocks.LAPIS_BLOCK), 'L', new ItemStack(Items.DYE, 1, 4));
		GameRegistry.addShapedRecipe(new ItemStack(CCubesItems.chancePendantT2, 1), "LBL", "BPB", "LBL", 'P', new ItemStack(CCubesItems.chancePendantT1), 'B', new ItemStack(Blocks.LAPIS_BLOCK), 'L', new ItemStack(Items.DYE, 1, 4));
		GameRegistry.addShapedRecipe(new ItemStack(CCubesItems.chancePendantT3, 1), "LBL", "BPB", "LBL", 'P', new ItemStack(CCubesItems.chancePendantT2), 'B', new ItemStack(Blocks.LAPIS_BLOCK), 'L', new ItemStack(Items.DYE, 1, 4));
		GameRegistry.addShapedRecipe(new ItemStack(CCubesItems.chancePendantT4, 1), "LBL", "BPB", "LBL", 'P', new ItemStack(CCubesItems.chancePendantT3), 'B', new ItemStack(Blocks.LAPIS_BLOCK), 'L', new ItemStack(Items.DYE, 1, 4));

		GameRegistry.addShapedRecipe(new ItemStack(CCubesItems.silkPendant, 1), "SBS", "SPS", "SBS", 'P', new ItemStack(CCubesItems.chancePendantT2), 'B', new ItemStack(Blocks.LAPIS_BLOCK), 'S', new ItemStack(Items.STRING));

		GameRegistry.addShapedRecipe(new ItemStack(CCubesItems.scanner, 1), "IGI", "GPG", "IGI", 'P', new ItemStack(CCubesItems.chancePendantT2), 'G', new ItemStack(Blocks.GLASS), 'I', new ItemStack(Items.IRON_INGOT));
	}
}

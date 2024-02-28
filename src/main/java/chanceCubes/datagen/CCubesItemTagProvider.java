package chanceCubes.datagen;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class CCubesItemTagProvider extends ItemTagsProvider
{
	protected CCubesItemTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider,
									TagsProvider<Block> blockTagProvider, ExistingFileHelper existingFileHelper)
	{
		super(packOutput, lookupProvider, blockTagProvider.contentsGetter(), CCubesCore.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider)
	{
		this.tag(RewardsUtil.BLACKLIST).add(Items.BEDROCK, Items.COMMAND_BLOCK, Items.COMMAND_BLOCK_MINECART,
				Items.CHAIN_COMMAND_BLOCK, Items.REPEATING_COMMAND_BLOCK, Items.JIGSAW, Items.STRUCTURE_BLOCK,
				Items.STRUCTURE_VOID, Items.AIR, Items.DEBUG_STICK, Items.KNOWLEDGE_BOOK, Items.REINFORCED_DEEPSLATE);
	}
}

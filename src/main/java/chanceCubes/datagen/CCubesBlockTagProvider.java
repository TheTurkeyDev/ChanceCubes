package chanceCubes.datagen;

import chanceCubes.CCubesCore;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class CCubesBlockTagProvider extends BlockTagsProvider
{
	protected CCubesBlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper)
	{
		super(packOutput, lookupProvider, CCubesCore.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider)
	{

	}
}

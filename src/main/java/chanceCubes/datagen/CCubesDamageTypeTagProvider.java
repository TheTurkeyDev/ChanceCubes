package chanceCubes.datagen;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CCubesDamageTypeTagProvider extends TagsProvider<DamageType> {
	protected CCubesDamageTypeTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(packOutput, Registries.DAMAGE_TYPE, lookupProvider, CCubesCore.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider)
	{
		this.tag(DamageTypeTags.BYPASSES_ARMOR).add(CCubesDamageTypes.MATH_FAIL, CCubesDamageTypes.MAZE_FAIL,
				CCubesDamageTypes.QUESTION_FAIL, CCubesDamageTypes.DIG_BUILD_FAIL, CCubesDamageTypes.MATCHING_FAIL);
		this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(CCubesDamageTypes.MATH_FAIL, CCubesDamageTypes.MAZE_FAIL,
				CCubesDamageTypes.QUESTION_FAIL, CCubesDamageTypes.DIG_BUILD_FAIL, CCubesDamageTypes.MATCHING_FAIL);
	}
}

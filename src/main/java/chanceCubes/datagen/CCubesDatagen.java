package chanceCubes.datagen;

import chanceCubes.CCubesCore;
import chanceCubes.listeners.WorldGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCubesDatagen
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = CompletableFuture.supplyAsync(CCubesDatagen::getProvider);
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(event.includeServer(), new CCubesRecipeProvider(packOutput));
		BlockTagsProvider blockTagsProvider;
		generator.addProvider(event.includeServer(), blockTagsProvider = new CCubesBlockTagProvider(packOutput, lookupProvider, existingFileHelper));
		generator.addProvider(event.includeServer(), new CCubesItemTagProvider(packOutput, lookupProvider, blockTagsProvider, existingFileHelper));

		generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(
				packOutput, lookupProvider, Set.of(CCubesCore.MODID)));
		generator.addProvider(event.includeServer(), new CCubesDamageTypeTagProvider(packOutput, lookupProvider, existingFileHelper));
	}

	private static HolderLookup.Provider getProvider()
	{
		final RegistrySetBuilder registryBuilder = new RegistrySetBuilder();
		registryBuilder.add(Registries.DAMAGE_TYPE, CCubesDamageTypeProvider::bootstrap);
		registryBuilder.add(Registries.CONFIGURED_FEATURE, WorldGen::configuredBootstrap);
		registryBuilder.add(Registries.PLACED_FEATURE, WorldGen::placedBootstrap);
		registryBuilder.add(ForgeRegistries.Keys.BIOME_MODIFIERS, CCubesBiomeModifiers::bootstrap);
		// We need the BIOME registry to be present, so we can use a biome tag, doesn't matter that it's empty
		registryBuilder.add(Registries.BIOME, context -> {
		});
		RegistryAccess.Frozen regAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
		return registryBuilder.buildPatch(regAccess, VanillaRegistries.createLookup());
	}
}

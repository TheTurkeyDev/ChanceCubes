package chanceCubes.components;

import chanceCubes.CCubesCore;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CCubesDataComponents
{
	public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, CCubesCore.MODID);

	public static final Supplier<DataComponentType<Integer>> CHANCE = DATA_COMPONENT_TYPES.register("chance", () ->
			DataComponentType.<Integer>builder()
					.persistent(Codec.INT)
					.networkSynchronized(ByteBufCodecs.INT)
					.build());

	public static final Supplier<DataComponentType<String>> REWARD = DATA_COMPONENT_TYPES.register("reward", () ->
			DataComponentType.<String>builder()
					.persistent(Codec.STRING)
					.networkSynchronized(ByteBufCodecs.STRING_UTF8)
					.build());

}

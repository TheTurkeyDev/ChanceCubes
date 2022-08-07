package chanceCubes.commands;

import chanceCubes.CCubesCore;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CCubesRewardArguments
{
	public static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY, CCubesCore.MODID);
	public static final RegistryObject<SingletonArgumentInfo<RewardArgument>> REWARD_ARGUMENT_TYPE = COMMAND_ARGUMENT_TYPES.register("reward", () ->
			ArgumentTypeInfos.registerByClass(RewardArgument.class,
					SingletonArgumentInfo.contextFree(RewardArgument::rewardArgument)));
}

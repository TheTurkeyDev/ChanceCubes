package chanceCubes.commands;

import chanceCubes.CCubesCore;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CCubesRewardArguments
{
	public static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, CCubesCore.MODID);
	public static final Supplier<SingletonArgumentInfo<RewardArgument>> REWARD_ARGUMENT_TYPE = COMMAND_ARGUMENT_TYPES.register("reward", () ->
			ArgumentTypeInfos.registerByClass(RewardArgument.class,
					SingletonArgumentInfo.contextFree(RewardArgument::rewardArgument)));
}

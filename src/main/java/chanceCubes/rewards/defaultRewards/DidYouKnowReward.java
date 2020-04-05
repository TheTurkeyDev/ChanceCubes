package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DidYouKnowReward extends BaseCustomReward
{
	private List<String> dyk = new ArrayList<>();

	public DidYouKnowReward()
	{
		super(CCubesCore.MODID + ":did_you_know", 0);
		dyk.add("Funwayguy created the original D20 model and animation.");
		dyk.add("Glenn is NOT a refference to the TV show \\'The Walking Dead\\', but is instead a reference to the streamer Sevadus.");
		dyk.add("The nuke reward that says \\'May death rain upon them\\' is a reference to the Essentials Bukkit plugin?");
		dyk.add("The real reason his name is pickles is because a user from Wyld\\'s Twtich chat suggested the reward.");
		dyk.add("Today is Darkosto\\'s Birthday!");
		dyk.add("The stick that the Leonidas zombie uses is called `The Spartan Kick`!");
	}

	@Override
	public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		String fact = "Did you know?\n" + dyk.get(RewardsUtil.rand.nextInt(dyk.size()));
		String command = "/summon minecraft:item ~ ~ ~ {Item:{id:\"minecraft:written_book\",Count:1b,tag:{title:\"Did You know?\",author:\"Chance Cubes\",generation:2,pages:['{\"text\":\"" + fact + "\",\"color\":\"black\"}']}}}";
		RewardsUtil.executeCommand(world, player, pos, command);
	}
}
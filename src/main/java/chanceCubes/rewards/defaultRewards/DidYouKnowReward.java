package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DidYouKnowReward implements IChanceCubeReward
{
	private List<String> dyk = new ArrayList<String>();

	public DidYouKnowReward()
	{
		dyk.add("The nuke reward that says 'May death rain upon them' is a reference to the Essentials Bukkit plugin?");
		dyk.add("The real reason his name is pickles is because a user from Wyld's Twtich chat suggested the reward.");
		dyk.add("Funwayguy created the original D20 model and animation.");
		dyk.add("Glenn is NOT a refference to the TV show 'The Walking Dead', but is instead a reference to the streamer Sevadus.");
		dyk.add("Today is Darkosto's Birthday!");
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		String fact = "Did you know?\n" + dyk.get(RewardsUtil.rand.nextInt(dyk.size()));
		String command = "/summon Item ~ ~1 ~ {Item:{id:written_book,Count:1,tag:{title:\"Did You know?\",author:\"Chance Cubes\",generation:0,pages:[\"{text:\\\"" + fact + "\\\",color:black}\"]}}}";
		RewardsUtil.executeCommand(world, player, command);
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Did_You_Know";
	}
}
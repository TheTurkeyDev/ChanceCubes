package chanceCubes.tests;

import chanceCubes.CCubesCore;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.rewards.IChanceCubeReward;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.BeforeBatch;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Rotation;
import net.minecraftforge.gametest.GameTestHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@GameTestHolder(CCubesCore.MODID)
public class RewardTests
{
	public static final String EMPTY_STRUCTURE = new ResourceLocation(CCubesCore.MODID, "empty").toString();

	@GameTestGenerator
	public static Collection<TestFunction> ChanceCubesRewardTests()
	{
		List<TestFunction> tests = new ArrayList<>();
		for(String rewardName : GlobalCCRewardRegistry.DEFAULT.getRewardNames())
		{
			TestFunction rewardTest = new TestFunction(
					"reward_test", //batchName
					"test_" + rewardName, //testName
					EMPTY_STRUCTURE, //structureName
					Rotation.NONE, //rotation
					100, //maxTicks
					0, //setupTicks
					true, //required
					(GameTestHelper helper) -> RewardTests.rewardTest(helper, rewardName) //function
			);
			tests.add(rewardTest);
		}

		return tests;
	}

	public static void rewardTest(GameTestHelper helper, String rewardName)
	{
		IChanceCubeReward reward = GlobalCCRewardRegistry.DEFAULT.getRewardByName(rewardName);
		BlockPos spawnPos = new BlockPos(1, 0, 0);
		BlockPos adjPos = helper.absolutePos(spawnPos);
		Player player = helper.makeMockPlayer();
		player.setPos(adjPos.getX(), adjPos.getY(), adjPos.getZ());
		reward.trigger(helper.getLevel(), adjPos, player, new JsonObject());
		helper.succeed();
	}
}

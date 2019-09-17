package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.biodomeGen.BioDomeGen;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public class BioDomeReward extends BaseCustomReward
{
	public BioDomeReward()
	{
		super(CCubesCore.MODID + ":BioDome", 0);
	}

	@Override
	public void trigger(final World world, final BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		// player.addChatMessage(new ChatComponentText("Hey! I can be a Pandora's Box to!"));
		(new BioDomeGen(player)).genRandomDome(pos, world, 25, true);
	}
}
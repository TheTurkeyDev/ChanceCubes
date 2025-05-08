package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Arrays;
import java.util.List;

public class CursedHeadReward extends BaseCustomReward
{
	private static final List<String> HEAD_NAMES = Arrays.asList("MrLubert", "giester", "Ughtrin", "burtekd", "Marioluigi7896", "Lothrazar", "dominai");

	public CursedHeadReward()
	{
		super(CCubesCore.MODID + ":cursed_head", -20);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		String username = HEAD_NAMES.get(RewardsUtil.rand.nextInt(HEAD_NAMES.size()));
		ItemStack head = new ItemStack(Items.PLAYER_HEAD);
		ResolvableProfile resolvableProfile = new ResolvableProfile(new GameProfile(Util.NIL_UUID, username));
		resolvableProfile.resolve().thenAccept((profile) -> {
			if(profile != null)
			{
				head.set(DataComponents.PROFILE, profile);
			}
		});

		head.enchant(level.holderOrThrow(Enchantments.BINDING_CURSE), 1);
		head.enchant(level.holderOrThrow(Enchantments.VANISHING_CURSE), 1);

		player.drop(player.getItemBySlot(EquipmentSlot.HEAD), true, false);
		player.setItemSlot(EquipmentSlot.HEAD, head);

		RewardsUtil.sendMessageToPlayer(player, username + " has cursed you till death do you part!");
		Scheduler.scheduleTask(new Task("delayed message", 20)
		{
			@Override
			public void callback()
			{
				RewardsUtil.sendMessageToPlayer(player, "<" + username + "> MUWAHAHAHA");
			}
		});
	}
}

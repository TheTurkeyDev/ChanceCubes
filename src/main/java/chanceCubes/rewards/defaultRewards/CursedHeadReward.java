package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CursedHeadReward extends BaseCustomReward
{
	private static final List<String> HEAD_NAMES = Arrays.asList("MrLubert", "giester", "Ughtrin", "burtekd", "Marioluigi7896", "Lothrazar", "dominai");

	public CursedHeadReward()
	{
		super(CCubesCore.MODID + ":cursed_head", -20);
	}

	@Override
	public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		String username = HEAD_NAMES.get(RewardsUtil.rand.nextInt(HEAD_NAMES.size()));
		ItemStack head = new ItemStack(Items.PLAYER_HEAD);
		CompoundNBT nbt = head.getTag();
		if(nbt == null)
		{
			nbt = new CompoundNBT();
			head.setTag(nbt);
		}
		nbt.putString("SkullOwner", username);

		head.addEnchantment(Enchantments.BINDING_CURSE, 1);
		head.addEnchantment(Enchantments.VANISHING_CURSE, 1);

		player.dropItem(player.getItemStackFromSlot(EquipmentSlotType.HEAD), true, false);
		player.setItemStackToSlot(EquipmentSlotType.HEAD, head);

		player.sendMessage(new StringTextComponent(username + " has cursed you till death do you part!"));
		Scheduler.scheduleTask(new Task("delayed message", 20)
		{
			@Override
			public void callback()
			{
				player.sendMessage(new StringTextComponent("<" + username + "> MUWAHAHAHA"));
			}
		});
	}
}

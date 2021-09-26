package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class ItemRenamer extends BaseCustomReward
{
	public ItemRenamer()
	{
		super(CCubesCore.MODID + ":item_rename", 10);
	}

	// @formatter:off
	private final String[] names = {"Turkey", "qnxb", "Darkosto", "Wyld", "Funwayguy", "ButtonBoy", "SlothMonster",
			"Vash", "Cazador", "KiwiFails", "Matrixis", "FlameGoat", "iChun", "tibbzeh", "Reninsane", 
			"Pulpy", "Zeek", "Sevadus", "Bob Ross", "T-loves", "Headwound", "JonBams", "Sketch",
			"Lewdicolo", "Sinful", "Drakma", "1chick", "Deadpine", "Amatt_", "Jacky", "Brae"};
	
	private final String[] adjectives = {"Destroyer", "Terror", "Wrath", "Smasher", "P90", "Wisdom", "Savior",
			"Lightning Bringer", "Rage", "Happiness", "Shocker", " Slayer", "Sunshine", "Giant Crayon", "Blade",
			"Tamer", "Order", "Sharp Edge", "Noodle", "Diamond", "Rod", "Big Giant Sharp Pokey Thing", "Majestic Item",
			"Wonder", "Awesomeness"};
	
	// @formatter:on

	@Override
	public void trigger(ServerLevel world, BlockPos pos, Player player, JsonObject settings)
	{
		List<ItemStack> stacks = new ArrayList<>();
		for(ItemStack stack : player.getInventory().items)
			if(!stack.isEmpty())
				stacks.add(stack);

		for(ItemStack stack : player.getInventory().armor)
			if(!stack.isEmpty())
				stacks.add(stack);

		if(stacks.size() == 0)
		{
			ItemStack dirt = new ItemStack(Blocks.DIRT);
			TextComponent name = new TextComponent("A lonely piece of dirt");
			name.setStyle(name.getStyle().withColor(TextColor.parseColor("#ff1111")));
			dirt.setHoverName(name);
			player.getInventory().add(dirt);
			RewardsUtil.executeCommand(world, player, player.getOnPos(), "/advancement grant @p only chancecubes:lonely_dirt");
			return;
		}

		for(int i = 0; i < 3; i++)
		{
			String name = names[RewardsUtil.rand.nextInt(names.length)];
			String adj = adjectives[RewardsUtil.rand.nextInt(adjectives.length)];

			if(name.substring(name.length() - 1).equalsIgnoreCase("s"))
				name += "'";
			else
				name += "'s";
			String newName = name + " " + adj;
			stacks.get(RewardsUtil.rand.nextInt(stacks.size())).setHoverName(new TextComponent(newName));
		}

		RewardsUtil.sendMessageToPlayer(player, "Those items of yours need a little personality!");
	}
}

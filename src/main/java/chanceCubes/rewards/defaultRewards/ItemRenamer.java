package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import com.google.gson.JsonObject;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class ItemRenamer extends BaseCustomReward
{
	public ItemRenamer()
	{
		super(CCubesCore.MODID + ":item_rename", 10);
	}

	// @formatter:off
	private String[] names = {"Turkey", "qnxb", "Darkosto", "Wyld", "Funwayguy", "ButtonBoy", "SlothMonster", 
			"Vash", "Cazador", "KiwiFails", "Matrixis", "FlameGoat", "iChun", "tibbzeh", "Reninsane", 
			"Pulpy", "Zeek", "Sevadus", "Bob Ross", "T-loves", "Headwound", "JonBams", "Sketch",
			"Lewdicolo", "Sinful", "Drakma", "1chick", "Deadpine", "Amatt_", "Jacky", "Brae"};
	
	private String[] adjectives = {"Destroyer", "Terror", "Wrath", "Smasher", "P90", "Wisdom", "Savior", 
			"Lightning Bringer", "Rage", "Happiness", "Shocker", " Slayer", "Sunshine", "Giant Crayon", "Blade",
			"Tamer", "Order", "Sharp Edge", "Noodle", "Diamond", "Rod", "Big Giant Sharp Pokey Thing", "Majestic Item",
			"Wonder", "Awesomeness"};
	
	// @formatter:on

	@Override
	public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, JsonObject settings)
	{
		List<ItemStack> stacks = new ArrayList<>();
		for(ItemStack stack : player.inventory.mainInventory)
			if(!stack.isEmpty())
				stacks.add(stack);

		for(ItemStack stack : player.inventory.armorInventory)
			if(!stack.isEmpty())
				stacks.add(stack);

		if(stacks.size() == 0)
		{
			ItemStack dirt = new ItemStack(Blocks.DIRT);
			dirt.setDisplayName(new StringTextComponent("A lonley piece of dirt"));
			player.inventory.addItemStackToInventory(dirt);
			RewardsUtil.executeCommand(world, player, player.getPosition(), "/advancement grant @p only chancecubes:lonely_dirt");
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
			stacks.get(RewardsUtil.rand.nextInt(stacks.size())).setDisplayName(new StringTextComponent(newName));
		}

		RewardsUtil.sendMessageToPlayer(player, "Those items of yours need a little personality!");
	}
}

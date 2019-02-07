package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemRenamer implements IChanceCubeReward
{
	private Random rand = new Random();

	// @formatter:off
	private String[] names = {"Turkey", "qnxb", "Darkosto", "Wyld", "Funwayguy", "ButtonBoy", "SlothMonster", 
			"Vash", "Cazador", "KiwiFails", "Matrixis", "FlameGoat", "iChun", "tibbzeh", "Reninsane", 
			"PulpJohnFiction", "Zeek", "Sevadus", "Bob Ross", "T-loves", "Headwound", "JonBams", "Sketch",
			"Lewdicolo", "Sinful", "Drakma", "1chick", "Deadpine", "Amatt_", "Jacky"};
	
	private String[] adjectives = {"Destroyer", "Terror", "Wrath", "Smasher", "P90", "Wisdom", "Savior", 
			"Lightning Bringer", "Rage", "Happiness", "Shocker", " Slayer", "Sunshine", "Giant Crayon", "Blade",
			"Tamer", "Order", "Sharp Edge", "Noodle", "Diamond", "Rod", "Big Giant Sharp Pokey Thing"};
	
	// @formatter:on

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		for(ItemStack stack : player.inventory.mainInventory)
			if(!stack.isEmpty())
				stacks.add(stack);

		for(ItemStack stack : player.inventory.armorInventory)
			if(!stack.isEmpty())
				stacks.add(stack);

		if(stacks.size() == 0)
		{
			ItemStack dirt = new ItemStack(Blocks.DIRT);
			dirt.setStackDisplayName("A lonley piece of dirt");
			player.inventory.addItemStackToInventory(dirt);
			RewardsUtil.executeCommand(world, player, "/advancement grant @p only chancecubes:lonely_dirt");
			return;
		}

		for(int i = 0; i < 3; i++)
		{
			String name = names[rand.nextInt(names.length)];
			String adj = adjectives[rand.nextInt(adjectives.length)];

			if(name.substring(name.length() - 1).equalsIgnoreCase("s"))
				name += "'";
			else
				name += "'s";
			String newName = name + " " + adj;
			stacks.get(rand.nextInt(stacks.size())).setStackDisplayName(newName);
		}

		player.sendMessage(new TextComponentString("Those items of yours need a little personality!"));

	}

	@Override
	public int getChanceValue()
	{
		return 10;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Item_Rename";
	}
}

package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chanceCubes.CCubesCore;
import net.minecraft.entity.player.EntityPlayer;
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
			"PulpJohnFiction"};
	
	private String[] adjectives = {"Destroyer", "Terror", "Wrath", "Smasher", "P90", "Wisdom", "Savior", 
			"Lightning Bringer", "Rage", "Happiness", "Shocker", " Slayer", "Sunshine", "Giant Crayon", "Blade",
			"Tamer", "Order"};
	
	// @formatter:on

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		for(ItemStack stack : player.inventory.mainInventory)
			if(stack != null)
				stacks.add(stack);

		for(ItemStack stack : player.inventory.armorInventory)
			if(stack != null)
				stacks.add(stack);

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

		player.addChatMessage(new TextComponentString("Those items of yours need a little personality!"));

	}

	@Override
	public int getChanceValue()
	{
		return -35;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Item_Rename";
	}
}
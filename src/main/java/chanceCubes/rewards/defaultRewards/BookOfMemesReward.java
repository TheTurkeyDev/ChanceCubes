package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chanceCubes.CCubesCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BookOfMemesReward implements IChanceCubeReward
{
	private Random random = new Random();
	private List<String> memes = new ArrayList<String>();

	public BookOfMemesReward()
	{
		memes.add("Sodium, atomic number 11, was first isolated by Peter Dager in 1807. A chemical component of salt, he named it Na in honor of the saltiest region on earth, North America.");
		memes.add("(╯°□°）╯︵ ┻━┻ \n ༼ᕗຈل͜ຈ༽ᕗ RAISE YOUR DONGERS ༼ᕗຈل͜ຈ༽ᕗ");
		memes.add("Darude- status \n â˜� Not Sandstorm \n â˜‘ Sandstorm");
		memes.add("( ͡° ͜ʖ ͡°) Every 60 seconds in Africa, a minute passes. Together we can stop this. Please spread the word ( ͡° ͜ʖ ͡°)");
		memes.add("YESTERDAY YOU SAID TOMMOROW, Don't let your dreams be memes, Don't meme your dreams be beams, Jet fuel won't melt tomorrow's memes, DON'T LET YOUR STEEL MEMES BE JET DREAMS");
		memes.add("If the human body is 75% water, how can you be 100% salt?");
		memes.add(" ▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ ◄ ▲ ▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ ◄▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ \n Sorry, I've dropped my bag of Doritos™ brand chips▲ ► ▼ ◄ ▲ ► ▼ ◄ ▲ ▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ ► ▼ ◄ ◄ ▲▲ ► ▼ ◄▼ ◄ ◄ ▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ ◄ ▲ ►");
		memes.add("Hey Chat....... \n \n \n \n 123");
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		String meme = memes.get(random.nextInt(memes.size()));

		ItemStack stack = new ItemStack(Items.written_book);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("title", "Book of Memes");
		nbt.setString("author", "Chance Cubes");
		NBTTagList pages = new NBTTagList();
		pages.set(1, new NBTTagString(meme));
		nbt.setTag("pages", pages);
		stack.setTagCompound(nbt);
		Entity itemEnt = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack.copy());
		world.spawnEntityInWorld(itemEnt);
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Book_Of_Memes";
	}

}
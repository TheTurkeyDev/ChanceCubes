package chanceCubes.items;

import java.util.List;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemChanceCube extends ItemBlock
{
	public ItemChanceCube(Block b)
	{
		super(b);
	}

	public void setChance(ItemStack stack, int chance)
	{
		if(chance > 100 || chance < -101)
			chance = -101;
		NBTTagCompound nbt = stack.stackTagCompound;
		if(nbt == null)
			nbt = new NBTTagCompound();
		nbt.setInteger("Chance", chance);
		stack.setTagCompound(nbt);
	}

	public int getChance(ItemStack stack)
	{
		if(stack.stackTagCompound == null)
			return -101;
		return stack.stackTagCompound.hasKey("Chance") ? stack.stackTagCompound.getInteger("Chance") : -101;
	}
	public String getChanceAsStringValue(ItemStack stack)
	{
		if(stack.stackTagCompound == null)
			return "Random";
		return stack.stackTagCompound.hasKey("Chance") ? stack.stackTagCompound.getInteger("Chance") == -101 ? "Random" : "" + stack.stackTagCompound.getInteger("Chance") : "Random";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) 
	{
        if(!stack.getItem().equals(CCubesBlocks.chanceCubeDispenser))
        {
            String chance = this.getChanceAsStringValue(stack);
            list.add("Chance Value: " + chance);
        }
	}

	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
	{
		boolean placed = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);

		TileEntity te = world.getTileEntity(x, y, z);
		if (te != null)
		{
			if(te instanceof TileChanceCube)
			{
				int chance = this.getChance(stack);
				if(chance != -101)
					((TileChanceCube)te).setChance(chance);
			}
			else if(te instanceof TileChanceD20)
			{
				int chance = this.getChance(stack);
				if(chance != -101)
					((TileChanceD20)te).setChance(chance);
			}
		}

		return placed;
	}
}

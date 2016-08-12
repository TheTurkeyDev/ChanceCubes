package chanceCubes.items;

import java.util.List;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null)
			nbt = new NBTTagCompound();
		nbt.setInteger("Chance", chance);
		stack.setTagCompound(nbt);
	}

	public int getChance(ItemStack stack)
	{
		if(stack.getTagCompound() == null)
			return -101;
		return stack.getTagCompound().hasKey("Chance") ? stack.getTagCompound().getInteger("Chance") : -101;
	}
	public String getChanceAsStringValue(ItemStack stack)
	{
		if(stack.getTagCompound() == null)
			return "Random";
		return stack.getTagCompound().hasKey("Chance") ? stack.getTagCompound().getInteger("Chance") == -101 ? "Random" : "" + stack.getTagCompound().getInteger("Chance") : "Random";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) 
	{
        if(stack.getItem().equals(CCubesBlocks.CUBE_DISPENSER))
        {
            String chance = this.getChanceAsStringValue(stack);
            list.add("Chance Value: " + chance);
        }

	}

	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, IBlockState blockState)
	{
		boolean placed = super.placeBlockAt(stack, player, world, pos, facing, hitX, hitY, hitZ, blockState);

		TileEntity te = world.getTileEntity(pos);
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

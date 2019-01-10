package chanceCubes.items;

import java.util.List;

import javax.annotation.Nullable;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemChanceCube extends ItemBlock
{
	public ItemChanceCube(Block b)
	{
		super(b, (new Item.Builder()).group(CCubesCore.modTab));
	}

	public void setChance(ItemStack stack, int chance)
	{
		if(chance > 100 || chance < -101)
			chance = -101;
		NBTTagCompound nbt = stack.getTag();
		if(nbt == null)
			nbt = new NBTTagCompound();
		nbt.setInt("Chance", chance);
		stack.setTag(nbt);
	}

	public int getChance(ItemStack stack)
	{
		if(stack.getTag() == null)
			return -101;
		return stack.getTag().hasKey("Chance") ? stack.getTag().getInt("Chance") : -101;
	}

	public String getChanceAsStringValue(ItemStack stack)
	{
		if(stack.getTag() == null)
			return "Random";
		return stack.getTag().hasKey("Chance") ? stack.getTag().getInt("Chance") == -101 ? "Random" : "" + stack.getTag().getInt("Chance") : "Random";
	}

	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn)
	{
		if(!stack.getItem().equals(Item.getItemFromBlock(CCubesBlocks.CUBE_DISPENSER)))
		{
			String chance = this.getChanceAsStringValue(stack);
			list.add(new TextComponentString("Chance Value: " + chance));
		}

		if(stack.getItem().equals(Item.getItemFromBlock(CCubesBlocks.COMPACT_GIANT_CUBE)))
		{
			list.add(new TextComponentString("WARNING: The Giant Chance Cube may cause some damage and/or place a lot of blocks down... You've been warned."));
		}
	}

	protected boolean placeBlock(BlockItemUseContext context, IBlockState state)
	{
		boolean placed = super.placeBlock(context, state);

		TileEntity te = context.getWorld().getTileEntity(context.getPos());
		if(te != null)
		{
			int chance = this.getChance(context.getItem());
			if(chance != -101)
			{
				if(te instanceof TileChanceCube)
				{
					((TileChanceCube) te).setChance(chance);
				}
				else if(te instanceof TileChanceD20)
				{
					((TileChanceD20) te).setChance(chance);
				}
			}
		}

		return placed;
	}
}

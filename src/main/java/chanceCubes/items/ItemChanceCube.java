package chanceCubes.items;

import java.util.List;

import javax.annotation.Nullable;

import chanceCubes.blocks.BaseChanceBlock;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.profiles.ProfileManager;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
	public ItemChanceCube(BaseChanceBlock b)
	{
		super(b);
		this.setRegistryName(b.getRegistryName());
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

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flagIn)
	{
		Item item = stack.getItem();
		if(!item.equals(Item.getItemFromBlock(CCubesBlocks.CUBE_DISPENSER)))
		{
			String chance = this.getChanceAsStringValue(stack);
			list.add("Chance Value: " + chance);
		}

		if(item.equals(Item.getItemFromBlock(CCubesBlocks.COMPACT_GIANT_CUBE)))
			list.add("WARNING: The Giant Chance Cube will probably cause lots damage and/or place a lot of blocks down... You've been warned.");
		else if(item.equals(Item.getItemFromBlock(CCubesBlocks.CHANCE_CUBE)))
			list.add("Warning: It is recommended you don't open these in or next toy your base.");

		if(item.equals(Item.getItemFromBlock(CCubesBlocks.CHANCE_CUBE)) || item.equals(Item.getItemFromBlock(CCubesBlocks.CHANCE_ICOSAHEDRON)))
		{
			list.add("==== Enabled Profiles ====");
			list.addAll(ProfileManager.getEnabledProfileNames());
		}
	}

	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, IBlockState blockState)
	{
		boolean placed = super.placeBlockAt(stack, player, world, pos, facing, hitX, hitY, hitZ, blockState);

		TileEntity te = world.getTileEntity(pos);
		if(te != null)
		{
			int chance = this.getChance(stack);
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

package chanceCubes.items;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.BaseChanceBlock;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.profiles.GlobalProfileManager;
import chanceCubes.profiles.PlayerProfileManager;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemChanceCube extends BlockItem
{
	public ItemChanceCube(BaseChanceBlock b)
	{
		super(b, getProps(b));
		this.setRegistryName(b.getRegistryName());
	}

	public static Properties getProps(Block b)
	{
		Properties props = new Properties();
		if(!b.equals(CCubesBlocks.GIANT_CUBE))
			props.group(CCubesCore.modTab);
		return props;
	}

	public void setChance(ItemStack stack, int chance)
	{
		if(chance > 100 || chance < -101)
			chance = -101;
		CompoundNBT nbt = stack.getTag();
		if(nbt == null)
			nbt = new CompoundNBT();
		nbt.putInt("Chance", chance);
		stack.setTag(nbt);
	}

	public int getChance(ItemStack stack)
	{
		if(stack.getTag() == null)
			return -101;
		return stack.getTag().contains("Chance") ? stack.getTag().getInt("Chance") : -101;
	}

	public String getChanceAsStringValue(ItemStack stack)
	{
		if(stack.getTag() == null)
			return "Random";
		return stack.getTag().contains("Chance") ? stack.getTag().getInt("Chance") == -101 ? "Random" : "" + stack.getTag().getInt("Chance") : "Random";
	}

	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn)
	{
		Item item = stack.getItem();
		if(!item.equals(Item.getItemFromBlock(CCubesBlocks.CUBE_DISPENSER)))
		{
			String chance = this.getChanceAsStringValue(stack);
			list.add(new StringTextComponent("Chance Value: " + chance));
		}

		if(item.equals(CCubesItems.COMPACT_GIANT_CUBE))
			list.add(new StringTextComponent(TextFormatting.RED + "WARNING: The Giant Chance Cube will probably cause lots damage and/or place a lot of blocks down... You've been warned."));
		else if(item.equals(CCubesItems.CHANCE_CUBE))
			list.add(new StringTextComponent(TextFormatting.RED + "Warning: It is recommended you don't open these in or next to your base."));
		else if(item.equals(CCubesItems.CHANCE_ICOSAHEDRON))
			list.add(new StringTextComponent(TextFormatting.RED + "WORK IN PROGRESS"));


		if(item.equals(CCubesItems.CHANCE_CUBE) || item.equals(CCubesItems.CHANCE_ICOSAHEDRON))
		{
			list.add(new StringTextComponent("==== Enabled Profiles ===="));
			if(Minecraft.getInstance().player != null)
			{
				PlayerProfileManager ppm = GlobalProfileManager.getPlayerProfileManager(Minecraft.getInstance().player.getUniqueID().toString());
				if(ppm != null)
					for(String rewardName : ppm.getEnabledProfileNames())
						list.add(new StringTextComponent(rewardName));
			}
		}
	}

	protected boolean placeBlock(BlockItemUseContext context, BlockState state)
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

package chanceCubes.items;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.BaseChanceBlock;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

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
			props.tab(CCubesCore.modTab);
		return props;
	}

	public void setChance(ItemStack stack, int chance)
	{
		if(chance > 100 || chance < -101)
			chance = -101;
		CompoundTag nbt = stack.getTag();
		if(nbt == null)
			nbt = new CompoundTag();
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

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag)
	{
		Item item = stack.getItem();
		if(!item.equals(CCubesItems.CUBE_DISPENSER))
		{
			String chance = this.getChanceAsStringValue(stack);
			list.add(new TextComponent("Chance Value: " + chance));
		}

		if(item.equals(CCubesItems.COMPACT_GIANT_CUBE))
			list.add(new TextComponent(ChatFormatting.RED + "WARNING: The Giant Chance Cube will probably cause lots damage and/or place a lot of blocks down... You've been warned."));
		else if(item.equals(CCubesItems.CHANCE_CUBE))
			list.add(new TextComponent(ChatFormatting.RED + "Warning: It is recommended you don't open these in or next to your base."));
		else if(item.equals(CCubesItems.CHANCE_ICOSAHEDRON))
			list.add(new TextComponent(ChatFormatting.RED + "WORK IN PROGRESS"));
	}

	@Override
	protected boolean placeBlock(BlockPlaceContext context, BlockState state)
	{
		boolean placed = super.placeBlock(context, state);

		BlockEntity te = context.getLevel().getBlockEntity(context.getClickedPos());
		if(te != null)
		{
			int chance = this.getChance(context.getItemInHand());
			if(chance != -101)
			{
				if(te instanceof TileChanceCube)
					((TileChanceCube) te).setChance(chance);
				else if(te instanceof TileChanceD20)
					((TileChanceD20) te).setChance(chance);
			}
		}

		return placed;
	}
}

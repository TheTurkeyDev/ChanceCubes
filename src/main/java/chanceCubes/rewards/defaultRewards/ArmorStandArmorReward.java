package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import com.google.gson.JsonObject;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import org.apache.commons.lang3.ArrayUtils;

public class ArmorStandArmorReward extends BaseCustomReward
{

	public ArmorStandArmorReward()
	{
		super(CCubesCore.MODID + ":armor_stand_armor", 40);
	}

	// @formatter:off
	private String[] names = {"dmodoomsirius", "MJRLegends", "Twp156", "JSL7", "Ratblade", "DerRedstoneProfi", "TurkeyDev"};
	
	private ItemStack[] headItems = {new ItemStack(Items.CHAINMAIL_HELMET), new ItemStack(Items.DIAMOND_HELMET),
			new ItemStack(Items.GOLDEN_HELMET), new ItemStack(Items.IRON_HELMET), new ItemStack(Items.LEATHER_HELMET),
			new ItemStack(Items.CREEPER_HEAD), new ItemStack(Items.SKELETON_SKULL), new ItemStack(Items.ZOMBIE_HEAD),
			new ItemStack(Items.WITHER_SKELETON_SKULL), new ItemStack(Items.PLAYER_HEAD), new ItemStack(Items.DRAGON_HEAD),
			new ItemStack(Blocks.CHEST)};
	
	private ItemStack[] chestItems = {new ItemStack(Items.CHAINMAIL_CHESTPLATE), new ItemStack(Items.DIAMOND_CHESTPLATE),
			new ItemStack(Items.GOLDEN_CHESTPLATE), new ItemStack(Items.IRON_CHESTPLATE), new ItemStack(Items.LEATHER_CHESTPLATE),
			new ItemStack(Items.ELYTRA), new ItemStack(Items.BLACK_BANNER)};
	
	private ItemStack[] legsItems = {new ItemStack(Items.CHAINMAIL_LEGGINGS), new ItemStack(Items.DIAMOND_LEGGINGS),
			new ItemStack(Items.GOLDEN_LEGGINGS), new ItemStack(Items.IRON_LEGGINGS), new ItemStack(Items.LEATHER_LEGGINGS)};
	
	private ItemStack[] bootsItems = {new ItemStack(Items.CHAINMAIL_BOOTS), new ItemStack(Items.DIAMOND_BOOTS),
			new ItemStack(Items.GOLDEN_BOOTS), new ItemStack(Items.IRON_BOOTS), new ItemStack(Items.LEATHER_BOOTS)};
	
	private ItemStack[] handItems = {new ItemStack(Items.CAKE), new ItemStack(Blocks.TORCH),
			new ItemStack(Items.SHIELD), new ItemStack(Items.IRON_SWORD), new ItemStack(Items.DIAMOND_HOE),
			new ItemStack(Items.BLACK_BANNER), new ItemStack(Items.COOKIE), new ItemStack(Items.STICK),
			new ItemStack(Items.GOLDEN_CARROT)};
	// @formatter:on

	@Override
	public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, JsonObject settings)
	{
		ArmorStandEntity armorStand = EntityType.ARMOR_STAND.create(world);
		String[] allNames = ArrayUtils.addAll(names, super.getSettingAsStringList(settings, "names", new String[0]));
		String name = allNames[RewardsUtil.rand.nextInt(allNames.length)];
		armorStand.setCustomName(new StringTextComponent(name));
		armorStand.setCustomNameVisible(true);
		armorStand.setPositionAndRotation(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0);

		ItemStack[] allHeadItems = ArrayUtils.addAll(headItems, super.getSettingAsItemStackList(settings, "headItemStacks", new ItemStack[0]));
		ItemStack headStack = allHeadItems[RewardsUtil.rand.nextInt(allHeadItems.length)].copy();
		if(headStack.getItem().equals(Items.PLAYER_HEAD))
		{
			CompoundNBT nbt = headStack.getTag();
			if(nbt == null)
			{
				nbt = new CompoundNBT();
				headStack.setTag(nbt);
			}
			nbt.putString("SkullOwner", name);
		}

		armorStand.setItemStackToSlot(EquipmentSlotType.HEAD, headStack);
		ItemStack[] allChestItems = ArrayUtils.addAll(chestItems, super.getSettingAsItemStackList(settings, "chestItemStacks", new ItemStack[0]));
		armorStand.setItemStackToSlot(EquipmentSlotType.CHEST, allChestItems[RewardsUtil.rand.nextInt(allChestItems.length)].copy());
		ItemStack[] allLegsItems = ArrayUtils.addAll(legsItems, super.getSettingAsItemStackList(settings, "legItemStacks", new ItemStack[0]));
		armorStand.setItemStackToSlot(EquipmentSlotType.LEGS, allLegsItems[RewardsUtil.rand.nextInt(allLegsItems.length)].copy());
		ItemStack[] allBootItems = ArrayUtils.addAll(bootsItems, super.getSettingAsItemStackList(settings, "bootItemStacks", new ItemStack[0]));
		armorStand.setItemStackToSlot(EquipmentSlotType.FEET, allBootItems[RewardsUtil.rand.nextInt(allBootItems.length)].copy());
		ItemStack[] allHandItems = ArrayUtils.addAll(handItems, super.getSettingAsItemStackList(settings, "handItemStacks", new ItemStack[0]));
		armorStand.setItemStackToSlot(EquipmentSlotType.MAINHAND, allHandItems[RewardsUtil.rand.nextInt(allHandItems.length)].copy());
		armorStand.setItemStackToSlot(EquipmentSlotType.OFFHAND, allHandItems[RewardsUtil.rand.nextInt(allHandItems.length)].copy());
		world.addEntity(armorStand);
	}
}
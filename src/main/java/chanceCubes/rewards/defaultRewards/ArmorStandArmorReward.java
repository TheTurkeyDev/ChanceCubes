package chanceCubes.rewards.defaultRewards;

import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SkullItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class ArmorStandArmorReward extends BaseCustomReward
{

	public ArmorStandArmorReward()
	{
		super(CCubesCore.MODID + ":Armor_Stand_Armor", 40);
	}

	// @formatter:off
	private String[] names = {"dmodoomsirius", "MJRLegends", "Twp156", "JSL7", "Ratblade", "DerRedstoneProfi", "Turkey2349"};
	
	private ItemStack[] headItems = {new ItemStack(Items.CHAINMAIL_HELMET), new ItemStack(Items.DIAMOND_HELMET),
			new ItemStack(Items.GOLDEN_HELMET), new ItemStack(Items.IRON_HELMET), new ItemStack(Items.LEATHER_HELMET),
			new ItemStack(Items.SKELETON_SKULL, 1), new ItemStack(Items.WITHER_SKELETON_SKULL, 1), new ItemStack(Items.CREEPER_HEAD, 1),
			new ItemStack(Items.DRAGON_HEAD, 1), new ItemStack(Items.PLAYER_HEAD, 1), new ItemStack(Items.ZOMBIE_HEAD, 1),
			new ItemStack(Blocks.CHEST)};
	
	private ItemStack[] chestItems = {new ItemStack(Items.CHAINMAIL_CHESTPLATE), new ItemStack(Items.DIAMOND_CHESTPLATE),
			new ItemStack(Items.GOLDEN_CHESTPLATE), new ItemStack(Items.IRON_CHESTPLATE), new ItemStack(Items.LEATHER_CHESTPLATE),
			new ItemStack(Items.ELYTRA), new ItemStack(Items.WHITE_BANNER)};
	
	private ItemStack[] legsItems = {new ItemStack(Items.CHAINMAIL_LEGGINGS), new ItemStack(Items.DIAMOND_LEGGINGS),
			new ItemStack(Items.GOLDEN_LEGGINGS), new ItemStack(Items.IRON_LEGGINGS), new ItemStack(Items.LEATHER_LEGGINGS)};
	
	private ItemStack[] bootsItems = {new ItemStack(Items.CHAINMAIL_BOOTS), new ItemStack(Items.DIAMOND_BOOTS),
			new ItemStack(Items.GOLDEN_BOOTS), new ItemStack(Items.IRON_BOOTS), new ItemStack(Items.LEATHER_BOOTS)};
	
	private ItemStack[] handItems = {new ItemStack(Blocks.CAKE), new ItemStack(Blocks.TORCH),
			new ItemStack(Items.SHIELD), new ItemStack(Items.IRON_SWORD), new ItemStack(Items.DIAMOND_HOE),
			new ItemStack(Items.WHITE_BANNER), new ItemStack(Items.COOKIE), new ItemStack(Items.STICK),
			new ItemStack(Items.GOLDEN_CARROT)};
	// @formatter:on

	@Override
	public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		ArmorStandEntity armorStand = EntityType.ARMOR_STAND.create(world);
		String name = names[RewardsUtil.rand.nextInt(names.length)];
		armorStand.setCustomName(new StringTextComponent(name));
		armorStand.setCustomNameVisible(true);
		armorStand.setPositionAndRotation(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0);

		ItemStack headStack = headItems[RewardsUtil.rand.nextInt(headItems.length)].copy();
		if(headStack.getItem() instanceof SkullItem && headStack.getDamage() == 3)
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
		armorStand.setItemStackToSlot(EquipmentSlotType.CHEST, chestItems[RewardsUtil.rand.nextInt(chestItems.length)].copy());
		armorStand.setItemStackToSlot(EquipmentSlotType.LEGS, legsItems[RewardsUtil.rand.nextInt(legsItems.length)].copy());
		armorStand.setItemStackToSlot(EquipmentSlotType.FEET, bootsItems[RewardsUtil.rand.nextInt(bootsItems.length)].copy());
		armorStand.setItemStackToSlot(EquipmentSlotType.MAINHAND, handItems[RewardsUtil.rand.nextInt(handItems.length)].copy());
		armorStand.setItemStackToSlot(EquipmentSlotType.OFFHAND, handItems[RewardsUtil.rand.nextInt(handItems.length)].copy());
		world.addEntity(armorStand);
	}
}
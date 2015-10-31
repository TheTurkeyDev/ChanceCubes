package chanceCubes.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.tileentities.TileChanceCube;

public class BlockChanceCube extends Block implements ITileEntityProvider
{
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	@SideOnly(Side.CLIENT)
	private IIcon[] specialIcons;


	public BlockChanceCube()
	{
		super(Material.ground);
		this.setHardness(0.5f);
		this.setBlockName("Chance_Cube");
		this.setCreativeTab(CCubesCore.modTab);
		this.setBlockTextureName("chancecubes:chanceCube");
		this.setLightLevel(2);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new TileChanceCube();
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
	{
		if(!world.isRemote && player != null && !(player instanceof FakePlayer))
		{
			TileChanceCube te = (TileChanceCube) world.getTileEntity(x, y, z);
			if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
			{
				ItemStack stack = new ItemStack(Item.getItemFromBlock(CCubesBlocks.chanceCube), 1);
				((ItemChanceCube) stack.getItem()).setChance(stack, te.getChance());
				this.dropBlockAsItem(world, x, y, z, stack);
				world.setBlockToAir(x, y, z);
				world.removeTileEntity(x, y, z);
				return true;
			}

			if(te != null)
			{
				world.setBlockToAir(x, y, z);
				ChanceCubeRegistry.INSTANCE.triggerRandomReward(world, x, y, z, player, te.getChance());
			}
		}
		return true;
	}

	@Override
	public int quantityDropped(Random p_149745_1_)
	{
		return 0;
	}

	@Override
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity)
	{
		return !(entity instanceof EntityWither) && super.canEntityDestroy(world, x, y, z, entity);
	}

	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion)
	{

	}

	@Override
	public boolean canDropFromExplosion(Explosion explosion)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register)
	{
		this.icons = new IIcon[6];
		this.icons[0] = register.registerIcon(CCubesCore.MODID + ":chancecube_face_1");
		this.icons[1] = register.registerIcon(CCubesCore.MODID + ":chancecube_face_6");
		this.icons[2] = register.registerIcon(CCubesCore.MODID + ":chancecube_face_2");
		this.icons[3] = register.registerIcon(CCubesCore.MODID + ":chancecube_face_5");
		this.icons[4] = register.registerIcon(CCubesCore.MODID + ":chancecube_face_4");
		this.icons[5] = register.registerIcon(CCubesCore.MODID + ":chancecube_face_3");
		if(CCubesSettings.hasHolidayTexture)
		{
			this.specialIcons = new IIcon[2];
			String texture = CCubesSettings.holidayTextureName;
			this.specialIcons[0] = register.registerIcon(CCubesCore.MODID + ":" + texture + "Top");
			this.specialIcons[1] = register.registerIcon(CCubesCore.MODID + ":" + texture);
		}
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if(CCubesSettings.hasHolidayTexture)
		{
			if(side == 0 || side == 1)
				return this.specialIcons[0];
			else
				return this.specialIcons[1];
		}
		else
			return this.icons[side];
	}
}
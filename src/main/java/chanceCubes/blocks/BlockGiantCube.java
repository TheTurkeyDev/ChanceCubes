package chanceCubes.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import team.chisel.ctmlib.CTM;
import team.chisel.ctmlib.TextureSubmap;
import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import chanceCubes.items.CCubesItems;
import chanceCubes.registry.GiantCubeRegistry;
import chanceCubes.tileentities.TileGiantCube;
import chanceCubes.util.GiantCubeUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import static team.chisel.ctmlib.Dir.*;

public class BlockGiantCube extends BlockContainer
{
	@SideOnly(Side.CLIENT)
	private TextureSubmap[] subMap;
	@SideOnly(Side.CLIENT)
	private TextureSubmap[] specialIcons;

	public BlockGiantCube()
	{
		super(Material.ground);
		this.setHardness(0.5f);
		this.setBlockName("Giant_Chance_Cube");
		this.setBlockTextureName("chancecubes:chanceCube");
		this.setLightLevel(2);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileGiantCube();
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
	{
		if(!world.isRemote && player != null && !(player instanceof FakePlayer))
		{
			TileGiantCube te = (TileGiantCube) world.getTileEntity(x, y, z);
			if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
			{
				ItemStack stack = new ItemStack(Item.getItemFromBlock(CCubesBlocks.chanceCompactGiantCube), 1);
				this.dropBlockAsItem(world, x, y, z, stack);
				GiantCubeUtil.removeStructure(te.getMasterX(), te.getMasterY(), te.getMasterZ(), world);
				return true;
			}
			if(te != null)
			{
				if(!te.hasMaster())
				{
					world.setBlockToAir(x, y, z);
					return false;
				}
				player.addChatMessage(new ChatComponentText("The Giant Cube and rewards are currently In developement"));
				player.addChatMessage(new ChatComponentText("Please let me know what you think of the idea and leave sugestions!"));
				GiantCubeRegistry.INSTANCE.triggerRandomReward(world, te.getMasterX(), te.getMasterY(), te.getMasterZ(), player, 0);
				GiantCubeUtil.removeStructure(te.getMasterX(), te.getMasterY(), te.getMasterZ(), world);
			}
		}
		return true;
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register)
	{
		this.subMap = new TextureSubmap[6];
		this.subMap[0] = new TextureSubmap(register.registerIcon(CCubesCore.MODID + ":chancecube_face_1"), 3, 3);
		this.subMap[1] = new TextureSubmap(register.registerIcon(CCubesCore.MODID + ":chancecube_face_6"), 3, 3);
		this.subMap[2] = new TextureSubmap(register.registerIcon(CCubesCore.MODID + ":chancecube_face_2"), 3, 3);
		this.subMap[3] = new TextureSubmap(register.registerIcon(CCubesCore.MODID + ":chancecube_face_5"), 3, 3);
		this.subMap[4] = new TextureSubmap(register.registerIcon(CCubesCore.MODID + ":chancecube_face_4"), 3, 3);
		this.subMap[5] = new TextureSubmap(register.registerIcon(CCubesCore.MODID + ":chancecube_face_3"), 3, 3);
		if(CCubesSettings.hasHolidayTexture)
		{
			this.specialIcons = new TextureSubmap[2];
			String texture = CCubesSettings.holidayTextureName;
			this.specialIcons[0] = new TextureSubmap(register.registerIcon(CCubesCore.MODID + ":" + texture + "Top"), 3, 3);
			this.specialIcons[1] = new TextureSubmap(register.registerIcon(CCubesCore.MODID + ":" + texture), 3, 3);
		}
	}

	@Override
	public IIcon getIcon(int side, int meta)
	{
		if(CCubesSettings.hasHolidayTexture)
		{
			if(side == 0 || side == 1)
				return this.specialIcons[0].getBaseIcon();
			else
				return this.specialIcons[1].getBaseIcon();
		}
		else
			return this.subMap[side].getBaseIcon();
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		CTM ctm = CTM.getInstance();
		ctm.buildConnectionMap(world, x, y, z, side, CCubesBlocks.chanceGiantCube, 0);

		if(ctm.connectedAnd(TOP, RIGHT, BOTTOM, LEFT))
		{
			return this.getIconAt(side, 1, 1);
		}
		else if(ctm.connectedAnd(TOP, BOTTOM, RIGHT))
		{
			return this.getIconAt(side, 0, 1);
		}
		else if(ctm.connectedAnd(LEFT, RIGHT, BOTTOM))
		{
			return this.getIconAt(side, 1, 0);
		}
		else if(ctm.connectedAnd(LEFT, RIGHT, TOP))
		{
			return this.getIconAt(side, 1, 2);
		}
		else if(ctm.connectedAnd(LEFT, BOTTOM, TOP))
		{
			return this.getIconAt(side, 2, 1);
		}
		else if(ctm.connectedAnd(BOTTOM, RIGHT))
		{
			return this.getIconAt(side, 0, 0);
		}
		else if(ctm.connectedAnd(TOP, RIGHT))
		{
			return this.getIconAt(side, 0, 2);
		}
		else if(ctm.connectedAnd(LEFT, BOTTOM))
		{
			return this.getIconAt(side, 2, 0);
		}
		else if(ctm.connectedAnd(LEFT, TOP))
		{
			return this.getIconAt(side, 2, 2);
		}
		else
		{
			return this.getIconAt(side, 1, 1);
		}
	}
	
	public IIcon getIconAt(int side, int x, int y)
	{
		if(CCubesSettings.hasHolidayTexture)
		{
			if(side == 0 || side == 1)
				return this.specialIcons[0].getSubIcon(x, y);
			else
				return this.specialIcons[1].getSubIcon(x, y);
		}
		else
			return this.subMap[side].getSubIcon(x, y);
	}
}
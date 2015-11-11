package chanceCubes.client.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;

import chanceCubes.config.CustomRewardsLoader;

public class ConfigGui extends GuiScreen
{
	private ConfigEditState editState;
	private GuiButton buttonNew;

	private String[] prevStage = new String[3];
	private String drawString = "";

	private CustomExtendedList entries;

	public ConfigGui(GuiScreen screen)
	{
	}

	@SuppressWarnings("unchecked")
	public void initGui()
	{
		editState = ConfigEditState.Files;

		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);

		this.buttonList.add(new GuiButton(0, 50, this.height - 40, 98, 20, "Back"));
		this.buttonList.add(this.buttonNew = new GuiButton(1, this.width / 2 - 50, this.height - 40, 100, 20, "New"));

		entries = new CustomExtendedList(this, this.mc, this.width, this.height, 32, this.height - 64, 36);

		loadFiles();
	}
	
    public void onGuiClosed() 
    {
    	CustomRewardsLoader.instance.loadCustomRewards();
    }

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		entries.drawScreen(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
		mc.fontRendererObj.drawString(this.drawString, this.width / 2 - (int)(drawString.length() * 2.5), 10, 0xFFFFFF);
	}

	protected void actionPerformed(GuiButton button)
	{
		if(button.enabled)
		{
			if(button.id == 0)
			{
				this.prevEditStage();
			}
			else if(button.id == 0)
			{
				// TODO: Create stuff
			}
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int mouseEvent) throws IOException
	{
		this.entries.mouseClicked(x, y, mouseEvent);
		super.mouseClicked(x, y, mouseEvent);
	}

	public void nextEditStage(String name)
	{
		switch(editState)
		{
			case Files:
			{
				editState = ConfigEditState.All_Rewards;
				this.loadFileRewards(name);
				this.prevStage[0] = name;
				break;
			}
			case All_Rewards:
			{
				editState = ConfigEditState.Single_Reward;
				this.loadSingleReward(name);
				this.prevStage[1] = name;
				break;
			}
			case Single_Reward:
			{
				editState = ConfigEditState.Reward_Type;
				this.loadRewardType(name);
				this.prevStage[2] = name;
				break;
			}
			case Reward_Type:
			{
				break;
			}
			case Edit_Reward_Type:
			{
				break;
			}
		}
	}

	public void prevEditStage()
	{
		switch(editState)
		{
			case Files:
			{
				break;
			}
			case All_Rewards:
			{
				editState = ConfigEditState.Files;
				this.loadFiles();
				break;
			}
			case Single_Reward:
			{
				editState = ConfigEditState.All_Rewards;
				this.loadFileRewards(this.prevStage[0]);
				break;
			}
			case Reward_Type:
			{
				editState = ConfigEditState.Single_Reward;
				this.loadSingleReward(this.prevStage[1]);
				break;
			}
			case Edit_Reward_Type:
			{
				editState = ConfigEditState.Reward_Type;
				//this.loadSingleReward(this.prevStage[1]);
				break;
			}
		}
	}

	public void loadFiles()
	{
		entries.clearElements();
		this.buttonNew.displayString = "New File";
		drawString = "Select the file that you would like to load";
		for(String s : CustomRewardsLoader.instance.getRewardsFiles())
			entries.addElement(s);
	}

	public void loadFileRewards(String file)
	{
		drawString = "Select the reward that you would like to edit";
		entries.clearElements();
		for(String s : CustomRewardsLoader.instance.getRewardsFromFile(file))
			entries.addElement(s);
	}
	
	public void loadSingleReward(String reward)
	{
		drawString = "Select the reward type that you would like to edit";
		entries.clearElements();
		for(String s : CustomRewardsLoader.instance.getReward(this.prevStage[0], reward))
			entries.addElement(s);
	}
	
	public void loadRewardType(String type)
	{
		drawString = "Select the reward type that you would like to edit";
		entries.clearElements();
		for(String s : CustomRewardsLoader.instance.getRewardType(this.prevStage[0], this.prevStage[1], type))
			entries.addElement(s);
	}

	public enum ConfigEditState
	{
		Files(0), All_Rewards(1), Single_Reward(2), Reward_Type(3), Edit_Reward_Type(4);

		private int posNum;

		ConfigEditState(int pos)
		{
			this.posNum = pos;
		}

		public int getPosition()
		{
			return this.posNum;
		}
	}
}
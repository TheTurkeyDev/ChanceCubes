package chanceCubes.client.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;

import chanceCubes.CCubesCore;
import chanceCubes.client.gui.CustomExtendedList.CustomTextEntry;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.rewards.rewardparts.ChestChanceItem;
import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.rewards.rewardparts.EntityPart;
import chanceCubes.rewards.rewardparts.ExpirencePart;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.MessagePart;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.rewardparts.ParticlePart;
import chanceCubes.rewards.rewardparts.PotionPart;
import chanceCubes.rewards.rewardparts.SoundPart;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConfigGui extends GuiScreen
{
	private ConfigEditState editState;
	private boolean isCreatingNew = false;
	private GuiButton buttonNew;
	private GuiButton buttonSave;
	private GuiButton buttonCancel;
	private GuiButton buttonback;
	private String buttonNewText = "New File";
	private GuiTextField textFieldNew;

	private String[] prevStage = new String[4];
	private String drawString = "";

	private CustomExtendedList entries;

	private JsonParser json;
	private Gson gson;

	private GuiScreen parentScreen;

	public ConfigGui(GuiScreen screen)
	{
		json = new JsonParser();

		gson = new GsonBuilder().setPrettyPrinting().create();

		parentScreen = screen;
	}

	public void initGui()
	{
		editState = ConfigEditState.Files;

		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);

		this.buttonList.add(buttonback = new GuiButton(0, 50, this.height - 40, 98, 20, "Back"));
		this.buttonList.add(this.buttonNew = new GuiButton(1, this.width / 2 - 50, this.height - 25, 100, 20, buttonNewText));
		this.buttonList.add(this.buttonCancel = new GuiButton(2, this.width / 2 - 50, this.height - 55, 100, 20, "Cancel"));
		this.buttonList.add(this.buttonSave = new GuiButton(3, this.width / 2 - 50, this.height - 25, 100, 20, "Save"));
		buttonCancel.visible = false;
		textFieldNew = new GuiTextField(4, mc.fontRendererObj, this.width - 250, this.height - 40, 200, 20);

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
		if(this.isCreatingNew)
			this.textFieldNew.drawTextBox();
		mc.fontRendererObj.drawString(this.drawString, this.width / 2 - (int) (drawString.length() * 2.5), 10, 0xFFFFFF);
	}

	protected void actionPerformed(GuiButton button)
	{
		if(button.enabled)
		{
			if(button.id == 0)
			{
				this.prevEditStage();
			}
			else if(button.id == 1)
			{
				if(this.isCreatingNew)
				{
					buttonNew.displayString = this.buttonNewText;
					File file = null;
					JsonElement fileJson = new JsonObject();
					switch(editState)
					{
						case Files:
						{
							String fileName = this.textFieldNew.getText();
							if(!fileName.contains(".json"))
								fileName += ".json";
							file = new File(CustomRewardsLoader.instance.getFolderFile().getAbsolutePath() + "/" + fileName);
							try
							{
								file.createNewFile();
							} catch(IOException e)
							{
								CCubesCore.logger.log(Level.ERROR, "Failed to create the new file for a custom reward!");
								e.printStackTrace();
								return;
							}
							break;
						}
						case All_Rewards:
						{
							String rewardName = this.textFieldNew.getText();
							file = new File(CustomRewardsLoader.instance.getFolderFile().getAbsolutePath() + "/" + this.prevStage[0]);
							if(!file.exists())
							{
								CCubesCore.logger.log(Level.ERROR, "Failed to find the file to edit a reward");
								return;
							}

							fileJson = json.parse(this.loadFileContents(file));
							JsonObject newRewardJson = new JsonObject();
							newRewardJson.addProperty("Chance", 0);
							fileJson.getAsJsonObject().add(rewardName, newRewardJson);
							break;
						}
						case Single_Reward:
						{
							String rewardType = this.textFieldNew.getText();
							file = new File(CustomRewardsLoader.instance.getFolderFile().getAbsolutePath() + "/" + this.prevStage[0]);
							if(!file.exists())
							{
								CCubesCore.logger.log(Level.ERROR, "Failed to find the file to edit a reward");
								return;
							}

							fileJson = json.parse(this.loadFileContents(file));
							JsonElement rewardJson = fileJson.getAsJsonObject().get(this.prevStage[1]);
							if(rewardJson == null)
							{
								CCubesCore.logger.log(Level.ERROR, "Failed to find the reward with the given name of " + this.prevStage[1]);
								return;
							}

							JsonArray rewardTypeArray = new JsonArray();
							rewardJson.getAsJsonObject().add(rewardType, rewardTypeArray);
							break;
						}
						case Reward_Type:
						{
							String defaultValue = this.textFieldNew.getText();
							file = new File(CustomRewardsLoader.instance.getFolderFile().getAbsolutePath() + "/" + this.prevStage[0]);
							if(!file.exists())
							{
								CCubesCore.logger.log(Level.ERROR, "Failed to find the file to edit a reward");
								return;
							}
							fileJson = json.parse(this.loadFileContents(file));
							JsonElement rewardJson = fileJson.getAsJsonObject().get(this.prevStage[1]);
							if(rewardJson == null)
							{
								CCubesCore.logger.log(Level.ERROR, "Failed to find the reward with the given name of " + this.prevStage[1]);
								return;
							}

							JsonArray rewardTypeArray = rewardJson.getAsJsonObject().get(this.prevStage[2]).getAsJsonArray();
							JsonObject rewardInstance = new JsonObject();
							rewardInstance.addProperty(this.prevStage[2].toLowerCase(), defaultValue);
							rewardTypeArray.add(rewardInstance);
							break;
						}
						case Edit_Reward_Type:
						{
							break;
						}
					}

					try
					{
						FileOutputStream outputStream = new FileOutputStream(file);
						OutputStreamWriter writer = new OutputStreamWriter(outputStream);
						writer.append(gson.toJson(fileJson));
						writer.close();
						outputStream.close();
					} catch(IOException e)
					{
						CCubesCore.logger.log(Level.ERROR, "Failed to create the new file for a custom reward!");
						e.printStackTrace();
						return;
					}

					this.textFieldNew.setText("");
					buttonCancel.visible = false;
					buttonback.enabled = true;
					this.reloadStage();
				}
				else
				{
					buttonNew.displayString = "Create";
					buttonCancel.visible = true;
					buttonback.enabled = false;
				}
				this.isCreatingNew = !this.isCreatingNew;
			}
			else if(button.id == 2)
			{
				this.isCreatingNew = false;
				buttonNew.displayString = this.buttonNewText;
				buttonCancel.visible = false;
				buttonback.enabled = true;
			}
			else if(button.id == 3)
			{
				File file = null;
				JsonElement fileJson = new JsonObject();
				file = new File(CustomRewardsLoader.instance.getFolderFile().getAbsolutePath() + "/" + this.prevStage[0]);
				if(!file.exists())
				{
					CCubesCore.logger.log(Level.ERROR, "Failed to find the file to edit a reward");
					return;
				}
				fileJson = json.parse(this.loadFileContents(file));
				JsonElement rewardJson = fileJson.getAsJsonObject().get(this.prevStage[1]);
				if(rewardJson == null)
				{
					CCubesCore.logger.log(Level.ERROR, "Failed to find the reward with the given name of " + this.prevStage[1]);
					return;
				}

				if(this.entries.getListEntry(0) instanceof CustomTextEntry && ((CustomTextEntry) this.entries.getListEntry(0)).getLabel().equalsIgnoreCase("Chance:"))
				{
					CustomTextEntry textBox = (CustomTextEntry) this.entries.getListEntry(0);
					try
					{
						int chance = Integer.parseInt(textBox.getTextBox().getText());
						if(chance < -100)
							chance = -100;
						else if(chance > 100)
							chance = 100;
						rewardJson.getAsJsonObject().addProperty("Chance", chance);
					} catch(NumberFormatException e)
					{
						CCubesCore.logger.log(Level.ERROR, "Failed to cast the chance value of the reward to an integer");
						return;
					}
					try
					{
						FileOutputStream outputStream = new FileOutputStream(file);
						OutputStreamWriter writer = new OutputStreamWriter(outputStream);
						writer.append(gson.toJson(fileJson));
						writer.close();
						outputStream.close();
					} catch(IOException e)
					{
						CCubesCore.logger.log(Level.ERROR, "Failed to create the new file for a custom reward!");
						e.printStackTrace();
						return;
					}
				}
				else
				{
					this.saveRewardPart(file, fileJson, rewardJson);
				}

				this.textFieldNew.setText("");
				buttonCancel.visible = false;
				buttonback.enabled = true;
				this.reloadStage();
			}
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int mouseEvent) throws IOException
	{
		this.textFieldNew.mouseClicked(x, y, mouseEvent);
		this.entries.mouseClicked(x, y, mouseEvent);
		super.mouseClicked(x, y, mouseEvent);
	}

	protected void keyTyped(char p_73869_1_, int p_73869_2_)
	{
		if(!this.textFieldNew.textboxKeyTyped(p_73869_1_, p_73869_2_))
		{
			this.entries.keyTyped(p_73869_1_, p_73869_2_);
		}
	}

	public void nextEditStage(int id, String name)
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
				editState = ConfigEditState.Edit_Reward_Type;
				this.loadSpecificRewardType(name, this.prevStage[2]);
				this.prevStage[3] = id + ":" + name;
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
				this.mc.displayGuiScreen(this.parentScreen);
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
				this.loadRewardType(this.prevStage[2]);
				break;
			}
		}
	}

	public void reloadStage()
	{
		switch(editState)
		{
			case Files:
			{
				this.loadFiles();
				break;
			}
			case All_Rewards:
			{
				this.loadFileRewards(this.prevStage[0]);
				break;
			}
			case Single_Reward:
			{
				this.loadSingleReward(this.prevStage[1]);
				break;
			}
			case Reward_Type:
			{
				this.loadRewardType(this.prevStage[2]);
				break;
			}
			case Edit_Reward_Type:
			{
				this.loadSpecificRewardType(this.prevStage[3].substring(this.prevStage[3].indexOf(":") + 1), this.prevStage[2]);
				break;
			}
		}
	}

	public void loadFiles()
	{
		buttonSave.visible = false;
		this.buttonNew.visible = true;
		entries.clearElements();
		this.buttonNew.displayString = "New File";
		this.buttonNewText = "New File";
		drawString = "Select the file that you would like to load";
		for(String s : CustomRewardsLoader.instance.getRewardsFiles())
			entries.addButton(s);
	}

	public void loadFileRewards(String file)
	{
		buttonSave.visible = false;
		this.buttonNew.visible = true;
		this.buttonNew.displayString = "New Reward";
		this.buttonNewText = "New Reward";
		drawString = "Select the reward that you would like to edit";
		entries.clearElements();
		for(String s : CustomRewardsLoader.instance.getRewardsFromFile(file))
			entries.addButton(s);
	}

	public void loadSingleReward(String reward)
	{
		buttonSave.visible = false;
		this.buttonNew.visible = true;
		this.buttonNew.displayString = "New Part";
		this.buttonNewText = "New Part";
		drawString = "Select the reward type that you would like to edit";
		entries.clearElements();
		for(String s : CustomRewardsLoader.instance.getReward(this.prevStage[0], reward))
			entries.addButton(s);
	}

	public void loadRewardType(String type)
	{
		buttonSave.visible = false;
		this.buttonNew.visible = true;
		this.buttonNew.displayString = "New " + type;
		this.buttonNewText = "New " + type;
		drawString = "Select the specific reward part that you would like to edit";
		entries.clearElements();
		if(type.equalsIgnoreCase("Chance"))
		{
			buttonSave.visible = true;
			this.buttonNew.visible = false;
			entries.addTextEntry("Chance", CustomRewardsLoader.instance.getRewardType(this.prevStage[0], this.prevStage[1], type).get(0));
		}
		else
		{
			for(String s : CustomRewardsLoader.instance.getRewardType(this.prevStage[0], this.prevStage[1], type))
				entries.addButton(s);
		}
	}

	public void loadSpecificRewardType(String rawJson, String type)
	{
		buttonSave.visible = true;
		this.buttonNew.visible = false;
		drawString = "Edit your reward parts";
		entries.clearElements();
		JsonObject convertedJson;
		try
		{
			convertedJson = json.parse(rawJson).getAsJsonObject();
		} catch(Exception e)
		{
			CCubesCore.logger.log(Level.ERROR, "Unable to parse the file ");
			return;
		}

		if(type.equalsIgnoreCase("Item"))
			this.loadRewardPart(convertedJson, ItemPart.elements);
		else if(type.equalsIgnoreCase("Block"))
			this.loadRewardPart(convertedJson, OffsetBlock.elements);
		else if(type.equalsIgnoreCase("Message"))
			this.loadRewardPart(convertedJson, MessagePart.elements);
		else if(type.equalsIgnoreCase("Command"))
			this.loadRewardPart(convertedJson, CommandPart.elements);
		else if(type.equalsIgnoreCase("Entity"))
			this.loadRewardPart(convertedJson, EntityPart.elements);
		else if(type.equalsIgnoreCase("Experience"))
			this.loadRewardPart(convertedJson, ExpirencePart.elements);
		else if(type.equalsIgnoreCase("Potion"))
			this.loadRewardPart(convertedJson, PotionPart.elements);
		else if(type.equalsIgnoreCase("Schematic"))
			this.loadRewardPart(convertedJson, new String[] { "fileName:S", "delay:I" });
		else if(type.equalsIgnoreCase("Sound"))
			this.loadRewardPart(convertedJson, SoundPart.elements);
		else if(type.equalsIgnoreCase("Chest"))
			this.loadRewardPart(convertedJson, ChestChanceItem.elements);
		else if(type.equalsIgnoreCase("Particle"))
			this.loadRewardPart(convertedJson, ParticlePart.elements);

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

	public void loadRewardPart(JsonObject convertedJson, String[] parts)
	{
		for(String s : parts)
		{
			s = s.substring(0, s.length() - 2);
			String value = "";
			if(convertedJson.has(s))
				value = convertedJson.get(s).getAsString();
			entries.addTextEntry(s, value);
		}
	}

	public void saveRewardPart(File file, JsonElement fileJson, JsonElement rewardJson)
	{
		JsonArray rewardTypeArray = rewardJson.getAsJsonObject().get(this.prevStage[2]).getAsJsonArray();
		JsonObject rewardInstance = rewardTypeArray.get(Integer.parseInt(this.prevStage[3].substring(0, this.prevStage[3].indexOf(":")))).getAsJsonObject();
		String[] rewardElements = this.getsElementsFromReward(this.prevStage[2]);
		for(int i = 0; i < this.entries.getSize(); i++)
		{
			IGuiListEntry entry = this.entries.getListEntry(i);
			if(entry instanceof CustomTextEntry)
			{
				CustomTextEntry textBox = (CustomTextEntry) entry;
				try
				{
					for(String s : rewardElements)
					{
						String s1 = s.substring(0, s.length() - 2);
						String s2 = s.substring(s.length() - 1);
						String label = textBox.getLabel().substring(0, textBox.getLabel().length() - 1);
						String value = textBox.getTextBox().getText();
						if(value.equalsIgnoreCase(""))
							continue;
						if(s1.equalsIgnoreCase(label))
						{
							if(s2.equalsIgnoreCase("I"))
								rewardInstance.addProperty(label, Integer.parseInt(value));
							if(s2.equalsIgnoreCase("S"))
								rewardInstance.addProperty(label, value);
							if(s2.equalsIgnoreCase("B"))
								rewardInstance.addProperty(label, Boolean.parseBoolean(value));
						}
					}
				} catch(Exception e)
				{
					CCubesCore.logger.log(Level.ERROR, "An error has occured while saving this reward! Please make sure all fields are correctly filled!");
				}

			}
		}
		this.prevStage[3] = this.prevStage[3].substring(0, this.prevStage[3].indexOf(":") + 1) + rewardInstance.toString();
		try
		{
			FileOutputStream outputStream = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(outputStream);
			writer.append(gson.toJson(fileJson));
			writer.close();
			outputStream.close();
		} catch(IOException e)
		{
			CCubesCore.logger.log(Level.ERROR, "Failed to create the new file for a custom reward!");
			e.printStackTrace();
			return;
		}
	}

	public String[] getsElementsFromReward(String reward)
	{
		if(reward.equalsIgnoreCase("Item"))
			return ItemPart.elements;
		else if(reward.equalsIgnoreCase("Block"))
			return OffsetBlock.elements;
		else if(reward.equalsIgnoreCase("Message"))
			return MessagePart.elements;
		else if(reward.equalsIgnoreCase("Command"))
			return CommandPart.elements;
		else if(reward.equalsIgnoreCase("Entity"))
			return EntityPart.elements;
		else if(reward.equalsIgnoreCase("Experience"))
			return ExpirencePart.elements;
		else if(reward.equalsIgnoreCase("Potion"))
			return PotionPart.elements;
		else if(reward.equalsIgnoreCase("Schematic"))
			return new String[] { "fileName:S", "delay:I" };
		else if(reward.equalsIgnoreCase("Sound"))
			return SoundPart.elements;
		else if(reward.equalsIgnoreCase("Chest"))
			return ChestChanceItem.elements;
		return new String[0];
	}

	public String loadFileContents(File file)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String result = "";
			String line = "";
			while((line = reader.readLine()) != null)
			{
				result += line;
			}
			reader.close();
			return result;
		} catch(IOException e)
		{
			return "";
		}

	}
}
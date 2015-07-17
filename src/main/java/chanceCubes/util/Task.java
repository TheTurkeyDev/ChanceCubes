package chanceCubes.util;

public abstract class Task
{
	public String name;
	public int delayLeft;
	
	public Task(String name, int delay)
	{
		this.name = name;
		this.delayLeft = delay;
	}
	
	public abstract void callback();
	
	public boolean tickTask()
	{
		this.delayLeft--;
		return this.delayLeft <= 0;
	}
}

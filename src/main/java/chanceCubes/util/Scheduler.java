package chanceCubes.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Scheduler
{
	private static Map<String, CustomEntry<Task,Integer>> tasks = new HashMap<String, CustomEntry<Task,Integer>>();
	
	public static boolean scheduleTask(String name, Integer delay, Task task)
	{
		if(tasks.containsKey(name))
			return false;
		CustomEntry<Task,Integer> entry = new CustomEntry<Task,Integer>(task, delay);
		tasks.put(name, entry);
		return true;
	}
	
	public static void tickTasks()
	{
		Iterator<String> iter = tasks.keySet().iterator();
		while(iter.hasNext())
		{
			String task = iter.next();
			CustomEntry<Task, Integer> entry = tasks.get(task);
			entry.setValue(entry.getValue()-1);
			if(entry.getValue() == 0)
			{
				iter.remove();
				entry.getKey().callback();
			}
		}
	}
}

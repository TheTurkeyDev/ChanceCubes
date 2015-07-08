package chanceCubes.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Scheduler
{
	private static Map<CustomEntry<Integer, String>, CustomEntry<Task, Integer>> tasks = new HashMap<CustomEntry<Integer, String>, CustomEntry<Task, Integer>>();

	public static boolean scheduleTask(String name, Integer delay, Task task)
	{
		if(tasks.containsKey(name))
			return false;
		CustomEntry<Integer, String> entryKey = new CustomEntry<Integer, String>(Scheduler.getNextID(), name);
		CustomEntry<Task, Integer> entryValue = new CustomEntry<Task, Integer>(task, delay);
		tasks.put(entryKey, entryValue);
		return true;
	}

	private static Integer getNextID()
	{
		int id = 1;
		List<Integer> ids = new ArrayList<Integer>();

		for(Entry<Integer, String> entry : tasks.keySet())
			ids.add(entry.getKey());

		if(!ids.contains(ids.size()))
			return ids.size();

		while(ids.contains(id))
			id++;

		return id;
	}

	public static void tickTasks()
	{
		Iterator<CustomEntry<Integer, String>> iter = tasks.keySet().iterator();
		while(iter.hasNext())
		{
			CustomEntry<Task, Integer> entryValue = tasks.get(iter.next());
			entryValue.setValue(entryValue.getValue() - 1);
			if(entryValue.getValue() == 0)
			{
				iter.remove();
				entryValue.getKey().callback();
			}
		}
	}
}

package chanceCubes.util;

import java.util.ArrayList;
import java.util.List;
import chanceCubes.rewards.IChanceCubeReward;

public class ListSort
{

	public static List<IChanceCubeReward> sortList(List<IChanceCubeReward> list)
	{
		List<IChanceCubeReward> toReturn = new ArrayList<IChanceCubeReward>();
		
		while(list.size() > 0)
		{
			IChanceCubeReward smallest = list.get(0);
			for(IChanceCubeReward type: list)
			{
				if(smallest.getLuckValue() > type.getLuckValue())
					smallest = type;
			}
			toReturn.add(toReturn.size(), smallest);
			list.remove(smallest);
		}
		return toReturn;
	}
}

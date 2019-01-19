package chanceCubes.rewards.variableTypes;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.rewards.variableParts.IPart;

public abstract class CustomVar
{
	protected List<IPart> parts = new ArrayList<>();

	public String getValue() {
		StringBuilder builder = new StringBuilder();
		for(IPart part: parts) {
			String val = part.getValue();
			System.out.print(val);
			builder.append(val);
		}
		System.out.println();
		return builder.toString();
	}

	public void addPart(IPart part)
	{
		this.parts.add(part);
	}
	
	public boolean isEmpty()
	{
		return this.parts.isEmpty();
	}

}

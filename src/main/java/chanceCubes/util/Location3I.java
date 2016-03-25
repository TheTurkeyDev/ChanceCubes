package chanceCubes.util;

public class Location3I
{
	private int x;
	private int y;
	private int z;

	public Location3I()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Location3I(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
	}

	public int getZ()
	{
		return this.z;
	}
	
	public float length()
	{
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public boolean equals(Location3I loc)
	{
		return loc.getX() == x && loc.getY() == y && loc.getZ() == z;
	}
	
	public Location3I add(int x, int y, int z)
	{
		return new Location3I(this.x + x, this.y + y, this.z + z);
	}
}

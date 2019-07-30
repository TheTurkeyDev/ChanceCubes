package chanceCubes.util;

public class Location2I
{
	private int x;
	private int y;

	public Location2I(int xloc, int yloc)
	{
		x = xloc;
		y = yloc;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public void setXY(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public Location2I add(int xa, int ya)
	{
		return new Location2I(x + xa, y + ya);
	}

	public Location2I subtract(int xa, int ya)
	{
		return new Location2I(x - xa, y - ya);
	}

	public Location2I multiply(int xa, int ya)
	{
		return new Location2I(x * xa, y * ya);
	}

	public Location2I divide(int xa, int ya)
	{
		return new Location2I(x / xa, y / ya);
	}

	public boolean equals(Location2I loc)
	{
		return x == loc.getX() && y == loc.getY();
	}

	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
}
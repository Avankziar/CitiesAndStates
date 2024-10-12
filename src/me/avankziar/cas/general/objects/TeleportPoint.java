package me.avankziar.cas.general.objects;

public class TeleportPoint
{
	private long id;
	private String teleportname;
	private String servername;
	private String worldname;
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	
	public TeleportPoint()
	{}
	
	public TeleportPoint(long id, String teleportname, String servername, String worldname, 
			double x, double y, double z, float yaw, float pitch)
	{
		setId(id);
		setTeleportname(teleportname);
		setServername(servername);
		setWorldname(worldname);
		setX(x);
		setY(y);
		setZ(z);
		setYaw(yaw);
		setPitch(pitch);
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getTeleportname()
	{
		return teleportname;
	}

	public void setTeleportname(String teleportname)
	{
		this.teleportname = teleportname;
	}

	public String getServername()
	{
		return servername;
	}

	public void setServername(String servername)
	{
		this.servername = servername;
	}

	public String getWorldname()
	{
		return worldname;
	}

	public void setWorldname(String worldname)
	{
		this.worldname = worldname;
	}

	public double getX()
	{
		return x;
	}

	public void setX(double x)
	{
		this.x = x;
	}

	public double getY()
	{
		return y;
	}

	public void setY(double y)
	{
		this.y = y;
	}

	public double getZ()
	{
		return z;
	}

	public void setZ(double z)
	{
		this.z = z;
	}

	public float getYaw()
	{
		return yaw;
	}

	public void setYaw(float yaw)
	{
		this.yaw = yaw;
	}

	public float getPitch()
	{
		return pitch;
	}

	public void setPitch(float pitch)
	{
		this.pitch = pitch;
	}
}

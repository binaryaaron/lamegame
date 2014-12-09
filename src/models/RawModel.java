package models;

import world.BoundingBox;

public class RawModel 
{
	private int vaoID;
	private int vertexCount;
	private BoundingBox box;
	/**
	 * Create a RawModel with an ID, vertex count and bounding box
	 * @param vaoIDin
	 * @param vertexCountIn
	 * @param box
	 */
	public RawModel(int vaoIDin, int vertexCountIn, BoundingBox box)
	{
		vaoID=vaoIDin;
		vertexCount=vertexCountIn;
		this.box = box;
	}

	/**
	 * Getters and setters
	 * @return
	 */
	public BoundingBox getBoundingBox()
	{
		return box;
	}

	public int getVaoID() 
	{
		return vaoID;
	}

	public void setVaoID(int vaoID) 
	{
		this.vaoID = vaoID;
	}

	public int getVertexCount() 
	{
		return vertexCount;
	}

	public void setVertexCount(int vertexCount) 
	{
		this.vertexCount = vertexCount;
	}
}

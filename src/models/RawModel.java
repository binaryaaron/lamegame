package models;

import world.BoundingBox;

public class RawModel 
{
	private int vaoID;
	private int vertexCount;
	private BoundingBox box;
	
	/**
	 * Create a RawModel with an ID, vertex count and bounding box
	 * A rawModel is essentially a blank statue compressed into data
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
	 * getter
	 * @return
	 */
	public BoundingBox getBoundingBox()
	{
		return box;
	}

	/**
	 * getter
	 * @return
	 */
	public int getVaoID() 
	{
		return vaoID;
	}

	/**
	 * setter
	 * @param vaoID
	 */
	public void setVaoID(int vaoID) 
	{
		this.vaoID = vaoID;
	}

	/**
	 * getter
	 * @return
	 */
	public int getVertexCount() 
	{
		return vertexCount;
	}

	/**
	 * getter
	 * @param vertexCount
	 */
	public void setVertexCount(int vertexCount) 
	{
		this.vertexCount = vertexCount;
	}
}

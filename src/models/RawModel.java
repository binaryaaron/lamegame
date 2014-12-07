package models;

import world.BoundingBox;

public class RawModel {
	private int vaoID;
	private int vertexCount;
	private BoundingBox box;
	public RawModel(int vaoIDin, int vertexCountIn, BoundingBox box){
		vaoID=vaoIDin;
		vertexCount=vertexCountIn;
		this.box = box;
		
	}

	public BoundingBox getBoundingBox()
	{
		return box;
	}

	public int getVaoID() {
		return vaoID;
	}

	public void setVaoID(int vaoID) {
		this.vaoID = vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}
	
	

}

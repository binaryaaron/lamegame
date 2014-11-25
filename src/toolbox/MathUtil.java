package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;

public class MathUtil {
	
	
	
public static Matrix4f createTransformationMatrix(Vector3f translation,float rx,float ry,float rz,float scale){
		

		
		
		
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		
		Matrix4f.translate(translation,matrix,matrix);
		Matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(ry),  new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rz),   new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Entity entity){
		
		Vector3f translation=entity.getPosition();
		float rx=entity.getRotX();
		float ry=entity.getRotY();
		float rz=entity.getRotZ();
		float scale=entity.getScale();
		
		
		
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		//Vector3f.add(new Vector3f(0,0,-1), translation, entity.front);
		
		
		Matrix4f.translate(translation,matrix,matrix);
		//Matrix4f.mul(matrix, entity.rotationMatrix, matrix);
		
		Matrix4f.rotate((float)Vector3f.angle(new Vector3f(0,entity.front.y,entity.front.z),new Vector3f(0,0,1) ), new Vector3f(1,0,0), matrix, matrix);
		//Matrix4f.rotate((float)Vector3f.angle(entity.front,new Vector3f(0,0,1) ), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float)(entity.getRotZ()),   entity.zAxis, matrix, matrix);
		
		
		
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera){
		
		Matrix4f matrix =new Matrix4f();
		Entity entity =camera.followObj;
		Matrix4f.rotate((float)Vector3f.angle(new Vector3f(0,entity.front.y,entity.front.z),new Vector3f(0,0,1) ), new Vector3f(1,0,0), matrix, matrix);
		
		System.out.println((float)Vector3f.angle(new Vector3f(0,entity.front.y,entity.front.z),new Vector3f(0,0,1) ));
		//Matrix4f.rotate((float)Vector3f.angle(entity.front,new Vector3f(0,0,-1) ),  new Vector3f(0,1,0), matrix, matrix);
		//Matrix4f.rotate((float)Vector3f.angle(entity.front,new Vector3f(0,0,-1) ),   new Vector3f(0,0,1), matrix, matrix);
		updateDirectionPoints(entity,matrix);
		Vector3f cameraPos=camera.getPosition();
		Vector3f negativeCameraPos= new Vector3f(cameraPos.x,cameraPos.y,cameraPos.z);
		Matrix4f.translate(negativeCameraPos, matrix, matrix);
		return matrix;
		
	}
	
	private static void updateDirectionPoints(Entity entity,Matrix4f matrix){
		
		//entity.front=new Vector3f(matrix.m20,matrix.m21,matrix.m22);
		//entity.right=new Vector3f(matrix.m00,matrix.m01,matrix.m02);
		System.out.println(entity.front);
		System.out.println(entity.right);
		System.out.println(matrix);
		
		
	}

}

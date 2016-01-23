package cn.edu.seu.iws.gspan;

import java.util.ArrayList;
import java.util.Iterator;

//finished
public class Vertex {
	public int label;
	public ArrayList<Edge> edge = new ArrayList<Edge>(); 	//和顶点相关的边
	Iterator<Edge> edge_iterator = edge.iterator();
	
	public void push(int from,int to,int elabel,int dir)		//顶点可能作为起始顶点或终止顶点
	{
		Edge new_edge = new Edge();
		new_edge.from = from;
		new_edge.to = to;
		new_edge.elabel =  elabel;
		new_edge.dir = dir;
		edge.add(new_edge);
	}
}

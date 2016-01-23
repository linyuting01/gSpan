package cn.edu.seu.iws.gspan;

import java.util.ArrayList;
import java.util.Collections;

public class History extends ArrayList<Edge>
{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Integer> edge;	
	private ArrayList<Integer> vertex;

	History()
	{
		
	}
	
	History(Graph g,PDFS p)
	{
		build(g,p);
	}
	
	public  boolean hasEdge(int id)
	{
            
            try {
                if(edge.get(id) == null || edge.get(id) == 0) return false;
            } catch (Exception e) {
                return false;
            }
		
            return true;
	}
	
	public boolean hasVertex(int id)
	{
            try {
                if(vertex.get(id) == null || vertex.get(id) == 0) return false;
            } catch (Exception e) {
                return false;
            }
            return true;
	}
	
	public void build(Graph graph,PDFS e)
	{	
		this.clear();
		edge=new ArrayList<Integer>((int) graph.edge_size());
		vertex=new ArrayList<Integer>(graph.size());
		
		//edge.clear();
		//vertex.clear();

		if(e != null)
		{
			this.add(e.edge);
			System.out.println("start---------e.edge.id:"+e.edge.id);
			//System.out.println("e.edge:"+e.edge);
			
			edge.add((int)e.edge.id);//......................
			vertex.add((int)e.edge.from);
			vertex.add((int)e.edge.to);
			
			PDFS p = e.prev;
			
			System.out.println("start--------while");
			while(p != null)
			{
				this.add(p.edge);
				edge.add((int)p.edge.id);//......................
				vertex.add(p.edge.from);
				vertex.add(p.edge.to);
				p = p.prev;
			}
			System.out.println("end--------while");
			Collections.reverse(this);
		}	
		
	}
}


//finished
/*
public class History extends ArrayList<Edge>
{
	private static final long serialVersionUID = 1L;
	
	//private ArrayList<Integer> edge = new ArrayList<Integer>();
	//int[] edge = new int[100];
	private HashMap<Long,Integer> edge = new HashMap<Long,Integer>();
	//private ArrayList<Integer> vertex = new ArrayList<Integer>();
	//int[] vertex = new int[100];
	private HashMap<Integer,Integer> vertex = new HashMap<Integer,Integer>();
	
	History()
	{
		
	}
	
	History(Graph g,PDFS p)
	{
		build(g,p);
	}
	
	public boolean hasEdge(long id)
	{
		if(edge.get(id) == null || edge.get(id) == 0) return false;
		else return true;
	}
	
	public boolean hasVertex(int id)
	{
		if(vertex.get(id) == null || vertex.get(id) == 0) return false;
		return true;
	}
	
	public void build(Graph graph,PDFS e)
	{	
		this.clear();
		//edge = new int[100];
		//vertex = new int[100];
//*************trouble*************************
		if(e != null)
		{
			this.add(e.edge);
			//edge.get(e.edge.id) = 1;//vertex[e.edge.from] = vertex[e.edge.to] = 1;
			//vertex[e.edge.to] = 1;
			//vertex.set(e.edge.to, 1);
			vertex.put(e.edge.to, 1);
			//vertex[e.edge.from] = 1;
			//vertex.set(e.edge.from, 1);
			vertex.put(e.edge.from, 1);
			//edge[e.edge.id] = 1;
			//edge.set(e.edge.id, 1);
			edge.put(e.edge.id, 1);
			
			PDFS p = e.prev;
			while(p != null)
			{
				this.add(p.edge);
				//vertex[p.edge.to] = 1;
				//vertex.set(p.edge.to, 1);
				vertex.put(p.edge.to, 1);
				//vertex[p.edge.from] = 1;
				//vertex.set(p.edge.from, 1);
				vertex.put(p.edge.from, 1);
				//edge[p.edge.id] =  1;
				//edge.set(p.edge.id, 1);
				edge.put(p.edge.id, 1);
				
				p = p.prev;
			}
			
			Collections.reverse(this);
		}	
		
	}
}
*/

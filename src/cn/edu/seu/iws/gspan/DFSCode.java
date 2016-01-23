package cn.edu.seu.iws.gspan;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

//finished  //toGraph....
public class DFSCode extends HashMap<Long,DFS>
{
	private static final long serialVersionUID = 1L;	
	private ArrayList<Integer> rmpath = new ArrayList<Integer>();
	
	public ArrayList<Integer> buidlRMPath()
	{
		rmpath.clear();
		
		int old_from = -1;
		
		for(int i=size()-1;i>=0;--i)
		{
			if((this.get((long)i).from < this.get((long)i).to) && (rmpath.isEmpty() || old_from == this.get((long)i).to))
			{
				rmpath.add(i);
				old_from = this.get((long)i).from;
			}
		}
		
		return rmpath;
	}
	
	public boolean toGraph(Graph g)
	{
		g.clear();
		
		Set<Map.Entry<Long, DFS>> set = this.entrySet();
		Iterator<Map.Entry<Long, DFS>> it = set.iterator();

		while(it.hasNext())
		{
			Map.Entry<Long, DFS> entry = (Map.Entry<Long, DFS>)it.next();
			DFS dfs = entry.getValue();
			//g = new Graph();
			if(dfs.fromlabel != -1)
			{
				//g.get(dfs.from).label = dfs.fromlabel;
				
				if(g.size() <= dfs.from || g.get(dfs.from) == null)
				{
					Vertex vv = new Vertex();
					vv.label = dfs.fromlabel;
					//g.remove(dfs.from);
					g.put((long)dfs.from, vv);
					//g.set(dfs.from, vv);
				}
				else
				{
					g.get(dfs.from).label = dfs.fromlabel;
				}				
			}
			if(dfs.tolabel != -1)
			{
				if(g.size() <= dfs.to || g.get(dfs.to) == null)
				{
					Vertex vv = new Vertex();
					vv.label = dfs.tolabel;
					g.put((long)dfs.to,vv);
				}
				else
				{
					g.get(dfs.to).label = dfs.tolabel;
				}
			}
			
			g.get((long)dfs.from).push(dfs.from,dfs.to, dfs.elabel,dfs.dir);
			
			if(g.directed == false && dfs.from != dfs.to)
			{
				g.get((long)dfs.to).push(dfs.to, dfs.from, dfs.elabel,-dfs.dir);
			}
		}
		
		g.buildEdge();
		
		return true;
	}
	
	public long nodeCount()
	{
		long nodecount = 0;
		
		Set<Map.Entry<Long, DFS>> set = this.entrySet();
		Iterator<Map.Entry<Long, DFS>> it = set.iterator();

		while(it.hasNext())
		{
			Map.Entry<Long, DFS> entry = (Map.Entry<Long, DFS>)it.next();
			DFS dfs = entry.getValue();
			
			nodecount = Math.max(nodecount, (long)(Math.max(dfs.from, dfs.to)) + 1);
		}
		
		return nodecount;
	}
	
	public void push(int from,int to,int fromlabel,int elabel,int tolabel,int dir)
	{
		DFS new_dfs = new DFS();
		new_dfs.from = from;
		new_dfs.to = to;
		new_dfs.fromlabel = fromlabel;
		new_dfs.elabel = elabel;
		new_dfs.tolabel = tolabel;
		new_dfs.dir = dir;
		
		this.put((long)this.size(), new_dfs);
	}
	
	public void pop()
	{
		this.remove((long)this.size()-1);
	}
	
	public FileWriter write(FileWriter writer) throws IOException
	{
		if(size() == 0)
		{
			return writer;
		}
		
		for(long i=0;i<size();++i)
		{
			String from = new Integer(this.get(i).from).toString();
			String to = new Integer(this.get(i).to).toString();
			String fromlabel = new Integer(this.get(i).fromlabel).toString();
			String elabel = new Integer(this.get(i).elabel).toString();
			String tolabel = new Integer(this.get(i).tolabel).toString();
			String dir = new Integer(this.get(i).dir).toString();
			
			writer.write(from + " " + to + " " + fromlabel + " " + elabel + " " + tolabel + " " + dir + System.getProperty("line.separator"));
		}
		writer.flush();
		
		return writer;
	}
}

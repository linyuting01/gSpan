package cn.edu.seu.iws.gspan;

import java.io.*;
import java.util.*;


//finished
public class Graph extends HashMap<Long,Vertex> //一个图包含了很多顶点
{
	private static final long serialVersionUID = 1L;
	private long edge_size_;
	public boolean directed;//是否是有向图
	
	//顶点迭代器
	
	Graph()
	{
		edge_size_ = 0;
		directed = false;
	}
	Graph(boolean _directed)
	{
		directed = _directed;
	}
	
	public long edge_size()
	{
		return edge_size_;
	}
	
	public long vertex_size()
	{
		return (long)this.size();
	}
	
	/******************************************/
	void buildEdge()
	{
		String buf;
		Map<String,Long> tmp = new HashMap<String,Long>();
		long id = 0;
		
		for(int from=0;from<(int)size();++from)
		{
			Iterator<Edge> it = this.get((long)from).edge.iterator();
			while(it.hasNext())
			{
				Edge e = it.next();
				
				//将int转换为string
				//String from = new Integer(from).toString();
				//String to = new Integer(e.to).toString();
				//String elabel = new Integer(e.elabel).toString();
				
				if(directed || from <= e.to)
				{	
					buf = from + " " + e.to + " " + e.elabel;
				}
				else
				{
					buf = e.to + " " + from + " " + e.elabel;
				}
				
				if(tmp.containsKey(buf))
				{
					e.id = tmp.get(buf);
				}
				else
				{
					e.id = id;
					tmp.put(buf, id);
					++id;
				}
				
			}
		}
		
		edge_size_ = id;
	}
	
	public FileWriter write(FileWriter writer) throws IOException
	{
		Set<String> tmp = new HashSet<String>();
		String buf;
		
		for(int from=0;from<size();++from)
		{
			String label = new Integer(this.get((long)from).label).toString();
			writer.write("v " + from + " " + label + System.getProperty("line.separator"));
			
			Iterator<Edge> it = this.get((long)from).edge.iterator();
			while(it.hasNext())
			{
				Edge edge = it.next();		
				//String a = new Integer(from).toString();
				//String b = new Integer(edge.to).toString();
				//String c = new Integer(edge.elabel).toString();
				//String d = new Integer(edge.dir).toString();
				
				if(edge.dir == -1)
				{
					buf = from + " " + edge.to + " " + edge.elabel + " " + edge.dir;
					tmp.add(buf);
				}
				
			}
		}
		
		Iterator<String> it2 = tmp.iterator();
		while(it2.hasNext())
		{
			//String output = it2.next();
			writer.write("e " + it2.next() + System.getProperty("line.separator"));
		}
		writer.flush();	
		return writer;
	}
	
	public FileReader read(FileReader reader) throws IOException
	{
		BufferedReader read = new BufferedReader(reader);
		ArrayList<String> result = new ArrayList<String>();
		
		String readgraph;		
		this.clear();//重新读入构造图
		while((readgraph = read.readLine()) != null)
		{
			result.clear();
			
			String[] splitread = readgraph.split(" ");
			for(String token : splitread)
			{
				result.add(token);
			}
			
			if(result.isEmpty())
			{
				
			}
			else if(result.get(0).equals("t"))
			{
				if(!isEmpty())
				{
					break;
				}
			}
			else if(result.get(0).equals("v") && result.size() >=3)
			{
				long id = Long.parseLong(result.get(1));
				//question!!!
				Vertex vex = new Vertex();
				vex.label = Integer.parseInt(result.get(2));
				this.put(id, vex);
				//this.get(id).label = Integer.parseInt(result.get(2));
			}
			else if(result.get(0).equals("e") && result.size() >=4)
			{
				int from = Integer.parseInt(result.get(1));
				int to = Integer.parseInt(result.get(2));
				int elabel = Integer.parseInt(result.get(3));
				int dir = -1;
				
				if(size()<=from || size()<=to)
				{
					System.out.println("Format Error:  define vertex lists before edges");
					System.exit(-1);
				}
				
				this.get(from).push(from, to, elabel,dir);
				if(directed == false && from != to)
				{
					this.get(to).push(to, from, elabel,-dir);
				}
			}		
		}	
		buildEdge();
		
		return reader;
	}
	
	public void check()
	{
		for(int from=0;from<size();++from)
		{
			Iterator<Edge> it = this.get(from).edge.iterator();
			while(it.hasNext())
			{
				Edge e = it.next();
				assert(e.from >= 0 && e.from < size());
				assert(e.to >= 0 && e.to < size());
			}
		}
	}
}

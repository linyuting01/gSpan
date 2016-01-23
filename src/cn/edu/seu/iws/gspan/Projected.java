package cn.edu.seu.iws.gspan;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

//finished
public class Projected extends HashMap<Long,PDFS>
{
	private static final long serialVersionUID = 1L;
	public void push(long id, Edge edge, PDFS prev)
	{
		PDFS d = new PDFS();
		d.id = id;
		d.edge = edge;
		d.prev = prev;
		this.put((long)this.size(),d);	
	}
	
	public void push1(long id,Edge edge,PDFS prev)
	{
		Set<Map.Entry<Long, PDFS>> set = this.entrySet();
		Iterator<Map.Entry<Long, PDFS>> it = set.iterator();

		while(it.hasNext())
		{
			Map.Entry<Long, PDFS> entry = (Map.Entry<Long, PDFS>)it.next();
			PDFS pdfs = entry.getValue();
			if(pdfs.edge.id == edge.id && pdfs.id == id)
			{
				return;
			}
		}
		PDFS d = new PDFS();
		d.id = id;
		d.edge = edge;
		d.prev = prev;
		this.put((long)this.size(),d);
	}
}

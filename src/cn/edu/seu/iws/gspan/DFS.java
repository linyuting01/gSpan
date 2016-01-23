package cn.edu.seu.iws.gspan;

//finished
public class DFS {
	public int from;
	public int to;
	public int fromlabel;
	public int elabel;
	public int tolabel;
	public int dir;;
	
	DFS()
	{
		from = 0;
		to = 0;
		fromlabel = 0;
		elabel = 0;
		tolabel = 0;
	}
	
	public static boolean equal(DFS d1,DFS d2)
	{
		if(d1.from == d2.from && d1.to == d2.to && d1.fromlabel == d2.fromlabel
				&& d1.elabel == d2.elabel && d1.tolabel == d2.tolabel && d1.dir == d2.dir)
			return true;
		else
			return false;
	}
}

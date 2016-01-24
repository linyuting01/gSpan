package cn.edu.seu.iws.gspan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;

public class gSpan {
    //private ArrayList<Graph> TRANS = new ArrayList<Graph>();

    private HashMap<Long, Graph> TRANS = new HashMap<Long, Graph>();
    private DFSCode DFS_CODE = new DFSCode();
    private DFSCode DFS_CODE_IS_MIN = new DFSCode();
    private Graph GRAPH_IS_MIN = new Graph();

    long ID;
    long minsup;
    long maxpat_min;
    long maxpat_max;
    boolean directed;
    boolean self_loop;
    FileWriter os;

    Map<Long, Map<Long, Long>> singleVertex = new HashMap<Long, Map<Long, Long>>();
    Map<Long, Long> singleVertexLabel = new HashMap<Long, Long>();

    boolean is_min() {
        if (DFS_CODE.size() == 1) {
            return true;
        }

        DFS_CODE.toGraph(GRAPH_IS_MIN);
        DFS_CODE_IS_MIN.clear();

        //projected 4
        HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> self_root = new HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>();
        //projected 4
        HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> root = new HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>();

        Set<Edge> edges = new HashSet<Edge>();

        int[] b2b = new int[8000];
        for (int i = 0; i < 8000; i++) {
            b2b[i] = 0;
        }

        for (long from = 0; from < GRAPH_IS_MIN.size(); ++from) {
            if (get_forward_root(GRAPH_IS_MIN, GRAPH_IS_MIN.get(from), edges)) {
                Iterator<Edge> it = edges.iterator();
                while (it.hasNext()) {
                    Edge edge = it.next();
                    if (edge.from == edge.to) {
                        Insert1_Projected4(self_root, GRAPH_IS_MIN.get((long) from).label, edge.elabel, GRAPH_IS_MIN.get((long) edge.to).label, edge.dir, 0, edge, null);
                    } else {
                        Insert1_Projected4(root, GRAPH_IS_MIN.get((long) from).label, edge.elabel, GRAPH_IS_MIN.get((long) edge.to).label, edge.dir, 0, edge, null);
                    }
                }
            }
        }

        if (self_loop == true) {
            Set<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>> pro4 = self_root.entrySet();
            Iterator<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>> it4 = pro4.iterator();
            Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> fromlabel = (Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>) it4.next();

            Set<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> pro3 = fromlabel.getValue().entrySet();
            Iterator<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> it3 = pro3.iterator();
            Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>> elabel = (Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>) it3.next();

            Set<Map.Entry<Integer, HashMap<Integer, Projected>>> pro2 = elabel.getValue().entrySet();
            Iterator<Map.Entry<Integer, HashMap<Integer, Projected>>> it2 = pro2.iterator();
            Map.Entry<Integer, HashMap<Integer, Projected>> tolabel = (Map.Entry<Integer, HashMap<Integer, Projected>>) it2.next();

            Set<Map.Entry<Integer, Projected>> pro1 = tolabel.getValue().entrySet();
            Iterator<Map.Entry<Integer, Projected>> it1 = pro1.iterator();
            Map.Entry<Integer, Projected> dir = (Map.Entry<Integer, Projected>) it1.next();

            DFS_CODE_IS_MIN.push(0, 0, fromlabel.getKey(), elabel.getKey(), tolabel.getKey(), dir.getKey());

            return (project_is_min(dir.getValue()));
        } else {
            Set<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>> pro4 = root.entrySet();
            Iterator<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>> it4 = pro4.iterator();
            Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> fromlabel = (Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>) it4.next();

            Set<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> pro3 = fromlabel.getValue().entrySet();
            Iterator<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> it3 = pro3.iterator();
            Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>> elabel = (Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>) it3.next();

            Set<Map.Entry<Integer, HashMap<Integer, Projected>>> pro2 = elabel.getValue().entrySet();
            Iterator<Map.Entry<Integer, HashMap<Integer, Projected>>> it2 = pro2.iterator();
            Map.Entry<Integer, HashMap<Integer, Projected>> tolabel = (Map.Entry<Integer, HashMap<Integer, Projected>>) it2.next();

            Set<Map.Entry<Integer, Projected>> pro1 = tolabel.getValue().entrySet();
            Iterator<Map.Entry<Integer, Projected>> it1 = pro1.iterator();
            Map.Entry<Integer, Projected> dir = (Map.Entry<Integer, Projected>) it1.next();

            DFS_CODE_IS_MIN.push(0, 0, fromlabel.getKey(), elabel.getKey(), tolabel.getKey(), dir.getKey());

            return (project_is_min(dir.getValue()));
        }
    }

    boolean project_is_min(Projected projected) {
        ArrayList<Integer> rmpath = DFS_CODE_IS_MIN.buidlRMPath();
        int minlabel = DFS_CODE_IS_MIN.get((long) 0).fromlabel;
        int maxtoc;

        if (!rmpath.isEmpty()) {
            maxtoc = DFS_CODE_IS_MIN.get((long) rmpath.get(0)).to;
        } else {
            maxtoc = DFS_CODE_IS_MIN.get((long) 0).to;
        }

        {//
            HashMap<Integer, HashMap<Integer, Projected>> root = new HashMap<Integer, HashMap<Integer, Projected>>();
            boolean flg = false;
            int newto = 0;
            Set<Edge> edges = new HashSet<Edge>();

            if (!rmpath.isEmpty()) {
                for (int i = rmpath.size() - 1; !flg && i >= 0; --i) {
                    for (long n = 0; n < projected.size(); ++n) {
                        long id = projected.get(n).id;
                        PDFS cur = projected.get(n);//question
                        History history = new History(GRAPH_IS_MIN, cur);
                        if (get_backward(GRAPH_IS_MIN, history.get(rmpath.get(i)), history.get(rmpath.get(0)), history, edges)) {
                            Iterator<Edge> it = edges.iterator();
                            while (it.hasNext()) {
                                Edge edge = it.next();
                                Insert_Projected2(root, edge.elabel, edge.dir, 0, edge, cur);
                                newto = DFS_CODE_IS_MIN.get(rmpath.get(i)).from;
                                flg = true;
                            }
                        }
                    }
                }

                if (!flg)//����һ�������������ѭ�����
                {
                    for (long n = 0; n < projected.size(); ++n) {
                        long id = projected.get(n).id;
                        PDFS cur = projected.get(n);
                        History history = new History(GRAPH_IS_MIN, cur);

                        Edge tmp = history.get(rmpath.get(0));

                        Iterator<Edge> it = GRAPH_IS_MIN.get((long) tmp.to).edge.iterator();
                        while (it.hasNext()) {
                            Edge edge = it.next();

                            if (history.hasEdge((int) edge.id)) {
                                continue;
                            }
                            if (edge.to == edge.from) {
                                Insert1_Projected2(root, edge.elabel, edge.dir, 0, edge, cur);
                                newto = DFS_CODE_IS_MIN.get(rmpath.get(0)).to;
                                flg = true;
                            }
                        }
                    }
                }

                if (flg) {
                    Set<Map.Entry<Integer, HashMap<Integer, Projected>>> pro2 = root.entrySet();
                    Iterator<Map.Entry<Integer, HashMap<Integer, Projected>>> it2 = pro2.iterator();
                    Map.Entry<Integer, HashMap<Integer, Projected>> elabel = (Map.Entry<Integer, HashMap<Integer, Projected>>) it2.next();

                    Set<Map.Entry<Integer, Projected>> pro1 = elabel.getValue().entrySet();
                    Iterator<Map.Entry<Integer, Projected>> it1 = pro1.iterator();
                    Map.Entry<Integer, Projected> dir = (Map.Entry<Integer, Projected>) it1.next();

                    DFS_CODE_IS_MIN.push(maxtoc, newto, -1, elabel.getKey(), -1, dir.getKey());
                    if (!DFS.equal(DFS_CODE.get(DFS_CODE_IS_MIN.size() - 1), DFS_CODE_IS_MIN.get(DFS_CODE_IS_MIN.size() - 1))) {
                        return false;
                    }
                    return project_is_min(dir.getValue());
                } else {
                    for (long n = 0; n < projected.size(); ++n) {
                        long id = projected.get(n).id;
                        PDFS cur = projected.get(n);
                        History history = new History(GRAPH_IS_MIN, cur);

                        Edge tmp = history.get(0);

                        Iterator<Edge> it = GRAPH_IS_MIN.get((long) tmp.to).edge.iterator();
                        while (it.hasNext()) {
                            Edge edge = it.next();
                            if (history.hasEdge((int) edge.id)) {
                                continue;
                            }
                            if (edge.to == edge.from) {
                                Insert1_Projected2(root, edge.elabel, edge.dir, 0, edge, cur);
                                newto = DFS_CODE_IS_MIN.get(0).to;
                                flg = true;
                            }
                        }
                    }

                    if (flg) {
                        Set<Map.Entry<Integer, HashMap<Integer, Projected>>> pro2 = root.entrySet();
                        Iterator<Map.Entry<Integer, HashMap<Integer, Projected>>> it2 = pro2.iterator();
                        Map.Entry<Integer, HashMap<Integer, Projected>> elabel = (Map.Entry<Integer, HashMap<Integer, Projected>>) it2.next();

                        Set<Map.Entry<Integer, Projected>> pro1 = elabel.getValue().entrySet();
                        Iterator<Map.Entry<Integer, Projected>> it1 = pro1.iterator();
                        Map.Entry<Integer, Projected> dir = (Map.Entry<Integer, Projected>) it1.next();

                        DFS_CODE_IS_MIN.push(maxtoc, newto, -1, elabel.getKey(), -1, dir.getKey());
                        if (!DFS.equal(DFS_CODE.get(DFS_CODE_IS_MIN.size() - 1), DFS_CODE_IS_MIN.get(DFS_CODE_IS_MIN.size() - 1))) {
                            return false;
                        }
                        return project_is_min(dir.getValue());
                    }
                }
            }
        }

        {
            boolean flg = false;
            int newfrom = 0;
            HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>> root = new HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>();
            Set<Edge> edges = new HashSet<Edge>();

            for (long n = 0; n < projected.size(); ++n) {
                long id = projected.get(n).id;
                PDFS cur = projected.get(n);
                History history = new History(GRAPH_IS_MIN, cur);
                if (!rmpath.isEmpty()) {
                    if (get_forward_pure(GRAPH_IS_MIN, history.get(rmpath.get(0)), minlabel, history, edges)) {
                        flg = true;
                        newfrom = maxtoc;
                        Iterator<Edge> it = edges.iterator();
                        while (it.hasNext()) {
                            Edge edge = it.next();
                            Insert_Projected3(root, edge.elabel, GRAPH_IS_MIN.get((long) edge.to).label, edge.dir, 0, edge, cur);

                        }
                    }
                } else if (get_forward_pure(GRAPH_IS_MIN, history.get(0), minlabel, history, edges)) {
                    flg = true;
                    newfrom = maxtoc;
                    Iterator<Edge> it = edges.iterator();
                    while (it.hasNext()) {
                        Edge edge = it.next();
                        Insert_Projected3(root, edge.elabel, GRAPH_IS_MIN.get((long) edge.to).label, edge.dir, 0, edge, cur);
                    }
                }
            }

            for (int i = 0; !flg && i < rmpath.size(); ++i) {
                for (long n = 0; n < projected.size(); ++n) {
                    long id = projected.get(n).id;
                    PDFS cur = projected.get(0);
                    History history = new History(GRAPH_IS_MIN, cur);

                    if (get_forward_rmpath(GRAPH_IS_MIN, history.get(rmpath.get(i)), minlabel, history, edges)) {
                        flg = true;
                        newfrom = DFS_CODE_IS_MIN.get(rmpath.get(i)).from;

                        Iterator<Edge> it = edges.iterator();
                        while (it.hasNext()) {
                            Edge edge = it.next();
                            Insert_Projected3(root, edge.elabel, GRAPH_IS_MIN.get(edge.to).label, edge.dir, 0, edge, cur);
                        }
                    }
                }
            }

            if (flg) {
                Set<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> pro3 = root.entrySet();
                Iterator<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> it3 = pro3.iterator();
                Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>> elabel = (Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>) it3.next();

                Set<Map.Entry<Integer, HashMap<Integer, Projected>>> pro2 = elabel.getValue().entrySet();
                Iterator<Map.Entry<Integer, HashMap<Integer, Projected>>> it2 = pro2.iterator();
                Map.Entry<Integer, HashMap<Integer, Projected>> tolabel = (Map.Entry<Integer, HashMap<Integer, Projected>>) it2.next();

                Set<Map.Entry<Integer, Projected>> pro1 = tolabel.getValue().entrySet();
                Iterator<Map.Entry<Integer, Projected>> it1 = pro1.iterator();
                Map.Entry<Integer, Projected> dir = (Map.Entry<Integer, Projected>) it1.next();

                DFS_CODE_IS_MIN.push(newfrom, maxtoc + 1, -1, elabel.getKey(), tolabel.getKey(), dir.getKey());
                if (!DFS.equal(DFS_CODE.get((long) DFS_CODE_IS_MIN.size() - 1), DFS_CODE_IS_MIN.get((long) DFS_CODE_IS_MIN.size() - 1))) {
                    return false;
                }
                return project_is_min(dir.getValue());
            }
        }

        return true;
    }

    //�����pattern��ÿ��graph�еĳ��ֵĴ���
    Map<Long, Long> support_counts(Projected projected) {
        Map<Long, Long> counts = new HashMap<Long, Long>();

        Set<Map.Entry<Long, PDFS>> set = projected.entrySet();
        Iterator<Map.Entry<Long, PDFS>> cur = set.iterator();
        //Iterator<PDFS> cur = projected.iterator();
        while (cur.hasNext()) {
            Map.Entry<Long, PDFS> entry = (Map.Entry<Long, PDFS>) cur.next();
            PDFS pdfs = entry.getValue();
            counts.put(pdfs.id, counts.get(pdfs.id) + 1);
        }

        return counts;
    }

    long support(Projected projected) {
        long oid = 0xffffffff;
        long size = 0;

        //Iterator<PDFS> cur = projected.iterator();
        Set<Map.Entry<Long, PDFS>> set = projected.entrySet();
        Iterator<Map.Entry<Long, PDFS>> cur = set.iterator();
        while (cur.hasNext()) {
            //PDFS pdfs = cur.next();
            Map.Entry<Long, PDFS> entry = (Map.Entry<Long, PDFS>) cur.next();
            PDFS pdfs = entry.getValue();

            if (oid != pdfs.id) {
                ++size;
            }
            oid = pdfs.id;
        }

        return size;
    }

    //finished
    void project(Projected projected) throws IOException {
        if (this.DFS_CODE.size() < 5) {
            long sup = support(projected);
            if (sup < minsup) {
                return;
            }

            if (is_min() == false) {
                return;
            }

            report(projected, sup);

            if (maxpat_max > maxpat_min && DFS_CODE.nodeCount() > maxpat_max) {
                return;
            }

            ArrayList<Integer> rmpath = DFS_CODE.buidlRMPath();
            int minlabel = DFS_CODE.get((long) 0).fromlabel;
            int maxtoc;
            if (!rmpath.isEmpty()) {
                maxtoc = DFS_CODE.get((long) rmpath.get(0)).to;
            } else {
                maxtoc = DFS_CODE.get((long) 0).to;
            }

            //projected 4
            HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> new_fwd_root = new HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>();
            //projected 3
            HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>> new_bck_root = new HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>();
            Set<Edge> edges = new HashSet<Edge>();

            for (long n = 0; n < projected.size(); ++n) {
                long id = projected.get(n).id;
                PDFS cur = projected.get(n);	//rerererererererer
                History history = new History(TRANS.get(id), cur);

                if (!rmpath.isEmpty()) {
                    for (int i = rmpath.size() - 1; i >= 0; --i) {
                        if (get_backward(TRANS.get(id), history.get(rmpath.get(i)), history.get(rmpath.get(0)), history, edges)) {
                            Iterator<Edge> it = edges.iterator();
                            while (it.hasNext()) {
                                Edge edge_it = it.next();
                                //cacacacacaccacacacacacacacacacac
                                //new_bck_root.get(DFS_CODE.get(rmpath.get(i)).from).get(edge_it.elabel).get(edge_it.dir).push(id, edge_it, cur);
                                Insert_Projected3(new_bck_root, DFS_CODE.get((long) rmpath.get(i)).from, edge_it.elabel, edge_it.dir, id, edge_it, cur);
                            }
                        }
                    }

                    Edge tmp = history.get(rmpath.get(0));

                    Iterator<Edge> it = TRANS.get(id).get((long) tmp.to).edge.iterator();
                    while (it.hasNext()) {
                        Edge edge = it.next();
                        if (history.hasEdge((int) edge.id)) {
                            continue;
                        }
                        if (edge.to == edge.from) {
                            Insert_Projected3(new_bck_root, DFS_CODE.get((long) rmpath.get(0)).to, edge.elabel, edge.dir, id, edge, cur);
                        }
                    }
                } else {
                    Edge tmp = history.get(0);
                    Iterator<Edge> it = TRANS.get((long) id).get((long) tmp.to).edge.iterator();
                    while (it.hasNext()) {
                        Edge edge = it.next();
                        if (history.hasEdge((int) edge.id)) {
                            continue;
                        }
                        if (edge.to == edge.from) {
                            Insert_Projected3(new_bck_root, DFS_CODE.get(0).to, edge.elabel, edge.dir, id, edge, cur);
                        }
                    }
                }

                if (!rmpath.isEmpty()) {
                    if (get_forward_pure(TRANS.get(id), history.get(rmpath.get(0)), minlabel, history, edges)) {
                        Iterator<Edge> it = edges.iterator();
                        while (it.hasNext()) {
                            Edge edge = it.next();
                            Insert_Projected4(new_fwd_root, maxtoc, edge.elabel, TRANS.get((long) id).get((long) edge.to).label, edge.dir, id, edge, cur);
                        }
                    }
                } else if (get_forward_pure(TRANS.get((long) id), history.get(0), minlabel, history, edges)) {
                    Iterator<Edge> it = edges.iterator();
                    while (it.hasNext()) {
                        Edge edge = it.next();
                        Insert_Projected4(new_fwd_root, maxtoc, edge.elabel, TRANS.get((long) id).get((long) edge.to).label, edge.dir, id, edge, cur);
                    }
                }

                for (int i = 0; i < rmpath.size(); ++i) {
                    if (get_forward_rmpath(TRANS.get(id), history.get(rmpath.get(i)), minlabel, history, edges)) {
                        Iterator<Edge> it = edges.iterator();
                        while (it.hasNext()) {
                            Edge edge = it.next();
                            Insert_Projected4(new_fwd_root, DFS_CODE.get((long) rmpath.get(i)).from, edge.elabel, TRANS.get(id).get((long) edge.to).label, edge.dir, id, edge, cur);
                        }
                    }
                }
            }

            Set<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> to = new_bck_root.entrySet();
            Iterator<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> to_it = to.iterator();
            while (to_it.hasNext()) {
                Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>> to_to = (Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>) to_it.next();

                Set<Map.Entry<Integer, HashMap<Integer, Projected>>> elabel = to_to.getValue().entrySet();
                Iterator<Map.Entry<Integer, HashMap<Integer, Projected>>> elabel_it = elabel.iterator();
                while (elabel_it.hasNext()) {
                    Map.Entry<Integer, HashMap<Integer, Projected>> elabel_elabel = (Map.Entry<Integer, HashMap<Integer, Projected>>) elabel_it.next();

                    Set<Map.Entry<Integer, Projected>> dir = elabel_elabel.getValue().entrySet();
                    Iterator<Map.Entry<Integer, Projected>> dir_it = dir.iterator();
                    while (dir_it.hasNext()) {
                        Map.Entry<Integer, Projected> dir_dir = (Map.Entry<Integer, Projected>) dir_it.next();

                        DFS_CODE.push(maxtoc, to_to.getKey(), -1, elabel_elabel.getKey(), -1, dir_dir.getKey());
                        project(dir_dir.getValue());
                        DFS_CODE.pop();
                    }
                }

            }
            //forward
            Set<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>> from = new_fwd_root.entrySet();
            Iterator<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>> from_it = from.iterator();
            while (from_it.hasNext()) {
                Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> from_from = (Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>) from_it.next();

                Set<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> elabel = from_from.getValue().entrySet();
                Iterator<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> elabel_it = elabel.iterator();
                while (elabel_it.hasNext()) {
                    Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>> elabel_elabel = (Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>) elabel_it.next();

                    Set<Map.Entry<Integer, HashMap<Integer, Projected>>> tolabel = elabel_elabel.getValue().entrySet();
                    Iterator<Map.Entry<Integer, HashMap<Integer, Projected>>> tolabel_it = tolabel.iterator();
                    while (tolabel_it.hasNext()) {
                        Map.Entry<Integer, HashMap<Integer, Projected>> tolabel_tolabel = (Map.Entry<Integer, HashMap<Integer, Projected>>) tolabel_it.next();

                        Set<Map.Entry<Integer, Projected>> dir = tolabel_tolabel.getValue().entrySet();
                        Iterator<Map.Entry<Integer, Projected>> dir_it = dir.iterator();
                        while (dir_it.hasNext()) {
                            Map.Entry<Integer, Projected> dir_dir = (Map.Entry<Integer, Projected>) dir_it.next();

                            DFS_CODE.push(from_from.getKey(), maxtoc + 1, -1, elabel_elabel.getKey(), tolabel_tolabel.getKey(), dir_dir.getKey());
                            project(dir_dir.getValue());
                            DFS_CODE.pop();
                        }
                    }
                }
            }

        }
    }

    void report(Projected projected, long sup) throws IOException {
        if (maxpat_max > maxpat_min && DFS_CODE.nodeCount() > maxpat_max) {
            return;
        }

        if (maxpat_min > 0 && DFS_CODE.nodeCount() < maxpat_min) {
            return;
        }

        Graph g = new Graph(directed);
        DFS_CODE.toGraph(g);

        String id = Long.toString(ID);
        String supstring = Long.toString(sup);

        os.write("t # " + id + " * " + supstring + "\r\n");
        os.flush();
        g.write(os);

        ++ID;

    }

    void report_single(Graph g, Map<Long, Long> ncount) throws IOException {
        long sup = 0;

        for (Map.Entry entry : ncount.entrySet()) {
            sup += (Integer) entry.getValue();
        }

        if (maxpat_max > maxpat_min && g.size() > maxpat_max) {
            return;
        }
        if (maxpat_min > 0 && g.size() < maxpat_min) {
            return;
        }

        String supstring = new Long(sup).toString();
        os.write("s #  " + supstring + "\r\n");
        os.flush();
        g.write(os);

    }

    FileReader read(FileReader a) throws IOException {
        BufferedReader read = new BufferedReader(a);
        ArrayList<String> result = new ArrayList<String>();
        String readgraph;

        long graph_number = 0;
        //Graph g = new Graph(directed);

        while (true) {
            Graph g = new Graph(directed);

            while ((readgraph = read.readLine()) != null) {
                result.clear();
                String[] splitread = readgraph.split(" ");
                for (String aa : splitread) {
                    result.add(aa);
                }

                if (result.isEmpty()) {

                } else if (result.get(0).equals("t")) {
                    if (!g.isEmpty()) {
                        break;
                    }
                } else if (result.get(0).equals("v") && result.size() >= 3) {
                    long id = Long.parseLong(result.get(1));
                    //question!!!
                    Vertex vex = new Vertex();
                    vex.label = Integer.parseInt(result.get(2));
                    g.put(id, vex);
                    //this.get(id).label = Integer.parseInt(result.get(2));
                } else if (result.get(0).equals("e") && result.size() >= 4) {
                    int from = Integer.parseInt(result.get(1));
                    //long from = Long.parseLong(result.get(1));
                    int to = Integer.parseInt(result.get(2));
                    int elabel = Integer.parseInt(result.get(3));
                    int dir = -1;

                    if (g.size() <= from || g.size() <= to) {
                        System.out.println("Format Error:  define vertex lists before edges");
                        return null;
                    }

                    g.get((long) from).push(from, to, elabel, dir);
                    //Vertex vvv = g.get(from);
                    //vvv.push(from, to, elabel, dir);
                    //g.put(from, vvv);

                    if (directed == false && from != to) {
                        g.get((long) to).push(to, from, elabel, -dir);
                    }
                }
            }
            g.buildEdge();
            if (g.isEmpty()) {
                break;
            }
            TRANS.put((long) TRANS.size(), g);
            ++graph_number;
            System.out.println("graph_number:" + graph_number);
        }
        /*while(true)
		{
			g.read(a);
			if(g.isEmpty())	break;
			TRANS.add(g);
			++graph_number;
			System.out.println("graph_number:" + graph_number);
		}*/
        if (graph_number == 1) {
            minsup = 1;
        }

        return a;
    }

    void run_intern() throws IOException {
        if (maxpat_min <= 1) {
            for (long id = 0; id < TRANS.size(); ++id) {
                for (long nid = 0; nid < TRANS.get(id).size(); ++nid) {
                    /*if(singleVertex.get(id).get(TRANS.get(id).get(nid).label) == 0)
					{
						singleVertexLabel.put(TRANS.get(id).get(nid).label, singleVertexLabel.get(TRANS.get(id).get(nid).label) + 1);
					}
					
					singleVertex.get(id).put(TRANS.get(id).get(nid).label, singleVertex.get(id).get(TRANS.get(id).get(nid).label) + 1);*/
                    if (singleVertex.get(id) == null) {
                        Map<Long, Long> map = new HashMap<Long, Long>();
                        map.put((long) TRANS.get(id).get(nid).label, (long) 0);
                        singleVertex.put(id, map);

                        if (singleVertexLabel.get((long) TRANS.get(id).get(nid).label) == null || singleVertexLabel.get((long) TRANS.get(id).get(nid).label) == 0) {
                            singleVertexLabel.put((long) TRANS.get(id).get(nid).label, (long) 1);
                        } else {
                            long temp = singleVertexLabel.get((long) TRANS.get(id).get(nid).label);
                            singleVertexLabel.put((long) TRANS.get(id).get(nid).label, temp + 1);
                        }

                    } else if (singleVertex.get(id).get((long) TRANS.get(id).get(nid).label) == null) {
                        singleVertex.get(id).put((long) TRANS.get(id).get(nid).label, (long) 0);

                        if (singleVertexLabel.get((long) TRANS.get(id).get(nid).label) == null || singleVertexLabel.get((long) TRANS.get(id).get(nid).label) == 0) {
                            singleVertexLabel.put((long) TRANS.get(id).get(nid).label, (long) 1);
                        } else {
                            long temp = singleVertexLabel.get((long) TRANS.get(id).get(nid).label);
                            singleVertexLabel.put((long) TRANS.get(id).get(nid).label, temp + 1);
                        }
                    } else if (singleVertex.get(id).get((long) TRANS.get(id).get(nid).label) == 0) {
                        if (singleVertexLabel.get((long) TRANS.get(id).get(nid).label) == null || singleVertexLabel.get((long) TRANS.get(id).get(nid).label) == 0) {
                            singleVertexLabel.put((long) TRANS.get(id).get(nid).label, (long) 1);
                        } else {
                            long temp = singleVertexLabel.get((long) TRANS.get(id).get(nid).label);
                            singleVertexLabel.put((long) TRANS.get(id).get(nid).label, temp + 1);
                        }
                    }

                    long tt = singleVertex.get((long) id).get((long) TRANS.get(id).get(nid).label);
                    singleVertex.get(id).put((long) TRANS.get(id).get(nid).label, tt + 1);
                }
            }

            /**
             * *************************************
             */
            Set<Map.Entry<Long, Long>> sing = singleVertexLabel.entrySet();
            Iterator<Map.Entry<Long, Long>> it = sing.iterator();
            while (it.hasNext()) {
                Map.Entry<Long, Long> it_sin = (Map.Entry<Long, Long>) it.next();

                if (it_sin.getValue() < minsup) {
                    continue;
                }

                long frequent_label = it_sin.getKey();

                Graph g = new Graph(directed);
                Vertex vex = new Vertex();
                vex.label = (int) frequent_label;
                g.put((long) 0, vex);

                if (maxpat_max > maxpat_min && g.size() > maxpat_max) {
                    return;
                }
                if (maxpat_min > 0 && g.size() < maxpat_min) {
                    return;
                }

                String o1 = "s # " + "\r" + it_sin.getValue() + "\r\n";
                os.write(o1);
                os.flush();
                g.write(os);
            }

        }

        Set<Edge> edges = new HashSet<Edge>();
        //projected 4
        HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> root = new HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>();
        //projected 4
        HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> self_root = new HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>();

        for (long id = 0; id < TRANS.size(); ++id) {
            System.out.println("graph id: " + id);
            Graph g = TRANS.get(id);

            for (long from = 0; from < g.size(); ++from) {
                if (get_forward_root(g, g.get(from), edges)) {
                    Iterator<Edge> it = edges.iterator();
                    while (it.hasNext()) {
                        Edge edge = it.next();
                        if (edge.from == edge.to) {
                            Insert_Projected4(self_root, g.get((long) from).label, edge.elabel, g.get((long) edge.to).label, edge.dir, id, edge, null);
                        } else {
                            Insert_Projected4(root, g.get((long) from).label, edge.elabel, g.get((long) edge.to).label, edge.dir, id, edge, null);
                        }
                    }
                }
            }
        }

        self_loop = true;
        Set<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>> pro4 = self_root.entrySet();
        Iterator<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>> it4 = pro4.iterator();
        while (it4.hasNext()) {
            Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> fromlabel = (Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>) it4.next();

            Set<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> pro3 = fromlabel.getValue().entrySet();
            Iterator<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> it3 = pro3.iterator();
            while (it3.hasNext()) {
                Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>> elabel = (Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>) it3.next();

                Set<Map.Entry<Integer, HashMap<Integer, Projected>>> pro2 = elabel.getValue().entrySet();
                Iterator<Map.Entry<Integer, HashMap<Integer, Projected>>> it2 = pro2.iterator();
                while (it2.hasNext()) {
                    Map.Entry<Integer, HashMap<Integer, Projected>> tolabel = (Map.Entry<Integer, HashMap<Integer, Projected>>) it2.next();

                    Set<Map.Entry<Integer, Projected>> pro1 = tolabel.getValue().entrySet();
                    Iterator<Map.Entry<Integer, Projected>> it1 = pro1.iterator();
                    while (it1.hasNext()) {
                        Map.Entry<Integer, Projected> dir = (Map.Entry<Integer, Projected>) it1.next();

                        DFS_CODE.push(0, 0, fromlabel.getKey(), elabel.getKey(), tolabel.getKey(), dir.getKey());
                        project(dir.getValue());
                        DFS_CODE.pop();
                    }
                }
            }
        }

        self_loop = false;
        Set<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>> pro41 = root.entrySet();
        Iterator<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>> it41 = pro41.iterator();
        while (it41.hasNext()) {
            Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> fromlabel = (Map.Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>>) it41.next();

            Set<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> pro3 = fromlabel.getValue().entrySet();
            Iterator<Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> it3 = pro3.iterator();
            while (it3.hasNext()) {
                Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>> elabel = (Map.Entry<Integer, HashMap<Integer, HashMap<Integer, Projected>>>) it3.next();

                Set<Map.Entry<Integer, HashMap<Integer, Projected>>> pro2 = elabel.getValue().entrySet();
                Iterator<Map.Entry<Integer, HashMap<Integer, Projected>>> it2 = pro2.iterator();
                while (it2.hasNext()) {
                    Map.Entry<Integer, HashMap<Integer, Projected>> tolabel = (Map.Entry<Integer, HashMap<Integer, Projected>>) it2.next();

                    Set<Map.Entry<Integer, Projected>> pro1 = tolabel.getValue().entrySet();
                    Iterator<Map.Entry<Integer, Projected>> it1 = pro1.iterator();
                    while (it1.hasNext()) {
                        Map.Entry<Integer, Projected> dir = (Map.Entry<Integer, Projected>) it1.next();

                        DFS_CODE.push(0, 1, fromlabel.getKey(), elabel.getKey(), tolabel.getKey(), dir.getKey());
                        project(dir.getValue());
                        DFS_CODE.pop();
                    }
                }
            }
        }
        int a = 0;
        System.out.print(a);
    }

    gSpan() {

    }

    public void run(FileReader is, FileWriter _os, long _minsup, long _maxpat_max, long _maxpat_min, boolean _directed) throws IOException {
        os = _os;
        ID = 0;
        minsup = _minsup;
        maxpat_min = _maxpat_min;
        maxpat_max = _maxpat_max;
        directed = _directed;
        self_loop = false;

        read(is);

        run_intern();
    }

    public boolean get_backward(Graph graph, Edge e1, Edge e2, History history, Set<Edge> result) {
        result.clear();

        assert (e1.from >= 0 && e1.from < graph.size());
        assert (e1.to >= 0 && e1.to < graph.size());
        assert (e2.to >= 0 && e2.to < graph.size());

        Iterator<Edge> it = graph.get((long) e2.to).edge.iterator();
        while (it.hasNext()) {
            Edge edge = it.next();

            if (history.hasEdge((int) edge.id)) {
                continue;
            }

            if ((edge.to == e1.from)
                    && ((e1.elabel < edge.elabel)
                    || (e1.elabel == edge.elabel)
                    && (graph.get((long) e1.to).label < graph.get((long) e2.to).label) || (e1.elabel == edge.elabel)
                    && (graph.get((long) e1.to).label == graph.get((long) e2.to).label) && e1.dir <= edge.dir)) {
                result.add(edge);
            }
        }

        return !result.isEmpty();
    }

    boolean get_forward_rmpath(Graph graph, Edge e, int minlabel, History history, Set<Edge> result) {
        result.clear();
        assert (e.to >= 0 && e.to < graph.size());
        assert (e.from >= 0 && e.from < graph.size());
        int tolabel = graph.get((long) e.to).label;

        Iterator<Edge> it = graph.get((long) e.from).edge.iterator();
        while (it.hasNext()) {
            Edge edge = it.next();

            int flag = 0;
            int tolabel2 = graph.get((long) edge.to).label;
            if (e.to == edge.to || minlabel > tolabel2 || history.hasVertex(edge.to)) {
                continue;
            }
            if (e.elabel < edge.elabel || (e.elabel == edge.elabel && tolabel < tolabel2) || (e.elabel == edge.elabel && tolabel == tolabel2 && e.dir <= edge.dir)) {
                result.add(edge);
            }
        }

        return !result.isEmpty();
    }

    boolean get_forward_pure(Graph graph, Edge e, int minlabel, History history, Set<Edge> result) {
        result.clear();
        assert (e.to >= 0 && e.to < graph.size());

        Iterator<Edge> it = graph.get((long) e.to).edge.iterator();
        while (it.hasNext()) {
            Edge edge = it.next();

            int flag = 0;
            assert (edge.to >= 0 && edge.to < graph.size());

            if (minlabel > graph.get((long) edge.to).label || history.hasVertex(edge.to)) {
                continue;
            }
            result.add(edge);
        }
        return !result.isEmpty();
    }

    boolean get_forward_root(Graph g, Vertex v, Set<Edge> result) {
        result.clear();

        Iterator<Edge> it = v.edge.iterator();
        while (it.hasNext()) {
            Edge edge = it.next();
            assert (edge.to >= 0 && edge.to < g.size());
            if (v.label <= g.get((long) edge.to).label) {
                result.add(edge);
            }
        }

        return !result.isEmpty();
    }

    void Insert_Projected2(HashMap<Integer, HashMap<Integer, Projected>> map,
            int a, int b,
            int id, Edge it, PDFS cur) 
    {
        if (map.get(a) == null) {
            Projected projected = new Projected();
            projected.push(id, it, cur);

            HashMap<Integer, Projected> map1 = new HashMap<Integer, Projected>();
            map1.put(b, projected);
            map.put(a, map1);
        } else if (map.get(a).get(b) == null) {
            Projected projected = new Projected();
            projected.push(id, it, cur);

            map.get(a).put(b, projected);
        } else {
            map.get(a).get(b).push(id, it, cur);
        }
    }

    void Insert1_Projected2(HashMap<Integer, HashMap<Integer, Projected>> map,
            int a, int b,
            int id, Edge it, PDFS cur) 
    {
        if (map.get(a) == null) {
            Projected projected = new Projected();
            projected.push1(id, it, cur);

            HashMap<Integer, Projected> map1 = new HashMap<Integer, Projected>();
            map1.put(b, projected);
            map.put(a, map1);
        } else if (map.get(a).get(b) == null) {
            Projected projected = new Projected();
            projected.push1(id, it, cur);

            map.get(a).put(b, projected);
        } else {
            map.get(a).get(b).push1(id, it, cur);
        }
    }

    void Insert_Projected3(HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>> map, int a, int b, int c, long id, Edge it, PDFS cur) {
        if (map.get(a) == null) {
            Projected projected = new Projected();
            projected.push(id, it, cur);

            HashMap<Integer, HashMap<Integer, Projected>> map1 = new HashMap<Integer, HashMap<Integer, Projected>>();
            HashMap<Integer, Projected> map2 = new HashMap<Integer, Projected>();
            map2.put(c, projected);
            map1.put(b, map2);
            map.put(a, map1);
        } else if (map.get(a).get(b) == null) {
            Projected projected = new Projected();
            projected.push(id, it, cur);

            HashMap<Integer, Projected> map2 = new HashMap<Integer, Projected>();
            map2.put(c, projected);
            map.get(a).put(b, map2);
        } else if (map.get(a).get(b).get(c) == null) {
            Projected projected = new Projected();
            projected.push(id, it, cur);

            map.get(a).get(b).put(c, projected);
        } else {
            map.get(a).get(b).get(c).push(id, it, cur);
        }
    }

    void Insert1_Projected3(HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>> map,
            int a, int b, int c,
            int id, Edge it, PDFS cur) 
    {
        if (map.get(a) == null) {
            Projected projected = new Projected();
            projected.push1(id, it, cur);

            HashMap<Integer, HashMap<Integer, Projected>> map1 = new HashMap<Integer, HashMap<Integer, Projected>>();
            HashMap<Integer, Projected> map2 = new HashMap<Integer, Projected>();
            map2.put(c, projected);
            map1.put(b, map2);
            map.put(a, map1);
        } else if (map.get(a).get(b) == null) {
            Projected projected = new Projected();
            projected.push1(id, it, cur);

            HashMap<Integer, Projected> map2 = new HashMap<Integer, Projected>();
            map2.put(c, projected);
            map.get(a).put(b, map2);
        } else if (map.get(a).get(b).get(c) == null) {
            Projected projected = new Projected();
            projected.push1(id, it, cur);

            map.get(a).get(b).put(c, projected);
        } else {
            map.get(a).get(b).get(c).push1(id, it, cur);
        }
    }

    void Insert_Projected4(HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> map, int a, int b, int c, int d, long id, Edge it, PDFS cur) {
        if (map.get(a) == null) {
            Projected projected = new Projected();
            projected.push(id, it, cur);

            HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>> map1 = new HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>();
            HashMap<Integer, HashMap<Integer, Projected>> map2 = new HashMap<Integer, HashMap<Integer, Projected>>();
            HashMap<Integer, Projected> map3 = new HashMap<Integer, Projected>();

            map3.put(d, projected);
            map2.put(c, map3);
            map1.put(b, map2);
            map.put(a, map1);
        } else if (map.get(a).get(b) == null) {
            Projected projected = new Projected();
            projected.push(id, it, cur);

            HashMap<Integer, HashMap<Integer, Projected>> map2 = new HashMap<Integer, HashMap<Integer, Projected>>();
            HashMap<Integer, Projected> map3 = new HashMap<Integer, Projected>();

            map3.put(d, projected);
            map2.put(c, map3);
            map.get(a).put(b, map2);
        } else if (map.get(a).get(b).get(c) == null) {
            Projected projected = new Projected();
            projected.push(id, it, cur);

            HashMap<Integer, Projected> map3 = new HashMap<Integer, Projected>();
            map3.put(d, projected);
            map.get(a).get(b).put(c, map3);
        } else if (map.get(a).get(b).get(c).get(d) == null) {
            Projected projected = new Projected();
            projected.push(id, it, cur);

            map.get(a).get(b).get(c).put(d, projected);
        } else {
            map.get(a).get(b).get(c).get(d).push(id, it, cur);
        }
    }

    void Insert1_Projected4(HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>> map, int a, int b, int c, int d, int id, Edge it, PDFS cur) {
        if (map.get(a) == null) {
            Projected projected = new Projected();
            projected.push1(id, it, cur);

            HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>> map1 = new HashMap<Integer, HashMap<Integer, HashMap<Integer, Projected>>>();
            HashMap<Integer, HashMap<Integer, Projected>> map2 = new HashMap<Integer, HashMap<Integer, Projected>>();
            HashMap<Integer, Projected> map3 = new HashMap<Integer, Projected>();

            map3.put(d, projected);
            map2.put(c, map3);
            map1.put(b, map2);
            map.put(a, map1);
        } else if (map.get(a).get(b) == null) {
            Projected projected = new Projected();
            projected.push1(id, it, cur);

            HashMap<Integer, HashMap<Integer, Projected>> map2 = new HashMap<Integer, HashMap<Integer, Projected>>();
            HashMap<Integer, Projected> map3 = new HashMap<Integer, Projected>();

            map3.put(d, projected);
            map2.put(c, map3);
            map.get(a).put(b, map2);
        } else if (map.get(a).get(b).get(c) == null) {
            Projected projected = new Projected();
            projected.push1(id, it, cur);

            HashMap<Integer, Projected> map3 = new HashMap<Integer, Projected>();
            map3.put(d, projected);
            map.get(a).get(b).put(c, map3);
        } else if (map.get(a).get(b).get(c).get(d) == null) {
            Projected projected = new Projected();
            projected.push1(id, it, cur);

            map.get(a).get(b).get(c).put(d, projected);
        } else {
            map.get(a).get(b).get(c).get(d).push1(id, it, cur);
        }
    }
}

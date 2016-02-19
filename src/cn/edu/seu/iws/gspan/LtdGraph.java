/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.seu.iws.gspan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

/**
 *
 * @author Limited
 */
public class LtdGraph {

    public LtdGraphName Name;                   /// t # 0 * 1
    public ArrayList<LtdGraphVertex> Vertexs;   /// v 0 1
    public ArrayList<LtdGraphEdge> Edges;       /// e 0 1 3 

    private boolean IsContains(ArrayList<LtdGraphEdgeMatrix> EdgeMatrixs, LtdGraphEdge Edge) {
        for (LtdGraphEdgeMatrix EdgeMatrix : EdgeMatrixs) {
            if (EdgeMatrix.equals(Edge))
                return true;
        }
        return false;
    }

    /// t # 0 * 1
    public static class LtdGraphName {

        public int id;

        public LtdGraphName() {
            id = 0;
        }

        public LtdGraphName(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "t # " + id + " * 1" + "\r\n";
        }

        public String toShortString() {
            return "t # " + id + "\r\n";
        }

    }

    /// v 0 1
    public static class LtdGraphVertex {

        public int id;
        public int label;

        public LtdGraphVertex() {
            id = 0;
            label = 0;
        }

        public LtdGraphVertex(int id, int label) {
            this.id = id;
            this.label = label;
        }

        @Override
        public String toString() {
            return "v " + id + " " + label + "\r\n";
        }

    }

    /// e 0 1 3 
    public static class LtdGraphEdge {

        public int from;
        public int to;
        public int label;

        public LtdGraphEdge() {
            from = 0;
            to = 0;
            label = 0;
        }

        public LtdGraphEdge(int from, int to, int label) {
            this.from = from;
            this.to = to;
            this.label = label;
        }

        /// e 0 1 3 
        @Override
        public String toString() {
            return "e " + from + " " + to + " " + label + "\r\n";
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 53 * hash + this.from;
            hash = 53 * hash + this.to;
            hash = 53 * hash + this.label;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final LtdGraphEdge other = (LtdGraphEdge) obj;
            if (this.from != other.from) {
                return false;
            }
            if (this.to != other.to) {
                return false;
            }
            if (this.label != other.label) {
                return false;
            }
            return true;
        }

    }

    public LtdGraph() {
        Name = new LtdGraphName();
        Vertexs = new ArrayList<LtdGraphVertex>();
        Edges = new ArrayList<LtdGraphEdge>();
    }

    public LtdGraph(LtdGraph graph) {
        this.Name = graph.Name;
        this.Vertexs = graph.Vertexs;
        this.Edges = graph.Edges;
    }

    public LtdGraph(LtdGraphName Name, ArrayList<LtdGraphVertex> Vertexs, ArrayList<LtdGraphEdge> Edges) {
        this.Name = Name;
        this.Vertexs = Vertexs;
        this.Edges = Edges;
    }

    public LtdGraph(ChildGraph graph) {
        this.Name = new LtdGraphName(Integer.parseInt(String.valueOf(graph.t.charAt(4))));

        Vertexs = new ArrayList<LtdGraphVertex>();
        Edges = new ArrayList<LtdGraphEdge>();

        for (String Point : graph.Points) {
            int id = Integer.parseInt(String.valueOf(Point.charAt(2)));
            int label = Integer.parseInt(String.valueOf(Point.charAt(4)));
            this.Vertexs.add(new LtdGraphVertex(id, label));
        }

        for (String Vector : graph.Vectors) {
            int from = Integer.parseInt(String.valueOf(Vector.charAt(2)));
            int to = Integer.parseInt(String.valueOf(Vector.charAt(4)));
            int label = Integer.parseInt(String.valueOf(Vector.charAt(6)));
            this.Edges.add(new LtdGraphEdge(from, to, label));
        }
    }

    public void show() {
        System.out.print("Name: " + Name.toString());
        System.out.println("Vertexs: ");
        for (LtdGraphVertex Vertex : Vertexs) {
            System.out.print(Vertex.toString());
        }
        System.out.println("Edges: ");
        for (LtdGraphEdge Edge : Edges) {
            System.out.print(Edge.toString());
        }
    }

    public void sort() {
        ///Vertexs.sort(null);
        ///Edges.sort(null);
    }

    public void clean() {
        this.sort();
        for (LtdGraphEdge Edge : Edges) {
            if (Edge.from > Edge.to) {
                int temp = Edge.from;
                Edge.from = Edge.to;
                Edge.to = temp;
            }
        }
        this.sort();
        ArrayList<LtdGraphEdge> temp = new ArrayList<LtdGraphEdge>();
        for (LtdGraphEdge Edge : Edges) {
            if (!temp.contains(Edge)) {
                temp.add(Edge);
            }
        }
        this.Edges = temp;
    }

    public ArrayList<LtdGraphEdgeMatrix> getEdgeMatrix() {
        ArrayList<LtdGraphEdgeMatrix> EdgeMatrixs = new ArrayList<LtdGraphEdgeMatrix>();

        /// Do what need to do here
        this.clean();
        this.sort();

        for (LtdGraphEdge Edge : Edges) {
            if (!IsContains(EdgeMatrixs, Edge)) {
                EdgeMatrixs.add(new LtdGraphEdgeMatrix(Edge.from, Edge.to, Edge.label));
            } else {
                for (LtdGraphEdgeMatrix edgeMatrix : EdgeMatrixs) {
                    if (edgeMatrix.equals(Edge)) {
                        edgeMatrix.labels.add(Edge.label);
                    }
                }
            }
        }

        return EdgeMatrixs;
    }

    public void showEdgeMatrix() {
        ArrayList<LtdGraphEdgeMatrix> EdgeMatrixs = this.getEdgeMatrix();
        System.out.println("Edge Matrix: ");
        for (LtdGraphEdgeMatrix EdgeMatrix : EdgeMatrixs) {
            System.out.println(EdgeMatrix.toString());
        }
    }

    public static class LtdGraphEdgeMatrix {

        public int from;
        public int to;
        public ArrayList<Integer> labels;

        public LtdGraphEdgeMatrix() {
            from = 0;
            to = 0;
            labels = new ArrayList<Integer>();
        }

        public LtdGraphEdgeMatrix(int from, int to, int label) {
            this.from = from;
            this.to = to;
            this.labels = new ArrayList<Integer>();
            this.labels.add(label);
        }

        @Override
        public String toString() {
            String s = "From: " + from + ", To: " + to + ", Labels: ";
            for (int label : labels) {
                s += label + " ";
            }
            return s;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 89 * hash + this.from;
            hash = 89 * hash + Objects.hashCode(this.to);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj.getClass() == LtdGraphEdge.class) {
                final LtdGraphEdge other = (LtdGraphEdge) obj;
                return (this.from == other.from) && (this.to == other.to);

            } else {
                if (this == obj) {
                    return true;
                }
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                final LtdGraphEdgeMatrix other = (LtdGraphEdgeMatrix) obj;
                if (this.from != other.from) {
                    return false;
                }
                if (this.to != other.to) {
                    return false;
                }
                return true;
            }
        }
    }

//    public static class LtdShowGraphEdgeMatrix_To {
//
//        public int id;
//        public int label;
//
//        public LtdShowGraphEdgeMatrix_To() {
//            id = 0;
//            label = 0;
//        }
//
//        public LtdShowGraphEdgeMatrix_To(int id, int label) {
//            this.id = id;
//            this.label = label;
//        }
//
//        @Override
//        public String toString() {
//            return id + " " + label;
//        }
//
//    }
}

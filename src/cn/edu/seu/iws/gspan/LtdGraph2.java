/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.seu.iws.gspan;

import java.util.ArrayList;

/**
 *
 * @author Limited
 */
public class LtdGraph2 extends LtdGraph {

    public ArrayList<LtdGraph2Vertex> Vertexs;
    public ArrayList<LtdGraph2Edge> Edges;
    public ArrayList<String> Relationships;
    public ArrayList<String> Names;
    public ArrayList<String> Pictures;

    public LtdGraph2Vertex GetVertexByName(String name) {
        for (int i = 0; i < Names.size(); i++) {
            if (Names.get(i).equals(name)) {
                return this.Vertexs.get(i);
            }

        }
        return new LtdGraph2Vertex();
    }

    private int GetRelationshipByName(String relation) {
        for (int i = 0; i < Relationships.size(); i++) {
            if (Relationships.get(i).equals(relation)) {
                return i;
            }

        }
        return -1;
    }

    private boolean CheckContainEdge(int from, int to, int relation) {
        for (LtdGraph2Edge Edge : Edges) {
            if (((Edge.from == from) && (Edge.to == to) && (Edge.label == relation)) || ((Edge.from == to) && (Edge.to == from) && (Edge.label == relation))) {
                return true;
            }
        }
        return false;
    }

    public static class LtdGraph2Vertex extends LtdGraphVertex {

        public LtdGraph2Vertex() {
            super();
        }

        public LtdGraph2Vertex(int id, int label) {
            super(id, label);
        }

        public LtdGraph2Vertex(String vertex) {
            String[] result = vertex.split("\\s+");
            this.id = Integer.parseInt(result[1]);
            this.label = Integer.parseInt(result[2]);
        }

    }

    public static class LtdGraph2Edge extends LtdGraphEdge {

        public LtdGraph2Edge() {
            super();
        }

        public LtdGraph2Edge(int from, int to, int label) {
            super(from, to, label);
        }

        public LtdGraph2Edge(String edge) {
            String[] result = edge.split("\\s+");
            this.from = Integer.parseInt(result[1]);
            this.to = Integer.parseInt(result[2]);
            this.label = Integer.parseInt(result[3]);
        }
    }

    public LtdGraph2() {
        this.Name = new LtdGraphName();

        this.Relationships = new ArrayList<String>();
        this.Names = new ArrayList<String>();
        this.Pictures = new ArrayList<String>();

        Vertexs = new ArrayList<LtdGraph2Vertex>();
        Edges = new ArrayList<LtdGraph2Edge>();

    }

    public LtdGraph2(int name) {
        this.Name = new LtdGraphName(name);

        this.Relationships = new ArrayList<String>();
        this.Names = new ArrayList<String>();
        this.Pictures = new ArrayList<String>();

        Vertexs = new ArrayList<LtdGraph2Vertex>();
        Edges = new ArrayList<LtdGraph2Edge>();

    }

    public LtdGraph2(ArrayList<LtdGraph2> Graphs) {
        /// Init
        this.Name = new LtdGraphName(0);

        this.Relationships = new ArrayList<String>();
        this.Names = new ArrayList<String>();
        this.Pictures = new ArrayList<String>();

        Vertexs = new ArrayList<LtdGraph2Vertex>();
        Edges = new ArrayList<LtdGraph2Edge>();

        /// Join graphs
        for (LtdGraph2 Graph : Graphs) {
            for (String Relationship : Graph.Relationships) {
                if (!this.Relationships.contains(Relationship)) {
                    this.Relationships.add(Relationship);
                }
            }

            for (String Name : Graph.Names) {
                if (!this.Names.contains(Name)) {
                    this.Vertexs.add(new LtdGraph2Vertex(this.Names.size(), 0));
                    this.Names.add(Name);
                }
            }

            for (String Picture : Graph.Pictures) {
                if (!this.Pictures.contains(Picture)) {
                    this.Pictures.add(Picture);
                }
            }

            for (int i = 0; i < Graph.Vertexs.size(); i++) {
                
                if (this.Names.contains(Graph.Names.get(Graph.Vertexs.get(i).id))) {
                    GetVertexByName(Graph.Names.get(Graph.Vertexs.get(i).id)).label = Graph.Vertexs.get(i).label;
                }

            }

            for (int i = 0; i < Graph.Edges.size(); i++) {
                String from = Graph.Names.get(Graph.Edges.get(i).from);
                String to = Graph.Names.get(Graph.Edges.get(i).to);
                String relation = Graph.Relationships.get(Graph.Edges.get(i).label - 1);

                if (!CheckContainEdge(GetVertexByName(from).id, GetVertexByName(to).id, GetRelationshipByName(relation))) {
                    if (GetVertexByName(from).id < GetVertexByName(to).id)
                        this.Edges.add(new LtdGraph2Edge(GetVertexByName(from).id, GetVertexByName(to).id, GetRelationshipByName(relation)));
                    else
                        this.Edges.add(new LtdGraph2Edge(GetVertexByName(to).id, GetVertexByName(from).id, GetRelationshipByName(relation)));
                }

            }
        }

    }

    @Override
    public void showEdgeMatrix() {
        super.showEdgeMatrix(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<LtdGraphEdgeMatrix> getEdgeMatrix() {
        return super.getEdgeMatrix(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cleanshort(int top) {
        super.cleanshort(top); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clean() {
        super.clean(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sort() {
        super.sort(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void show() {
        super.show(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean IsContains(ArrayList<LtdGraphEdgeMatrix> EdgeMatrixs, LtdGraphEdge Edge) {
        return super.IsContains(EdgeMatrixs, Edge); //To change body of generated methods, choose Tools | Templates.

    }

}

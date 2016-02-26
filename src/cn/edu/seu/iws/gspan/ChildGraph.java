/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.seu.iws.gspan;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Limited
 */
public class ChildGraph {

    public String t;
    public ArrayList<String> Points;
    public ArrayList<String> Vectors;

    public ChildGraph() {
        t = "";
        Points = new ArrayList<String>();
        Vectors = new ArrayList<String>();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.t);
        hash = 97 * hash + Objects.hashCode(this.Points);
        hash = 97 * hash + Objects.hashCode(this.Vectors);
        return hash;
    }

    public void sort() {
        Points.sort(null);
        Vectors.sort(null);
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
        final ChildGraph other = (ChildGraph) obj;
        this.sort();
        other.sort();

        if (!IsPointsEquals(other.Points)) {
            return false;
        }
        if (!IsVectorsEquals(other.Vectors)) {
            return false;
        }
        return true;
    }

    private boolean IsPointsEquals(ArrayList<String> Points) {
        if (this.Points.size() != Points.size()) {
            return false;
        }

        int check = 0;

        for (String Point : Points) {
            String PointClone1 = "";
            String PointClone2 = "";

            String[] Split;
            Split = Point.split(" ");
            Split[2] = "1";
            for (String string : Split) {
                PointClone1 += string + " ";
            }
            Split[2] = "2";
            for (String string : Split) {
                PointClone2 += string + " ";
            }

            PointClone1 = PointClone1.substring(0, PointClone1.length() - 1);
            PointClone2 = PointClone2.substring(0, PointClone2.length() - 1);

            if (this.Points.contains(PointClone1) || this.Points.contains(PointClone2)) {
                check++;
            }
        }

        return this.Points.size() == check;
    }

    private boolean IsVectorsEquals(ArrayList<String> Vectors) {
        if (this.Vectors.size() != Vectors.size()) {
            return false;
        }

        int check = 0;

        for (String Vector : Vectors) {
            String VectorClone = "";

            String[] Split;
            Split = Vector.split(" ");
            String temp = Split[1];
            Split[1] = Split[2];
            Split[2] = temp;
            for (String string : Split) {
                VectorClone += string + " ";
            }
            VectorClone = VectorClone.substring(0, VectorClone.length() - 1);

            if (this.Vectors.contains(Vector) || this.Vectors.contains(VectorClone)) {
                check++;
            }
        }

        return this.Vectors.size() == check;
    }

}

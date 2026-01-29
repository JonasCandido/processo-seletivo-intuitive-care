package com.jonascandido.ansdespesas.model;

public record Trimestre(int ano, int trimestre)
        implements Comparable<Trimestre> {

    @Override
    public int compareTo(Trimestre o) {
        int c = Integer.compare(this.ano, o.ano);
        return c != 0 ? c : Integer.compare(this.trimestre, o.trimestre);
    }
}

package it.unipr.aotlab.dmat.core.loaders;

import it.unipr.aotlab.dmat.core.matrixPiece.Triplet;

import java.util.Iterator;

public interface Loader {
    Iterator<Triplet> getIterator();
    int getNofCols();
    int getNofRows();
    int getNofElements();
}

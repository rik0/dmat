package it.unipr.aotlab.dmat.core.util;

import it.unipr.aotlab.dmat.core.generated.Rectangle;
import it.unipr.aotlab.dmat.core.generated.Rectangle.RectangleBody;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrices.Matrix;

import java.util.LinkedList;

public class Splitter {
    public static class ChunkInfo {
        public Chunk first;
        public Chunk second;
        public Rectangle.RectangleBody intersect;

        public ChunkInfo(Chunk first, Chunk second, RectangleBody intersect) {
            this.first = first;
            this.second = second;
            this.intersect = intersect;
        }
    }

    private static boolean intersect(Chunk first, Chunk second) {
        return !(first.getStartRow() >= second.getEndRow()
                || second.getStartCol() >= first.getEndCol()
                || second.getStartRow() >= first.getEndRow()
                || first.getStartCol() >= second.getEndCol());
    }

    private static ChunkInfo intersection(Chunk first, Chunk second) {
        Rectangle.RectangleBody intersection = Rectangle.RectangleBody
                .newBuilder()
                .setStartRow(Math.max(first.getStartRow(), second.getStartRow()))
                .setStartCol(Math.max(first.getStartCol(), second.getStartCol()))
                .setEndRow(Math.min(first.getEndRow(), second.getEndRow()))
                .setEndCol(Math.min(first.getEndCol(), second.getEndCol()))
                .build();

        return new ChunkInfo(first, second, intersection);
    }

    public static void addIntersection(LinkedList<ChunkInfo> list, Chunk first, Chunk second) {
        if (intersect(first, second)) {
            list.add(intersection(first, second));
        }
    }

    public static Iterable<ChunkInfo> splitMatrix(Matrix firstMatrix,
            Matrix secondMatrix) {
        LinkedList<Splitter.ChunkInfo> workZones = new LinkedList<Splitter.ChunkInfo>();

        for (Chunk firstChunk : firstMatrix.getChunks()) {
            for (Chunk secondChunk : secondMatrix.getChunks()) {
                addIntersection(workZones, firstChunk, secondChunk);
            }
        }

        return workZones;
    }
}

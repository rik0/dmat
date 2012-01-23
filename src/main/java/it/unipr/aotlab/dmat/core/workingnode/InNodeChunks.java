package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.formats.ChunkAccessor;
import it.unipr.aotlab.dmat.core.formats.Formats;
import it.unipr.aotlab.dmat.core.generated.ChunkDescription;
import it.unipr.aotlab.dmat.core.generated.ChunkDescription.MatricesOnTheWire;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces;
import it.unipr.aotlab.dmat.core.semirings.SemiRing;
import it.unipr.aotlab.dmat.core.semirings.SemiRings;

public class InNodeChunks {
    static InNodeChunk<?> build(WorkingNode wn, Chunk chunk) {
        InNodeChunk<?> buildingChunk = null;

        switch (chunk.getElementType()) {
        case INT32:
        case UINT32: {
            InNodeChunkInt32 typedBuildingChunk = new InNodeChunkInt32(chunk);
            InNodeChunks.<Integer> build(wn, chunk, typedBuildingChunk);
            buildingChunk = typedBuildingChunk;
        }
            break;

        case BOOL: {
            InNodeChunkBoolean typedBuildingChunk = new InNodeChunkBoolean(chunk);
            InNodeChunks.<Boolean> build(wn, chunk, typedBuildingChunk);
            buildingChunk = typedBuildingChunk;
        }
            break;

        case INT8:
        case UINT8:

        case INT16:
        case UINT16:

        case INT64:
        case UINT64:

        case FLOAT32:
        case FLOAT64:

        }
        if (buildingChunk == null) {
            throw new DMatInternalError(
                    "Unknown (unimplemented?) Element Type. Chunk: " + chunk);
        }

        return buildingChunk;
    }

    private static <E> void build(WorkingNode wn, Chunk chunk, InNodeChunk<E> inNodeChunk) {
        inNodeChunk.hostNode = wn;
        inNodeChunk.chunk = chunk;
        inNodeChunk.accessor = (ChunkAccessor<E>) Formats.build(chunk);
        setSemiring(chunk, inNodeChunk);
        setMatrixPiece(chunk, inNodeChunk);
    }

    private static <E> void setMatrixPiece(Chunk chunk,
            InNodeChunk<E> inNodeChunk) {
        MatricesOnTheWire matricesOnTheWireTag = chunk.getMatricesOnTheWire();
        if (matricesOnTheWireTag
                .equals(ChunkDescription.MatricesOnTheWire.DEFAULTMATRICESONTHEWIRE))
            inNodeChunk.matrixPiece = MatrixPieces.defaultMatrixPiece(chunk
                    .getElementType());
        else
            inNodeChunk.matrixPiece = MatrixPieces
                    .matrixPiece(matricesOnTheWireTag);
    }

    private static <E> void setSemiring(Chunk chunk, InNodeChunk<E> inNodeChunk) {
        ChunkDescription.SemiRing semiringTag = chunk.getSemiring();
        if (semiringTag.equals(ChunkDescription.SemiRing.DEFAULTSEMIRING))
            inNodeChunk.semiring = (SemiRing<E>) SemiRings
                    .defaultSemiring(chunk.getElementType());
        else
            inNodeChunk.semiring = (SemiRing<E>) SemiRings
                    .semiring(semiringTag);

    }
}

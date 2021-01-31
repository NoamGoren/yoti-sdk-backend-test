package com.noam.goren.technical.model;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Hoover {

    public static final String NORTH = "N";
    public static final String SOUTH = "S";
    public static final String WEST = "W";
    public static final String EAST = "E";


    @Id
    @GeneratedValue
    private long id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn
    @NotNull
    private Point startPoint;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn
    private Point endPoint;
    @NotNull
    private String instructions;
    private int cleanedPatchesCount;

    public Hoover() {}

    public Hoover(final Point startPoint, final String instructions) {
        this.startPoint = startPoint;
        this.endPoint = null;
        this.instructions = instructions;
        this.cleanedPatchesCount = 0;
    }
    public Point getStartPosition() {
        return startPoint;
    }

    public void setStartPosition(final Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getEndPosition() {
        return endPoint;
    }

    public void setEndPosition(final Point endPosition) {
        this.endPoint = endPosition;
    }

    public String getInstructions() {
        return instructions;
    }

    public int getCleanedPatchesCount() {
        return cleanedPatchesCount;
    }

    public void setCleanedPatchesCount(final int cleanedPatchesCount) {
        this.cleanedPatchesCount = cleanedPatchesCount;
    }

    public void increasePatchCount() {
        this.cleanedPatchesCount++;
    }

}

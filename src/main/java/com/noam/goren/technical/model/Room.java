package com.noam.goren.technical.model;

import org.hibernate.annotations.WhereJoinTable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

import java.util.List;

@Entity
public class Room {

    @Id
    @GeneratedValue
    private long id;
    @NotNull
    private Integer width;
    @NotNull
    private Integer height;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @WhereJoinTable(clause = "isDirtPatch=true")
    @NotNull
    private List<Point> dirtPatchesList;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn
    @NotNull
    private Hoover hoover;

    public Room() {}

    public Room(final int width, final int height, final List<Point> dirtPatchesList, final Hoover hoover) {
        this.width = width;
        this.height = height;
        this.dirtPatchesList = new ArrayList<>(dirtPatchesList);
        this.hoover = hoover;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    public List<Point> getDirtPatchesList() {
        return new ArrayList<>(dirtPatchesList);
    }

    public void setDirtPatchesList(final List<Point> dirtPatchesList) {
        this.dirtPatchesList = new ArrayList<>(dirtPatchesList);
    }

    public Hoover getHoover() {
        return hoover;
    }

    public void setHoover(final Hoover hoover) {
        this.hoover = hoover;
    }
}

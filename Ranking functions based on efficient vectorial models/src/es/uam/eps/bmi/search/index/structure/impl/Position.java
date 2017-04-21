/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.index.structure.impl;

import java.io.Serializable;

/**
 *
 * @author Javi
 */
public class Position {
    private final Long position;
    private final Integer size;

    public Position(Long position, Integer size) {
        this.position = position;
        this.size = size;
    }

    public Integer getSize() {
        return size;
    }
    
    public Long getPos() {
        return position;
    }
    
}
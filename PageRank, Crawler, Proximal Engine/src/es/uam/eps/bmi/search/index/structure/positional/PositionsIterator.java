/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.index.structure.positional;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Javi
 */
public class PositionsIterator implements Iterator<Integer> {
    List<Integer> positions;
    int up;

    public PositionsIterator(List<Integer> positions) {
        this.positions = positions;
    }

   public boolean hasNext() {
        return up < positions.size();
    }

    public Integer next() {
        return positions.get(up++);
    }
    
    public boolean contains(Integer o){
        return positions.contains(o);
       
    }
    
}

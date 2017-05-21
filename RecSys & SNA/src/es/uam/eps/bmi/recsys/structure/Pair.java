/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.structure;

import java.util.Objects;

/**
 *
 * @author Javi
 */
public class Pair<L,R>{
    private L l;
    private R r;
    public Pair(L l, R r){
        this.l = l;
        this.r = r;
    }
    public L getL(){ return l; }
    public R getR(){ return r; }
    public void setL(L l){ this.l = l; }
    public void setR(R r){ this.r = r; }
     
    @Override 
    public boolean equals(Object other){
        if(other == null) return false;
        if(other == this) return true;
        if (!(other instanceof Pair))return false;
        Pair<L,R> pairAux = (Pair<L,R>)other;
        return (pairAux.l.equals(this.l) && pairAux.r.equals(this.r)) || (pairAux.r.equals(this.l) && pairAux.l.equals(this.r));
    }

    
    @Override
    public String toString(){
        return l+" "+r;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        int hash1 = 13 * hash + Objects.hashCode(this.l);
        int hash2 = 13 * hash + Objects.hashCode(this.r);
        return hash1*hash2;
    }

}

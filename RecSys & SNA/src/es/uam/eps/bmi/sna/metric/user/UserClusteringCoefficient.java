/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.sna.metric.user;

import es.uam.eps.bmi.sna.metric.LocalMetric;
import es.uam.eps.bmi.sna.ranking.Ranking;
import es.uam.eps.bmi.sna.ranking.RankingImpl;
import es.uam.eps.bmi.sna.structure.UndirectedSocialNetwork;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author root
 */
public class UserClusteringCoefficient<U> implements LocalMetric{

    HashMap<Object, Double> map;
    int k;
    
    public UserClusteringCoefficient(int topK){
        k = topK;
    }

    public UserClusteringCoefficient() {
        System.out.print("?");
        System.out.flush();
    }

    @Override
    public Ranking compute(UndirectedSocialNetwork network) {

        HashMap<Object, List<Object>> usermaps = new HashMap<>();
        this.map = new HashMap<>();
        Ranking r = new RankingImpl(k);
        double contador;
        
        for (Object user : network.getUsers()){
            usermaps.put(user, new ArrayList<>());
            usermaps.get(user).addAll(network.getContacts(user));
        }
                
        for (Object u : network.getUsers()){
            Set <U> contacts = new HashSet<>(network.getContacts(u));
            contador = 0;
            for (U v : contacts){
                Set<U> set = new HashSet<>(network.getContacts(v));
                set.retainAll(contacts);
                contador += set.size();
            }
            
            map.put(u, (contador)/(contacts.size()*(contacts.size() - 1)));
        }
        
        for (Object key : map.keySet()){
            r.add((Comparable)key, map.get(key));
        }
        return r;
    }
    
    @Override
    public double compute(UndirectedSocialNetwork network, Comparable element) {
        return this.map.get(element);
    }
    
    public String toString(){
        return "User Clustering Coefficient";
    }
    
}

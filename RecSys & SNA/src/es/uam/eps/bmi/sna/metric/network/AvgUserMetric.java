/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.sna.metric.network;

import es.uam.eps.bmi.sna.metric.GlobalMetric;
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
public class AvgUserMetric<U> implements GlobalMetric {

    HashMap<Object, Double> map;
    int k;
    LocalMetric g;
    public AvgUserMetric(LocalMetric g){
        this.g = g;
    }

    @Override
    public double compute(UndirectedSocialNetwork network) {

        HashMap<Object, List<Object>> usermaps = new HashMap<>();
        this.map = new HashMap<>();
        double contador;
        double ret = 0;
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
            if (contacts.size() == 1)               
                ret += (contador)/(contacts.size());
            else
                ret += (contador)/(contacts.size()*(contacts.size() - 1));
        }
        double size = network.getUsers().size();
        return (ret/size);
    }
    
    public String toString(){
        return "Avg User Clustering Coefficient";
    }
}
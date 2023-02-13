package com.kulala.staticsfunc.dataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
/**
 * Created by Administrator on 2017/8/12.
 */

public class ListMe<E,T>{
    private List<E> data1;
    private List<T> data2;

    public ListMe(){
        data1 = new ArrayList<E>();
        data2 = new ArrayList<T>();
    }
    public int size() {
        return data1.size();
    }
    public boolean containsL(Object o) {
        return data1.contains(o);
    }
    public boolean containsR(Object o) {
        return data2.contains(o);
    }
    public void add(E l,T r) {
        data1.add(l);
        data2.add(r);
    }
    public void add(int index, E l,T r) {
        data1.add(index,l);
        data2.add(index,r);
    }
    public boolean remove(int index) {
        if(data1.size()>index){
            data1.remove(index);
            data2.remove(index);
            return true;
        }
        return false;
    }
    public boolean removeFromL(Object o) {
        if(data1.contains(o)){
            int pos = data1.indexOf(o);
            data1.remove(pos);
            data2.remove(pos);
            return true;
        }
        return false;
    }
    public boolean removeFromR(Object o) {
        if(data2.contains(o)){
            int pos = data2.indexOf(o);
            data1.remove(pos);
            data2.remove(pos);
            return true;
        }
        return false;
    }
    public void clear() {
        data1 = new ArrayList<E>();
        data2 = new ArrayList<T>();
    }
    public Object getL(int index) {
        return data1.get(index);
    }
    public Object getR(int index) {
        return data2.get(index);
    }
}

package com.datasift.client.historics;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class HistoricsQueryList extends BaseDataSiftResult implements Iterable<HistoricsQuery> {
    protected final List<HistoricsQuery> data = new ArrayList<HistoricsQuery>();
    private final int count;

    public HistoricsQueryList() {
        count = 0;
    }

    @JsonCreator
    public HistoricsQueryList(@JsonProperty("data") List<HistoricsQuery> data, @JsonProperty("count") int count) {
        if (data != null) {
            this.data.addAll(data);
        }
        this.count = count;
    }

    /**
     * The number of items returned
     */
    public int getCount() {
        return count;
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public boolean contains(Object o) {
        return data.contains(o);
    }

    public Iterator<HistoricsQuery> iterator() {
        return data.iterator();
    }

    public Object[] toArray() {
        return data.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return data.toArray(a);
    }

    public boolean add(HistoricsQuery historicsQuery) {
        return data.add(historicsQuery);
    }

    public boolean remove(Object o) {
        return data.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return data.containsAll(c);
    }

    public boolean addAll(Collection<? extends HistoricsQuery> c) {
        return data.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends HistoricsQuery> c) {
        return data.addAll(index, c);
    }

    public boolean removeAll(Collection<?> c) {
        return data.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return data.retainAll(c);
    }

    public void clear() {
        data.clear();
    }

    public HistoricsQuery get(int index) {
        return data.get(index);
    }

    public HistoricsQuery set(int index, HistoricsQuery element) {
        return data.set(index, element);
    }

    public void add(int index, HistoricsQuery element) {
        data.add(index, element);
    }

    public HistoricsQuery remove(int index) {
        return data.remove(index);
    }

    public int indexOf(Object o) {
        return data.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return data.lastIndexOf(o);
    }

    public ListIterator<HistoricsQuery> listIterator() {
        return data.listIterator();
    }

    public ListIterator<HistoricsQuery> listIterator(int index) {
        return data.listIterator(index);
    }

    public List<HistoricsQuery> subList(int fromIndex, int toIndex) {
        return data.subList(fromIndex, toIndex);
    }
}

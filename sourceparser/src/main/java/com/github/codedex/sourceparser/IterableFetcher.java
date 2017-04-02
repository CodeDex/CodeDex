package com.github.codedex.sourceparser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IPat on 02.04.2017.
 */

/**
 * The IterableFetcher lets you iterate over its fetched results.
 * @param <I> stands for input  - Represents the input that gets fetched
 * @param <O> stands for output - Represents the unique entities that get iterated over
 * @param <B> stands for buffer - Represents the shared object
 * @see IterableFetcher#prepareSharedObject(I)
 */
public abstract class IterableFetcher<I, O, B> implements Iterable<O> {

    private final List<O> entityArray;
    private int size = -1;

    public IterableFetcher(I input) {
        this.entityArray = fetch(input);
    }

    /**
     * Prepare an object that size and fetch can share.
     * @param input the object the shared object gets fetched from
     * @return the shared object
     */
    protected abstract B prepareSharedObject(I input);

    /**
     * Initializes ArrayList in List<O> fetch(I)
     * @see IterableFetcher#fetch(Object)
     *
     * @param sharedObject prepareSharedObject(I)
     * @return the lists size
     */
    protected abstract int size(B sharedObject);

    public int size() {
        return this.size;
    }

    protected abstract void fetch(List<O> buffer, B sharedObject);

    private List<O> fetch(I input) {
        final B sharedObject = prepareSharedObject(input);
        size = size(sharedObject);
        List<O> buffer = new ArrayList<>(size);
        fetch(buffer, sharedObject);
        return buffer;
    }

    /*
    private O getEntity(int index) {
        return entityArray.get(index);
    }

    protected I getInput() {
        return this.input;
    }

    public static class OutputIterator<I, O> implements Iterator<O> {

        private final IterableFetcher<I, O> fetcher;
        private int iterator;

        public OutputIterator(IterableFetcher<I, O> fetcher) {
            this(fetcher, 0);
        }

        private OutputIterator(IterableFetcher<I, O> fetcher, int iterator) {
            this.fetcher = fetcher;
            this.iterator = iterator;
        }

        public boolean hasNext() {
            return iterator < fetcher.entityArray.size();
        }

        public O next() {
            return fetcher.getEntity(iterator++);
        }
    }
    */

    public Iterator<O> iterator() {
        return entityArray.iterator();
    }
}

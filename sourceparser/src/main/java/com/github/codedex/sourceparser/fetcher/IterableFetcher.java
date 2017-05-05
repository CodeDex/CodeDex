package com.github.codedex.sourceparser.fetcher;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Patrick "IPat" Hein
 *
 * The IterableFetcher lets you iterate over its fetched results.
 * @param <I> stands for input  - Represents the input that gets fetched
 * @param <O> stands for output - Represents the unique entities that get iterated over
 * @see IterableFetcher#fetch(List, I)
 */
public abstract class IterableFetcher<I, O> implements Iterable<O> {

    private final List<O> entities = new LinkedList<>();
    private final List<O> immEntities = Collections.unmodifiableList(this.entities);
    private final I input;

    public IterableFetcher(I input) {
        this.input = input;
        fetch(this.entities, input);
    }

    /**
     * Fetches the elements and puts them into a list.
     * @param buffer
     */
    protected abstract void fetch(List<O> buffer, I input);

    public List<O> getEntities() {
        return this.immEntities;
    }

    public Iterator<O> iterator() {
        return immEntities.iterator();
    }

    protected I getInput() {
        return this.input;
    }
}

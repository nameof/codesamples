package com.nameof.codesample;

import java.util.Arrays;
import java.util.Iterator;

public class SimpleStream {
    private final Iterator<Integer> iterator;
    private final Op f;
    private static final Op EMPTY = data -> data;

    public SimpleStream(Iterator<Integer> iterator) {
        this.iterator = iterator;
        this.f = EMPTY;
    }

    public SimpleStream(Iterator<Integer> iterator, Op f) {
        this.iterator = iterator;
        this.f = f;
    }

    public void print() {
        while (iterator.hasNext()) {
            System.out.println(f.accept(iterator.next()));
        }
    }

    public SimpleStream map(Op f) {
        return new SimpleStream(iterator, combineOp(f));
    }

    private Op combineOp(Op f) {
        return data -> f.accept(SimpleStream.this.f.accept(data));
    }

    public static void main(String[] args) {
        SimpleStream ss = new SimpleStream(Arrays.asList(1, 2, 3).iterator());
        ss.map(data -> data * 2).map(data -> data + 1).print();
    }
}


@FunctionalInterface
interface Op {
    int accept(int data);
}

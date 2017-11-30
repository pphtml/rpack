package org.superbiz.geo;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Point;
import rx.Observable;

import static com.github.davidmoten.rtree.geometry.Geometries.point;
import static com.github.davidmoten.rtree.geometry.Geometries.rectangle;

public class GeoTest {
    public static void main(String[] args) throws InterruptedException {
        RTree<String, Point> tree = RTree.maxChildren(5).create();
        tree = tree.add("DAVE", point(10, 20))
                .add("FRED", point(12, 25))
                .add("MARY", point(97, 125))
                .add("red", point(10, 20))
                .add("green", point(12, 25))
                .add("blue", point(97, 125));

        Observable<Entry<String, Point>> entries =
                tree.search(rectangle(8, 15, 30, 35));
        entries.subscribe(System.out::println);
    }
}

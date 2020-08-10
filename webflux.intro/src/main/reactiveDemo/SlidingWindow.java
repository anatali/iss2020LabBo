package reactiveDemo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import reactor.core.publisher.Flux;

//From https://stackoverflow.com/questions/41970036/project-reactor-how-to-create-a-sliding-window-flux-from-a-java-8-stream
public class SlidingWindow {

	public static void main(String[] args) {
        System.out.println("Different sliding windows for sequence 0 to 9:");
        SlidingWindow flux = new SlidingWindow();
        
        for (int windowSize = 1; windowSize < 5; windowSize++) {
            flux.slidingWindow(windowSize, IntStream.range(0, 10).boxed())
                .map(SlidingWindow::listToString)
                .subscribe(System.out::print);
            System.out.println();
        }

        //show stream difference: x(i)-x(i-1)
        List<Integer> sequence = Arrays.asList(new Integer[]{10, 12, 11, 9, 13, 17, 21});
        System.out.println("Show difference 'x(i)-x(i-1)' for " + listToString(sequence));
        flux.slidingWindow(2, sequence.stream())
            .doOnNext(SlidingWindow::printlist)
            .map(list -> list.get(1) - list.get(0))
            .subscribe(System.out::println);
        System.out.println();
    }//main

    public <T> Flux<List<T>> slidingWindow(int windowSize, Stream<T> stream) {
        if (windowSize > 0) {
            Flux<List<T>> flux = Flux.fromStream(stream).map(ele -> Arrays.asList(ele));
            for (int i = 1; i < windowSize; i++) {
                flux = addDepth(flux);
            }
            return flux;
        } else {
            return Flux.empty();
        }
    }
    
    protected <T> Flux<List<T>> addDepth(Flux<List<T>> flux) {
        return flux.flatMap(list -> Flux.just(list, list))
            .skip(1)
            .buffer(2)
            .filter(list -> list.size() == 2)
            .map(list -> flatten(list));
    }

    protected <T> List<T> flatten(List<List<T>> list) {
        LinkedList<T> newl = new LinkedList<>(list.get(1));
        newl.addFirst(list.get(0).get(0));
        return newl;
    }

    static String listToString(List list) {
        return list.stream()
            .map(i -> i.toString())
            .collect(Collectors.joining(", ", "[ ", " ], "))
            .toString();
    }

    static void printlist(List list) {
        System.out.print(listToString(list));
    }

 
}

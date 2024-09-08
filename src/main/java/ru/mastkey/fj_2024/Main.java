package ru.mastkey.fj_2024;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mastkey.fj_2024.list.CustomLinkedList;

import java.util.stream.IntStream;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        var customLinkedList = new CustomLinkedList<Integer>();

        customLinkedList.add(1);
        LOGGER.info("After added 1: " + customLinkedList);

        var element = customLinkedList.get(0);
        LOGGER.info("Element on 0 index: " + element.toString());

        var size = customLinkedList.size();
        LOGGER.info("Size of list: " + size);

        var contains = customLinkedList.contains(1);
        LOGGER.info("Contains element: " + contains);

        customLinkedList.remove(0);
        LOGGER.info("After removed 1: " + customLinkedList);

        var list = IntStream.range(0, 10)
                .boxed()
                .toList();

        customLinkedList.addAll(list);
        LOGGER.info("After added 10 elements from list: " + customLinkedList);

        CustomLinkedList<Integer> cstm = IntStream.range(0, 15)
                .boxed()
                .reduce(new CustomLinkedList<>(),
                        (listt, el) -> {
                            listt.add(el);
                            return listt;
                        },
                        (list1, list2) -> {
                            list1.addAll(list2);
                            return list1;
                        }
                );

        LOGGER.info("After added 15 elements from stream: " + cstm);
    }
}

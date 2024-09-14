package ru.mastkey.fj_2024.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CustomLinkedListTest {
    private CustomLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new CustomLinkedList<>();
    }

    @Test
    void addNotNullElementTest() {
        list.add(1);

        var result = list.contains(1);

        assertThat(result).isTrue();
    }

    @Test
    void addNullElementTest() {
        list.add(null);

        var result = list.contains(null);

        assertThat(result).isTrue();
    }

    @Test
    void getElementByIndexTest() {
        list.add(1);
        list.add(2);

        var result = list.get(1);

        assertThat(result).isEqualTo(2);
    }

    @Test
    void getElementByIndexOutOfBoundTest() {
        list.add(1);

        assertThatThrownBy(() -> list.get(2)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void removeByIndexElementTest() {
        list.add(1);
        list.add(2);

        list.remove(1);

        assertThat(list.contains(2)).isFalse();
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    void removeByIndexOutOfBoundsExceptionTest() {
        list.add(1);
        list.add(2);

        assertThatThrownBy(() -> list.remove(2)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void containsElementFoundTest() {
        list.add(1);

        var result = list.contains(1);

        assertThat(result).isTrue();
    }

    @Test
    void containsElementNotFoundTest() {
        list.add(2);

        var result = list.contains(1);

        assertThat(result).isFalse();
    }

    @Test
    void customListSizeWithNoElementsTest() {
        var size = list.size();

        assertThat(size).isEqualTo(0);
    }

    @Test
    void customListSizeTest() {
        IntStream
                .range(0, 10)
                .boxed()
                .forEach(list::add);

        var size = list.size();

        assertThat(size).isEqualTo(10);
    }

    @Test
    void addAllFromCollectionTest() {
        var arrayList = new ArrayList<Integer>();
        IntStream
                .range(0, 10)
                .boxed()
                .forEach(arrayList::add);

        list.addAll(arrayList);

        IntStream
                .range(0, 10)
                .boxed()
                .forEach(i ->
                    assertThat(list.contains(arrayList.get(i))).isTrue()
                );
        assertThat(list.size()).isEqualTo(arrayList.size());
    }

    @Test
    void addAllFromCustomLinkedListTest() {
        var customList = new CustomLinkedList<Integer>();
        IntStream
                .range(0, 10)
                .boxed()
                .forEach(customList::add);

        list.addAll(customList);

        IntStream
                .range(0, 10)
                .boxed()
                .forEach(i ->
                        assertThat(list.contains(customList.get(i))).isTrue()
                );

        assertThat(list.size()).isEqualTo(customList.size());
    }
}
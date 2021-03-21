package com.tsystems.javaschool.tasks.subsequence;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Subsequence {

    /**
     * Checks if it is possible to get a sequence which is equal to the first
     * one by removing some elements from the second one.
     *
     * @param x first sequence
     * @param y second sequence
     * @return <code>true</code> if possible, otherwise <code>false</code>
     */
    @SuppressWarnings("rawtypes")
    public boolean find(List x, List y) {

        //проверяем списки на null
        if (x == null || y == null) throw new IllegalArgumentException();

        //если список х пустой, то можно сразу вернуть true
        if (x.isEmpty()) return true;

        //если список х, который должен быть меньше у, окажется больше y, возвращаем false
        if (x.size() > y.size()) return false;

        //кладем в очередь список х для удобства в дальнейшем извлечении и удалении объектов
        Queue queue = new LinkedList(x);

        //проходим по списку у в поисках элементов из х, которые при нахождении сразу удаляем из х, а по у продолжаем искать дальше (не сначала)
        for (Object elementY : y){
            if (elementY.equals(queue.peek())) queue.remove();
            if (queue.isEmpty()) break;
        }

        //если в очереди из элементов х не осталось элементов, значит список у содержит все элементы х
        if (queue.isEmpty()) return true;

        return false;
    }
}

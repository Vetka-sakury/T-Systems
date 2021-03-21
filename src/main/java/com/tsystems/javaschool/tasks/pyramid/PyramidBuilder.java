package com.tsystems.javaschool.tasks.pyramid;

import java.util.*;

import static java.lang.Math.sqrt;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {

        //проверяем на наличие пустых значений
        if (inputNumbers == null || inputNumbers.size() == 0 || inputNumbers.contains(null)) throw new CannotBuildPyramidException();

        //вычисляем количество строк
        int rowsNumber = countRowsNumber(inputNumbers);

        //если в результате не целое число, пирамиду построить невозможно
        if (rowsNumber == -1) throw new CannotBuildPyramidException();

        //количество столбцов для пирамиды вычисляется по формуле (2 * кол-во строк - 1)
        int columnsNumber = rowsNumber * 2 - 1;

        //сортируем по возрастанию
        Collections.sort(inputNumbers);

        //кладем коллекцию в очередь для удобного извлечения
        Queue<Integer> queue = new LinkedList<>(inputNumbers);

        //создаем пирамиду
        int [][] pyramide= new int [rowsNumber] [columnsNumber];

        //старт для верхней строки
        int start = (pyramide[0].length)/2;

        //в цикле последовательно заполняем значениями из очереди
        for (int i = 0; i < pyramide.length; i++){
            int startInsideRow = start;

            //цикл по строке
            //количество проходов по строке <= номера самой строки (каждая последующая строка содержит на одно заполненное значение больше)
            for (int j = 0; j <= i; j++){

                //заполняем необходимую позицию в массиве значением из очереди (одновременно удаляя его из нее)
                pyramide [i] [startInsideRow] = queue.remove();

                //шаг между значениями на одной строке равен 2
                startInsideRow += 2;
            }

            //при переходе на новую строку смещаем стартовую позицию для заполнения на 1 влево
            start --;
        }

        return pyramide;
    }

    //количество строк в пирамиде (и сама возможность ее построения) определяется из решения уравнения (N*(N+1))/2=C,
    // где N - это количество строк, а С - количество элементов в последовательности. Корень уравнения (положительный) N=(√(1+8С)-1)/2
    // если N - целое число, пирамиду построить можно
    private int countRowsNumber(List<Integer> inputNumbers) {
        double rowsNumber = (sqrt(1+8*inputNumbers.size())-1)/2;
        if (rowsNumber == Math.ceil(rowsNumber)){
            return (int)rowsNumber;
        }
        else
        return -1;
    }
}


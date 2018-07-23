package javahighconcurrent.ch5.matrix_multiply;

import org.jmatrices.dbl.Matrix;
import org.jmatrices.dbl.MatrixFactory;
import org.jmatrices.dbl.operator.MatrixOperator;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * 使用ForkJoinPool进行同步的一个案例
 */
public class PMatrixMul {

    public static final int granularity = 3;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Matrix m1 = MatrixFactory.getRandomMatrix(300, 300, null);
        Matrix m2 = MatrixFactory.getRandomMatrix(300, 300 ,null);
        MatrixMulTask task = new MatrixMulTask(m1, m2, null);
        ForkJoinTask<Matrix> result = forkJoinPool.submit(task);
        Matrix pr = result.get();

        Matrix answer = MatrixOperator.multiply(m1, m2);
        Matrix temp = MatrixOperator.subtract(answer, pr);
        System.out.println(temp);
    }

}

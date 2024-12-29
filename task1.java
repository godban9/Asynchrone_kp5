import java.util.Scanner;
import java.util.concurrent.*;

public class task1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("First num for add: ");
        int num1 = scanner.nextInt();
        System.out.print("Second: ");
        int num2 = scanner.nextInt();

        CompletableFuture<Integer> additionTask = CompletableFuture.supplyAsync(() -> {
            try { 
                TimeUnit.SECONDS.sleep(2); 
            } catch (InterruptedException e) { e.printStackTrace(); }
            return num1 + num2;
        });

        System.out.print("First num for mult: ");
        int mult_num1 = scanner.nextInt();
        System.out.print("Second: ");
        int mult_num2 = scanner.nextInt();
        CompletableFuture<Integer> multiplicationTask = CompletableFuture.supplyAsync(() -> {
            try { 
                TimeUnit.SECONDS.sleep(2); 
            } catch (InterruptedException e) { e.printStackTrace(); }
            return mult_num1 * mult_num2;
        });

        CompletableFuture<Integer> combinedTask = additionTask.thenCombine(multiplicationTask, Integer::sum);

        CompletableFuture<Integer> composedTask = additionTask.thenCompose(result ->
            CompletableFuture.supplyAsync(() -> result * 2));

        CompletableFuture<Void> allTasks = CompletableFuture.allOf(additionTask, multiplicationTask, composedTask);
        allTasks.join();

        CompletableFuture<Object> anyTask = CompletableFuture.anyOf(additionTask, multiplicationTask, composedTask);

        try {
            System.out.println("Combined: " + combinedTask.get());
            System.out.println("Composed: " + composedTask.get());
            System.out.println("First completed task: " + anyTask.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}

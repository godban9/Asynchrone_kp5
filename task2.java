import java.util.concurrent.*;

public class task2 {
    public static void main(String[] args) {
        CompletableFuture<Boolean> checkAvailability = CompletableFuture.supplyAsync(() -> {
            try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println("Перевiрка наявностi мiсць...");
            return true; //Допустимо, що є, в ідеалі тут має бути запит до бд(?)
        });

        CompletableFuture<Double> findBestPrice = CompletableFuture.supplyAsync(() -> {
            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println("Пошук найкращої цiни...");
            return 250.0;  //DB(?)
        });

        CompletableFuture<String> bookTicket = checkAvailability.thenCombine(findBestPrice, (available, price) -> {
            if (available) {
                System.out.println("Бронювання квитка за ціною: $" + price);
                return "Бронювання підтверджено за $" + price;
            } else {
                return "Бронювання не вдалося - немає місць.";
            }
        });

        CompletableFuture<Void> allTasks = CompletableFuture.allOf(checkAvailability, findBestPrice, bookTicket);
        allTasks.join();

        try {
            System.out.println(bookTicket.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

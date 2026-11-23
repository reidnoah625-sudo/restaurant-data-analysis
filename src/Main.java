import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) throws Exception {
        HashMap<String, Integer> totalSold = new HashMap<>();   // item -> total units
        HashMap<String, Double> prices = new HashMap<>();       // item -> price
        HashSet<String> days = new HashSet<>();                 // unique dates
        TreeMap<Integer, Integer> unitsByHour = new TreeMap<>(); // hour -> units sold
        TreeMap<Integer, Double> moneyByHour = new TreeMap<>();  // hour -> money made
        double totalMoney = 0;

        // ---- 1. Read the file ----
        BufferedReader reader = new BufferedReader(new FileReader("sales.csv"));
        reader.readLine(); // skip header

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");

            String date = parts[0];
            int hour = Integer.parseInt(parts[1]);
            String item = parts[2];
            int quantity = Integer.parseInt(parts[3]);
            double price = Double.parseDouble(parts[4]);
            double money = quantity * price;

            days.add(date);
            prices.put(item, price);
            totalSold.put(item, totalSold.getOrDefault(item, 0) + quantity);
            unitsByHour.put(hour, unitsByHour.getOrDefault(hour, 0) + quantity);
            moneyByHour.put(hour, moneyByHour.getOrDefault(hour, 0.0) + money);
            totalMoney = totalMoney + money;
        }
        reader.close();

        // ---- 2. Money made ----
        System.out.println("=== SALES REPORT ===");
        System.out.println("Days of data: " + days.size());
        System.out.printf("Total money made: $%.2f%n", totalMoney);

        // ---- 3. Best seller ----
        String bestByUnits = "";
        int mostUnits = 0;
        String bestByMoney = "";
        double mostMoney = 0;

        System.out.println("\n--- Revenue by item ---");
        for (String item : totalSold.keySet()) {
            int units = totalSold.get(item);
            double money = units * prices.get(item);
            System.out.printf("%s: %d sold, $%.2f%n", item, units, money);

            if (units > mostUnits) {
                mostUnits = units;
                bestByUnits = item;
            }
            if (money > mostMoney) {
                mostMoney = money;
                bestByMoney = item;
            }
        }
        System.out.println("\nBest seller (most units): " + bestByUnits);
        System.out.println("Best seller (most money): " + bestByMoney);

        // ---- 4. Busiest time of day ----
        System.out.println("\n--- Sales by hour (each # = 3 items) ---");
        int busiestHour = 0;
        int busiestUnits = 0;

        for (int hour : unitsByHour.keySet()) {
            int units = unitsByHour.get(hour);
            double money = moneyByHour.get(hour);
            String bar = "#".repeat(units / 3);
            System.out.printf("%02d:00 | %3d items | $%7.2f | %s%n", hour, units, money, bar);

            if (units > busiestUnits) {
                busiestUnits = units;
                busiestHour = hour;
            }
        }
        System.out.printf("%nBusiest hour: %02d:00 to %02d:59 (%d items)%n",
                busiestHour, busiestHour, busiestUnits);
    }
}
package project; 
import java.util.*;
import java.io.*;

class OverweightBagException extends Exception {
    public OverweightBagException(String message) {
        super(message);
    }
}
/* ================= 2. INTERFACE ================= */
interface Weighable {
    double getWeight();
}

/* ================= 3. ITEM ================= */
class Item implements Weighable {
    private String name;
    private double weight;

    public Item(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    @Override
    public double getWeight() {
        return weight;
    }
}
/* ================= 4. PERSON & SUBCLASSES ================= */
abstract class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String getCategoryFile();
    public abstract String getCategoryName();
}
class Adult extends Person {
    public Adult(String name) { super(name); }
    public String getCategoryFile() { return "adult_items.txt"; }
    public String getCategoryName() { return "Adult"; }
}
class Elderly extends Person {
    public Elderly(String name) { super(name); }
    public String getCategoryFile() { return "elderly_items.txt"; }
    public String getCategoryName() { return "Elderly"; }
}
class Baby extends Person {
    public Baby(String name) { super(name); }
    public String getCategoryFile() { return "baby_items.txt"; }
    public String getCategoryName() { return "Baby"; }
}
class ChronicPatient extends Person {
    public ChronicPatient(String name) { super(name); }
    public String getCategoryFile() { return "chronic_items.txt"; }
    public String getCategoryName() { return "Chronic Patient"; }
}

/* ================= 5. RISK LEVEL ================= */
enum RiskLevel { HIGH, MEDIUM, LOW }
class EmergencyBag {
    private double capacity;
    private double currentWeight;

    public EmergencyBag(double capacity) {
        this.capacity = capacity;
    }
    public void addItem(Item item) throws OverweightBagException {
        if (currentWeight + item.getWeight() > capacity) {
            throw new OverweightBagException("Bag capacity exceeded! Item: " + item.getName());
        }
        currentWeight += item.getWeight();
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public double getCapacity() {
        return capacity;
    }
}
/* ================= 7. ITEM LOADER ================= */
class ItemLoader {

    public static ArrayList<Item> loadItems(String file) throws Exception {
        ArrayList<Item> items = new ArrayList<>();
        File f = new File(file); 
        
        if (!f.exists()) return items;

        Scanner sc = new Scanner(f);
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] arr = line.split(";");
            if(arr.length >= 2) {
                String name = arr[0].trim();
                try {
                    double weight = Double.parseDouble(arr[1].trim());
                    items.add(new Item(name, weight));
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid line in " + file);
                }
            }
        }
        sc.close();
        return items;
    }

    public static void appendItem(String filename, String name, double weight) {
        File file = new File(filename); 
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(name + ";" + weight + System.lineSeparator());
            System.out.println("Item added to " + filename);
        } catch (IOException e) {
            System.out.println("File write error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

/* ================= 8. MAIN CLASS ================= */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("1- Create New Earthquake Bag");
            System.out.println("2- Add New Item to Category");
            System.out.println("0- Exit");
            System.out.print("Your choice: ");

            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine(); 
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number.");
                sc.nextLine();
                continue;
            }
            switch (choice) {
                case 1 -> createBag(sc);
                case 2 -> addNewItemToFile(sc);
                case 0 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice.\n");
            }
        }
    }
    private static void createBag(Scanner sc) {
        System.out.print("How many family members are there? ");
        int count = sc.nextInt();
        sc.nextLine();

        ArrayList<Person> family = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            System.out.print(i + ". person's name: ");
            String name = sc.nextLine();

            System.out.print("Select category: 1-Adult 2-Elderly 3-Baby 4-Chronic patient: ");
            int type = sc.nextInt();
            sc.nextLine();

            switch (type) {
                case 1 -> family.add(new Adult(name));
                case 2 -> family.add(new Elderly(name));
                case 3 -> family.add(new Baby(name));
                case 4 -> family.add(new ChronicPatient(name));
                default -> {
                    System.out.println("Invalid type, added as Adult by default.");
                    family.add(new Adult(name));
                }
            }
        }
        System.out.print("Risk level: 1-HIGH 2-MEDIUM 3-LOW: ");
        int r = sc.nextInt();
        sc.nextLine();
        RiskLevel risk = (r == 1 ? RiskLevel.HIGH : r == 2 ? RiskLevel.MEDIUM : RiskLevel.LOW);
        System.out.print("Bag capacity (kg): ");
        double cap = sc.nextDouble();
        sc.nextLine();
        EmergencyBag bag = new EmergencyBag(cap);
        ArrayList<Item> addedItems = new ArrayList<>();

        try {
            // Risk seviyesine göre temel dosya seçimi 
            String baseFile = switch (risk) {
                case HIGH -> "base_items_high.txt";
                case MEDIUM -> "base_items_medium.txt";
                default -> "base_items_low.txt";
            };

            // Base itemları yükle
            for (Item it : ItemLoader.loadItems(baseFile)) {
                bag.addItem(it);
                addedItems.add(it);
            }

            // Kişiye özel itemları yükle
            for (Person p : family) {
                for (Item it : ItemLoader.loadItems(p.getCategoryFile())) {
                    bag.addItem(it);
                    addedItems.add(it);
                }
            }

        } catch (OverweightBagException e) {
            //Özel hata yakalanmalı ve mesaj gösterilmeli 
            System.out.println("\n⚠ WARNING: " + e.getMessage());
            System.out.println("Some items could not be added due to capacity limits.");
        } catch (Exception e) {
            System.out.println("Error loading items: " + e.getMessage());
        }

        /* ===== SUMMARY */
        System.out.println("\n==== BAG SUMMARY ====");
        System.out.println("Family members:");
        for (Person p : family) {
            System.out.println("- " + p.getName() + " (" + p.getCategoryName() + ")");
        }

        System.out.println("\nItems in the bag:");
        for (int i = 0; i < bag.getCurrentWeight(); i++) {

        }
        for (int i = 0; i < addedItems.size(); i++) {

            Item it = addedItems.get(i);
             System.out.printf("%d. %-25s %.2f kg%n", i + 1, it.getName(), it.getWeight());
        }
        System.out.printf("%nTotal Weight: %.2f kg%n", bag.getCurrentWeight());
        System.out.printf("Maximum Capacity: %.2f kg%n", bag.getCapacity());
        System.out.println("Status: " +
                (bag.getCurrentWeight() > bag.getCapacity() ? "Over capacity!" : "Within limit"));
        System.out.println("Press Enter to return to the menu...");
        sc.nextLine();
    }

    private static void addNewItemToFile(Scanner sc) {
        System.out.println("Select category to add item:");
        System.out.println("1-Adult 2-Baby 3-Elderly 4-Chronic patient");
        int t = sc.nextInt();
        sc.nextLine();
        String file = switch (t) {
            case 1 -> "adult_items.txt";
            case 2 -> "baby_items.txt";
            case 3 -> "elderly_items.txt";
            case 4 -> "chronic_items.txt";
            default -> null;
        };
        if (file == null) {
            System.out.println("Invalid category.");
            return;
        }
        System.out.print("Item name: ");
        String name = sc.nextLine();

        System.out.print("Weight (kg): ");
        String weightStr = sc.nextLine().replace(",", "."); 
        try {
            double weight = Double.parseDouble(weightStr);
            ItemLoader.appendItem(file, name, weight);
        } catch (NumberFormatException e) {
            System.out.println("Invalid weight format!");
        }
    }
}
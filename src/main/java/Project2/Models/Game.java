package Project2.Models;

public class Game {
    private String Name;
    private double Price;
    private int AvailableHoursStart;
    private int AvailableHoursEnd;
    private boolean[] Days;
    private int UnavailableHoursStart;
    private int UnavailableHoursEnd;
    private String Description;

    public Game(String name, double price, int availableHoursStart, int availableHoursEnd, boolean[] days, int unavailableHoursStart, int unavailableHoursEnd, String description) {
        this.Name = name;
        this.Price = price;
        this.AvailableHoursStart = availableHoursStart;
        this.AvailableHoursEnd = availableHoursEnd;
        this.Days = days;
        this.UnavailableHoursStart = unavailableHoursStart;
        this.UnavailableHoursEnd = unavailableHoursEnd;
        this.Description = description;
    }

    // GETTERS, SETTERS deleted due to not using
    public String getName() {
        return Name;
    }

    public double getPrice() {
        return Price;
    }

    public int getAvailableHoursStart() {
        return AvailableHoursStart;
    }

    public int getAvailableHoursEnd() {
        return AvailableHoursEnd;
    }

    public boolean[] getDays() {
        return Days;
    }

    public int getUnavailableHoursStart() {
        return UnavailableHoursStart;
    }

    public int getUnavailableHoursEnd() {
        return UnavailableHoursEnd;
    }

    public String getDescription() {
        return Description;
    }
}
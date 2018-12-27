public class Event {
    private String name;
    private String description;
    private int startHour;
    private int startMinutes;
    private int endHour;
    private int endMinutes;
    private int day;
    private int year;
    private int month;

    public Event(String name, int startHour, int startMinutes, int endHour, int endMinutes, String description, int day, int year, int month) {
        this.name = name;
        this.startHour = startHour;
        this.startMinutes = startMinutes;
        this.endHour = endHour;
        this.endMinutes = endMinutes;
        this.description = description;
        this.day = day;
        this.year = year;
        this.month = month;
    }
}

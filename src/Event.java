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

    public Event(String name) {
        this.name = name;
    }

    public Event(String name, int startHour, int startMinutes, int endHour, int endMinutes, String description, int day, int month, int year) {
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

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Event)) return false;
        Event o = (Event) obj;
        return o.getName().equals(this.name);
    }

    public String concatenateData(){
        return name+"~"+Integer.toString(startHour)+"~"+Integer.toString(startMinutes)+"~"+
                Integer.toString(endHour)+"~"+Integer.toString(endMinutes)+"~"+description+"~"+
                Integer.toString(day)+"~"+Integer.toString(month)+"~"+Integer.toString(year);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMinutes() {
        return startMinutes;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMinutes() {
        return endMinutes;
    }

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public void setStartMinutes(int startMinutes) {
        this.startMinutes = startMinutes;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public void setEndMinutes(int endMinutes) {
        this.endMinutes = endMinutes;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}

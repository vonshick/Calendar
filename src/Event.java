public class Event {
    private String name;
    private String description;
    private String startHour;
    private String startMinutes;
    private String endHour;
    private String endMinutes;
    private String day;
    private String year;
    private String month;

    Event(String name) {
        this.name = name;
    }

    Event(String name, String startHour, String startMinutes, String endHour, String endMinutes, String description, String day, String month, String year) {
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

    String concatenateData(){
        return name+"~"+startHour+"~"+startMinutes+"~"+
                endHour+"~"+endMinutes+"~"+description+"~"+
                day+"~"+month+"~"+year;
    }

    String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStartHour() {
        return startHour;
    }

    public String getStartMinutes() {
        return startMinutes;
    }

    public String getEndHour() {
        return endHour;
    }

    public String getEndMinutes() {
        return endMinutes;
    }

    public String getDay() {
        return day;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public void setStartMinutes(String startMinutes) {
        this.startMinutes = startMinutes;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public void setEndMinutes(String endMinutes) {
        this.endMinutes = endMinutes;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}

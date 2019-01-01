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
//        if (obj == this) return true;
        if (!(obj instanceof Event)) return false;
//        Event o = (Event) obj;
//        return o.getName().equals(this.name);
//        return o.getName().equals(this.name) && o.getDay().equals(this.day)
        return (obj == this);
    }

    String concatenateData(){
        return name+"~"+startHour+"~"+startMinutes+"~"+
                endHour+"~"+endMinutes+"~"+description+"~"+
                day+"~"+month+"~"+year;
    }

    String getName() { return name; }

    String getDescription() {
        return description;
    }

    String getStartHour() {
        return startHour;
    }

    String getStartMinutes() {
        return startMinutes;
    }

    String getEndHour() {
        return endHour;
    }

    String getEndMinutes() {
        return endMinutes;
    }

    String getDay() {
        return day;
    }

    String getYear() {
        return year;
    }

    String getMonth() {
        return month;
    }

    void setName(String name) {
        this.name = name;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    void setStartMinutes(String startMinutes) {
        this.startMinutes = startMinutes;
    }

    void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    void setEndMinutes(String endMinutes) {
        this.endMinutes = endMinutes;
    }

    void setDay(String day) {
        this.day = day;
    }

    void setYear(String year) {
        this.year = year;
    }

    void setMonth(String month) {
        this.month = month;
    }
}

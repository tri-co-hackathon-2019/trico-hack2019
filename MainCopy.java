import java.io.*;
import java.time.format.TextStyle;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;


public class Main{

  public static final String MENU_CSV = "hours.csv";

  public static final int HALL = 0;
  public static final int DATE = 1;
  public static final int MEAL = 2;
  public static final int OPEN_HOUR = 3;
  public static final int CLOSE_HOUR = 4;
  private static Hashtable<String, String> halls = new Hashtable<String,String>();
  private static Hashtable<String, String> meals = new Hashtable<String,String>();
  private static Hashtable<String, Integer> daysInt= new Hashtable<String, Integer>();
  private static Hashtable<Integer, String> daysToString= new Hashtable<Integer, String>();


  public static void main(String[] args) throws FileNotFoundException{
    //set up dictionaries

    halls.put("erd","Erdman Dining Hall");
    halls.put("nd", "New Dorm Dining hall");

    meals.put("b","breakfast");
    meals.put("l", "lunch");
    meals.put("d","dinner");
    meals.put("br","brunch/lunch");

    daysInt.put("Monday",0);
    daysInt.put("Tuesday",1);
    daysInt.put("Wednesday",2);
    daysInt.put("Thursday",3);
    daysInt.put("Friday",4);
    daysInt.put("Saturday",5);
    daysInt.put("Sunday",6);


    daysToString.put(0,"Monday");
    daysToString.put(1,"Tuesday");
    daysToString.put(2,"Wednesday");
    daysToString.put(3,"Thursday");
    daysToString.put(4,"Friday");
    daysToString.put(5,"Saturday");
    daysToString.put(6,"Sunday");

    Scanner scanner = new Scanner(new File(MENU_CSV));
    ArrayList<Hours> hours = new ArrayList<Hours>();
    while(scanner.hasNextLine()){
      String str = scanner.nextLine();
      String[] info = str.split(",");
      Hours hour = new Hours(info[HALL],info[DATE],info[MEAL],
      info[OPEN_HOUR],info[CLOSE_HOUR], halls, meals, daysInt, daysToString);

      hours.add(hour);

    }

    for(int i = 0; i<hours.size(); i++){
      System.out.println(hours.get(i).toString() + "   "+ hours.get(i).isOpenNow());
    }

  }


}

class Hours{
  private String hall;
  private String range;
  private String meal;
  private LocalTime start;
  private LocalTime end;

  private Boolean[] days = new Boolean[7];
  private Hashtable<String, Integer> daysInt;
  private Hashtable<Integer, String> daysToString;

  public Hours(String hall, String range, String meal,
   String start,
   String end, Hashtable<String, String> halls, Hashtable<String, String> meals,
  Hashtable<String, Integer> daysInt, Hashtable<Integer, String> daysToString){
    this.daysInt = daysInt;
    this.daysToString = daysToString;
    this.hall = halls.get(hall);
    this.range = range;
    this.meal = meals.get(meal);

    String[] startSplit = start.split(":");
    this.start = LocalTime.of(Integer.parseInt(startSplit[0]),Integer.parseInt(startSplit[1]));

    String[] endSplit = end.split(":");
    this.end = LocalTime.of(Integer.parseInt(endSplit[0]),Integer.parseInt(endSplit[1]));

    days[daysInt.get("Monday")] = range.indexOf("m") != -1;
    days[daysInt.get("Tuesday")] = range.indexOf("t") != -1;
    days[daysInt.get("Wednesday")] = range.indexOf("w") != -1;
    days[daysInt.get("Thursday")] = range.indexOf("r") != -1;
    days[daysInt.get("Friday")] = range.indexOf("f") != -1;
    days[daysInt.get("Saturday")] = range.indexOf("s") != -1;
    days[daysInt.get("Sunday")] = range.indexOf("u") != -1;
  }

  public String getHall(){
    return hall;
  }

  public Boolean isOpenDay(String dayOfWeek){
    return days[daysInt.get(dayOfWeek)];
  }

  public Boolean isAfterStartTime(){
    return start.isBefore(LocalTime.now());
  }

  public Boolean isBeforeEndTime(){
    return end.isAfter(LocalTime.now());
  }

  public Boolean isOpenNow(){
    return isBeforeEndTime() && isAfterStartTime() && isOpenDay(LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL,new Locale("en")));
  }

  public String getDays(){
    String str = "";
    for(int i = 0; i<days.length;i++){
      if(days[i]){
        str+=daysToString.get(i)+", ";
      }
    }
    return str.substring(0, str.lastIndexOf(", "));
  }
  @Override
  public String toString(){
    return hall + " serves "+ meal + " from "+ start.toString()+ " until " + end.toString()+ " on "+ getDays()+ ".";
  }


}

package backfills;

import java.time.LocalDateTime;

import db_connection.ScheduleUploader;

public class ScheduleBackfill {
  

  public static void main(String args[]) {

    String databaseUrl = System.getenv("DATABASE_URL");
    String databaseUsername = System.getenv("DATABASE_USERNAME");
    String databasePassword = System.getenv("DATABASE_PASSWORD");
    String scheduleUrl = "https://api-web.nhle.com/v1/schedule/";

    ScheduleUploader schedule = new ScheduleUploader(databaseUrl, databaseUsername, databasePassword, scheduleUrl);

    LocalDateTime scheduleDate = LocalDateTime.of(2023, 7, 1, 0, 0);
    LocalDateTime scheduleBuffer = scheduleDate;

    //^ Uploads 1yr of games (52 weeks) from starting date specified above
    for (int i = 0; i < 52; i++) {

      System.out.println("Date = " + scheduleDate.toString().substring(0, 10));

      schedule.getSchedule(scheduleDate.toString().substring(0, 10));
      schedule.uploadWeek();

      scheduleBuffer = scheduleDate.plusWeeks(1);
      scheduleDate = scheduleBuffer;
    }

  }

}

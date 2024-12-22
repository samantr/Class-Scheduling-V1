package com.esenyurt.database;

import com.esenyurt.entity.*;
import com.esenyurt.enums.Hours;

import java.sql.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class MSSQLDatabaseConnector {
    public static String URL = "jdbc:sqlserver://localhost:1433;databaseName=class_schedule;trustServerCertificate=true";
    public static String USER = "sa";
    public static String PASSWORD = "Sam27578";

    public static Connection connection = null;
    public static Connection connectToDatabase() {
        try {
            if (connection!=null && !connection.isClosed())
            {
                return connection;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            // Load the SQL Server JDBC Driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            // Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            //System.out.println("Connected to the database successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }
        return connection;
    }

    // Method to fetch data from a table
    public static ResultSet fetchData(String query) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connectToDatabase().prepareStatement(query);
            resultSet = statement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error fetching data: " + e.getMessage());
        }

        return resultSet;
    }

    public static List<Schedule> fetchSchedules(int genNo,int chromosomeNo)
    {
        String query = String.format("select top 2 * from tbl_schedule s where s.run_id = %d and chromosome_no = %d order by day_of_the_week , classroom_id",genNo,chromosomeNo);
        ResultSet resultSet = fetchData(query);
        List<Schedule> scheduleList = new ArrayList<>();
        Schedule schedule;
        try {
            while (resultSet.next()) {
                Subject subject = findSubject(resultSet.getInt("subject_id"));
                schedule = new Schedule();
                schedule.teacher = findTeacher(resultSet.getInt("teacher_id"));
                schedule.classroom = findClassroom(resultSet.getInt("classroom_id"));
                schedule.subject = subject;
                schedule.timeSlot = findTimeSlot(resultSet.getInt("day_of_the_week"),resultSet.getTime("time_begin"));
                schedule.duration = subject.getUnits();
                scheduleList.add(schedule);
            }
        } catch(SQLException e){
            System.err.println("Error fetching data: " + e.getMessage());
            e.printStackTrace();
        } finally{
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return scheduleList;
    }
    public static List<Teacher> fetchPersons()
    {
        String query = " select teacher_id,name,last_name from tbl_teacher";
        ResultSet resultSet = fetchData(query);
        List<Teacher> teacherList = new ArrayList<>();
        Teacher teacher;
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("teacher_id");
                String name = resultSet.getString("name");
                teacher = new Teacher();
                teacher.setId(id);
                teacher.setName(name);
                teacherList.add(teacher);
            }
        } catch(SQLException e){
            System.err.println("Error fetching data: " + e.getMessage());
            e.printStackTrace();
        } finally{
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        return teacherList;
    }

    public static List<Classroom> fetchClassRooms()
    {
        String query = "  select  classroom_id, name, classroom_type, capacity from tbl_classroom";
        ResultSet resultSet = fetchData(query);
        List<Classroom> classRoomList = new ArrayList<>();
        Classroom classRoom;
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("classroom_id");
                String name = resultSet.getString("name");
                String classRoomType = resultSet.getString("classroom_type");
                classRoom = new Classroom();
                classRoom.setId(id);
                classRoom.setClassRoomName(name);
                classRoom.setClassRoomType(classRoomType);
                classRoom.isLab = (classRoomType.equals("LAB")?true : false);
                classRoomList.add(classRoom);
            }
        } catch(SQLException e){
            System.err.println("Error fetching data: " + e.getMessage());
            e.printStackTrace();
        } finally{
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return classRoomList;
    }

    public static List<Subject> fetchSubject()
    {
        String query = " select  subject_id,  code,  name,  theory_units ,practical_units   from tbl_subject";
        ResultSet resultSet = fetchData(query);
        List<Subject> subjectList = new ArrayList<>();
        Subject subject;
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("subject_id");
                String name = resultSet.getString("name");
                int theoryUnits = resultSet.getInt("theory_units");
                int practicalUnits = resultSet.getInt("practical_units");
                subject = new Subject();
                subject.setId(id);
                subject.setName(name);
                subject.setUnits(theoryUnits+practicalUnits);
                subject.setPractical((practicalUnits>0)?true:false);
                subjectList.add(subject);
            }
        } catch(SQLException e){
            System.err.println("Error fetching data: " + e.getMessage());
            e.printStackTrace();
        } finally{
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return subjectList;
    }

    public static long insertRun(RunEntity run) {
        String sql = "INSERT INTO tbl_run (population_size, generation_count) VALUES (?, ?)";
        long id = 0;

        try (Connection connection = connectToDatabase();
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters for the INSERT query
            pstmt.setInt(1, run.populationSize);
            pstmt.setInt(2, run.generationCount);

            // Execute the INSERT query
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getLong(1); // Get the generated key
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error inserting run: " + e.getMessage());
        }

        return id;
    }

    public static void insertSchedule(Schedule schedule, int runId, int generationNo, int fitness, int chromosomeNo) throws SQLException {
        String sql = "INSERT INTO tbl_schedule (teacher_id ,day_of_the_week,time_begin ,time_end, subject_id, classroom_id,run_id,generation_no ,fitness, chromosome_no) VALUES     (?,?,?,?,?,?,?,?,?,?)";
        Connection connection = connectToDatabase();
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            // Set the values for the placeholders
            pstmt.setInt(1, schedule.teacher.getId());
            pstmt.setInt(2, schedule.timeSlot.day.getValue());

            Time sqlTime = Time.valueOf(schedule.timeSlot.time.label);
            pstmt.setTime(3, sqlTime);

            long threeHoursInMillis = schedule.subject.units * 60 * 60 * 1000;
            Time endTime = new Time(sqlTime.getTime() + threeHoursInMillis);
            pstmt.setTime(4, endTime);

            pstmt.setInt(5, schedule.subject.id);
            pstmt.setInt(6, schedule.classroom.id);
            pstmt.setInt(7, runId);
            pstmt.setInt(8, generationNo);
            pstmt.setInt(9, fitness);
            pstmt.setInt(10, chromosomeNo);

            // Execute the insert
            int rowsAffected = pstmt.executeUpdate();
            //System.out.println("Rows inserted: " + rowsAffected);

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            connection.close();
        }
    }

    public static void insertSchedules(List<Schedule> scheduleList,int runId, int generationNo, int fitness, int chromosomeNo ) throws SQLException {
        for (Schedule schedule: scheduleList)
        {
            insertSchedule(schedule,runId,generationNo,fitness,chromosomeNo);
        }
    }

    public static Classroom findClassroom(int id)
    {
        for (Classroom classroom:fetchClassRooms() )
        {
            if(classroom.getId() == id)
                return classroom;
        }

        return null;
    }
    public static Teacher findTeacher(int id)
    {
        for (Teacher teacher:fetchPersons() )
        {
            if(teacher.getId() == id)
                return teacher;
        }

        return null;
    }

    public static Subject findSubject(int id)
    {
        for (Subject subject:fetchSubject() )
        {
            if(subject.getId() == id)
                return subject;
        }

        return null;
    }

    public static TimeSlot findTimeSlot(int day, Time date)
    {
        DayOfWeek dayOfWeek = DayOfWeek.of(day);
        int index = Integer.parseInt(String.format("%2s", date.toString()).replace(' ', '0').substring(0,2)) - 9;
        Hours hour = Hours.values()[index];
        TimeSlot timeSlot = new TimeSlot(dayOfWeek,hour);
        return timeSlot;

    }
}

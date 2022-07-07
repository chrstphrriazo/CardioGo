package com.example.cardiogo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    /*
    *   query for populating WORKOUT table
    *    <-- weight classes notation -->
    *       1 for underweight/normal
    *       2 for overweight
    * */


    String workouts_query= "INSERT INTO WORKOUT " +
            "('exerciseName','unlocksAt','weightClass','exerciseGIF','exerciseType') " +
            "VALUES ('High Knees Marching',1,'ALL','highkneesmarch','type1')," +
            "('Run in Place',3,'ALL','runinplace','type1')," +
            "('Level 1 Burpee',1,'1','burpees','type2')," +
            "('Skater Jump',1,'1','skaterjump','type3')," +
            "('Plank Salute',1,'1','planksalute','type4')," +
            "('Reverse Lunges',1,'1','reverselunges','type5')," +
            "('Knee Level Burpees',1,'2','kneelevelburpees','type2')," +
            "('Side to Side Floor Touch',1,'2','sidetoside','type3')," +
            "('Knee Level Plank Salute',1,'2','kneelevelplanksalute','type4')," +
            "('Middle Shin Level Step Up',1,'2','midlevelstepup','type5')," +

            "('Stationary Marching',1,'3','stationarymarch','type1'), " +
            "('High Knees Marching',2,'3','highkneesmarch','type1'), " +
            "('Hip Level Stepping Burpees',1,'3','stepingburpees','type2'), " +
            "('Step Jack',1,'3','halfjumpingjack','type3'), " +
            "('Inclined Pushup',1,'3','inclinedpushup','type4'), " +
            "('Chair Squat',1,'3','chairsquat','type5')";

//    String workouts_query= "INSERT INTO WORKOUT " +
//            "('exerciseName','unlocksAt','weightClass','exerciseGIF','exerciseType') " +
//            "VALUES ('High Knees Marching',1,'ALL','highkneesmarch','type1')," +
//            "('Run in Place',2,'ALL','runinplace','type1')," + // day 2
//            "('Level 1 Burpee',1,'1','burpees','type2')," +
//            "('Skater Jump',1,'1','skaterjump','type3')," +
//            "('Plank Salute',1,'1','planksalute','type4')," +
//            "('Reverse Lunges',1,'1','reverselunges','type5')," +
//            "('Knee Level Burpees',1,'2','kneelevelburpees','type2')," +
//            "('Side to Side Floor Touch',1,'2','sidetoside','type3')," +
//            "('Knee Level Plank Salute',1,'2','kneelevelplanksalute','type4')," +
//            "('Middle Shin Level Step Up',1,'2','midlevelstepup','type5')," +
//
//             "('Stationary Marching',1,'3','stationarymarch','type1'), " +
//             "('High Knees Marching',2,'3','highkneesmarch','type1'), " +
//             "('Hip Level Stepping Burpees',1,'3','stepingburpees','type2'), " +
//             "('Step Jack',1,'3','halfjumpingjack','type3'), " +
//             "('Inclined Pushup',1,'3','inclinedpushup','type4'), " +
//             "('Chair Squat',1,'3','chairsquat','type5')";





    String achievement_query= "INSERT INTO ACHIEVEMENTS " +
            "('achievementName','unlocksAt', 'minimumStreak','achievementDrawable') " +
            "VALUES ('Road to Fitness',2,0,'achievements_fitness')," +
            "('Reach Level 3',3,0,'achievements_lvl3')," +
            "('Workout Machine',1,3,'achievements_workout')," +
            "('Reach Level 5',5,0,'achievements_lvl5')," +
            "('Fitness Mentality',1,5,'achievements_mentality')," +
            "('Consistent Fitness',1,7,'achievements_streak7')";

//    String achievement_query= "INSERT INTO ACHIEVEMENTS " +
//            "('achievementName','unlocksAt', 'minimumStreak','achievementDrawable') " +
//            "VALUES ('Road to Fitness',2,0,'achievements_fitness')," + //day 1
//            "('Reach Level 3',2,0,'achievements_lvl3')," + //day 1
//            "('Workout Machine',2,2,'achievements_workout')," + //day2
//            "('Reach Level 5',2,0,'achievements_lvl5')," + //day1
//            "('Fitness Mentality',2,2,'achievements_mentality')," + //day2
//            "('Consistent Fitness',2,2,'achievements_streak7')"; //day2


    String userWorkoutsQuery="INSERT INTO Destination SELECT * FROM Source";
    public static final String DBNAME = "GoCardio.db";
    public DatabaseHelper(Context context){
        super(context, DBNAME, null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //REAL is for float
        try{
            createTables(db);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void createTables(SQLiteDatabase db){


        db.execSQL("create Table USERS(userID INTEGER primary key autoincrement," +
                " username TEXT," +
                " password TEXT," +
                " birthday TEXT," +
                " age INTEGER," +
                " height REAL," +
                " weight REAL," +
                " BMI REAL," +
                " BMIClass INTEGER," +
                " level INTEGER," +
                " EXP INTEGER," +
                " EXP_BAR INTEGER," +
                " streak INTEGER," +
                " highestStreak INTEGER,"+
                " avatar TEXT)");

        db.execSQL("create Table USERS_HISTORY(historyID INTEGER primary key autoincrement," +
                " user TEXT," +
                " aveHeartRate INTEGER," +
                " aveOxygenSat INTEGER," +
                " dateToday TEXT," +
                " workoutStatus TEXT )");

        db.execSQL("create Table WORKOUT (workoutID INTEGER primary key autoincrement," +
                " exerciseName TEXT," +
                " unlocksAt INTEGER," +
                " weightClass TEXT," +
                " exerciseGIF TEXT," +
                " exerciseType TEXT )");

        db.execSQL("create Table ACHIEVEMENTS (achievementID INTEGER primary key autoincrement," +
                " achievementName TEXT," +
                " unlocksAt INTEGER," +
                " minimumStreak INTEGER," +
                " achievementDrawable TEXT)");

        db.execSQL(workouts_query);
        db.execSQL(achievement_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists USERS");
        db.execSQL("drop Table if exists USERS_HISTORY");

    }

    public Boolean createUser(String username, String password, String birthday, int age, float height, float weight, float BMI, int BMIClass, int streak){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("birthday", birthday);
        contentValues.put("age", age);
        contentValues.put("height", height);
        contentValues.put("weight", weight);
        contentValues.put("BMI", BMI);
        contentValues.put("BMIClass", BMIClass);
        contentValues.put("streak", streak);
        contentValues.put("level", 1);
        contentValues.put("EXP", 0);
        contentValues.put("EXP_BAR", 90);
        contentValues.put("highestStreak", 0);
        contentValues.put("avatar", "default_avatar");

        long result = db.insert("USERS", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }


    public Boolean insertWorkoutData(String username, int aveHeartRate, int aveOxygenSat, String dateToday, String workoutStatus){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("user", username); //TEXT
        contentValues.put("aveHeartRate", aveHeartRate); //INTEGER
        contentValues.put("aveOxygenSat", aveOxygenSat); //INTEGER
        contentValues.put("dateToday", dateToday); //TEXT
        contentValues.put("workoutStatus", workoutStatus); //TEXT

        long result = db.insert("USERS_HISTORY", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }

    public Boolean checkExistingUsername(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From  USERS where username=?", new String[] {username});
        if(cursor.getCount()>0){
            return false;}
        else{
            return true;}
    }


    public Boolean loginVerification(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from USERS where username=? and password=?", new String[]{username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    //get level of the current user
    public Cursor getLevel(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select level From  USERS where username=?", new String[] {username});
        cursor.moveToFirst();
        return cursor;
    }


    //get experience bar of the current user
    public Cursor getExpbar(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select EXP_BAR From  USERS where username=?", new String[] {username});
        cursor.moveToFirst();
        return cursor;
    }

    //get experience points of the current user
    public Cursor getExp(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select EXP From  USERS where username=?", new String[] {username});
        cursor.moveToFirst();
        return cursor;
    }

    //get weight class of the current user
    public Cursor getWeightClass(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select BMIClass From  USERS where username=?", new String[] {username});
        cursor.moveToFirst();
        return cursor;
    }

    //get bmi of the current user
    public Cursor getBMI(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select BMI From  USERS where username=?", new String[] {username});
        cursor.moveToFirst();
        return cursor;
    }


    //get avatar of the current user
    public Cursor getAvatar(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select avatar From  USERS where username=?", new String[] {username});
        cursor.moveToFirst();
        return cursor;
    }


    //update user experience bar in database
    public void update_expBar(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  contentValues = new ContentValues();
        contentValues.put("EXP_BAR", MainActivity.xp_bar);
        db.update("users", contentValues, "username = ?", new String[] {username});
    }

    //update user experience points in database
    public void update_exp(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  contentValues = new ContentValues();
        contentValues.put("EXP", MainActivity.user_xp);
        db.update("users", contentValues, "username = ?", new String[] {username});
    }

    //update user level in database
    public void update_level(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  contentValues = new ContentValues();
        contentValues.put("level", MainActivity.user_level);
        db.update("users", contentValues, "username = ?", new String[] {username});
    }

    //update user avatar database
    public void update_avatar(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  contentValues = new ContentValues();
        contentValues.put("avatar", ChangeAvatarActivity.myAvatarName);
        db.update("users", contentValues, "username = ?", new String[] {username});
    }

    //update username database
    public void update_username(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  contentValues = new ContentValues();
        contentValues.put("username", ChangeAvatarActivity.myUsername);
        db.update("users", contentValues, "username = ?", new String[] {username});
    }

    //update username history database
    public void update_usernameHistory(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  contentValues = new ContentValues();
        contentValues.put("user", ChangeAvatarActivity.myUsername);
        db.update("USERS_HISTORY", contentValues, "user = ?", new String[] {username});
    }

    //update height database
    public void update_height(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  contentValues = new ContentValues();
        contentValues.put("height", ChangeAvatarActivity.myHeight);
        db.update("users", contentValues, "username = ?", new String[] {username});
    }

    //update weight database
    public void update_weight(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  contentValues = new ContentValues();
        contentValues.put("weight", ChangeAvatarActivity.myWeight);
        db.update("users", contentValues, "username = ?", new String[] {username});
    }

    //update weight database
    public void update_bmi(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  contentValues = new ContentValues();
        contentValues.put("BMIClass", MainActivity.myWeightClass);
        contentValues.put("BMI", MainActivity.BMI);
        db.update("users", contentValues, "username = ?", new String[] {username});
    }

    public void update_highestStreak(String username, int highestStreak){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  contentValues = new ContentValues();
        contentValues.put("highestStreak", highestStreak);
        db.update("users", contentValues, "username = ?", new String[] {username});

    }



    public Cursor myUserAchievements(String streak, String level) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("ACHIEVEMENTS", new String[]{"achievementName", "unlocksAt", "minimumStreak","achievementDrawable"},
                "unlocksAt <= ? AND minimumStreak <= ?",new String[] {level, streak},
                null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }



    //get all available exercises for current user
    public Cursor myUserWorkouts(String weight, String level) {
        SQLiteDatabase db = this.getReadableDatabase();
        //if obese ibang query weight == 3
        //else :
        if (weight.equals("3")){
            Cursor cursor = db.query("WORKOUT", new String[]{"exerciseName", "unlocksAt", "weightClass", "exerciseGIF", "exerciseType"},
                    "(unlocksAt <= ? AND WEIGHTCLASS = ?)",new String[] {level, weight},
                    null, null, null, null);
            cursor.moveToFirst();
            return cursor;
        }else{
            Cursor cursor = db.query("WORKOUT", new String[]{"exerciseName", "unlocksAt", "weightClass", "exerciseGIF", "exerciseType"},
                    "(unlocksAt <= ? AND WEIGHTCLASS = ?) OR (unlocksAt <= ? AND WEIGHTCLASS = 'ALL')",new String[] {level, weight, level},
                    null, null, null, null);
            cursor.moveToFirst();
            return cursor;
        }
    }

    //get all available exercises for current user
    public Cursor workOuts(String weight) {
        SQLiteDatabase db = this.getReadableDatabase();
        if(weight.equals("3")){
            Cursor cursor = db.query("WORKOUT", new String[]{"exerciseName", "unlocksAt", "weightClass", "exerciseGIF", "exerciseType"},
                    "WEIGHTCLASS = ?",new String[] {weight},
                    null, null, null, null);
            cursor.moveToFirst();
            return cursor;
        }else{
            Cursor cursor = db.query("WORKOUT", new String[]{"exerciseName", "unlocksAt", "weightClass", "exerciseGIF", "exerciseType"},
                    "WEIGHTCLASS = ? OR WEIGHTCLASS = 'ALL'",new String[] {weight},
                    null, null, null, null);
            cursor.moveToFirst();
            return cursor;
        }
    }

    public Cursor achievements() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("ACHIEVEMENTS", new String[]{"achievementName"},
                null,
                null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }


    //FETCH WORKOUT HISTORY DATA --- Date only
    public Cursor workoutHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("USERS_HISTORY", new String[]{"dateToday"}, null,null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor userHistory(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("USERS_HISTORY", new String[]{"dateToday", "workoutStatus", "aveHeartRate"}, "user = ?",new String[] {username},null,null,null);
        cursor.moveToFirst();
        return cursor;
    }

    public void updateStreak(String username, int streak){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  contentValues = new ContentValues();
        contentValues.put("streak", streak);
        db.update("USERS", contentValues, "username = ?", new String[] {username});
    }

    public Cursor getStreak(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("USERS", new String[]{"streak"}, "username = ?",new String[] {username},null,null,null);
        cursor.moveToFirst();
        return cursor;

    }

    public Cursor getAge(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("USERS", new String[]{"age"}, "username = ?",new String[] {username},null,null,null);
        cursor.moveToFirst();
        return cursor;

    }

    public Cursor getHighestStreak(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select highestStreak From  USERS where username=?", new String[] {username});
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor userWorkoutHistoryData(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("USERS_HISTORY", new String[]{"aveHeartRate","aveOxygenSat","dateToday", "workoutStatus"}, "user = ?",new String[] {username},null,null,null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getUserData(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("USERS", new String[]{"username","height","weight", "avatar"}, "username = ?",new String[] {username},null,null,null);
        cursor.moveToFirst();
        return cursor;
    }

}

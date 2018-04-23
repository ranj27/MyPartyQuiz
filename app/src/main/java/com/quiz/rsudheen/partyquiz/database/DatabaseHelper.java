package com.quiz.rsudheen.partyquiz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rsudheen on 1/5/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "quizManager";

    // Table Names
    private static final String TABLE_QUIZ = "quiz";
    private static final String TABLE_QUESTIONS = "questions";
    private static final String TABLE_ROUNDS = "rounds";
    private static final String TABLE_SCORE = "scores";
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_QUIZ_CATEGORY="quiz_category";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    //Quiz table column names
    private static final String KEY_QUIZ_NAME = "quiz_name";
    private static final String KEY_QUIZ_ROUNDS = "quizRounds";

   //SCore table column names
    private static String KEY_SCORE_COMPLEXITY = "complexity";
    private static String KEY_SCORE_VALUE = "score";

    //Questions table column names
    private static final String KEY_QUESTION = "question";
    private static final String KEY_CHOICES = "choices";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_QUIZ_ID = "quiz_id";
    private static final String KEY_ROUND_ID = "round_id";
    private static final String KEY_SCORE_ID = "score_id";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_PHOTO = "photo";

    //Categories table column names
    private static final String KEY_CATEGORY_NAME = "category_name";

    //Table Create statements
    //Quiz table create statement
    private static final String CREATE_TABLE_QUIZ = "CREATE TABLE "
            + TABLE_QUIZ + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_QUIZ_NAME
            + " TEXT UNIQUE, " + KEY_QUIZ_ROUNDS + " INTEGER, " + KEY_CREATED_AT
            + " DATETIME)";


    //Scores table create statement
    private static final String CREATE_TABLE_SCORE = "CREATE TABLE "
            + TABLE_SCORE + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_SCORE_COMPLEXITY
            + " TEXT UNIQUE, " + KEY_SCORE_VALUE + " INTEGER, " + KEY_CREATED_AT + " DATETIME)";

    //Insert default values into the score table
    private static final String INSERT_ROWS_TABLE_SCORE = "INSERT INTO "
            + TABLE_SCORE + " (" + KEY_SCORE_COMPLEXITY + "," + KEY_SCORE_VALUE
            + ") SELECT 'LOW' as " + KEY_SCORE_COMPLEXITY + "," + "5 as " + KEY_SCORE_VALUE
            + " UNION SELECT 'MEDIUM' as " + KEY_SCORE_COMPLEXITY + "," + "10 as " + KEY_SCORE_VALUE
            + " UNION SELECT 'HIGH' as " + KEY_SCORE_COMPLEXITY + "," + "15 as " + KEY_SCORE_VALUE;

    //Categories table create statement
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE "
            + TABLE_CATEGORY + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CATEGORY_NAME
            + " TEXT UNIQUE, " + KEY_CREATED_AT + " DATETIME)";

    //Quiz Category table create statement
    private static final String CREATE_TABLE_QUIZ_CATEGORY = "CREATE TABLE "
            + TABLE_QUIZ_CATEGORY + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CATEGORY_ID + " INTEGER, "
            + KEY_QUIZ_ID + " INTEGER, " + KEY_ROUND_ID + " INTEGER)";

    //Insert default values into the categories table
    private static final String INSERT_ROWS_TABLE_CATEGORY = "INSERT INTO "
            + TABLE_CATEGORY + " (" + KEY_CATEGORY_NAME + ") VALUES ('MOVIES'),('HISTORY'),('FUN STUFF'),('SCIENCE'),('MATH'),('EPICS')";

    //Questions table create statement
    private static final String CREATE_TABLE_QUESTIONS = "CREATE TABLE "
            + TABLE_QUESTIONS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_QUESTION
            + " TEXT UNIQUE, " + KEY_CHOICES + " TEXT, " + KEY_ANSWER + " TEXT, " + KEY_QUIZ_ID + " INTEGER, "
            + KEY_ROUND_ID + " INTEGER, " + KEY_SCORE_ID + " INTEGER, " + KEY_CATEGORY_ID + " INTEGER, "
            + KEY_PHOTO + " BLOB, " + KEY_CREATED_AT + " DATETIME)";


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DatabaseHelper(Context applicationContext) {
        super(applicationContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_QUIZ);
        sqLiteDatabase.execSQL(CREATE_TABLE_SCORE);
        sqLiteDatabase.execSQL(INSERT_ROWS_TABLE_SCORE);
        sqLiteDatabase.execSQL(CREATE_TABLE_CATEGORY);
        sqLiteDatabase.execSQL(INSERT_ROWS_TABLE_CATEGORY);
        sqLiteDatabase.execSQL(CREATE_TABLE_QUESTIONS);
        sqLiteDatabase.execSQL(CREATE_TABLE_QUIZ_CATEGORY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_CATEGORY);
        onCreate(sqLiteDatabase);
    }

    //CRUD operations on Quiz table
    //Create/Insert into Quiz table
    public long createQuiz(Quiz quiz) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_QUIZ_NAME, quiz.getQuizName());
        contentValues.put(KEY_QUIZ_ROUNDS, quiz.getQuizRounds());

        //insert row
        long rowData = db.insert(TABLE_QUIZ, null, contentValues);
        return rowData;

    }

    //Read from Quiz table by ID
    public Quiz getQuizById(long quizId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Quiz quiz = null;

        String selectQuery = "SELECT  * FROM " + TABLE_QUIZ + " WHERE "
                + KEY_ID + " = " + quizId;

        //Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        if (c.getCount() > 0) {
            quiz = new Quiz();
            quiz.setQuizId(c.getInt(c.getColumnIndex(KEY_ID)));
            quiz.setQuizName((c.getString(c.getColumnIndex(KEY_QUIZ_NAME))));
            quiz.setQuizRounds(c.getString(c.getColumnIndex(KEY_QUIZ_ROUNDS)));
            quiz.setCreateDt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        }

        return quiz;
    }

    //Insert default categories to Quiz_Category table
    public void insertDefaultCategories(int quizId, int roundId){
        SQLiteDatabase db = this.getWritableDatabase();
        String insertQuery = "INSERT INTO " + TABLE_QUIZ_CATEGORY + "(" + KEY_CATEGORY_ID + ","
                + KEY_QUIZ_ID + "," + KEY_ROUND_ID + ") VALUES "
                + "(1," + quizId + "," + roundId + "),"
                + "(2," + quizId + "," + roundId + "),"
                + "(3," + quizId + "," + roundId + "),"
                + "(4," + quizId + "," + roundId + "),"
                + "(5," + quizId + "," + roundId + "),"
                + "(6," + quizId + "," + roundId + ")";


        db.execSQL(insertQuery);

    }

    //Read quiz by name
    public Quiz getQuizByName(String quizName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Quiz quiz = null;

        String selectQuery = "SELECT  * FROM " + TABLE_QUIZ + " WHERE "
                + KEY_QUIZ_NAME + " = '" + quizName + "'";

        //Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        if (c.getCount() > 0) {
            quiz = new Quiz();
            quiz.setQuizId(c.getInt(c.getColumnIndex(KEY_ID)));
            quiz.setQuizName((c.getString(c.getColumnIndex(KEY_QUIZ_NAME))));
            quiz.setQuizRounds(c.getString(c.getColumnIndex(KEY_QUIZ_ROUNDS)));
            quiz.setCreateDt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        }

        return quiz;
    }

    //Read all rows from Quiz table
    public List<Quiz> getAllQuiz() {
        List<Quiz> quizzes = new ArrayList<Quiz>();
        String selectQuery = "SELECT  * FROM " + TABLE_QUIZ;

        // Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Quiz quiz = new Quiz();
                quiz.setQuizId(c.getInt((c.getColumnIndex(KEY_ID))));
                quiz.setQuizName((c.getString(c.getColumnIndex(KEY_QUIZ_NAME))));
                quiz.setQuizRounds(c.getString(c.getColumnIndex(KEY_QUIZ_ROUNDS)));
                quiz.setCreateDt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to todo list
                quizzes.add(quiz);
            } while (c.moveToNext());
        }

        return quizzes;
    }

    //Read all from Scores table
    public List<Scores> getAllScore() {
        List<Scores> scores = new ArrayList<Scores>();
        String selectQuery = "SELECT  * FROM " + TABLE_SCORE;

        // Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Scores score = new Scores();
                score.setScoreId(c.getInt((c.getColumnIndex(KEY_ID))));
                score.setComplexity((c.getString(c.getColumnIndex(KEY_SCORE_COMPLEXITY))));
                score.setScore(c.getInt(c.getColumnIndex(KEY_SCORE_VALUE)));

                // adding to todo list
                scores.add(score);
            } while (c.moveToNext());
        }

        return scores;
    }

    public Categories getCategoryById(int categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Categories category = new Categories();
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY + " WHERE "
                + KEY_ID + " = " + categoryId;

        //Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        if (c.getCount() > 0) {
            category.setCategoryId(c.getInt(c.getColumnIndex(KEY_ID)));
            category.setCategoryName((c.getString(c.getColumnIndex(KEY_CATEGORY_NAME))));
        }

        return category;

    }

    //Read all from Categories table
    public List<Categories> getAllCategories() {
        List<Categories> categories = new ArrayList<Categories>();
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;

        // Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Categories category = new Categories();
                category.setCategoryId(c.getInt((c.getColumnIndex(KEY_ID))));
                category.setCategoryName((c.getString(c.getColumnIndex(KEY_CATEGORY_NAME))));

                // adding to todo list
                categories.add(category);
            } while (c.moveToNext());
        }

        return categories;
    }

    //Get Questions based on quiz ID and round id
    public List<Questions> getQuestionsById(int quizId, int roundId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Questions> questions = new ArrayList<Questions>();

        String selectQuery = "SELECT  * FROM " + TABLE_QUESTIONS + " WHERE "
                + KEY_QUIZ_ID + " = " + quizId + " AND " + KEY_ROUND_ID + " = " + roundId;

        //Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        if (c.getCount() > 0) {
            do {
                Questions question = new Questions();
                question.setQuestionId(c.getInt(c.getColumnIndex(KEY_ID)));
                question.setQuestion((c.getString(c.getColumnIndex(KEY_QUESTION))));
                question.setRoundId(c.getInt(c.getColumnIndex(KEY_ROUND_ID)));
                question.setCategoryId(c.getInt(c.getColumnIndex(KEY_CATEGORY_ID)));
                question.setAnswer(c.getString(c.getColumnIndex(KEY_ANSWER)));
                question.setChoices(c.getString(c.getColumnIndex(KEY_CHOICES)));
                question.setCategoryId(c.getInt(c.getColumnIndex(KEY_CATEGORY_ID)));
//                question.setCategoryName(c.getString(c.getColumnIndex(KEY_CATEGORY_NAME)));
                questions.add(question);
            } while (c.moveToNext());
        }

        return questions;
    }

    public List<Scores> getScoresbyQuizId(int quizId, int roundId) {
        List<Scores> scores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT *  FROM " + TABLE_SCORE
                + " WHERE " + KEY_ID + " IN (SELECT  DISTINCT " + KEY_SCORE_ID + " FROM " + TABLE_QUESTIONS + " WHERE "
                + KEY_QUIZ_ID + " = " + quizId + " AND " + KEY_ROUND_ID + " = " + roundId + ")";

        //Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        if (c.getCount() > 0) {
            do {
                Scores score = new Scores();
                score.setComplexity(c.getString(c.getColumnIndex(KEY_SCORE_COMPLEXITY)));
                score.setScore(c.getInt(c.getColumnIndex(KEY_SCORE_VALUE)));
                scores.add(score);
            } while (c.moveToNext());

        }

        return scores;
    }

    //Get Questions Categories based on quiz ID and round id
    public List<Categories> getCategoriesByQuizId(int quizId, int roundId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Categories> categories = new ArrayList<>();

        String selectQuery = "SELECT " + KEY_CATEGORY_NAME + " FROM " + TABLE_CATEGORY
                + " WHERE " + KEY_ID + " IN (SELECT  DISTINCT " + KEY_CATEGORY_ID + " FROM " + TABLE_QUESTIONS + " WHERE "
                + KEY_QUIZ_ID + " = " + quizId + " AND " + KEY_ROUND_ID + " = " + roundId + ")";

        //Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        if (c.getCount() > 0) {
            do {
                Categories category = new Categories();
                category.setCategoryName(c.getString(c.getColumnIndex(KEY_CATEGORY_NAME)));
                categories.add(category);
            } while (c.moveToNext());

        }

        return categories;
    }


    //Get Questions Categories based on quiz ID and round id
    public List<Categories> getCategoriesFoQuizId(int quizId, int roundId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Categories> categories = new ArrayList<>();

        /*String selectQuery = "SELECT DISTINCT " + KEY_CATEGORY_NAME + " FROM " + TABLE_QUESTIONS
                + " WHERE " + KEY_QUIZ_ID + " = " + quizId + " AND " + KEY_ROUND_ID + " = " + roundId;
*/
       /* String selectQuery = "SELECT " + KEY_CATEGORY_NAME + " FROM " + TABLE_CATEGORY + " WHERE "
                + KEY_ID + " IN ( SELECT " + KEY_CATEGORY_ID + " FROM " + TABLE_QUIZ_CATEGORY + " WHERE "
                + KEY_QUIZ_ID + "=" + quizId + " AND " + KEY_ROUND_ID + "=" + roundId + " ORDER BY " + KEY_ID + ")";
        //Log.e(LOG, selectQuery);*/

        String selectQuery = "SELECT " +  "b." + KEY_CATEGORY_NAME + " FROM " + TABLE_QUIZ_CATEGORY + " a , " + TABLE_CATEGORY + " b" +
                " WHERE  a."+ KEY_CATEGORY_ID + "=b."+KEY_ID+" AND a."+KEY_QUIZ_ID +"="+quizId +" AND a."+ KEY_ROUND_ID +"="+roundId;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        if (c.getCount() > 0) {
            do {
                Categories category = new Categories();
                category.setCategoryName(c.getString(c.getColumnIndex(KEY_CATEGORY_NAME)));
                categories.add(category);
            } while (c.moveToNext());

        }

        return categories;
    }


    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public Categories getCategoryByName(String s) {
        SQLiteDatabase db = this.getReadableDatabase();
        Categories category = new Categories();
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY + " WHERE "
                + KEY_CATEGORY_NAME + " LIKE '" + s.toUpperCase() + "'";

        //Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        if (c.getCount() > 0) {

            category.setCategoryId(c.getInt(c.getColumnIndex(KEY_ID)));
            category.setCategoryName((c.getString(c.getColumnIndex(KEY_CATEGORY_NAME))));
        }

        return category;

    }

    public Scores getScoresbyValue(String scoreValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        Scores score = new Scores();

        String selectQuery = "SELECT  * FROM " + TABLE_SCORE + " WHERE "
                + KEY_SCORE_VALUE + " = " + scoreValue;

        //Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        if (c.getCount() > 0) {

            score.setScoreId(c.getInt(c.getColumnIndex(KEY_ID)));
            score.setScore((c.getInt(c.getColumnIndex(KEY_SCORE_VALUE))));
            score.setComplexity(c.getString(c.getColumnIndex(KEY_SCORE_COMPLEXITY)));
        }


        return score;
    }

    public void createNewQuestionsforQuiz(int quizId, int roundId) {
        String insertQuery = "INSERT INTO " + TABLE_QUESTIONS + " (" + KEY_QUIZ_ID + "," + KEY_ROUND_ID + ") VALUES (" + quizId + "," + roundId + ")";
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < 36; i++) {
            db.execSQL(insertQuery);
        }
    }

    public long insertNewQuestion(String questionTxt, String answerTxt, String choice2Txt, String choice3Txt, String choice4Txt, int quizId, int roundId, String categoryName, int categoryId, String score, byte[] photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String choices = choice2Txt.concat(";").concat(choice3Txt).concat(";").concat(choice4Txt);
       /* String insertQuery = "INSERT INTO " + TABLE_QUESTIONS + " (" + KEY_QUIZ_ID + "," + KEY_ROUND_ID + "," + KEY_QUESTION + ","
                + KEY_ANSWER + "," + KEY_CHOICES + "," + KEY_CATEGORY_NAME + ") VALUES ( " + quizId + "," + roundId + "," + questionTxt + "," + answerTxt
                + "," + choices + "," + categoryName + ")";

        db.execSQL(insertQuery);*/

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_QUIZ_ID, quizId);
        contentValues.put(KEY_ROUND_ID, roundId);
        contentValues.put(KEY_QUESTION, questionTxt);
        contentValues.put(KEY_ANSWER, answerTxt);
        contentValues.put(KEY_CHOICES, choices);
//        contentValues.put(KEY_CATEGORY_NAME, categoryName);
        contentValues.put(KEY_CATEGORY_ID,categoryId);
        contentValues.put(KEY_PHOTO,photo);

        //insert row
        long rowData = db.insert(TABLE_QUESTIONS, null, contentValues);
        return rowData;

    }

    public Questions getQuestionById(int questionID) {

        SQLiteDatabase db = this.getReadableDatabase();
        Questions questions = null;

        String selectQuery = "SELECT  * FROM " + TABLE_QUESTIONS + " WHERE "
                + KEY_ID + " = " + questionID;

        //Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        if (c.getCount() > 0) {
            questions = new Questions();
            questions.setQuestionId(c.getInt(c.getColumnIndex(KEY_ID)));
            questions.setQuestion((c.getString(c.getColumnIndex(KEY_QUESTION))));
            questions.setAnswer(c.getString(c.getColumnIndex(KEY_ANSWER)));
            questions.setCategoryId(c.getInt(c.getColumnIndex(KEY_CATEGORY_ID)));
            questions.setPhoto(c.getBlob(c.getColumnIndex(KEY_PHOTO)));
//            questions.setCategoryName(c.getString(c.getColumnIndex(KEY_CATEGORY_NAME)));
        }


        return questions;
    }

    public List<Questions> getQuestionsByCategory(String categoryName, int quizId, int roundId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Questions> questions = new ArrayList<>();
       /* String selectQuery = "SELECT  * FROM " + TABLE_QUESTIONS + " WHERE "
                + KEY_CATEGORY_ID + "= (SELECT  " + KEY_ID + " FROM " + TABLE_CATEGORY + " WHERE " + KEY_CATEGORY_NAME + " LIKE '" +categoryName + "') AND " + KEY_QUIZ_ID + " = " + quizId + " AND "
                + KEY_ROUND_ID + " = " + roundId;*/
       String selectQuery = "SELECT a." + KEY_ID + ", b."+KEY_CATEGORY_NAME + " FROM " + TABLE_QUESTIONS + " a, " + TABLE_CATEGORY + " b "
               + "WHERE a."+KEY_CATEGORY_ID + "= b."+KEY_ID +" AND a."+KEY_QUIZ_ID + "=" + quizId + " AND a."+KEY_ROUND_ID + "=" + roundId
               + " AND b." + KEY_CATEGORY_NAME + " LIKE '" + categoryName + "'";

        //Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        if (c.getCount() > 0) {
            do {
                Questions question = new Questions();
                question.setQuestionId(c.getInt(c.getColumnIndex(KEY_ID)));
//                question.setQuestion((c.getString(c.getColumnIndex(KEY_QUESTION))));
//                question.setAnswer(c.getString(c.getColumnIndex(KEY_ANSWER)));
                question.setCategoryName(c.getString(c.getColumnIndex(KEY_CATEGORY_NAME)));
//                question.setCategoryId(c.getInt(c.getColumnIndex(KEY_CATEGORY_ID)));
//                question.setChoices(c.getString(c.getColumnIndex(KEY_CHOICES)));
                questions.add(question);
            } while (c.moveToNext());
        }


        return questions;
    }

    public long UpdateQuestion(int questionId, String questionTxt, String answerTxt, String choice2Txt, String choice3Txt, String choice4Txt, int quizId, int roundId, String categoryName, int categoryId, String score, byte[] photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String choices = choice2Txt.concat(";").concat(choice3Txt).concat(";").concat(choice4Txt);

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_QUIZ_ID, quizId);
        contentValues.put(KEY_ROUND_ID, roundId);
        contentValues.put(KEY_QUESTION, questionTxt);
        contentValues.put(KEY_ANSWER, answerTxt);
        contentValues.put(KEY_CHOICES, choices);
//        contentValues.put(KEY_CATEGORY_NAME, categoryName);
        contentValues.put(KEY_CATEGORY_ID,categoryId);
        contentValues.put(KEY_PHOTO,photo);

        //update0 row
        long rowData = db.update(TABLE_QUESTIONS, contentValues, KEY_ID + " = ?", new String[]{String.valueOf(questionId)});
        return rowData;

    }

    public void updateQuizCategoryName(int quizId, int roundId, String currentCategoryName, String newCategoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Categories categories = null;
        int categoryId =-1;
        newCategoryName = newCategoryName.toUpperCase();
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + KEY_CATEGORY_NAME + " LIKE '" + newCategoryName + "'";
        Cursor c = db.rawQuery(selectQuery,null);
        if (c !=null ){
            c.moveToFirst();
        }
        if (c.getCount()>0){
            categories = new Categories();
            categories.setCategoryId(c.getInt(c.getColumnIndex(KEY_ID)));
        }
        if (categories!=null) {
            categoryId = categories.getCategoryId();
        }else{
            String insertQuery = "INSERT INTO " + TABLE_CATEGORY + "(" + KEY_CATEGORY_NAME + ") VALUES ('" + newCategoryName + "')";
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_CATEGORY_NAME,newCategoryName);
            //db.rawQuery(insertQuery,null);
            long rowdata = db.insert(TABLE_CATEGORY,null,contentValues);
            categoryId = (int) rowdata;
        }
        //Update category in Quiz_Category table
        if (categoryId!=-1) {
            String updateQuery = "UPDATE " + TABLE_QUIZ_CATEGORY + " SET " + KEY_CATEGORY_ID + " = " + categoryId +
                    " WHERE " + KEY_CATEGORY_ID + " = (SELECT " + KEY_ID + " FROM " + TABLE_CATEGORY + " WHERE " + KEY_CATEGORY_NAME
                    + " LIKE '" + currentCategoryName + "') AND " + KEY_QUIZ_ID + "=" + quizId + " AND " + KEY_ROUND_ID + "=" + roundId;
            db.execSQL(updateQuery);
            //Update categories for the questions
            updateQuery = "UPDATE " + TABLE_QUESTIONS + " SET " + KEY_CATEGORY_ID + " = " + categoryId + " WHERE " + KEY_CATEGORY_ID + " = "
                    + "(SELECT " + KEY_ID + " FROM " + TABLE_CATEGORY + " WHERE " + KEY_CATEGORY_NAME + " LIKE '" + currentCategoryName + "')"
                    + " AND " + KEY_ROUND_ID + " = " + roundId + " AND " + KEY_QUIZ_ID + " = " + quizId;
            db.execSQL(updateQuery);

        }



    }

    public void updateQuestionCategory(int quizId, int roundId, String currentCategoryName, String newCategoryName) {
    }
}


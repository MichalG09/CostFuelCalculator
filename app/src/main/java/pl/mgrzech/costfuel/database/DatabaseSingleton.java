package pl.mgrzech.costfuel.database;

import android.content.Context;

public class DatabaseSingleton {

    private static Database INSTANCE;

    private DatabaseSingleton() {
    }

    public static Database getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new Database(context);
        }
        return INSTANCE;
    }
}

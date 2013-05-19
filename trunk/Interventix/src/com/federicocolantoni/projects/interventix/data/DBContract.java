package com.federicocolantoni.projects.interventix.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by federico on 19/05/13.
 */
public class DBContract {

    public final static String AUTHORITY = "com.federicocolantoni.projects.interventix";
    final static Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    private DBContract() {

    }

    public static class Data {

        public static final String PATH = "interventix";
        public static final String DB_TABLE = PATH;
        public final static Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();
        final static String MIME_TYPE = "vnd.com.federicocolantoni." + PATH;
        public final static String COLLECTION_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + MIME_TYPE;
        public final static String SINGLE_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + MIME_TYPE;

        public static class Fields {

            public final static String _ID = BaseColumns._ID;
            public final static String TYPE = "type";
            public final static String INDEX = "indexed";
            public final static String[] DATA =
                    {"data0", "data1", "data2", "data3", "data4", "data5", "data6", "data7", "data8", "data9"};
            public final static String[] BLOB = {"blob0", "blob1", "blob2"};
        }
    }
}

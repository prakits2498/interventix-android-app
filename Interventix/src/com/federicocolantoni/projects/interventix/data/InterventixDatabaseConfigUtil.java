package com.federicocolantoni.projects.interventix.data;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

public class InterventixDatabaseConfigUtil extends OrmLiteConfigUtil {

    public static void main(String[] args) throws SQLException, IOException {

	writeConfigFile("ormlite_config.txt");
    }
}

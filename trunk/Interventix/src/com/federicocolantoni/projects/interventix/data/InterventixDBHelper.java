package com.federicocolantoni.projects.interventix.data;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.entity.Cliente;
import com.federicocolantoni.projects.interventix.entity.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.entity.Intervento;
import com.federicocolantoni.projects.interventix.entity.Utente;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Created by federico on 19/05/13.
 */
public class InterventixDBHelper extends OrmLiteSqliteOpenHelper {
    
    private final static String DB_NAME = "Interventix_DB";
    private final static int DB_VERSION = 1;
    
    private Dao<Utente, Long> utenteDao = null;
    private RuntimeExceptionDao<Utente, Long> runtimeUtenteDao = null;
    
    private Dao<Cliente, Long> clienteDao = null;
    private RuntimeExceptionDao<Cliente, Long> runtimeClienteDao = null;
    
    private Dao<DettaglioIntervento, Long> dettaglioInterventoDao = null;
    private RuntimeExceptionDao<DettaglioIntervento, Long> runtimeDettaglioInterventoDao = null;
    
    private Dao<Intervento, Long> interventoDao = null;
    private RuntimeExceptionDao<Intervento, Long> runtimeInterventoDao = null;
    
    public InterventixDBHelper(Context context) {
    
	super(context, DB_NAME, null, DB_VERSION, R.raw.ormlite_config);
    }
    
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
    
	try {
	    TableUtils.createTableIfNotExists(connectionSource, Utente.class);
	    TableUtils.createTableIfNotExists(connectionSource, Cliente.class);
	    TableUtils.createTableIfNotExists(connectionSource, DettaglioIntervento.class);
	    TableUtils.createTableIfNotExists(connectionSource, Intervento.class);
	}
	catch (SQLException e) {
	    throw new RuntimeException();
	}
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    
    }
    
    public Dao<Utente, Long> getUtenteDao() throws SQLException {
    
	if (utenteDao == null)
	    utenteDao = getDao(Utente.class);
	
	return utenteDao;
    }
    
    public RuntimeExceptionDao<Utente, Long> getRuntimeUtenteDao() {
    
	if (runtimeUtenteDao == null)
	    runtimeUtenteDao = getRuntimeExceptionDao(Utente.class);
	
	return runtimeUtenteDao;
    }
    
    public Dao<Cliente, Long> getClienteDao() throws SQLException {
    
	if (clienteDao == null)
	    clienteDao = getDao(Cliente.class);
	
	return clienteDao;
    }
    
    public RuntimeExceptionDao<Cliente, Long> getRuntimeClienteDao() {
    
	if (runtimeClienteDao == null)
	    runtimeClienteDao = getRuntimeExceptionDao(Cliente.class);
	
	return runtimeClienteDao;
    }
    
    public Dao<DettaglioIntervento, Long> getDettaglioInterventoDao() throws SQLException {
    
	if (dettaglioInterventoDao == null)
	    dettaglioInterventoDao = getDao(DettaglioIntervento.class);
	
	return dettaglioInterventoDao;
    }
    
    public RuntimeExceptionDao<DettaglioIntervento, Long> getRuntimeDettaglioInterventoDao() {
    
	if (runtimeDettaglioInterventoDao == null)
	    runtimeDettaglioInterventoDao = getRuntimeExceptionDao(DettaglioIntervento.class);
	
	return runtimeDettaglioInterventoDao;
    }
    
    public Dao<Intervento, Long> getInterventoDao() throws SQLException {
    
	if (interventoDao == null)
	    interventoDao = getDao(Intervento.class);
	
	return interventoDao;
    }
    
    public RuntimeExceptionDao<Intervento, Long> getRuntimeInterventoDao() {
    
	if (runtimeInterventoDao == null)
	    runtimeInterventoDao = getRuntimeExceptionDao(Intervento.class);
	
	return runtimeInterventoDao;
    }
    
    @Override
    public void close() {
    
	super.close();
	
	utenteDao = null;
	runtimeUtenteDao = null;
	
	clienteDao = null;
	runtimeClienteDao = null;
	
	dettaglioInterventoDao = null;
	runtimeDettaglioInterventoDao = null;
	
	interventoDao = null;
	runtimeInterventoDao = null;
    }
}

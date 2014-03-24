package com.federicocolantoni.projects.interventix.task;

import java.util.ArrayList;

import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.application.Interventix;
import com.federicocolantoni.projects.interventix.models.Cliente;
import com.federicocolantoni.projects.interventix.models.InterventoController;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class GetClientiAsyncTask extends AsyncTask<Void, Void, ArrayList<Cliente>> {

    public GetClientiAsyncTask() {

    }

    @Override
    protected ArrayList<Cliente> doInBackground(Void... params) {

	ArrayList<Cliente> listaClienti = new ArrayList<Cliente>();

	RuntimeExceptionDao<Cliente, Long> clienteDao = Interventix.getDbHelper().getRuntimeClienteDao();

	listaClienti = (ArrayList<Cliente>) clienteDao.queryForAll();

	return listaClienti;
    }

    @Override
    protected void onPostExecute(ArrayList<Cliente> result) {

	InterventoController.listaClienti = result;
    }
}

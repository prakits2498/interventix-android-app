package com.federicocolantoni.projects.interventix.task;

import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.models.Cliente;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class GetNominativoClienteAsyncTask extends AsyncTask<Long, Void, Cliente> {

    public GetNominativoClienteAsyncTask() {

    }

    @Override
    protected Cliente doInBackground(Long... params) {

	RuntimeExceptionDao<Cliente, Long> clienteDao = com.federicocolantoni.projects.interventix.application.Interventix_.getDbHelper().getRuntimeClienteDao();

	Cliente cliente = clienteDao.queryForId(params[0]);

	return cliente;
    }
}

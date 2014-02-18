package com.federicocolantoni.projects.interventix.utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

public abstract class ManagedAsyncTask<Params, Progress, Result> {

    private TaskManagerFragment mManager;

    private InternalAsyncTask mTask;

    public ManagedAsyncTask(Context context) {

	this(context, TaskManagerFragment.DEFAULT_TAG);
    }

    public ManagedAsyncTask(Context context, String fragmentTag) {

	FragmentManager fragmentManager = ((ActionBarActivity) context).getSupportFragmentManager();

	mManager = (TaskManagerFragment) fragmentManager.findFragmentByTag(fragmentTag);

	if (mManager == null) {
	    mManager = new TaskManagerFragment();

	    fragmentManager.beginTransaction().add(mManager, fragmentTag).commit();
	}

	mTask = new InternalAsyncTask();
    }

    protected void onPreExecute() {

    }

    protected abstract Result doInBackground(Params... params);

    protected void onProgressUpdate(Progress... values) {

    }

    protected void onPostExecute(Result result) {

    }

    protected void onCancelled() {

    }

    public ManagedAsyncTask<Params, Progress, Result> execute(Params... params) {

	mTask.execute(params);

	return this;
    }

    public ActionBarActivity getActivity() {

	return (ActionBarActivity) mManager.getActivity();
    }

    public boolean cancel(boolean mayInterruptIfRunning) {

	return mTask.cancel(mayInterruptIfRunning);
    }

    public boolean isCancelled() {

	return mTask.isCancelled();
    }

    public Result get() throws InterruptedException, ExecutionException {

	return mTask.get();
    }

    public Result get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {

	return mTask.get(timeout, unit);
    }

    public AsyncTask.Status getStatus() {

	return mTask.getStatus();
    }

    protected class InternalAsyncTask extends AsyncTask<Params, Progress, Result> {

	@Override
	protected void onPreExecute() {

	    ManagedAsyncTask.this.onPreExecute();
	}

	@Override
	protected Result doInBackground(Params... params) {

	    return ManagedAsyncTask.this.doInBackground(params);
	}

	@Override
	protected void onProgressUpdate(final Progress... values) {

	    mManager.runWhenReady(new Runnable() {

		@Override
		public void run() {

		    ManagedAsyncTask.this.onProgressUpdate(values);
		}
	    });

	    return;
	};

	@Override
	protected void onPostExecute(final Result result) {

	    mManager.runWhenReady(new Runnable() {

		@Override
		public void run() {

		    ManagedAsyncTask.this.onPostExecute(result);
		}
	    });

	    return;
	}

	@Override
	protected void onCancelled() {

	    mManager.runWhenReady(new Runnable() {

		@Override
		public void run() {

		    ManagedAsyncTask.this.onCancelled();
		}
	    });
	}
    }
}

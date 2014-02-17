package com.federicocolantoni.projects.interventix.core;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;

public class BufferInvioIntervento {

	private Timer timerBuffer;
	private BufferInterventoTask interventoTask;

	public BufferInvioIntervento(Context context) {

	}

	public void startTimerInterventi() {

		timerBuffer = new Timer("TimerBufferIntervento", true);
		interventoTask = new BufferInterventoTask();
		// System.out.println(this.getClass().getSimpleName() +
		// " in esecuzione");
		timerBuffer.scheduleAtFixedRate(interventoTask, 0l, 3000);
	}

	public void stopTimerInterventi() {

		if (interventoTask.cancel()) {
			// System.out.println(interventoTask.getClass().getSimpleName() +
			// " cancellato");
			timerBuffer.purge();

			timerBuffer = new Timer("TimerBufferIntervento", true);
		}
	}

	private class BufferInterventoTask extends TimerTask {

		public BufferInterventoTask() {

		}

		@Override
		public void run() {

			// System.out.println(this.getClass().getSimpleName() + ": " + new
			// DateTime(DateTime.now()).toString("dd/MM/yyyy hh:mm:ss"));
		}
	}
}

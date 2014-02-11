package com.federicocolantoni.projects.interventix.core;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.Constants.BUFFER_TYPE;
import com.federicocolantoni.projects.interventix.Interventix;
import com.federicocolantoni.projects.interventix.Interventix_;
import com.federicocolantoni.projects.interventix.service.InterventixService;

public class BufferInterventix {

	private static BufferInterventix bufferSingleton;

	private Timer timerBuffer;
	private BufferTask interventoTask, clienteTask;

	private BufferInterventix() {

		bufferSingleton = this;
	}

	public static synchronized BufferInterventix getBufferInterventix() {

		if (bufferSingleton == null)
			bufferSingleton = new BufferInterventix();

		return bufferSingleton;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

		throw new CloneNotSupportedException();
	}

	public void startTimer(BUFFER_TYPE type) {

		timerBuffer = new Timer(this.getClass().getSimpleName(), true);

		switch (type) {
			case BUFFER_INTERVENTO:

				interventoTask = new BufferTask(type);
				System.out.println(this.getClass().getSimpleName() + " in esecuzione - " + type.name());
				timerBuffer.scheduleAtFixedRate(interventoTask, 0l, 1000 * 60);

				break;

			case BUFFER_CLIENTE:

				clienteTask = new BufferTask(type);
				System.out.println(this.getClass().getSimpleName() + " in esecuzione - " + type.name());
				timerBuffer.scheduleAtFixedRate(clienteTask, 0l, 1000 * 30);

				break;
		}
	}

	public void stopTimer() {

		if (interventoTask.cancel() && clienteTask.cancel()) {
			System.out.println(interventoTask.getClass().getSimpleName() + " - " + interventoTask.getBufferType() + " cancellato");
			System.out.println(clienteTask.getClass().getSimpleName() + " - " + clienteTask.getBufferType() + " cancellato");
			timerBuffer.purge();
		}

		timerBuffer = new Timer(this.getClass().getSimpleName(), true);
	}

	private class BufferTask extends TimerTask {

		private BUFFER_TYPE type;

		public BufferTask(BUFFER_TYPE type) {

			this.type = type;
		}

		public String getBufferType() {

			return type.name();
		}

		@Override
		public void run() {

			Intent interventixService = new Intent(Interventix.getContext(), InterventixService.class);

			switch (type) {
				case BUFFER_INTERVENTO:

					System.out.println(type.name() + " avviato");

					interventixService.setAction(Constants.ACTION_GET_INTERVENTI);

					Interventix_.getContext().startService(interventixService);

					break;

				case BUFFER_CLIENTE:

					System.out.println(type.name() + " avviato");

					interventixService.setAction(Constants.ACTION_GET_CLIENTI);

					Interventix_.getContext().startService(interventixService);

					break;
			}
		}
	}
}

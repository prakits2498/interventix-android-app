package com.federicocolantoni.projects.interventix.core;

import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.DateTime;

import android.content.Context;

public class BufferInvioCliente {
    
    private Timer timerBuffer;
    private BufferClienteTask clienteTask;
    
    public BufferInvioCliente(Context context) {
	
    }
    
    public void startTimerClienti() {
	
	timerBuffer = new Timer("TimerBufferCliente", true);
	clienteTask = new BufferClienteTask();
	System.out.println(this.getClass().getSimpleName() + " in esecuzione");
	timerBuffer.scheduleAtFixedRate(clienteTask, 0l, 3000);
    }
    
    public void stopTimerClienti() {
	
	if (clienteTask.cancel()) {
	    System.out.println(clienteTask.getClass().getSimpleName() + " cancellato");
	    timerBuffer.purge();
	    
	    timerBuffer = new Timer("TimerBufferCliente", true);
	}
    }
    
    private class BufferClienteTask extends TimerTask {
	
	public BufferClienteTask() {
	    
	}
	
	@Override
	public void run() {
	    
	    System.out.println(this.getClass().getSimpleName() + ": " + new DateTime(DateTime.now()).toString("dd/MM/yyyy hh:mm:ss"));
	}
    }
}

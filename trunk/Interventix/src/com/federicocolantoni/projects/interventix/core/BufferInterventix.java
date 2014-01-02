package com.federicocolantoni.projects.interventix.core;

import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.DateTime;

import com.federicocolantoni.projects.interventix.Constants.BUFFER_TYPE;

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
    
    public Object clone() throws CloneNotSupportedException {
	
	throw new CloneNotSupportedException();
    }
    
    public void startTimer(BUFFER_TYPE type) {
	
	timerBuffer = new Timer(this.getClass().getSimpleName(), true);
	
	switch (type) {
	    case BUFFER_INTERVENTO:
		
		interventoTask = new BufferTask();
		System.out.println(this.getClass().getSimpleName() + " in esecuzione - " + type.name());
		timerBuffer.scheduleAtFixedRate(interventoTask, 0l, 3000);
		
		break;
	    
	    case BUFFER_CLIENTE:
		
		clienteTask = new BufferTask();
		System.out.println(this.getClass().getSimpleName() + " in esecuzione - " + type.name());
		timerBuffer.scheduleAtFixedRate(clienteTask, 0l, 2500);
		
		break;
	}
    }
    
    public void stopTimer() {
	
	if (interventoTask.cancel() && clienteTask.cancel())
	    timerBuffer.purge();
	
	timerBuffer = new Timer(this.getClass().getSimpleName(), true);
    }
    
    private class BufferTask extends TimerTask {
	
	@Override
	public void run() {
	    
	    System.out.println(this.getClass().getSimpleName() + ": " + new DateTime(DateTime.now()).toString("dd/MM/yyyy hh:mm:ss"));
	}
    }
}

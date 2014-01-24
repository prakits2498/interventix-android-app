package com.federicocolantoni.projects.interventix.utils;

public interface TaskManager {
    
    public boolean isReady();
    
    public void runWhenReady(Runnable runnable);
}

package com.federicocolantoni.projects.interventix.helpers;

public interface TaskManager {

    public boolean isReady();

    public void runWhenReady(Runnable runnable);
}

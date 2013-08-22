package org.alfresco.content.transform;

public interface ContentTransformerNodeWorkerProgressReporter
{
    public void onTransformationStarted();
    
    public void onTransformationProgress(float progress);
    
    public void onTransformationComplete();
}

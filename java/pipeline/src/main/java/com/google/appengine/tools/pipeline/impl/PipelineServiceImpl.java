// Copyright 2011 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License"); you may not
// use this file except in compliance with the License. You may obtain a copy of
// the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
// License for the specific language governing permissions and limitations under
// the License.

package com.google.appengine.tools.pipeline.impl;

import com.google.appengine.tools.pipeline.Job;
import com.google.appengine.tools.pipeline.Job0;
import com.google.appengine.tools.pipeline.Job1;
import com.google.appengine.tools.pipeline.Job2;
import com.google.appengine.tools.pipeline.Job3;
import com.google.appengine.tools.pipeline.Job4;
import com.google.appengine.tools.pipeline.Job5;
import com.google.appengine.tools.pipeline.Job6;
import com.google.appengine.tools.pipeline.JobInfo;
import com.google.appengine.tools.pipeline.JobSetting;
import com.google.appengine.tools.pipeline.NoSuchObjectException;
import com.google.appengine.tools.pipeline.OrphanedObjectException;
import com.google.appengine.tools.pipeline.PipelineService;

import java.util.Set;
import java.util.UUID;

/**
 * Implements {@link PipelineService} by delegating to {@link PipelineManager}.
 *
 * @author rudominer@google.com (Mitch Rudominer)
 */
public class PipelineServiceImpl implements PipelineService {

    @Override
    public UUID startNewPipeline(final Job0<?> jobInstance, final JobSetting... settings) {
        return PipelineManager.startNewPipeline(settings, jobInstance);
    }

    @Override
    public <T1> UUID startNewPipeline(final Job1<?, T1> jobInstance, final T1 arg1, final JobSetting... settings) {
        return PipelineManager.startNewPipeline(settings, jobInstance, arg1);
    }

    @Override
    public <T1, T2> UUID startNewPipeline(final Job2<?, T1, T2> jobInstance, final T1 arg1, final T2 arg2,
                                          final JobSetting... settings) {
        return PipelineManager.startNewPipeline(settings, jobInstance, arg1, arg2);
    }

    @Override
    public <T1, T2, T3> UUID startNewPipeline(final Job3<?, T1, T2, T3> jobInstance, final T1 arg1, final T2 arg2,
                                              final T3 arg3, final JobSetting... settings) {
        return PipelineManager.startNewPipeline(settings, jobInstance, arg1, arg2, arg3);
    }

    @Override
    public <T1, T2, T3, T4> UUID startNewPipeline(final Job4<?, T1, T2, T3, T4> jobInstance, final T1 arg1,
                                                  final T2 arg2, final T3 arg3, final T4 arg4, final JobSetting... settings) {
        return PipelineManager.startNewPipeline(settings, jobInstance, arg1, arg2, arg3, arg4);
    }

    @Override
    public <T1, T2, T3, T4, T5> UUID startNewPipeline(final Job5<?, T1, T2, T3, T4, T5> jobInstance,
                                                      final T1 arg1, final T2 arg2, final T3 arg3, final T4 arg4, final T5 arg5, final JobSetting... settings) {
        return PipelineManager.startNewPipeline(settings, jobInstance, arg1, arg2, arg3, arg4, arg5);
    }

    @Override
    public <T1, T2, T3, T4, T5, T6> UUID startNewPipeline(
            final Job6<?, T1, T2, T3, T4, T5, T6> jobInstance, final T1 arg1, final T2 arg2, final T3 arg3, final T4 arg4, final T5 arg5,
            final T6 arg6, final JobSetting... settings) {
        return PipelineManager.startNewPipeline(settings, jobInstance, arg1, arg2, arg3, arg4, arg5,
                arg6);
    }

    @Override
    public UUID startNewPipelineUnchecked(final Job<?> jobInstance, final Object[] arguments,
                                          final JobSetting... settings) {
        return PipelineManager.startNewPipeline(settings, jobInstance, arguments);
    }

    @Override
    public void stopPipeline(final UUID jobHandle) throws NoSuchObjectException {
        PipelineManager.stopJob(jobHandle);
    }

    @Override
    public void cancelPipeline(final UUID jobHandle) throws NoSuchObjectException {
        PipelineManager.cancelJob(jobHandle);
    }

    @Override
    public void deletePipelineRecords(final UUID pipelineHandle) throws NoSuchObjectException,
            IllegalStateException {
        deletePipelineRecords(pipelineHandle, false, false);
    }

    @Override
    public void deletePipelineRecords(final UUID pipelineHandle, final boolean force, final boolean async)
            throws NoSuchObjectException, IllegalStateException {
        PipelineManager.deletePipelineRecords(pipelineHandle, force, async);
    }

    @Override
    public JobInfo getJobInfo(final UUID jobHandle) throws NoSuchObjectException {
        return PipelineManager.getJob(jobHandle);
    }

    @Override
    public void submitPromisedValue(final UUID promiseHandle, final Object value)
            throws NoSuchObjectException, OrphanedObjectException {
        PipelineManager.acceptPromisedValue(promiseHandle, value);
    }

    @Override
    public void cleanBobs(final String prefix) {
        PipelineManager.getBackEnd().cleanBlobs(prefix);
    }

    @Override
    public void shutdown() {
        PipelineManager.shutdown();
    }

    @Override
    public Set<UUID> getTestPipelines() {
        return PipelineManager.getBackEnd().getTestPipelines();
    }
}
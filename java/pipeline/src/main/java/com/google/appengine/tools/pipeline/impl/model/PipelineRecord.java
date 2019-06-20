package com.google.appengine.tools.pipeline.impl.model;

import com.google.appengine.tools.pipeline.PipelineInfo;
import com.google.appengine.tools.pipeline.impl.backend.PipelineMutation;
import com.google.cloud.spanner.Mutation;
import com.google.cloud.spanner.StructReader;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public final class PipelineRecord implements Record, PipelineInfo {
    public static final String DATA_STORE_KIND = "Pipeline";
    public static final String PIPELINE_KEY_PROPERTY = "pipelineKey";
    public static final String ROOT_JOB_DISPLAY_NAME = "rootJobDisplayName";
    public static final List<String> PROPERTIES = ImmutableList.<String>builder()
            .add(PIPELINE_KEY_PROPERTY)
            .add(ROOT_JOB_DISPLAY_NAME)
            .build();
    private final UUID pipelineKey;
    private final String rootJobDisplayName;
    // transient
    private JobRecord rootJob;

    public PipelineRecord(final UUID pipelineKey, final String rootJobDisplayName) {
        this.pipelineKey = pipelineKey;
        this.rootJobDisplayName = rootJobDisplayName;
    }

    public PipelineRecord(@Nullable final String prefix, @Nonnull final StructReader entity) {
        pipelineKey = UUID.fromString(entity.getString(Record.property(prefix, PIPELINE_KEY_PROPERTY)));
        rootJobDisplayName = entity.isNull(Record.property(prefix, ROOT_JOB_DISPLAY_NAME))
                ? null
                : entity.getString(Record.property(prefix, ROOT_JOB_DISPLAY_NAME));
    }

    public static List<String> propertiesForSelect(@Nullable final String prefix) {
        return Record.propertiesForSelect(DATA_STORE_KIND, PROPERTIES, prefix);
    }

    public UUID getPipelineKey() {
        return pipelineKey;
    }

    public String getPipelineDisplayName() {
        return rootJobDisplayName;
    }

    public JobRecord getRootJob() {
        return rootJob;
    }

    public void inflateRootJob(final JobRecord jobRecord) {
        rootJob = jobRecord;
    }

    @Override
    public String getDatastoreKind() {
        return DATA_STORE_KIND;
    }

    @Override
    public PipelineMutation toEntity() {
        final Mutation.WriteBuilder writeBuilder = Mutation.newInsertOrUpdateBuilder(DATA_STORE_KIND);
        writeBuilder.set(PIPELINE_KEY_PROPERTY).to(pipelineKey.toString());
        writeBuilder.set(ROOT_JOB_DISPLAY_NAME).to(rootJobDisplayName);
        return new PipelineMutation(writeBuilder);
    }

    @Override
    public State getJobState() {
        return rootJob.getJobState();
    }

    @Override
    public Object getOutput() {
        return rootJob.getOutput();
    }

    @Override
    public String getError() {
        return rootJob.getError();
    }

    @Override
    public Throwable getException() {
        return rootJob.getException();
    }
}

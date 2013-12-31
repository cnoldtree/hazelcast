/*
 * Copyright (c) 2008-2013, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.mapreduce.impl;

import com.hazelcast.config.JobTrackerConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.spi.InitializingObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractJobTracker implements JobTracker, InitializingObject {

    protected final Map<String, TrackableJob> trackableJobs = new ConcurrentHashMap<String, TrackableJob>();

    protected final HazelcastInstance hazelcastInstance;
    protected final JobTrackerConfig jobTrackerConfig;
    protected final String name;

    AbstractJobTracker(String name, JobTrackerConfig jobTrackerConfig, HazelcastInstance hazelcastInstance) {
        this.name = name;
        this.jobTrackerConfig = jobTrackerConfig;
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void initialize() {
    }

    @Override
    public Object getId() {
        return getName();
    }

    @Override
    public String getPartitionKey() {
        return getName();
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final String getServiceName() {
        return MapReduceService.SERVICE_NAME;
    }

    public <V> void registerTrackableJob(TrackableJob<V> trackableJob) {
        trackableJobs.put(trackableJob.getJobId(), trackableJob);
    }

    public <V> boolean unregisterTrackableJob(TrackableJob<V> trackableJob) {
        return trackableJobs.remove(trackableJob) != null;
    }

}

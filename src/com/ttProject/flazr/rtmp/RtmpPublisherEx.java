package com.ttProject.flazr.rtmp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flazr.rtmp.RtmpPublisher;
import com.flazr.rtmp.RtmpReader;

public abstract class RtmpPublisherEx extends RtmpPublisher {
    private static final Logger logger = LoggerFactory.getLogger(RtmpPublisherEx.class);
    public RtmpPublisherEx(final RtmpReader reader, final int streamId, final int bufferDuration, 
            boolean useSharedTimer, boolean aggregateModeEnabled) {
    	super(reader, streamId, bufferDuration, useSharedTimer, aggregateModeEnabled);
    	logger.info("RtmpPublisherExがよばれたよん。");
    }
}

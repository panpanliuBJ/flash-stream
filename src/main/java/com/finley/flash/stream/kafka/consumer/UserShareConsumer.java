package com.finley.flash.stream.kafka.consumer;

import com.finley.flash.stream.kafka.model.MiningData;
import com.finley.flash.stream.service.UserShareService;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;


public abstract class UserShareConsumer<T> extends AbstractConsumer<MiningData> {

    protected Logger log;
    protected UserShareService shareService;

    public UserShareConsumer(Logger log, UserShareService shareService) {
        this.log = log;
        this.shareService = shareService;
    }

    @Override
    public boolean sinkMessage(List<MiningData> miningDatas) {
        if (CollectionUtils.isEmpty(miningDatas)) {
            return false;
        }

        if (log.isDebugEnabled()) {
            for (MiningData miningData : miningDatas) {
                log.debug("{}->{}", getName(), miningData.toString());
            }
        }

        final List<T> userSpeeds = toUserShare(miningDatas);
        return shareService.batchInsertOrUpdate(userSpeeds);
    }

    /**
     * convert to UserShareXXX
     */
    protected abstract List<T> toUserShare(List<MiningData> miningDatas);
}

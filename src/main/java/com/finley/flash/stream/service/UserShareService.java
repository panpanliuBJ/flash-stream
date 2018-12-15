package com.finley.flash.stream.service;

import com.finley.flash.stream.domain.BaseUserShare;
import java.util.List;


public interface UserShareService<T extends BaseUserShare> {

    /**
     * insert or update user share
     *
     * @param userShares user share
     * @return boolean
     */
    boolean batchInsertOrUpdate(List<T> userShares);
}

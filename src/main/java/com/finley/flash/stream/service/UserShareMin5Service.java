package com.finley.flash.stream.service;

import com.finley.flash.stream.dao.UserShare5MinMapper;
import com.finley.flash.stream.domain.UserShare5Min;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserShareMin5Service implements UserShareService<UserShare5Min> {

    @Autowired
    private UserShare5MinMapper mapper;

    @Override
    public boolean batchInsertOrUpdate(List<UserShare5Min> userShares) {
        if (CollectionUtils.isEmpty(userShares)) {
            return false;
        }

        return mapper.batchUpsertSelective(userShares, UserShare5Min.Column.excludes(UserShare5Min.Column.updateTime)) == userShares.size();
    }

}

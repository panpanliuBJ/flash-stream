package com.finley.flash.stream.service;

import com.finley.flash.stream.dao.UserShare15MinMapper;
import com.finley.flash.stream.domain.UserShare15Min;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserShareMin15Service implements UserShareService<UserShare15Min> {

    @Autowired
    private UserShare15MinMapper mapper;

    @Override
    public boolean batchInsertOrUpdate(List<UserShare15Min> userShares) {
        if (CollectionUtils.isEmpty(userShares)) {
            return false;
        }
        return mapper.batchUpsertSelective(userShares, UserShare15Min.Column.excludes(UserShare15Min.Column.updateTime)) == userShares.size();

    }
}

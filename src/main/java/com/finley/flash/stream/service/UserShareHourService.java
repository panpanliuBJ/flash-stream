package com.finley.flash.stream.service;

import com.finley.flash.stream.dao.UserShareHourMapper;
import com.finley.flash.stream.domain.UserShareHour;
import com.finley.flash.stream.domain.UserShareHour.Column;
import com.finley.flash.stream.domain.UserShareHourExample;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserShareHourService implements UserShareService<UserShareHour> {

    @Autowired
    private UserShareHourMapper mapper;

    @Override
    public boolean batchInsertOrUpdate(List<UserShareHour> userShares) {
        if (CollectionUtils.isEmpty(userShares)) {
            return false;
        }
        return mapper.batchUpsertSelective(userShares, UserShareHour.Column.excludes(UserShareHour.Column.updateTime)) == userShares.size();
    }
}

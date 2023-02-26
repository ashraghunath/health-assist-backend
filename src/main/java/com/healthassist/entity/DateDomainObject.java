package com.healthassist.entity;

import com.healthassist.util.TimeUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
abstract class DateDomainObject {
    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    public DateDomainObject() {
        this.createdAt = TimeUtil.nowUTC();
        this.updatedAt = TimeUtil.nowUTC();
    }
}
package com.healthassist.entity;

import java.time.LocalDateTime;

//import com.healthassist.util.TimeUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
abstract class DateDomainObject {
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public DateDomainObject() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void update() {
        this.updatedAt = LocalDateTime.now();
    }
}
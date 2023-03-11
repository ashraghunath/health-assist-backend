package com.healthassist.entity;

import java.time.LocalDate;

//import com.healthassist.util.TimeUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
abstract class DateDomainObject {
    private LocalDate createdAt;

    private LocalDate updatedAt;

    public DateDomainObject() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }
    
    public void update() {
        this.updatedAt = LocalDate.now();
    }
}
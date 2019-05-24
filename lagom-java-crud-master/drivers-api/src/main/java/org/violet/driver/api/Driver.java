package org.violet.driver.api;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.annotation.concurrent.Immutable;

@Value
@Builder
@Immutable
@JsonDeserialize
@AllArgsConstructor
public class Driver {
    /**
     * 驾驶员编号
     * 姓名
     * 身份证
     * 驾驶证号
     * 性别
     * 准驾车型
     * 发证机关
     * 发证时间
     */
    String driverNum;
    String name;
    String id;
    String licenseId;
    String sex;
    String carType;
    String office;
    String time;
}

package org.violet.car.api;


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
public final class Car {
    /**
     * 车辆编号
     * 号牌号码
     * 号牌种类
     * 车身颜色
     * 车架号
     * 车主
     * 联系方式
     */
    String vehicleNumber;
    String carNum;
    String plateType;
    String carColor;
    String frameNum;
    String owner;
    String tel;
}

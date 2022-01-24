package com.wushiyii.dispatch;

import lombok.*;

import java.lang.reflect.Method;

/**
 * @Author: wgq
 * @Date: 2022/1/22 15:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EndpointMetaInfo {

    private Object endpointObject;

    private Method method;

    private String path;

}

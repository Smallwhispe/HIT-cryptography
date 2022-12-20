package com.example.form;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since 2022-10-16
 */
@Data

public class ConfirmForm implements Serializable {

    private String code;

    private String key;

    private String orderhash;

    private String signature;
}

package com.vlt.lab2;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactItem implements Serializable {
    private String name;
    private String phone;
    private boolean status;
    private String imagePath;
}

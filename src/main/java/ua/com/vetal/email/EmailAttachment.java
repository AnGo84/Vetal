package ua.com.vetal.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.activation.DataSource;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailAttachment {
    private String name;
    private DataSource dataSource;


}

package server.TripToN.admin;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AdminProperties {

    @Value("${admin.login-id}")
    private String adminLoginId;

    @Value("${admin.password}")
    private String adminPassword;
}

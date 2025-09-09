package org.example.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
public class RoleHierarchyConfig {

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        // ADMIN hereda todo lo que tiene RECEPCIONISTA
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_RECEPCIONISTA");
        return roleHierarchy;
    }
}

package com.example.usermicroservice.dtos;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSecurity implements UserDetails {
    private Long idUser;
    private String username;
    private String password;  // Не забувайте зберігати пароль у цьому класі
    
    // Реалізуємо метод getAuthorities
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Якщо у вас є роль або привілеї, можна тут повернути список
        return Collections.emptyList(); // Повертаємо порожній список, якщо нема ролей
    }

    // Реалізуємо метод getPassword
    @Override
    public String getPassword() {
        return password;  // Повертаємо пароль
    }

    // Реалізуємо метод getUsername
    @Override
    public String getUsername() {
        return username;
    }

    // Реалізуємо метод isAccountNonExpired
    @Override
    public boolean isAccountNonExpired() {
        return true; // Якщо акаунт не має терміну дії
    }

    // Реалізуємо метод isAccountNonLocked
    @Override
    public boolean isAccountNonLocked() {
        return true; // Якщо акаунт не заблокований
    }

    // Реалізуємо метод isCredentialsNonExpired
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Якщо пароль не протермінований
    }

    // Реалізуємо метод isEnabled
    @Override
    public boolean isEnabled() {
        return true; // Якщо акаунт активний
    }
}

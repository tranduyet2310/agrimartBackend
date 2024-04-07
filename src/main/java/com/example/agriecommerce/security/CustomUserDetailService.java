package com.example.agriecommerce.security;

import com.example.agriecommerce.entity.Supplier;
import com.example.agriecommerce.entity.User;
import com.example.agriecommerce.repository.SupplierRepository;
import com.example.agriecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository, SupplierRepository supplierRepository) {
        this.userRepository = userRepository;
        this.supplierRepository = supplierRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<GrantedAuthority> authorities = new HashSet<>();
            SimpleGrantedAuthority s = new SimpleGrantedAuthority(user.getRoles().getName());
            authorities.add(s);
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
        } else {
            Supplier supplier = supplierRepository.findByEmail(email).orElseThrow(
                    () -> new UsernameNotFoundException("user not found with email: " + email)
            );
            Set<GrantedAuthority> authorities = new HashSet<>();
            SimpleGrantedAuthority s = new SimpleGrantedAuthority(supplier.getRoles().getName());
            authorities.add(s);
            return new org.springframework.security.core.userdetails.User(supplier.getEmail(), supplier.getPassword(), authorities);
        }
    }
}

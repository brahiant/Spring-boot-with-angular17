package com.springboot.backend.brahian.usersapp.users_backend.services;

// Importaciones necesarias para Spring Security
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import com.springboot.backend.brahian.usersapp.users_backend.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.backend.brahian.usersapp.users_backend.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio personalizado para cargar los detalles del usuario durante la autenticación.
 * Esta clase implementa UserDetailsService, que es la interfaz que Spring Security
 * utiliza para obtener información del usuario durante el proceso de autenticación.
 */
public class UseDetailsService implements UserDetailsService {

    // Inyección de dependencia del repositorio de usuarios
    // Esto nos permite acceder a la base de datos para buscar usuarios
    @Autowired
    private UserRepository userRepository;

    /**
     * Método principal que Spring Security llama cuando un usuario intenta autenticarse.
     * Este método busca al usuario en la base de datos y convierte la información
     * en un objeto UserDetails que Spring Security puede usar.
     * 
     * @param username - El nombre de usuario que se está intentando autenticar
     * @return UserDetails - Objeto con los detalles del usuario para Spring Security
     * @throws UsernameNotFoundException - Si el usuario no existe en la base de datos
     */
    @Transactional(readOnly = true) // Transacción de solo lectura para optimizar la consulta
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // 1. Buscar el usuario en la base de datos por su nombre de usuario
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // 2. Convertir los roles del usuario en autoridades que Spring Security entiende
        // Esto toma la lista de roles del usuario y los convierte en objetos GrantedAuthority
        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName())) // Cada rol se convierte en una autoridad
            .collect(Collectors.toList()); // Se recolectan en una lista

        // 3. Crear y retornar un objeto UserDetails con la información del usuario
        // Este constructor más completo permite controlar el estado de la cuenta
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),  // Nombre de usuario
            user.getPassword(),  // Contraseña (debe estar encriptada)
            true,               // enabled: La cuenta está activa (puede hacer login)
            true,               // accountNonExpired: La cuenta no ha expirado
            true,               // credentialsNonExpired: Las credenciales no han expirado
            true,               // accountNonLocked: La cuenta no está bloqueada
            authorities         // Lista de roles/permisos del usuario
        );
    }
}

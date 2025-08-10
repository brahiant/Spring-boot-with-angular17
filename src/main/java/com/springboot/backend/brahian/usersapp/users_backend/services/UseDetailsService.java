package com.springboot.backend.brahian.usersapp.users_backend.services;

// Importaciones necesarias para Spring Security
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.springboot.backend.brahian.usersapp.users_backend.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.backend.brahian.usersapp.users_backend.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

/**
 * Servicio que implementa UserDetailsService de Spring Security
 * para manejar la autenticación y autorización de usuarios
 * 
 * Este servicio es responsable de:
 * - Cargar los detalles del usuario desde la base de datos
 * - Convertir los roles del usuario a autoridades de Spring Security
 * - Crear un objeto UserDetails para la autenticación
 */
@Service
public class UseDetailsService implements UserDetailsService {

    // Repositorio para acceder a los datos de usuarios en la base de datos
    @Autowired
    private UserRepository repository;

    /**
     * Método principal que carga un usuario por su nombre de usuario
     * 
     * @param username Nombre de usuario a buscar
     * @return UserDetails objeto con la información del usuario y sus autoridades
     * @throws UsernameNotFoundException si el usuario no existe en el sistema
     */
    @Transactional(readOnly = true) // Solo lectura para optimizar rendimiento
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Buscar el usuario en la base de datos por nombre de usuario
        Optional<User> optionalUser = repository.findByUsername(username);

        // Verificar si el usuario existe, si no, lanzar excepción
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException(String.format("Username %s no existe en el sistema", username));
        }

        // Obtener el usuario del Optional
        User user = optionalUser.orElseThrow();

        // Convertir los roles del usuario a autoridades de Spring Security
        // Cada rol se convierte en un SimpleGrantedAuthority
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        // Crear y retornar un objeto UserDetails de Spring Security
        // Los parámetros booleanos representan:
        // - enabled: cuenta habilitada
        // - accountNonExpired: cuenta no expirada  
        // - credentialsNonExpired: credenciales no expiradas
        // - accountNonLocked: cuenta no bloqueada
        return new org.springframework.security.core.userdetails.User(username,
                user.getPassword(),
                true,  // enabled
                true,  // accountNonExpired
                true,  // credentialsNonExpired
                true,  // accountNonLocked
                authorities);
    }
}

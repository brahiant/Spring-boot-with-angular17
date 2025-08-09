package com.springboot.backend.brahian.usersapp.users_backend.auth.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springboot.backend.brahian.usersapp.users_backend.entities.User;

import org.springframework.security.authentication.AuthenticationManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import java.io.IOException;

/**
 * Filtro personalizado para la autenticación JWT
 * Este filtro intercepta las peticiones de login y procesa las credenciales enviadas en JSON
 * Extiende de UsernamePasswordAuthenticationFilter que es el filtro estándar de Spring Security
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    // AuthenticationManager es el responsable de validar las credenciales contra la base de datos
    // Internamente usa UserDetailsService para cargar los datos del usuario
    private AuthenticationManager authenticationManager;

    /**
     * Constructor que inyecta el AuthenticationManager
     * @param authenticationManager - Manejador que coordina el proceso de autenticación
     */
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Método que se ejecuta cuando llega una petición de login (POST /login por defecto)
     * Su trabajo es extraer las credenciales del usuario de la petición HTTP
     * 
     * FLUJO DE FUNCIONAMIENTO:
     * 1. Recibe la petición HTTP con credenciales en JSON
     * 2. Extrae username y password del JSON
     * 3. Crea un token de autenticación
     * 4. Delega al AuthenticationManager para validar las credenciales
     * 5. El AuthenticationManager usa UserDetailsService para buscar el usuario en BD
     * 6. Si las credenciales son válidas, retorna un objeto Authentication
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // Variables para almacenar las credenciales extraídas del JSON
        String username = null;
        String password = null;
        User user = null;
        
        try {
            // PASO 1: Leer el JSON del cuerpo de la petición HTTP
            // ObjectMapper convierte el JSON en un objeto User
            // Por ejemplo: {"username": "admin", "password": "12345"}
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            
            // PASO 2: Extraer username y password del objeto User
            username = user.getUsername();
            password = user.getPassword();

        } catch (StreamReadException e) {
            // Error al leer el stream de la petición
            e.printStackTrace();
        } catch (DatabindException e) {
            // Error al convertir el JSON al objeto User
            e.printStackTrace();
        } catch (IOException e) {
            // Error general de entrada/salida
            e.printStackTrace();
        }
        
        // PASO 3: Crear un token de autenticación con las credenciales
        // Este token aún NO está autenticado, solo contiene las credenciales
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        
        // PASO 4: Delegar al AuthenticationManager para validar las credenciales
        // AQUÍ ES DONDE SE CONECTA CON UserDetailsService:
        // 1. authenticationManager.authenticate() busca un AuthenticationProvider
        // 2. El provider usa UserDetailsService.loadUserByUsername() para buscar el usuario en BD
        // 3. Compara la contraseña enviada con la almacenada en BD
        // 4. Si coinciden, retorna un Authentication exitoso
        // 5. Si no coinciden, lanza una excepción
        return authenticationManager.authenticate(authToken);
    }
}

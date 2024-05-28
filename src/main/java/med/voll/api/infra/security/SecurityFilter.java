package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Classe anotado com @Component par aser instanciada pelo spring
//Herdando a classe OncePerRequestFilter que immplenta o filtro do spring
@Component
public class SecurityFilter extends OncePerRequestFilter {


    @Autowired
    TokenService tokenService;

    @Autowired
    UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var tokenJWT = recuperarToken(request);

        if (tokenJWT!=null){
            var subject = tokenService.getSubject(tokenJWT);
            var usuario = repository.findByLogin(subject);
            var authentication = new UsernamePasswordAuthenticationToken(usuario,null,usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
       //chama o próximo filtro e caso não haja mais filtros segue o fluxo da requisição
        filterChain.doFilter(request,response);
    }

    private String recuperarToken(HttpServletRequest request) {
        //na requisição é enviado um token no cabeçalho com o nome de Authorization
        //Armazenamos esse token na variável authorizationHeader
        var authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader!=null){
            return authorizationHeader.replace("Bearer ","");

        }
        //retornamos token sem o prefixo Bearer
        return null;

    }
}

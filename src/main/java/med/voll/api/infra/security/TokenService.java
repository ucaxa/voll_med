package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import med.voll.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {

    //A anotação @Value indica que spring deve ler o valor da variável que está no arquivo application.properties
    @Value("${api.security.token.secret}")
    public String secret;

    public String gerarToken(Usuario usuario){


        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return  JWT.create()
                    .withIssuer("API Voll Med")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException ("Erro ao gerar o token", exception);
            // Invalid Signing configuration / Couldn't convert Claims.
        }

    }

    private Instant dataExpiracao() {
     //   return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
        return LocalDateTime.now().plusMonths(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String getSubject(String tokenJWT){

        try {
            var algoritmo = Algorithm.HMAC256(secret);
           return  JWT.require(algoritmo)
                    // specify any specific claim validations
                    .withIssuer("API Voll Med")
                    // reusable verifier instance
                    .build().verify(tokenJWT).getSubject();

        } catch (JWTVerificationException exception){
            // Invalid signature/claims
            throw new RuntimeException("Token JWT inválido ou expirado");
        }
    }
}

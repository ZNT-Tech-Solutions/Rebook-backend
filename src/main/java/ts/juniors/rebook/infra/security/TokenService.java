package ts.juniors.rebook.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;
import ts.juniors.rebook.domain.entity.Usuario;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {



    public String gerarToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256("12345");
            return JWT.create()
                    .withIssuer("API Rebook")
                    .withSubject(usuario.getEmail())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("erro ao gerar token jwt", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256("12345");
            return JWT.require(algoritmo)
                    .withIssuer("API Rebook")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    public Long getUserIdFromToken(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256("12345");
            return JWT.require(algoritmo)
                    .withIssuer("API Rebook")
                    .build()
                    .verify(tokenJWT)
                    .getClaim("id").asLong();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expiradinho!");
        }
    }




    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}
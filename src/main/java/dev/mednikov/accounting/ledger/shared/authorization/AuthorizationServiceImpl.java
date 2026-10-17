package dev.mednikov.accounting.ledger.shared.authorization;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorizationServiceImpl implements AuthorizationService{

    private final static Logger logger = LoggerFactory.getLogger(AuthorizationServiceImpl.class);

    private final byte[] key;

    public AuthorizationServiceImpl(@Value("${app.signing-key}") String signingKey){
        this.key = signingKey.getBytes();
    }


    @Override
    public AuthorizedUser getAuthorizedUser(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(this.key);
            SignedJWT jwt = SignedJWT.parse(token);
            if (jwt.verify(verifier)){
                JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
                String userId = claimsSet.getSubject();
                String organizationId = claimsSet.getClaimAsString("organizationId");
                List<String> authorities = claimsSet.getStringListClaim("authorities");
                return new AuthorizedUser(userId, organizationId, authorities);
            } else {
                throw new AuthorizationException();
            }
        } catch (Exception ex){
            logger.error(ex.getMessage());
            throw new AuthorizationException();
        }


    }

}

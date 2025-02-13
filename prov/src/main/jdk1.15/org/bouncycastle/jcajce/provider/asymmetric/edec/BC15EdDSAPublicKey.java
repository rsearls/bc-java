package org.bouncycastle.jcajce.provider.asymmetric.edec;

import java.math.BigInteger;
import java.security.interfaces.EdECPublicKey;
import java.security.spec.EdECPoint;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.NamedParameterSpec;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.params.Ed448PublicKeyParameters;
import org.bouncycastle.util.Arrays;

class BC15EdDSAPublicKey
    extends  BCEdDSAPublicKey
    implements EdECPublicKey
{
    BC15EdDSAPublicKey(AsymmetricKeyParameter pubKey)
    {
        super(pubKey);
    }

    BC15EdDSAPublicKey(SubjectPublicKeyInfo keyInfo)
    {
        super(keyInfo);
    }

    BC15EdDSAPublicKey(byte[] prefix, byte[] rawData)
        throws InvalidKeySpecException
    {
        super(prefix, rawData);
    }

    @Override
    public EdECPoint getPoint()
    {
        byte[] keyData;
        if (eddsaPublicKey instanceof Ed448PublicKeyParameters)
        {
            keyData = ((Ed448PublicKeyParameters)eddsaPublicKey).getEncoded();
        }
        else
        {
            keyData = ((Ed25519PublicKeyParameters)eddsaPublicKey).getEncoded();
        }

        Arrays.reverseInPlace(keyData);

        boolean xOdd = (keyData[0] & 0x80) != 0;
        keyData[0] &= 0x7f;

        return new EdECPoint(xOdd, new BigInteger(1, keyData));
    }

    @Override
    public NamedParameterSpec getParams()
    {
        if (eddsaPublicKey instanceof Ed448PublicKeyParameters)
        {
            return NamedParameterSpec.ED448;
        }
        else
        {
            return NamedParameterSpec.ED25519;
        }
    }
}

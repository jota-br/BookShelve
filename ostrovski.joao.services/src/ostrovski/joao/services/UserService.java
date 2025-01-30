package ostrovski.joao.services;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;
import ostrovski.joao.db.helpers.TransformToDTO;
import ostrovski.joao.common.model.dto.UserDTO;
import ostrovski.joao.db.UserRepository;
import ostrovski.joao.db.helpers.GetSessionJPA;
import ostrovski.joao.db.model.jpa.UserJPA;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class UserService {

    public static UserDTO authentication(String login, String password) {

        Session session = null;
        try {
            session = GetSessionJPA.getSessionFactory().openSession();
            UserJPA user = UserRepository.getUserByLogin(session, login);
            if (user != null) {
                byte[] salt = Base64.getDecoder().decode(user.getSalt());
                String hashedPassword = hashPassword(password, salt);

                if (user.getHash().equals(hashedPassword)) {
                    return TransformToDTO.transformToUserDTO(user);
                }
            }
        } catch (HibernateException e) {
            Logger.log(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return null;
    }

    public static UserDTO userGeneration(String login, String password, int profileId) {
        if (login.isEmpty() || password.isEmpty() || profileId != 3) {
            Logger.log(new IllegalArgumentException(ExceptionMessage.NULL_PARAM.getMessage()));
            return null;
        }
        byte[] salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);

        UserJPA user = UserRepository.userRegistration(login, encodedSalt, hashedPassword, profileId);
        if (user != null) {
            return TransformToDTO.transformToUserDTO(user);
        }
        return null;
    }

    private static byte[] generateSalt() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[32];
            sr.nextBytes(salt);
            return salt;
        } catch (NoSuchAlgorithmException e) {
            Logger.log(e);
        }
        return null;
    }

    private static String hashPassword(String password, byte[] salt) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            Logger.log(e);
        }
        return null;
    }
}

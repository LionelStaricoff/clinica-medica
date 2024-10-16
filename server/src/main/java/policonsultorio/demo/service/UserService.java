package policonsultorio.demo.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import policonsultorio.demo.dto.LoginRequestDTO;
import policonsultorio.demo.dto.request.LoginDtoResponse;
import policonsultorio.demo.entity.User;
import policonsultorio.demo.repository.UserRepository;
import policonsultorio.demo.service.Doctor.DoctorServiceImpl;

import java.util.Map;


@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorServiceImpl doctorServiceImpl;

    public LoginRequestDTO register(LoginRequestDTO loginRequestDto) {

        User usuario = userRepository.findByName(loginRequestDto.name());
        if (usuario == null) {
            User u = new User(loginRequestDto);
            u.setActive(true);
            usuario = userRepository.save(u);
        } else if (!usuario.getActive()) {
            usuario.setActive(true);
            usuario = userRepository.save(usuario);
        } else if (usuario.getActive()) {
            throw new EntityExistsException("Entity is exist");
        }

        return new LoginRequestDTO(usuario);

    }

    public LoginRequestDTO findByUserId(Long id) {
        User usuario = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entoty not found"));
        return new LoginRequestDTO(usuario);
    }


    public User deleteLogicUser(int id) {
        Long idUser = Long.valueOf(id);
        var userDb = findByUserId( idUser);
      User user = new User(userDb);
      user.setActive(false);
      return userRepository.save(user);

    }

    public User login(LoginDtoResponse loginRequestDto) {

        var userDb = userRepository.findByName(loginRequestDto.name());
        if (!userDb.getPassword().equals(loginRequestDto.password())) throw new EntityNotFoundException("password not match");

        return userDb;

    }
}

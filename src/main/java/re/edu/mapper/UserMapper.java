package re.edu.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import re.edu.dto.response.UserResponse;
import re.edu.entity.Users;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserResponse toResponse(Users user) {
        return modelMapper.map(user, UserResponse.class);
    }
}

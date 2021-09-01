package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.RoleDto;
import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * RoleServiceImp
 * @author Kirenai
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("role.service")
public class RoleServiceImp extends GeneralServiceImp<RoleDto, Long, Role> implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public JpaRepository<Role, Long> getRepo() {
        return this.roleRepository;
    }

	@Override
	public RoleDto create(RoleDto data) {
        log.info("Calling the create method in " + this.getClass());
        Role role = this.modelMapper.map(data, Role.class);
        Role roleSaved = this.roleRepository.save(role);
        return this.modelMapper.map(roleSaved, RoleDto.class);
	}

	@Override
	public RoleDto update(Long id, RoleDto data) {
        log.info("Calling the update method in " + this.getClass());
        return this.roleRepository.findById(id)
            .map(u -> {
                u.setName(data.getName());
                return this.modelMapper.map(this.roleRepository.save(u), RoleDto.class);
            })
            .orElseThrow(() -> new IllegalStateException("User don't found"));
	}

}

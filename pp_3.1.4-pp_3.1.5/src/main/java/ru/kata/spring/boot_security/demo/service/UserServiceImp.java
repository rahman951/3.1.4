package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		
	}
	
	@Override
	
	public List<User> getAll() {
		return userRepository.findAll();
	}
	
	@Override
	@Transactional
	public void add(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		userRepository.save(user);
	}
	
	@Override
	@Transactional
	public void updateUser(User user) {
		User updatedUser = userRepository.getById(user.getId());
		if (!passwordEncoder.encode(user.getPassword()).equals(updatedUser.getPassword()) && !"".equals(user.getPassword())) {
			updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		updatedUser.setName(user.getFirstName());
		updatedUser.setLastName(user.getLastName());
		updatedUser.setEmail(user.getEmail());
		updatedUser.setAge(user.getAge());
		updatedUser.setRoles(user.getRoles());
		userRepository.save(updatedUser);
	}
	
	@Override
	@Transactional
	public void deleteUser(long id) {
		userRepository.deleteById(id);
	}
	
	@Override
	@Transactional
	public User showUser(long id) {
		return userRepository.getById(id);
	}
	
	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(name);
		if (user == null) {
			throw new UsernameNotFoundException("No user found with username" + name);
		}
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),
				user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
		
	}
}

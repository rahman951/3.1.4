package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table
public class User implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	private String firstName;
	
	
	private String lastName;
	
	
	private byte age;
	
	
	private String username;
	
	
	private String password;
	
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	
	private List<Role> roles;
	
	public User() {
	
	}
	
	public User(String firstName, String lastName, byte age, String username, String password, List<Role> roles) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.username = username;
		this.password = password;
		this.roles = roles;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return username;
	}
	
	public void setEmail(String email) {
		this.username = email;
	}
	
	public byte getAge() {
		return age;
	}
	
	public void setAge(byte age) {
		this.age = age;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getAuthority()))
				.collect(Collectors.toList());
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public List<Role> getRoles() {
		return roles;
	}
	
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	@Override
	public String toString() {
		return "User{" +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", age='" + age + '\'' +
				", email='" + username + '\'' +
				", password='" + password + '\'' +
				", id=" + id +
				", roles=" + roles +
				'}';
	}
}

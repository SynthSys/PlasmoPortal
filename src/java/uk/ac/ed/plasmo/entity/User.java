package uk.ac.ed.plasmo.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
	
	private Integer oid;
	private String userName;
	private String email;
	private String given;
	private String family;
	private String organisation;
	private String uuid;
	private String password;
	private String confirmPassword;
	private List<String> groups;
	private List<User> supervisesUsers;
	private User supervisor;
	private Boolean isSupervisor=false;
	
	public Integer getOid() {
		return oid;
	}
	public void setOid(Integer oid) {
		this.oid = oid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGiven() {
		return given;
	}
	public void setGiven(String given) {
		this.given = given;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public String getOrganisation() {
		return organisation;
	}
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public List<String> getGroups() {
                if (groups == null) groups = new ArrayList<>();
		return groups;
	}
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}
	public List<User> getSupervisesUsers() {
		return supervisesUsers;
	}
	public void setSupervisesUsers(List<User> supervisedUsers) {
		this.supervisesUsers = supervisedUsers;
	}
	public User getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(User supervisor) {
		this.supervisor = supervisor;
	}
	public void setIsSupervisor(Boolean isSupervisor) {
		this.isSupervisor = isSupervisor;
	}
	public Boolean getIsSupervisor() {
		return isSupervisor;
	}

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.oid);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.oid, other.oid)) {
            return false;
        }
        return true;
    }

        
        
}

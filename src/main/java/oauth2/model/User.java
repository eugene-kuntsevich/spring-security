package oauth2.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User
{

	private Long id;

	private String login;

	private String name;

	@Id
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getLogin()
	{
		return login;
	}

	public void setLogin(String login)
	{
		this.login = login;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}

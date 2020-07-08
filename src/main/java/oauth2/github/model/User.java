package oauth2.github.model;

import lombok.Data;

/**
 * @author Nikolay Bondarchuk
 * @since 2020-04-05
 */
@Data
public class User {

    private Long id;

    private String login;

    private String name;

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

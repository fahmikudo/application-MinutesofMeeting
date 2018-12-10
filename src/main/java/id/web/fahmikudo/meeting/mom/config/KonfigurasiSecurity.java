package id.web.fahmikudo.meeting.mom.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class KonfigurasiSecurity extends WebSecurityConfigurerAdapter {

    private static final String SQL_LOGIN
            = "select username, password, enabled\n" +
            "from users\n" +
            "where username = ?";

    private static final String SQL_PERMISSION
            = "select u.username, r.nama_role as authority\n" +
            "from users u join user_roles ur on u.id_user = ur.id_user\n" +
            "join roles r on ur.id_role = r.id_role\n" +
            "where u.username = ?";

    @Autowired
    private DataSource dataSource;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .usersByUsernameQuery(SQL_LOGIN)
                .authoritiesByUsernameQuery(SQL_PERMISSION)
                .dataSource(dataSource);

    }

}

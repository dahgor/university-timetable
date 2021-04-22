package configuration;

import dao.DaoException;
import dao.implementations.*;
import dao.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.FileNotFoundException;

@Configuration
@Import(DaoPropertiesConfig.class)
@PropertySource("classpath:database.properties")
public class DaoConfig {
    @Value("${driverClassName}")
    private String driver;
    @Value("${url}")
    private String url;
    @Value("${user}")
    private String user;
    @Value("${password}")
    private String password;

    @Autowired
    private DaoPropertiesConfig daoProperties;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public AuditoryDao auditoryDao() throws FileNotFoundException, DaoException {
        return new AuditoryDao(jdbcTemplate(), daoProperties.auditoryProperties());
    }

    @Bean
    public LessonDao lessonDao() throws FileNotFoundException, DaoException {
        return new LessonDaoImpl(jdbcTemplate(), daoProperties.lessonProperties());
    }

    @Bean
    public TimePeriodDao timePeriodDao() throws FileNotFoundException, DaoException {
        return new TimePeriodDao(jdbcTemplate(), daoProperties.timePeriodProperties());
    }

    @Bean
    public TimeDao timeDao() throws FileNotFoundException, DaoException {
        return new TimeDaoImpl(jdbcTemplate(), daoProperties.timeProperties());
    }

    @Bean
    public StudentDao studentDao() throws FileNotFoundException, DaoException {
        return new StudentDaoImpl(jdbcTemplate(), daoProperties.studentProperties());
    }

    @Bean
    public GroupDao groupDao() throws FileNotFoundException, DaoException {
        return new GroupDaoImpl(jdbcTemplate(), daoProperties.groupProperties());
    }

    @Bean
    public CourseDao courseDao() throws FileNotFoundException, DaoException {
        return new CourseDaoImpl(jdbcTemplate(), daoProperties.courseProperties());
    }

    @Bean
    public ProfessorDao professorDao() throws FileNotFoundException, DaoException {
        return new ProfessorDaoImpl(jdbcTemplate(), daoProperties.professorProperties());
    }
}
